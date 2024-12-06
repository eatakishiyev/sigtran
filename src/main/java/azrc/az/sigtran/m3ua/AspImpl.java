/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import azrc.az.sctp.ClientAssociation;
import azrc.az.sctp.SctpAssociation;
import azrc.az.sctp.SctpListener;
import azrc.az.sigtran.m3ua.io.M3UAMessageByteArrayOutputStream;
import azrc.az.sigtran.m3ua.messages.MessageFactory;
import azrc.az.sigtran.m3ua.messages.aspsm.*;
import azrc.az.sigtran.m3ua.messages.asptm.ASPActive;
import azrc.az.sigtran.m3ua.messages.asptm.ASPActiveAck;
import azrc.az.sigtran.m3ua.messages.asptm.ASPInactive;
import azrc.az.sigtran.m3ua.messages.asptm.ASPInactiveAck;
import azrc.az.sigtran.m3ua.messages.mgmt.ErrorCodes;
import azrc.az.sigtran.m3ua.messages.mgmt.ErrorMessage;
import azrc.az.sigtran.m3ua.messages.ssnm.DestinationAvailable;
import azrc.az.sigtran.m3ua.messages.transfer.PayloadData;
import azrc.az.sigtran.m3ua.parameters.*;
import azrc.az.sigtran.utils.ByteUtils;
import com.sun.nio.sctp.MessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author eatakishiyev
 */
//An ASP contains an SCTP endpoint and may be configured to process
//signalling traffic within more than one Application Server
public final class AspImpl extends Asp implements SctpListener {

    private transient final static Logger LOGGER = LoggerFactory.getLogger(AspImpl.class);

    private SctpAssociation sctpAssociation;

    private Boolean noDelay = true;
    protected int soSndBuf = 8192;
    protected int soRcvBuf = 8192;

    private LinkType connectionMode = LinkType.CLIENT;

    protected String name;
    //  Variable used to prevent collision of message sending process indications from stateMachines
    private long next = 0;
    // protected fields

    private transient ExecutorService[] workers;
    protected boolean congested = false;
    protected int sls2Stream[];

    private int workersCount = -1;

    protected long aspUpTimeOut = 2000;//2 seconds
    protected long aspDownTimeOut = 2000;//2 seconds
    protected long aspActiveTimeOut = 2000;//2 seconds
    protected long aspInactiveTimeOut = 2000;//2 seconds

    private long delayWithSecondsOnAssociationStart = 10;

    protected transient final ManagementMessageHandler managementMessageHandler;
    protected transient final StateMaintenanceMessageHandler stateMaintenanceMessageHandler;
    protected transient final TrafficManagementMessageHandler trafficManagementMessageHandler;
    protected transient final SignallingNetworkManagementMessageHandler signallingNetworkManagementMessageHandler;
    private transient final M3UAStackImpl stack;
    //Counters


