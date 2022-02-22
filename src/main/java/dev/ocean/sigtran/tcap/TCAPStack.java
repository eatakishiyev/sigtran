/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.apache.logging.log4j.*;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.general.ErrorReason;
import dev.ocean.sigtran.sccp.general.SCCPProvider;
import dev.ocean.sigtran.sccp.messages.MessageHandling;
import dev.ocean.sigtran.tcap.messages.AbortMessageImpl;
import dev.ocean.sigtran.tcap.messages.BeginMessageImpl;
import dev.ocean.sigtran.tcap.messages.ContinueMessageImpl;
import dev.ocean.sigtran.tcap.messages.EndMessageImpl;
import dev.ocean.sigtran.tcap.messages.MessageFactory;
import dev.ocean.sigtran.tcap.messages.MessageType;
import dev.ocean.sigtran.tcap.messages.UnknownMessage;
import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.tcap.primitives.tc.PAbortCause;
import dev.ocean.sigtran.tcap.primitives.tc.TCBegin;
import dev.ocean.sigtran.tcap.primitives.tc.TCContinue;
import dev.ocean.sigtran.tcap.primitives.tc.TCEnd;
import dev.ocean.sigtran.tcap.primitives.tc.TCLCancel;
import dev.ocean.sigtran.tcap.primitives.tc.TCPAbort;
import dev.ocean.sigtran.tcap.primitives.tc.TCUAbort;
import dev.ocean.sigtran.tcap.primitives.tr.TRNotice;
import dev.ocean.sigtran.utils.ByteUtils;
import java.util.concurrent.ThreadFactory;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author root
 */
public class TCAPStack implements TCAPStackMBean {

    private final Logger logger = LogManager.getLogger(TCAPStack.class);
    private final TCAPProviderImpl tcapProvider;
    private boolean started = false;
    private SCCPProvider sccpProvider;

    protected long keepAliveTime = 60000;
    private String mbeanName = "dev.ocean.sigtran.management:type=Management,name=TCAP";
    private TCUser tcUser;

//    protected ISMStorage ismStorage;
//    protected DialogueStorage dialogueStorage;
    private final ConcurrentHashMap<Long, TCAPDialogue> dialogues = new ConcurrentHashMap<>();

