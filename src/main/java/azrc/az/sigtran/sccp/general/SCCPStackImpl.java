/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general;

import azrc.az.sigtran.m3ua.M3UAProvider;
import azrc.az.sigtran.m3ua.MTPTransferMessage;
import azrc.az.sigtran.m3ua.ServiceIdentificator;
import azrc.az.sigtran.m3ua.parameters.Cause;
import azrc.az.sigtran.sccp.address.SubSystemNumber;
import azrc.az.sigtran.sccp.controls.SCCPConnectionlessControlImpl;
import azrc.az.sigtran.sccp.controls.SCCPRoutingControlImpl;
import azrc.az.sigtran.sccp.general.configuration.SCCPConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 *
 * @author root
 */
public class SCCPStackImpl implements SCCPStack {

    private static transient final Logger logger = LoggerFactory.getLogger(SCCPStackImpl.class);
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