    protected AspImpl(String name, String localHost, String additionalAddress,
                      int localPort, String peerHost, int peerPort, int inStreams,
                      int outStreams, boolean autreconnect, boolean noDelay, int soSndBuf,
                      int soRcvBuf, LinkType mode,
                      int aspUpTimeOut, int aspDownTimeOut,
                      int aspActiveTimeOut, int aspInactiveTimeOut, M3UAStackImpl stack) throws Exception {
        this.stack = stack;
        this.name = name;
        this.noDelay = noDelay;
        this.soSndBuf = soSndBuf;
        this.soRcvBuf = soRcvBuf;
        this.connectionMode = mode;
        this.aspUpTimeOut = aspUpTimeOut;
        this.aspDownTimeOut = aspDownTimeOut;
        this.aspActiveTimeOut = aspActiveTimeOut;
        this.aspInactiveTimeOut = aspInactiveTimeOut;


        managementMessageHandler = new ManagementMessageHandler(this);
        stateMaintenanceMessageHandler = new StateMaintenanceMessageHandler(this);
        trafficManagementMessageHandler = new TrafficManagementMessageHandler(this);
        signallingNetworkManagementMessageHandler = new SignallingNetworkManagementMessageHandler(this, stack);

        InetAddress _additionalAddress = null;
        if (additionalAddress != null) {
            _additionalAddress = InetAddress.getByName(additionalAddress);
        }
        if (connectionMode == LinkType.CLIENT) {
            LOGGER.info(String.format("Configuring client association %s", this));
            this.sctpAssociation
                    = stack.sctpProvider.createClientAssociation(new InetSocketAddress(localHost, localPort),
                    _additionalAddress, new InetSocketAddress(peerHost, peerPort),
                    inStreams, outStreams, soRcvBuf, soSndBuf);
            ((ClientAssociation) sctpAssociation).setAutoReconnect(autreconnect);
        } else {
            LOGGER.info(String.format("Configuring server association ", this));
            if (additionalAddress != null) {
                _additionalAddress = InetAddress.getByName(additionalAddress);
            }
            this.sctpAssociation
                    = stack.sctpProvider.createServerAssociation(new InetSocketAddress(localHost, localPort),
                    _additionalAddress, new InetSocketAddress(peerHost, peerPort),
                    inStreams, outStreams);
        }
        sctpAssociation.setListener(this);

    }

    @Override
    public void sendAspUpAck() throws IOException {
        AspUpAck aspUpAck = new AspUpAck();

        this.write(aspUpAck);
    }

    public boolean isCongested() {
        return congested;
    }

    @Override
    public void start() throws IOException {
        sctpAssociation.open();

    }

    @Override
    public void stop() {
        long currentTime = System.currentTimeMillis();
        if (stack.getNodeType() == NodeType.ASP) {

            if (next > currentTime) {
                return;
            }

            LOGGER.info(String.format("[M3UALink]:Stoping ASP. Sending ASPDown message. Link will be available."
                    + "%s", this));


            AspDown aspDown = (AspDown) MessageFactory.createMessage(MessageClass.ASPSM, MessageType.ASPDN);
            try {
                write(aspDown);
            } catch (IOException ex) {
                LOGGER.error(String.format("[M3UALink]:ErrorOccured while send ASPDown message. %s", this), ex);
            }
            next = currentTime + this.aspDownTimeOut;

            stateMachines.values().forEach((stateMachine) -> {
                try {
                    stateMachine.doTransition("SEND_DOWN");
                } catch (Exception ex) {
                    LOGGER.error(String.format("[M3UALink]:ErrorOccured while transite state machine to SEND_DOWN state. %s", this), ex);
                }
            });
        } else {
            LOGGER.error(String.format("[M3UALink]:Send ASP_DOWN message. ASP_DOWN must be sent from AS type NODE. m3uaStack.nodeType = %s. %s",
                    stack.getNodeType(), this));
        }
    }

    @Override
    public void onCommunicationUp(SctpAssociation assoc) {
        try {
            int inStreams = assoc.association().maxInboundStreams();
            int outStreams = assoc.association().maxOutboundStreams();

            LOGGER.info(String.format("[M3UALink]:onCommunicationUp: Creating SLS to OutStream mapping table. Initializing M3UALink workers"
                    + "InStreams = %s OutStreams = %s %s", inStreams, outStreams, this));

            mapSls2Stream(outStreams);
            initWorkers();

            if (stack.getNodeType() == NodeType.ASP || stack.getNodeType() == NodeType.IPSP_CLIENT) {
                LOGGER.info(String.format("[M3UALink]:onCommuncationUp: NodeType "
                        + "%s selected.Sending AspUp message. %s", stack.getNodeType(), this));
                sendAspUp();
            }
        } catch (Exception ex) {
            LOGGER.error("[M3UALink]:OnCommunicationUp: Error occured while handling communication up notification", ex);
        }
    }

    @Override
    public void onCommunicationDown(SctpAssociation assoc) {
        this.onCommunicationShutdown(assoc);
    }

