/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.general;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.linkedlogics.performance.counter.PerfCounter;
import com.linkedlogics.performance.counter.PerfStorage;
import dev.ocean.sigtran.m3ua.M3UAProvider;
import dev.ocean.sigtran.m3ua.MTPTransferMessage;
import dev.ocean.sigtran.m3ua.ServiceIdentificator;
import dev.ocean.sigtran.sccp.general.configuration.SCCPConfiguration;
import org.apache.logging.log4j.*;
import dev.ocean.sigtran.m3ua.parameters.Cause;
import dev.ocean.sigtran.sccp.address.SubSystemNumber;
import dev.ocean.sigtran.sccp.controls.SCCPConnectionlessControlImpl;
import dev.ocean.sigtran.sccp.controls.SCCPRoutingControlImpl;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 *
 * @author root
 */
public class SCCPStackImpl implements SCCPStack {

    private static transient final Logger logger = LogManager.getLogger(SCCPStackImpl.class);
    private transient Map<SubSystemNumber, SCCPUser> users = new HashMap<>();
    private transient SCCPProviderImpl sccpProvider;
    private transient final M3UAProvider m3UAProvider;
    private transient SCCPRoutingControlImpl sccpRoutingControl;
    private transient SCCPConnectionlessControlImpl sccpConnectionlessControl;
    private transient SignallingLinksetSelectorGenerator slsGenerator;
    protected transient ScheduledExecutorService sstScheduler = Executors.newScheduledThreadPool(5, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "SCCP-SST-Scheduler");
        }
    });
    private StackState state = StackState.IDLE;
    private transient SCCPLayerManagement sccpLayerManagement;
    private String configFile = "./conf/sccp.xml";
    private String linksetSelectionMode = SignallingLinksetSelectorGenerator.LinksetSelectionMode.NONE.name();

    private final String mbeanName = "dev.ocean.sigtran.management:type=Management,name=SCCP";

    private boolean ignoreSST = false;
    private long sstTimerMin = 3000;
    private long sstTimerMax = 6000;
    private long sstTimerIncreaseBy = 100;

    private transient static SCCPStackImpl instance;

    //Counters
    public final PerfCounter packetsReceived;

    public final PerfCounter unknownMessageTypeCounter;
    public final PerfCounter hopCounterViolation;

    public final PerfCounter subsystemFailureCounter;

    public final PerfCounter gttFailureAddressNature;

    public final PerfCounter gttFailureSpecAddress;

    public final PerfCounter unitDataCounter;
    public final PerfCounter unitDataServiceCounter;

    public final PerfCounter extendedUnitDataCounter;
    public final PerfCounter extendedUnitDataServiceCounter;

    public final PerfCounter sstCounter;
    public final PerfCounter sspCounter;
    public final PerfCounter ssaCounter;

    public final PerfCounter reassambleTimerExpired;

    public final PerfCounter messageForLocalSSN;

    public final PerfCounter unqualifiedErrorCounter;

    public final PerfCounter reassemblyErrorCounter;

    //
    public final PerfCounter sccpFailureCounter;

    public final PerfCounter mtpFailureCounter;

    public final PerfCounter unequippedUserCounter;

    private boolean returnOnlyFirstSegment = false;


    public static SCCPStackImpl getInstance() {
        return instance;
    }
    private int maxMessageSize;
    private int minNotSegmentedMessageSize;
    private boolean removeSpc;
    private int hopCounter;
    private long reassemblyTimer;

    public SCCPStackImpl(SCCPConfiguration sccpConfiguration, M3UAProvider m3UAProvider) throws Exception {
        logger.info("==========================| Starting SCCP Stack |==========================");
        this.m3UAProvider = m3UAProvider;
        this.m3UAProvider.addUser(this);

        logger.info("[SCCP]: Initilizing counters...");

        unknownMessageTypeCounter = new PerfCounter("SCCP", "UnknownMessageTypeCounter", 0);
        PerfStorage.registerCounter(unknownMessageTypeCounter);

        packetsReceived = new PerfCounter("SCCP", "PacketsReceived", 0);
        PerfStorage.registerCounter(packetsReceived);

        hopCounterViolation = new PerfCounter("SCCP", "HopCounterViolationCounter", 0);
        PerfStorage.registerCounter(hopCounterViolation);

        subsystemFailureCounter = new PerfCounter("SCCP", "SubSystemFailureCounter", 0);
        PerfStorage.registerCounter(subsystemFailureCounter);

        gttFailureAddressNature = new PerfCounter("SCCP", "NoTranslationAddressSuchNature", 0);
        PerfStorage.registerCounter(gttFailureAddressNature);

        gttFailureSpecAddress = new PerfCounter("SCCP", "NoTranslationSpecificAddress", 0);
        PerfStorage.registerCounter(gttFailureSpecAddress);

        unitDataCounter = new PerfCounter("SCCP", "UnitDataCounter", 0);
        unitDataServiceCounter = new PerfCounter("SCCP", "UnitDataServiceCounter", 0);

        PerfStorage.registerCounter(unitDataCounter);
        PerfStorage.registerCounter(unitDataServiceCounter);

        extendedUnitDataCounter = new PerfCounter("SCCP", "ExtUnitDataCounter", 0);
        extendedUnitDataServiceCounter = new PerfCounter("SCCP", "ExtUnitDataServiceCounter", 0);
        PerfStorage.registerCounter(extendedUnitDataCounter);
        PerfStorage.registerCounter(extendedUnitDataServiceCounter);

        reassemblyErrorCounter = new PerfCounter("SCCP", "ReassemblyErrorCounter", 0);
        PerfStorage.registerCounter(reassemblyErrorCounter);

        sccpFailureCounter = new PerfCounter("SCCP", "SccpFailureCounter", 0);
        PerfStorage.registerCounter(sccpFailureCounter);

        mtpFailureCounter = new PerfCounter("SCCP", "MtpFailureCounter", 0);
        PerfStorage.registerCounter(mtpFailureCounter);

        unequippedUserCounter = new PerfCounter("SCCP", "UnequippedUserCounter", 0);
        PerfStorage.registerCounter(unequippedUserCounter);

        sstCounter = new PerfCounter("SCCP", "SSTCounter", 0);
        sspCounter = new PerfCounter("SCCP", "SSPCounter", 0);
        ssaCounter = new PerfCounter("SCCP", "SSACounter", 0);
        PerfStorage.registerCounter(sstCounter);
        PerfStorage.registerCounter(sspCounter);
        PerfStorage.registerCounter(ssaCounter);

        reassambleTimerExpired = new PerfCounter("SCCP", "ReassambleTimerExpiredCounter", 0);
        PerfStorage.registerCounter(reassambleTimerExpired);

        messageForLocalSSN = new PerfCounter("SCCP", "MessageForLocalSSNCounter", 0);
        PerfStorage.registerCounter(messageForLocalSSN);

        unqualifiedErrorCounter = new PerfCounter("SCCP", "UnqualifiedErrorCounter", 0);
        PerfStorage.registerCounter(unqualifiedErrorCounter);
        try {

            logger.info("[SCCP]: Starting SCCP Provider... ");
            this.sccpProvider = new SCCPProviderImpl(this);

            logger.info("[SCCP]: Starting SCCP Layer Management...");
            this.sccpLayerManagement = new SCCPLayerManagement(this);
            this.sccpLayerManagement.loadConfiguration(sccpConfiguration);

            logger.info("[SCCP]: Starting SCCP Routing Control... ");
            this.sccpRoutingControl = new SCCPRoutingControlImpl(this);

            logger.info("[SCCP]: Starting SCCP Connectionless Control... ");
            this.sccpConnectionlessControl = new SCCPConnectionlessControlImpl(this);

            logger.info(String.format("[SCCP]: Starting SCCP SignallingLinkSetSelectorGenerator[%s]...", this.linksetSelectionMode));
            this.slsGenerator = new SignallingLinksetSelectorGenerator(m3UAProvider.getMaxSls(), SignallingLinksetSelectorGenerator.LinksetSelectionMode.valueOf(this.linksetSelectionMode));

            this.state = StackState.RUNNING;
            logger.info("==========================| SCCP Stack started|==========================");
            
            logger.info(String.format("[SCCP]: Registering MBean. %s", this.mbeanName));
            MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
            mbeanServer.registerMBean(this.sccpLayerManagement, new ObjectName(this.mbeanName));
        } catch (Throwable ex) {
            logger.error("[SCCP]: ErrorOccured: ", ex);
        }

    }

    public SCCPProviderImpl getProvider() {
        return this.sccpProvider;
    }

    /**
     * @return the m3uaProvider
     */
    public M3UAProvider getM3uaProvider() {
        return m3UAProvider;
    }

    /**
     *
     * @param mtpTransferMessage
     * @throws java.lang.Exception
     */
    @Override
    public void mtpTransferRequest(MTPTransferMessage mtpTransferMessage) throws Exception {
        if (this.state == StackState.IDLE) {
            logger.error("MTP transfer request received. SCCP stack is not running.");
            return;
        }

        m3UAProvider.send(mtpTransferMessage);
    }

    @Override
    public void onMtpTransferIndication(MTPTransferMessage mtpTransferMessage) {
        if (this.state == StackState.IDLE) {
            logger.error("MTP transfer indication received. SCCP stack is not runnning.");
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("mtpTransferIndication received: %s", mtpTransferMessage));
        }

        this.sccpRoutingControl.onMtpTransferIndication(mtpTransferMessage);
    }

    @Override
    public void onMTPPause(int dpc) {
        if (this.state == StackState.IDLE) {
            logger.error(String.format("MTP Pause received. SCCP stack is not running. DPC = %s", dpc));
            return;
        }

        RemoteSignallingPoint remoteSignallingPoint = this.sccpLayerManagement.getRemoteSignallingPoint(dpc);
        if (remoteSignallingPoint == null) {
            this.logger.error(String.format("MTPPause received: No Remote SPC found. DPC = %s", dpc));
            return;
        }
        remoteSignallingPoint.onMTPPause(dpc);
    }

    @Override
    public void onMTPResume(int dpc) {
        if (this.state == StackState.IDLE) {
            logger.error(String.format("MTP Resume received. SCCP stack is not "
                    + "running. DPC = %s", dpc));
            return;
        }
        RemoteSignallingPoint remoteSignallingPoint = this.sccpLayerManagement.
                getRemoteSignallingPoint(dpc);
        if (remoteSignallingPoint == null) {
            this.logger.error(String.format("MTPResume received: No Remote SPC "
                    + "found. DPC = %s", dpc));
            return;
        }
        remoteSignallingPoint.onMTPResume();
    }

    @Override
    public void onMTPStatus(int dpc, int congestionLevel) {
        if (this.state == StackState.IDLE) {
            logger.error(String.format("MTP Status for congestion received. SCCP stack is not running. DPC = %s CongestionLevel = %s", dpc, congestionLevel));
            return;
        }
        RemoteSignallingPoint remoteSignallingPoint = this.sccpLayerManagement.getRemoteSignallingPoint(dpc);
        if (remoteSignallingPoint == null) {
            this.logger.error(String.format("MTPStatus received: Congestion indication. No Remote SPC found. DPC = %s Level = %s", dpc, congestionLevel));
            return;
        }
        remoteSignallingPoint.onMTPStatus(dpc, congestionLevel);
    }

    @Override
    public void onMTPStatus(int dpc, Cause cause) {
        if (this.state == StackState.IDLE) {
            logger.error(String.format("MTP Status received. SCCP stack is not running. DPC = %s Cause = %s ", dpc, cause));
            return;
        }
        RemoteSignallingPoint remoteSignallingPoint = this.sccpLayerManagement.getRemoteSignallingPoint(dpc);
        if (remoteSignallingPoint == null) {
            if (logger.isDebugEnabled()) {
                this.logger.debug(String.format("MTPStatus received. No RemoteSignallingPoint found. DPC = %s", dpc));
            }
            return;
        }
        remoteSignallingPoint.onMTPStatus(dpc, cause);
    }

    /**
     * @return the sccpConnectionlessControl
     */
    public SCCPConnectionlessControlImpl getSccpConnectionlessControl() {
        return sccpConnectionlessControl;
    }

    /**
     * @return the sccpRoutingControl
     */
    public SCCPRoutingControlImpl getSccpRoutingControl() {
        return sccpRoutingControl;
    }

    @Override
    public ServiceIdentificator getServiceIdentificator() {
        return ServiceIdentificator.SCCP;
    }

    @Override
    public void addSCCPUser(SubSystemNumber ssn, SCCPUser user) throws IOException {
        sccpLayerManagement.createLocalSubSystem(ssn);
        user.setSccpProvider(sccpProvider);
        users.put(ssn, user);
    }

    @Override
    public void removeSCCPUser(SubSystemNumber ssn) throws IOException {
        sccpLayerManagement.removeLocalSubSystem(ssn.value());
        users.remove(ssn);
    }

    @Override
    public SCCPUser getSCCPUser(SubSystemNumber ssn) {
        return this.users.get(ssn);
    }

    /**
     * @return the slsGenerator
     */
    public SignallingLinksetSelectorGenerator getSlsGenerator() {
        return slsGenerator;
    }

    /**
     * @return the sccpLayerManagement
     */
    public SCCPLayerManagement getSccpLayerManagement() {
        return sccpLayerManagement;
    }

    /**
     * @return the configPath
     */
    public String getConfigFile() {
        return configFile;
    }

    /**
     * @return the mbeanName
     */
    public String getMbeanName() {
        return mbeanName;
    }

    /**
     * @return the linksetSelectionMode
     */
    public String getLinksetSelectionMode() {
        return linksetSelectionMode;
    }

    /**
     * @param linksetSelectionMode the linksetSelectionMode to set
     */
    public void setLinksetSelectionMode(String linksetSelectionMode) {
        this.linksetSelectionMode = linksetSelectionMode;
    }

    @Override
    public void setIgnoreSST(boolean ignoreSST) {
        this.ignoreSST = ignoreSST;
    }

    @Override
    public boolean isIgnoreSST() {
        return ignoreSST;
    }

    @Override
    public long getSstTimerMax() {
        return sstTimerMax;
    }

    @Override
    public void setSstTimerMax(long sstTimerMax) {
        this.sstTimerMax = sstTimerMax;
    }

    @Override
    public long getSstTimerMin() {
        return sstTimerMin;
    }

    @Override
    public void setSstTimerMin(long sstTimerMin) {
        this.sstTimerMin = sstTimerMin;
    }

    @Override
    public long getSstTimerIncreaseBy() {
        return sstTimerIncreaseBy;
    }

    @Override
    public void setSstTimerIncreaseBy(long sstTimerIncreaseBy) {
        this.sstTimerIncreaseBy = sstTimerIncreaseBy;
    }

    @Override
    public boolean isReturnOnlyFirstSegment() {
        return this.returnOnlyFirstSegment;
    }

    @Override
    public void setReturnOnlyFirstSegment(boolean returnOnlyFirstSegment) {
        this.returnOnlyFirstSegment = returnOnlyFirstSegment;
    }

    @Override
    public void setMaxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }

    @Override
    public int getMaxMessageSize() {
        return maxMessageSize;
    }

    @Override
    public void setMinNotSegmentedMessageSize(int minNotSegmentedMessageSize) {
        this.minNotSegmentedMessageSize = minNotSegmentedMessageSize;
    }

    @Override
    public int getMinNotSegmentedMessageSize() {
        return minNotSegmentedMessageSize;
    }

    @Override
    public boolean isRemoveSpc() {
        return this.removeSpc;
    }

    @Override
    public void setRemoveSpc(boolean removeSpc) {
        this.removeSpc = removeSpc;
    }

    @Override
    public int getHopCounter() {
        return this.hopCounter;
    }

    @Override
    public void setHopCounter(int hopCounter) {
        this.hopCounter = hopCounter;
    }

    @Override
    public long getReassemblyTimer() {
        return this.reassemblyTimer;
    }

    @Override
    public void setReassemblyTimer(long reassemblyTimer) {
        this.reassemblyTimer = reassemblyTimer;
    }
}