    protected final ScheduledExecutorService keepAliveScheduler = Executors.newScheduledThreadPool(5, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "TCAPKeepAliveTimer");
        }
    });
    protected final ScheduledExecutorService invocationScheduler = Executors.newScheduledThreadPool(5, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "TCAPInvocationsTimer");
        }
    });
    protected final ScheduledExecutorService rejectScheduler = Executors.newScheduledThreadPool(5, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "TCAPRejectTimer");
        }
    });

    private final TransactionIdPool transactionIdPool;
    private final String stackName;

    /**
     *
     * @param stackName stack name
     * @param minTrId start of transaction id
     * @param maxTrId end of transaction id
     * @param keepAliveTime TCAP dialog keep alive timer in seconds
     * @throws Exception
     */
    public TCAPStack(String stackName, long minTrId, long maxTrId, long keepAliveTime) throws Exception {
        logger.info("==========================| Starting TCAP Stack |==========================");
        this.stackName = stackName;
        this.keepAliveTime = keepAliveTime;
        this.tcapProvider = new TCAPProviderImpl(this);
        this.mbeanName = mbeanName.concat("-").concat(stackName);

        this.registerMbean();

        this.started = true;
        this.transactionIdPool = new TransactionIdPool(minTrId, maxTrId);
        logger.info("==========================| TCAP Stack started |==========================");
    }

    private void registerMbean() {
        try {
            if (mbeanName != null && !mbeanName.trim().equals("")) {
                logger.info(String.format("Registering MBean %s", this.getMbeanName()));
                MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
                mbeanServer.registerMBean(this, new ObjectName(this.getMbeanName()));
            }

        } catch (Exception ex) {
            logger.error("Error while initialize TCAPStack ", ex);
        }
    }

    private void unregisterMbean() {
        try {
            if (mbeanName != null && !mbeanName.trim().equals("")) {
                logger.info(String.format("UnRegistering MBean %s", this.getMbeanName()));
                MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
                mbeanServer.unregisterMBean(new ObjectName(this.getMbeanName()));
            }
        } catch (Exception ex) {
            logger.error("Error while initialize TCAPStack ", ex);
        }
    }

    public void stop() {
        logger.info("Stopping TCAP Stack...");
        this.unregisterMbean();
        this.started = false;
        logger.info("TCAP Stack stopped.");

    }

    /**
     * @return the sCCPProviderImpl
     */
    public SCCPProvider getSCCPProvider() {
        return sccpProvider;
    }

    /**
     * @return the started
     */
    public boolean isStarted() {
        return started;
    }

    @Override
    public long getKeepAliveTimer() {
        return this.keepAliveTime;
    }

    @Override
    public void setKeepAliveTimer(long keepAliveTimer) {
        this.keepAliveTime = keepAliveTimer;
    }

    /**
     * @return the mbeanName
     */
    public String getMbeanName() {
        return mbeanName;
    }

    /**
     * @param mbeanName the mbeanName to set
     */
    public void setMbeanName(String mbeanName) {
        this.mbeanName = mbeanName;
    }

    @Override
    public void onMessage(SCCPAddress calledParty, SCCPAddress callingParty, byte[] userData, boolean sequenceControl, Integer sequenceNumber, MessageHandling messageHandling) {
        if (!this.isStarted()) {
            logger.info("Message received from SCCP but TCAP stack is down.");
            return;
        }

        AsnInputStream ais = new AsnInputStream(userData);
        try {
            int tag = ais.readTag();
            MessageType messageType = MessageType.getInstance(tag);

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Received message: MessageType = %s CalledParty [%s] CallingParty[%s] SequenceControl = %s SequenceNumber = %d MessageHandling = %s UserData = %s",
                        messageType, calledParty, callingParty, sequenceControl, sequenceNumber, messageHandling, ByteUtils.bytes2Hex(userData)));
            }
            switch (messageType) {
                case BEGIN:
                    tcapProvider.tcapStat.receivedBeginCount.incrementAndGet();
                    this.processBegin(userData, calledParty, callingParty, ais,
                            sequenceControl, sequenceNumber, messageHandling);
                    break;
                case CONTINUE:
                    tcapProvider.tcapStat.receivedContinueCount.incrementAndGet();
                    this.processContinue(userData, ais, callingParty, calledParty,
                            sequenceControl, sequenceNumber, messageHandling);
                    break;
                case END:
                    tcapProvider.tcapStat.receivedEndCount.incrementAndGet();
                    this.processEnd(userData, ais, callingParty, calledParty,
                            sequenceControl, sequenceNumber, messageHandling);
                    break;
                case ABORT:
                    tcapProvider.tcapStat.receivedAbortCount.incrementAndGet();
                    this.processAbort(userData, ais, callingParty, sequenceControl,
                            sequenceNumber, messageHandling);
                    break;
                default:
                    tcapProvider.tcapStat.receivedUnknownMessage.incrementAndGet();
                    this.processUnknownMessage(userData, ais, callingParty, calledParty,
                            sequenceControl, sequenceNumber, messageHandling);
                    break;
            }
        } catch (IOException ex) {
            tcapProvider.tcapStat.receivedUnparsableMessage.incrementAndGet();
            logger.error(String.format("Failure on message receive: %s", ex));
        }
    }

    @Override
    public void onNotice(SCCPAddress calledParty, SCCPAddress callingParty, byte[] userData, ErrorReason errorReason, int importance) {
        AsnInputStream asnInputStream = new AsnInputStream(userData);
        try {
            tcapProvider.tcapStat.receivedNoticeCount.incrementAndGet();
            asnInputStream.readTag();

            UnknownMessage unknownMessage = MessageFactory.createUnknown(asnInputStream);
            Long otid = unknownMessage.getOriginatingTransactionId();

            if (otid != null) {
                TCAPDialogue tcapDialogue = this.dialogues.get(otid);
                if (tcapDialogue != null) {
                    tcapDialogue.setRawData(userData);
                    tcapDialogue.onNotice(otid, callingParty, calledParty, errorReason);
                }
            }

        } catch (IOException ex) {
            logger.error("Error occured while onNotice. {}", ex);
        }
    }

    private void processBegin(byte[] userData, SCCPAddress calledParty,
            SCCPAddress callingParty, AsnInputStream ais, boolean sequenceControl,
            Integer sequenceNumber, MessageHandling messageHandling) {
        BeginMessageImpl beginMessage = null;
        int currPos = ais.position();
        try {

            //create only TR portion.  Also check for correctly formatted TR portion.
            beginMessage = MessageFactory.createBegin(ais);

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("TC_BEGIN received: %s", beginMessage));
            }
            //Assign local transaction id
            TCAPDialogue tcapTransactionStateMachine
                    = this.createDialogue(false, new QoS(messageHandling,
                            sequenceControl, sequenceNumber));

            tcapTransactionStateMachine.setRawData(userData);
            tcapTransactionStateMachine.beginReceived(userData, beginMessage,
                    callingParty, calledParty);

        } catch (IncorrectSyntaxException ex) {
            try {
                tcapProvider.tcapStat.receivedUnparsableMessage.incrementAndGet();
                ais.position(currPos);
                UnknownMessage unknownMessage = MessageFactory.createUnknown(ais);

                //OTID derivable
                if (unknownMessage.getOriginatingTransactionId() != null) {
                    this.assembleTrPortionOfAbortMessage(unknownMessage.getOriginatingTransactionId(),
                            calledParty, callingParty, PAbortCause.BADDLY_FORMATTED_TRANSACTION_PORTION,
                            new QoS(messageHandling, sequenceControl, sequenceNumber));
                }

                logger.error(String.format("Incorrect syntax of received BEGIN message:"
                        + "ClngPty = %s; CldPty = %s; %s; Data = %s", callingParty, calledParty,
                        unknownMessage, ByteUtils.bytes2Hex(userData)), ex);
            } catch (Exception e) {
                logger.error(String.format("Unexpected error during process BEGIN message:"
                        + "ClngPty = %s; CldPty = %s; Data = %s", callingParty, calledParty,
                        ByteUtils.bytes2Hex(userData)), e);
            }

        } catch (ResourceLimitationException exception) {

            if (beginMessage != null) {
                //Specification Q.774 Figure A.3 sheet 1 of 4 (6)
                this.assembleTrPortionOfAbortMessage(beginMessage.getOriginatingTransactionId(),
                        callingParty, calledParty, PAbortCause.RESOURCE_LIMITATION,
                        new QoS(messageHandling, sequenceControl, sequenceNumber));
            }
            logger.error(String.format("ResourceLimitation during handle BEGIN message:"
                    + "ClngPty = %s; CldPty = %s; %s; Data = %s", callingParty, calledParty,
                    beginMessage, ByteUtils.bytes2Hex(userData)), exception);
        }
    }

    private void processContinue(byte[] userData, AsnInputStream ais, SCCPAddress callingParty,
            SCCPAddress calledParty, boolean sequenceControl, Integer sequenceNumber,
            MessageHandling messageHandling) {
        int currPos = ais.position();
        Long destinationTransactionID = null;
        ContinueMessageImpl continueMessage = null;
        try {
            continueMessage = MessageFactory.createContinue(ais);
            destinationTransactionID = continueMessage.getDestinationTransactionId();

            TCAPDialogue tcapDialogue = this.dialogues.get(destinationTransactionID);
            //DTID assigned?
            if (tcapDialogue == null) {
                logger.error(String.format("No transaction state machine found. "
                        + "Try to prepare AbortMessage. %s ", continueMessage));
                //OTID derivable?
                if (continueMessage.getOriginatingTransactionId() != null) {
                    this.assembleTrPortionOfAbortMessage(continueMessage.getOriginatingTransactionId(),
                            calledParty, callingParty, PAbortCause.UNRECOGNIZED_TRANSACTION_ID,
                            new QoS(messageHandling, sequenceControl, sequenceNumber));
                }
            } else {
                tcapDialogue.setRawData(userData);
                tcapDialogue.continueReceived(continueMessage, callingParty,
                        new QoS(messageHandling, sequenceControl, sequenceNumber));
            }
        } catch (IncorrectSyntaxException ex) {
            try {
                tcapProvider.tcapStat.receivedUnparsableMessage.incrementAndGet();
                ais.position(currPos);
                UnknownMessage unknownMessage = MessageFactory.createUnknown(ais);
                destinationTransactionID = unknownMessage.getDestinationTransactionId();

                //OTID derivable?
                if (unknownMessage.getOriginatingTransactionId() != null) {

                    this.assembleTrPortionOfAbortMessage(unknownMessage.getOriginatingTransactionId(),
                            calledParty, callingParty, PAbortCause.BADDLY_FORMATTED_TRANSACTION_PORTION,
                            new QoS(messageHandling, sequenceControl, sequenceNumber));
                    //DTID assigned?
                    if (destinationTransactionID != null) {
                        TCAPDialogue tcapDialogue = this.dialogues.get(destinationTransactionID);
                        if (tcapDialogue != null) {
                            tcapDialogue.localAbort(PAbortCause.BADDLY_FORMATTED_TRANSACTION_PORTION);
                            this.dialogueTerminated(tcapDialogue);
                        }
                    }
                }
                logger.error(String.format("Incorrect syntax of received CONTINUE message:"
                        + "ClngPty = %s; CldPty = %s; %s; Data = %s", callingParty, calledParty,
                        unknownMessage, ais.toString()), ex);
            } catch (Exception e) {
                logger.error(String.format("Unexpected exception during handle CONTINUE message:"
                        + "ClngPty = %s; CldPty = %s; %s; %s", callingParty, calledParty,
                        continueMessage, ais), e);
            }
        }
    }

    private void processEnd(byte[] userData, AsnInputStream ais, SCCPAddress callingParty,
            SCCPAddress calledParty, boolean sequenceControl, Integer sequenceNumber, MessageHandling messageHandling) {
        int currPos = ais.position();
        Long destinationTransactionID = null;
        EndMessageImpl endMessage = null;
        try {
            endMessage = MessageFactory.createEnd(ais);
            destinationTransactionID = endMessage.getDestinationTransactionId();
            TCAPDialogue tcapDialogue = this.dialogues.get(destinationTransactionID);
            //Silent discard the message
            if (tcapDialogue == null) {
                logger.error("No TcapTransactionStateMachine found for ENDMESSAGE. DTID " + destinationTransactionID);
            } else {
                tcapDialogue.setRawData(userData);
                tcapDialogue.endReceived(endMessage, callingParty, new QoS(messageHandling, sequenceControl, sequenceNumber));
            }
        } catch (IncorrectSyntaxException ex) {
            try {
                tcapProvider.tcapStat.receivedUnparsableMessage.incrementAndGet();
                ais.position(currPos);
                UnknownMessage unknownMessage = MessageFactory.createUnknown(ais);

                //DTID assigned?
                if (unknownMessage.getDestinationTransactionId() != null) {
                    TCAPDialogue tcapDialogue = this.dialogues.get(unknownMessage.getDestinationTransactionId());
                    if (tcapDialogue != null) {
                        tcapDialogue.localAbort(PAbortCause.BADDLY_FORMATTED_TRANSACTION_PORTION);
                        this.dialogueTerminated(tcapDialogue);
                    }
                }
                logger.error(String.format("Incorrect syntax of received END message:"
                        + "ClngPty = %s; CldPty = %s; %s; Data = %s", callingParty, calledParty,
                        unknownMessage, ais.toString()), ex);
            } catch (Exception e) {
                logger.error(String.format("Unexpected exception during handle END message:"
                        + "ClngPty = %s; CldPty = %s; %s; Data = %s", callingParty, calledParty,
                        endMessage, ais.toString()), e);
            }
        }
    }

    private void processAbort(byte[] userData, AsnInputStream ais, SCCPAddress callingParty,
            boolean sequenceControl, Integer sequenceNumber, MessageHandling messageHandling) {
        int currPoss = ais.position();
        Long destinationTransactionID = null;

        try {
            AbortMessageImpl abortMessage = MessageFactory.createAbort(ais);
            destinationTransactionID = abortMessage.getDestinationTransactionId();

            TCAPDialogue tcapDialogue = this.dialogues.get(destinationTransactionID);
            //Silently discard message if DTID not assigned
            if (tcapDialogue == null) {
                logger.error("Abort received, but no tcapTransactionStateMachine to process it. DTID =" + abortMessage.getDestinationTransactionId());
            } else {
                tcapDialogue.setRawData(userData);
                tcapDialogue.abortReceived(abortMessage, callingParty, new QoS(messageHandling, sequenceControl, sequenceNumber));
            }
        } catch (IncorrectSyntaxException ex) {
            tcapProvider.tcapStat.receivedUnparsableMessage.incrementAndGet();
            logger.error("Error occured during receive abort message: {}", ex);
            try {
                ais.position(currPoss);
                UnknownMessage unknownMessage = MessageFactory.createUnknown(ais);

                //DTID assigned?
                if (unknownMessage.getDestinationTransactionId() != null) {
                    TCAPDialogue tcapDialogue = this.dialogues.get(unknownMessage.getDestinationTransactionId());
                    if (tcapDialogue != null) {
                        tcapDialogue.localAbort(PAbortCause.BADDLY_FORMATTED_TRANSACTION_PORTION);
                        this.dialogueTerminated(tcapDialogue);
                    }
                }
            } catch (Exception e) {
                logger.error("Error occured during receive end message: {}", e);
            }

        }
    }

    private void processUnknownMessage(byte[] userData, AsnInputStream ais, SCCPAddress callingParty,
            SCCPAddress calledParty, boolean sequenceControl, Integer sequenceNumber,
            MessageHandling messageHandling) {
        UnknownMessage unknownMessage = MessageFactory.createUnknown(ais);

        //OTID derivable?
        if (unknownMessage.getOriginatingTransactionId() != null) {
            this.assembleTrPortionOfAbortMessage(unknownMessage.getOriginatingTransactionId(), callingParty, calledParty, PAbortCause.UNRECOGNIZED_MESSAGE_TYPE, new QoS(messageHandling, sequenceControl, sequenceNumber));

            //DTID assigned?
            if (unknownMessage.getDestinationTransactionId() != null) {
                TCAPDialogue tcapDialogue = this.dialogues.get(unknownMessage.getDestinationTransactionId());
                if (tcapDialogue != null) {
                    tcapDialogue.setRawData(userData);
                    tcapDialogue.localAbort(PAbortCause.UNRECOGNIZED_MESSAGE_TYPE);
                }
            }
        }
    }

    private void assembleTrPortionOfAbortMessage(Long transactionId, SCCPAddress calledParty, SCCPAddress callingParty, PAbortCause pAbortCause, QoS qos) {
        try {
            AsnOutputStream aos = new AsnOutputStream();
            AbortMessageImpl abortMessage = MessageFactory.createAbort();
            abortMessage.setDestinationTransactionId(transactionId);
            abortMessage.setPAbortCause(pAbortCause);
            abortMessage.encode(aos);
            this.tcapProvider.send(callingParty, calledParty, qos, aos.toByteArray());
        } catch (IncorrectSyntaxException ex) {
            logger.error("Error occured during send AbortMessage. Error {}", ex);
        }
    }

    //Remotely initiate dialogue
    protected TCAPDialogue createDialogue(boolean localDialogue, QoS qos) throws ResourceLimitationException {
        Long dialogueId = transactionIdPool.getId();
        TCAPDialogue tcapDialogue = new TCAPDialogue(this, dialogueId, localDialogue, qos);
        dialogues.put(dialogueId, tcapDialogue);

        tcapProvider.tcapStat.createdDialogueCount.incrementAndGet();
        tcapProvider.tcapStat.concurrentDialogueCount.incrementAndGet();
//        dialogueStorage.initDialogue(tcapDialogue);OffHeap
        return tcapDialogue;
    }

    @Override
    public void setUser(TCUser tcUser) {
        this.tcUser = tcUser;
    }

    @Override
    public TCUser getUser() {
        return this.tcUser;
    }

    //========================Dialogue portion notifications====================
    public void onBegin(byte[] encodedData, TCBegin indication) {
        if (tcUser != null) {
            tcUser.onBegin(encodedData, indication);
        } else {
            logger.error("error: No user registered.");
        }
    }

    public void onContinue(TCContinue indication) {
        if (tcUser != null) {
            tcUser.onContinue(indication);
        } else {
            logger.error("error: No user registered.");
        }
    }

    public void onEnd(TCEnd indication) {
        if (tcUser != null) {
            tcUser.onEnd(indication);
        } else {
            logger.error("error: No user registered.");
        }
    }

    public void onPAbort(TCPAbort indication) {
        if (tcUser != null) {
            tcUser.onPAbort(indication);
        } else {
            logger.error("error: No user registered.");
        }
    }

    public void onUAbort(TCUAbort indication) {
        if (tcUser != null) {
            tcUser.onUAbort(indication);
        } else {
            logger.error("error: No user registered.");
        }
    }

    public void onNotice(TRNotice tRNotice) {
        if (tcUser != null) {
            tcUser.onNotice(tRNotice);
        } else {
            logger.error("error: No user registered.");
        }
    }

    public void onTCLCancel(TCAPDialogue dialogue, short invokeId, int opCode) {
        TCLCancel tcLCancel = new TCLCancel();
        tcLCancel.setInvokeId(invokeId);
        tcLCancel.setOperationCode(opCode);
        tcLCancel.setDialogue(dialogue);

        if (tcUser != null) {
            tcUser.onTCLCancel(tcLCancel);
        } else {
            logger.error("error: No user registered.");
        }
    }

    public void onDialogueTimeOut(long dialogueId) {
        TCAPDialogue dialogue = this.dialogues.get(dialogueId);
        tcapProvider.tcapStat.timeoutedDialogueCount.incrementAndGet();
        if (dialogue == null) {
            logger.warn("DialogueTimeOut received, no dialogue found : " + dialogueId);
            return;
        }

        dialogueTerminated(dialogue);
        if (tcUser != null) {
            dialogue.mutex.lock();
            try {
                tcUser.onDialogueTimeOut(dialogueId);
            } finally {
                dialogue.mutex.unlock();
            }
        } else {
            logger.warn(String.format("[TCAPStack]:Dialogue expired. No user "
                    + "found to fire event. %s", dialogue));
        }
    }

    public void dialogueTerminated(TCAPDialogue dialogue) {
        if (dialogue != null) {
            this.dialogues.remove(dialogue.getDialogueId());
            transactionIdPool.releaseId(dialogue.getDialogueId());
            tcapProvider.tcapStat.concurrentDialogueCount.decrementAndGet();
            dialogue.terminated();
        } else {
            logger.warn(String.format("[TCAPStack]:DialogueTerminated: Dialogue"
                    + " not found. Can not terminate ISM, cancel keepAliveTimer,"
                    + " release dialogueId! %s", dialogue));
        }
    }

    public TCAPProviderImpl getProvider() {
        return tcapProvider;
    }

    protected final TCAPDialogue getDialogue(Long dialogueId) {
        return dialogues.get(dialogueId);
    }

    @Override
    public int totalDialogueCounts() {
        return tcapProvider.tcapStat.concurrentDialogueCount.get();
    }

    public String getStackName() {
        return stackName;
    }

    @Override
    public void setSccpProvider(SCCPProvider sccpProvider) {
        this.sccpProvider = sccpProvider;
    }

}