    @Override
    public void onCommunicationShutdown(SctpAssociation assoc) {
        LOGGER.warn(String.format("[M3UALink]:Communication lost or shutdown."
                + " Stopping workers. %s", this));

        stopWorkers();
        if (stack.getNodeType() == NodeType.ASP
                || stack.getNodeType() == NodeType.IPSP_CLIENT
                || stack.getNodeType() == NodeType.IPSP_SERVER) {
            LOGGER.warn(String.format("[M3UALink]:Communication lost or shutdown."
                    + " Transiting state machines to COMM_LOST state. %s", this));
            try {
                for (AspStateMachine aspFsm : aspStateMachines()) {
                    aspFsm.doTransition("COMM_LOST");
                    aspFsm.getAs().doTransition("AS_DOWN");
                }
            } catch (Exception ex) {
                LOGGER.error(String.format("[M3UALink]:Error occured while"
                        + " transite state machine to COMM_LOST %s ", this), ex);
            }
        }
    }

    protected void send(final SctpPayload payload) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("[M3UALink]:Sending data %s %s", this, payload));
        }

        if (sctpAssociation != null) {
            sctpAssociation.write(payload.getData(), payload.getStreamNumber(),
                    payload.getProtocolId());
        } else {
            LOGGER.error(String.format("[M3UALink]:Sctp association is not established yet. %s %s",
                    this, payload));

            throw new IOException("Sctp Association is not established yet.");
        }
    }

    @Override
    public void onData(byte[] data, MessageInfo msgInfo) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("[M3UALink]:onData: Handling incoming data %s",
                    ByteUtils.bytes2Hex(data)));
        }


        if (msgInfo.payloadProtocolID() != M3UAStack.M3UA_PROTOCOL_ID) {
            LOGGER.error(String.format("[M3UALink]:onData:Incorrect payload data received:"
                            + " Expecting M3UA[3], received[%s]. %s",
                    msgInfo.payloadProtocolID(), ByteUtils.bytes2Hex(data), this));
            return;
        }

        int streamNumber = msgInfo.streamNumber();

        ExecutorService executorService = findWorker(streamNumber);
        executorService.submit(new MessageHandler(data, this, stack));
    }

    private ExecutorService findWorker(int streamNumber) {
        int workerId = streamNumber % workersCount;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("[M3UALink]:FindWorker. Looking for worker"
                            + " to pass receveid data to it. StreamNumber = %s; WorkerId = %s",
                    streamNumber, workerId));
        }

        return this.workers[workerId];
    }

    @Override
    public void shutdown() {
        try {
            if (sctpAssociation != null) {
                sctpAssociation.close();
                onCommunicationDown(sctpAssociation);
            }
        } catch (Exception ex) {
            LOGGER.error("Error: " + this, ex);
        }

    }

    @Override
    public void sendAspActive(As as) throws IOException {
        long currentTime = System.currentTimeMillis();
        if (next > currentTime) {
            return;
        }

        ASPActive aspActive = new ASPActive();
        if (this.getTrafficMode() != TrafficMode.UNKNOWN
                || this.getTrafficMode() != null) {
            aspActive.setTrafficMode(this.getTrafficMode());
        }

        if (as.isSendRoutingContext()) {
            aspActive.setRoutingContext(new RoutingContext(new Integer[]{as.getRc()}));
        }

        this.write(aspActive);


        next = currentTime + this.aspActiveTimeOut;
    }

    @Override
    public void sendAspActiveAck(As as) throws IOException {
        ASPActiveAck aspActiveAck = new ASPActiveAck();
        if (as.isSendRoutingContext()) {
            aspActiveAck.setRoutingContext(new RoutingContext(new Integer[]{as.getRc()}));
        }
        this.write(aspActiveAck);

    }

    @Override
    public void sendAspInactiveAck(As as) throws IOException {
        ASPInactiveAck aspInactiveAck = (ASPInactiveAck) MessageFactory.createMessage(ASPInactiveAck.MESSAGE_CLASS, ASPInactiveAck.MESSAGE_TYPE);
        if (as.isSendRoutingContext()) {
            aspInactiveAck.setRoutingContext(new RoutingContext(new Integer[]{as.getRc()}));
        }

        this.write(aspInactiveAck);
    }

    @Override
    public void sendDAVA(AffectedPointCode affectedPointCode) throws IOException {
        DestinationAvailable destinationAvailable = new DestinationAvailable();
        destinationAvailable.setAffectedPointCode(affectedPointCode);
        this.write(destinationAvailable);
    }

    @Override
    public long getAspActiveTimeOut() {
        return this.aspActiveTimeOut;
    }

    @Override
    public long getAspDownTimeOut() {
        return this.aspDownTimeOut;
    }

    @Override
    public long getAspInactiveTimeOut() {
        return this.aspInactiveTimeOut;
    }

    @Override
    public long getAspUpTimeOut() {
        return this.aspUpTimeOut;
    }

    /**
     * Inactivate Asp in all Application Servers.
     *
     * @throws Exception
     */
    @Override
    public void sendAspInactive() throws Exception {
        if (this.stack.getNodeType() == NodeType.ASP) {

            long currentTime = System.currentTimeMillis();
            if (next > currentTime) {
                return;
            }

            ASPInactive aSPInactive = new ASPInactive();
            this.write(aSPInactive);
            next = currentTime + this.aspInactiveTimeOut;

            for (AspStateMachine stateMachine : stateMachines.values()) {
                stateMachine.doTransition("SEND_INACTIVE");
            }
        } else {
            LOGGER.error(String.format("Send ASP_INACTIVE message. Incorrect message. m3uaStack.nodeType = %s", this.stack.getNodeType()));
        }
    }

    /**
     * Inactivate Asp in selected Application Server[RC].
     *
     * @param rc Routing Context
     * @throws Exception
     */
    @Override
    public void sendAspInactive(Integer rc) throws Exception {
        if (this.stack.getNodeType() == NodeType.ASP) {

            AspStateMachine stateMachine = getAspStateMachine(rc);
            if (stateMachine != null) {
                long currentTime = System.currentTimeMillis();
                if (next > currentTime) {
                    return;
                }

                ASPInactive aspInactive = (ASPInactive) MessageFactory.createMessage(ASPInactive.MESSAGE_CLASS, ASPInactive.MESSAGE_TYPE);
                if (rc > 0) {
                    aspInactive.setRoutingContext(new Integer[]{rc});
                }

                this.write(aspInactive);
                next = currentTime + this.aspInactiveTimeOut;

                stateMachine.doTransition("SEND_INACTIVE");

            } else {
                LOGGER.error("Routing Context not configured. RC = {}" + rc);
            }
        } else {
            LOGGER.error(String.format("Send ASP_INACTIVE message. Incorrecct message. m3uaStack.nodeType = %s", this.stack.getNodeType()));
        }
    }

    @Override
    public void setName(String name) throws IOException {
        this.name = name;
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public AspStateMachine getAspStateMachine(int rc) {
        return stateMachines.get(rc);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AspImpl) {
            return ((Asp) obj).getName().trim().toLowerCase().equals(this.name.trim().toLowerCase());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public void sendAspUp() {
        try {
            long currentTime = System.currentTimeMillis();
            if (next > currentTime) {
                return;
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Sending ASP_UP message. As = %s", this.name));
            }
            AspUp aspUp = (AspUp) MessageFactory.createMessage(MessageClass.ASPSM, MessageType.ASPUP);
            if (getAspId() != null && getAspId() >= 0) {
                aspUp.setAspIdentifier(new ASPIdentifier(getAspId()));
            }

            if (stack.getInfoString() != null) {
                aspUp.setInfoString(new InfoString(stack.getInfoString()));
            }
            this.write(aspUp);

            next = currentTime + this.aspUpTimeOut;

            for (AspStateMachine stateMachine : stateMachines.values()) {
                LOGGER.info("Selected stateMachine = " + stateMachine);
                stateMachine.doTransition("SEND_UP");
            }

        } catch (Exception ex) {
            LOGGER.error("Error occured during up the ASP.", ex);
        }
    }

    @Override
    public void sendHeartBeat(byte[] data) {
        try {
            HeartBeat heartBeat = new HeartBeat(data);
            this.write(heartBeat);
        } catch (IOException ex) {
            LOGGER.error("Error occured: ", ex);
        }
    }

    @Override
    public void sendHeartBeatAck(byte[] data) {
        try {
            HeartbeatAck heartbeatAck = (HeartbeatAck) MessageFactory.createMessage(MessageClass.ASPSM, MessageType.BEAT_ACK);
            heartbeatAck.setHeartBeatData(data);
            this.write(heartbeatAck);
        } catch (IOException ex) {
            LOGGER.error("Error occured: ", ex);
        }

    }

    @Override
    public void sendError(ErrorCodes errorCode, DiagnosticInformation diagnosticInformation, RoutingContext rc) {
        try {
            ErrorMessage error = (ErrorMessage) MessageFactory.createMessage(MessageClass.MGMT, MessageType.ERR);
            error.setErrorCode(errorCode);
            error.setRoutingContext(rc);
            error.setDiagnosticInformation(diagnosticInformation);
            this.write(error);
        } catch (IOException ex) {
            LOGGER.error("Error occured: ", ex);
        }
    }

    @Override
    public void sendPayload(PayloadData payload) {
        try {
            this.write(payload, payload.getProtocolData().getSls());
        } catch (IOException ex) {
            LOGGER.error("ErrorOccured: ", ex);
        }
    }

    public void removeAsByRc(int rc) {
        stateMachines.remove(rc);
    }

    @Override
    public StateMaintenanceMessageHandler getStateMaintenanceMessageHandler() {
        return this.stateMaintenanceMessageHandler;
    }

    @Override
    public TrafficManagementMessageHandler getTrafficManagementMessageHandler() {
        return this.trafficManagementMessageHandler;
    }

    @Override
    public ManagementMessageHandler getManagementMessageHandler() {
        return this.managementMessageHandler;
    }

    @Override
    public SignallingNetworkManagementMessageHandler getSignallingNetworkManagementMessageHandler() {
        return signallingNetworkManagementMessageHandler;
    }

    private void mapSls2Stream(int inboundStreams) {
        LOGGER.info(String.format("[M3UALink]:Preparing SLS2Stream mapping table: "
                        + "OutboundStreams = %s. MaxSLS = %s; %s", inboundStreams, stack.maxSLS,
                this));

        try {
            sls2Stream = new int[stack.maxSLS];

            int streamId = 1;
            sls2Stream[0] = 0;

            for (int i = 1; i < sls2Stream.length; i++) {
                if (streamId >= inboundStreams - 1) {
                    streamId = 1;
                }
                sls2Stream[i] = streamId++;
            }
        } catch (Exception ex) {
            LOGGER.error(String.format("[M3UALink]:SLS => STREAM mapping table"
                            + " can not be created: OutboundStreams = %s. MaxSLS = %s; %s",
                    inboundStreams, stack.maxSLS, this), ex);
        }
    }

    @Override
    public Collection<AspStateMachine> aspStateMachines() {
        return stateMachines.values();
    }

    @Override
    public NodeType getNodeType() {
        return stack.getNodeType();
    }

    @Override
    public int getRcCount() {
        return this.stateMachines.size();
    }

    private void write(Message message) throws IOException {
        this.write(message, 0);
    }

    private void write(Message message, int sls) throws IOException {
        try {
            M3UAMessageByteArrayOutputStream mbaos = new M3UAMessageByteArrayOutputStream(message.getMessageClass(), message.getMessageType());
            message.encode(mbaos);
            int streamNumber = 0;

            switch (message.getMessageClass()) {
                case TRANSFER_MESSAGE:
                    streamNumber = sls2Stream[sls];
                    if (streamNumber == 0) {
                        streamNumber = 1;
                    }
                    break;
                case MGMT:
                case RKM:
                case ASPSM:
                    if (message.getMessageType() == MessageType.BEAT
                            || message.getMessageType() == MessageType.BEAT_ACK
                            || message.getMessageType() == MessageType.NTFY) {
                        streamNumber = sls2Stream[new Random().nextInt(sls2Stream.length)];
                        break;
                    }
                    streamNumber = 0;
                    break;
            }

            SctpPayload sctpPayload = new SctpPayload(3, mbaos.toByteArray(), streamNumber);
            send(sctpPayload);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private void initWorkers() {
        LOGGER.info(String.format("[M3UALink]:InitWorkers: Initiating M3UAWorkerThreads. Count = %s", workersCount));
        this.workers = new ExecutorService[workersCount];
        for (int i = 0; i < workersCount; i++) {
            this.workers[i] = Executors.newSingleThreadExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "M3UAWorker");
                }
            });
        }
    }

    private void stopWorkers() {
        if (workers != null) {
            for (ExecutorService worker : workers) {
                worker.shutdownNow();
            }
        }
    }

    @Override
    public LinkType getLinkType() {
        return connectionMode;
    }

    @Override
    public void setLinkType(LinkType connectionMode) {
        this.connectionMode = connectionMode;
    }

    @Override
    public boolean isNoDelay() {
        return this.noDelay;
    }

    @Override
    public void setNoDelay(boolean noDelay) {
        this.noDelay = noDelay;
    }

    @Override
    public void setSoRcvBuf(int soRcvBuf) {
        this.soRcvBuf = soRcvBuf;
    }

    @Override
    public int getSoRcvBuf() {
        return soRcvBuf;
    }

    @Override
    public void setSoSndBuf(int soSndBuf) {
        this.soSndBuf = soSndBuf;
    }

    @Override
    public int getSoSndBuf() {
        return soSndBuf;
    }

    @Override
    public int getProtocolId() {
        return 3;//M3UA
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("M3UALink[");
        sb.append("Name = ").append(name)
                .append(sctpAssociation)
                .append(" RxBufferSize = ").append(soRcvBuf)
                .append(" TxBufferSize = ").append(soSndBuf)
                .append(" ConnectionMode = ").append(connectionMode)
                .append(" AspUpTimeOut = ").append(aspUpTimeOut)
                .append(" AspDownTimeOut = ").append(aspDownTimeOut)
                .append(" AspActiveTimeOut = ").append(aspActiveTimeOut)
                .append(" AspInactiveTimeOut = ").append(aspInactiveTimeOut);
        return sb.toString();
    }

    @Override
    public SctpAssociation getSctpAssociation() {
        return sctpAssociation;
    }

    @Override
    public int getWorkersCount() {
        return workersCount;
    }

    @Override
    public void setWorkersCount(int workersCount) {
        this.workersCount = workersCount;
    }

    @Override
    public boolean isConnected() {
        return sctpAssociation.isConnected();
    }

    public void addAs(As as) throws Exception {
        AspStateMachine aspStateMachine = new AspStateMachine(this, as);
        this.stateMachines.put(as.getRc(), aspStateMachine);
    }


    public String getLocalAddress() {
        return this.sctpAssociation.getLocalAddress().getAddress().getHostAddress();
    }

    public String getAdditionalAddress() {
        if (this.sctpAssociation.getAdditionalAddress() == null) {
            return "";
        }
        return this.sctpAssociation.getAdditionalAddress().getHostAddress();
    }

    public int getLocalPort() {
        return this.sctpAssociation.getLocalAddress().getPort();
    }

    public String getPeerAddress() {
        return this.sctpAssociation.getRemoteAddress().getAddress().getHostAddress();
    }

    public int getPeerPort() {
        return this.sctpAssociation.getRemoteAddress().getPort();
    }

    public int getStreams() {
        return this.sctpAssociation.getInStreams();
    }
}
