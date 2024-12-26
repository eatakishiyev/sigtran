/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap;

import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.general.ErrorReason;
import azrc.az.sigtran.tcap.components.*;
import azrc.az.sigtran.tcap.dialogueAPDU.*;
import azrc.az.sigtran.tcap.dialogues.intrefaces.DialogueAbortPDU;
import azrc.az.sigtran.tcap.dialogues.intrefaces.DialoguePDU;
import azrc.az.sigtran.tcap.dialogues.intrefaces.DialogueResponsePDU;
import azrc.az.sigtran.tcap.messages.*;
import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.tcap.parameters.*;
import azrc.az.sigtran.tcap.parameters.interfaces.ApplicationContext;
import azrc.az.sigtran.tcap.parameters.interfaces.Component;
import azrc.az.sigtran.tcap.parameters.interfaces.DialogueServiceProviderDiagnostic;
import azrc.az.sigtran.tcap.parameters.interfaces.DialogueServiceUserDiagnostic;
import azrc.az.sigtran.tcap.primitives.AbortReason;
import azrc.az.sigtran.tcap.primitives.tc.*;
import azrc.az.sigtran.tcap.primitives.tr.TRNotice;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author eatakishiyev
 */
public class TCAPDialogue implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TCAPDialogue.class);
    protected transient TCAPStack tcapStack;
    private Long dialogueId;

    //Serailizable fields
    private long remoteTransactionId;
    private SCCPAddress callingParty;
    private SCCPAddress calledParty;
    private QoS qos;
    private boolean localInitiated;
    private ApplicationContext ac;
    private TCAPDialogueState state;
    //Component sublayer fields
    private final List<Component> components = Collections.synchronizedList(new ArrayList());
    protected ReentrantLock mutex;
    private final AtomicInteger invokeIdCounter = new AtomicInteger(0);
    protected ScheduledFuture<?> keepAliveTask;
    private final ConcurrentHashMap<Short, InvocationStateMachine> ismTable = new ConcurrentHashMap<>();
    private byte[] rawData;

    public TCAPDialogue(TCAPStack tcapStack, Long dialogueId, boolean localInitiated, QoS qos) {
        this.mutex = new ReentrantLock();
        this.tcapStack = tcapStack;
        this.dialogueId = dialogueId;
        this.localInitiated = localInitiated;
        this.qos = qos;
        this.state = TCAPDialogueState.IDLE;
        if(tcapStack != null) {
            this.keepAliveTask = tcapStack.keepAliveScheduler.schedule(this, tcapStack.keepAliveTime, TimeUnit.SECONDS);
        }
    }

    public void userCancel(Short invokeId) throws Exception {
        try {
            mutex.lock();
            Iterator<Component> componentIter = this.components.iterator();
            while (componentIter.hasNext()) {
                Component component = componentIter.next();

                if (component.getComponentType() == ComponentType.INVOKE
                        && component.getInvokeId().shortValue() == invokeId) {
                    componentIter.remove();
                } else {
                    this.terminateIsm(invokeId);
                }
            }
        } finally {
            mutex.unlock();
        }
    }

    public void beginReceived(byte[] encodedData, BeginMessageImpl beginMessage, SCCPAddress callingParty, SCCPAddress calledParty) {
        /* ITU Q.774 Page 12
             * The receiving transaction sub-layer stores the received Originating Address as the destination address
             for this transaction and its own address as the Originating Address (from memory or from the
             destination address of the received N-UNITDATA indication primitive). The TC-user receives a
             TC-BEGIN indication primitive containing the received Destination Address and Originating
             Address.
         */
        try {
            mutex.lock();
            this.remoteTransactionId = beginMessage.getOriginatingTransactionId();//Remote transaction id

            this.callingParty = callingParty;//Remote address
            this.calledParty = calledParty;//Local address

            if (beginMessage.getDialogPortion() != null
                    && beginMessage.getDialogPortion().length > 0) {
                DialoguePortionImpl dialoguePortion = new DialoguePortionImpl();
                DialoguePDU dialogueAPDU;
                try {
                    dialoguePortion.decode(new AsnInputStream(beginMessage.getDialogPortion()));
                    dialogueAPDU = dialoguePortion.getDialogueAPDU();

                    if (dialogueAPDU.getDialogueType() != DialoguePDU.DIALOGUE_REQUEST_APDU_TAG) {
                        throw new IncorrectSyntaxException();
                    }
                } catch (IncorrectSyntaxException ex) {
                    //See ITU Q.774 3.2.2 Abnormal procedures
                    DialogueAbortPDUImpl abortDialogue = DialogueFactory.createDialogueAbort(AbortSource.DIALOGUE_SERVICE_PROVIDER);
                    dialoguePortion = new DialoguePortionImpl();
                    dialoguePortion.setAsn(true);
                    dialoguePortion.setOid(true);
                    dialoguePortion.setOidValue(ObjectIdentifiers.DIALOGUE_AS_ID.value());
                    dialoguePortion.setDialogAPDU(abortDialogue);

                    AsnOutputStream aos = new AsnOutputStream();
                    try {
                        dialoguePortion.encode(aos);

                        AbortMessageImpl abortMessage = MessageFactory.createAbort();
                        abortMessage.setUAbortCause(aos.toByteArray());

                        aos.reset();

                        abortMessage.setDestinationTransactionId(beginMessage.getOriginatingTransactionId());

                        abortMessage.encode(aos);

                        //swap calling party and called party,send aport message
                        this.tcapStack.getProvider().send(callingParty, calledParty,
                                this.qos, aos.toByteArray());

                    } catch (IncorrectSyntaxException _ex) {
                        LOGGER.error("{}", _ex);
                    }
                    tcapStack.dialogueTerminated(this);
                    //Discard components awaiting transmission
                    components.clear();
                    return;
                }

                DialogueRequest dialogueRequest = (DialogueRequest) dialogueAPDU;
                //Protocol version 1 supported?
                if (dialogueRequest.getProtocolVersion().isCorrect()) {

                    //Set AC mode. This is set when an AARQ APDU is present
                    //as user data in the TR-BEGIN primitive
                    this.ac = dialogueRequest.getApplicationContext();

                    TCBegin beginIndication = new TCBegin(this);

                    beginIndication.setApplicationContext(dialogueRequest.getApplicationContext());
                    beginIndication.setUserInformation(dialogueAPDU.getUserInformation());
                    if (beginMessage.getComponents() != null
                            && beginMessage.getComponents().length > 0) {
                        beginIndication.setComponents(this.doComponents(beginMessage.getComponents()));
                    }
                    this.state = TCAPDialogueState.INITIATION_RECEIVED;

//                    this.tcapStack.dialogueStorage.store(this);
                    tcapStack.onBegin(encodedData, beginIndication);

                } else {
                    //Protocol version 1 is not supported
                    AssociateSourceDiagnosticImpl associateResultDiagnostic = new AssociateSourceDiagnosticImpl(DialogueServiceProviderDiagnostic.NO_COMMON_DIALOGUE_PORTION);
                    AssociateResult associateResult = ParameterFactory.createAssociateResult(Result.REJECT_PERMANENT);
                    DialogueResponsePDU dialogueResponsePDUImpl = DialogueFactory.createDialogueResponse(dialogueRequest.getApplicationContext(), associateResult, associateResultDiagnostic);

                    AsnOutputStream aos = new AsnOutputStream();

                    dialoguePortion = new DialoguePortionImpl();
                    dialoguePortion.setDialogAPDU(dialogueResponsePDUImpl);
                    dialoguePortion.setOidValue(ObjectIdentifiers.DIALOGUE_AS_ID.value());
                    dialoguePortion.setAsn(true);
                    dialoguePortion.setOid(true);

                    try {
                        dialoguePortion.encode(aos);
                        AbortMessageImpl abortMessage = MessageFactory.createAbort();
                        abortMessage.setUAbortCause(aos.toByteArray());

                        aos.reset();

                        abortMessage.setDestinationTransactionId(beginMessage.getOriginatingTransactionId());

                        abortMessage.encode(aos);

                        this.tcapStack.getProvider().send(callingParty, calledParty, qos, aos.toByteArray());
                    } catch (IncorrectSyntaxException ex) {
                        LOGGER.error("{}", ex);
                    } finally {
                        tcapStack.dialogueTerminated(this);
                        //Discard components awaiting transmission
                        components.clear();
                    }
                }
            } else {
                //DialoguePortion not included
                TCBegin beginIndciation = new TCBegin(this);

                //Dialogue Poriton not included and because of this no ApplicationContext mode set , send null application context to user
                beginIndciation.setApplicationContext(null);
                beginIndciation.setDialogue(this);
                if (beginMessage.getComponents() != null
                        && beginMessage.getComponents().length > 0) {
                    beginIndciation.setComponents(this.doComponents(beginMessage.getComponents()));
                }

                state = TCAPDialogueState.INITIATION_RECEIVED;

//                this.tcapStack.dialogueStorage.store(this);
                tcapStack.onBegin(encodedData, beginIndciation);
            }
        } finally {
            mutex.unlock();
        }
    }

    public void continueReceived(ContinueMessageImpl continueMessage, SCCPAddress callingParty, QoS qos) {
        try {
            mutex.lock();
            switch (this.state) {
                case INITIATION_SENT:
                    try {

                        this.remoteTransactionId = continueMessage.getOriginatingTransactionId();//Remote Transaction Id
                        this.calledParty = callingParty;//Remote address                

                        TCContinue continueIndication = new TCContinue();

                        if (continueMessage.getDialogPortion() != null
                                && continueMessage.getDialogPortion().length > 0) {
                            if (this.isAcModeSet()) {
                                DialoguePortionImpl dialoguePortion = DialogueFactory.createDialoguePortion(new AsnInputStream(continueMessage.getDialogPortion()));

                                DialoguePDU dialoguePDU = dialoguePortion.getDialogueAPDU();
                                if (dialoguePDU.getDialogueType() != DialoguePDU.DIALOGUE_RESPONSE_APDU_TAG) {
                                    throw new IncorrectSyntaxException();
                                }

                                DialogueResponsePDUImpl dialogueAARE = (DialogueResponsePDUImpl) dialoguePDU;

                                continueIndication.setApplicationContextName(this.ac);
                                continueIndication.setDialogue(this);
                                continueIndication.setUserInformation(dialogueAARE.getUserInformation());
                                if (continueMessage.getComponents() != null
                                        && continueMessage.getComponents().length > 0) {
                                    continueIndication.setComponents(this.doComponents(continueMessage.getComponents()));
                                }

                                this.state = TCAPDialogueState.ACTIVE;

                                tcapStack.onContinue(continueIndication);
                            } else {
                                throw new IncorrectSyntaxException();
                            }
                        } else {
                            if (this.isAcModeSet()) {
                                throw new IncorrectSyntaxException();
                            }

                            continueIndication.setApplicationContextName(this.ac);//null
                            continueIndication.setDialogue(this);
                            if (continueMessage.getComponents() != null
                                    && continueMessage.getComponents().length > 0) {
                                continueIndication.setComponents(this.doComponents(continueMessage.getComponents()));
                            }

                            this.state = TCAPDialogueState.ACTIVE;

//                            this.tcapStack.dialogueStorage.store(this);
                            tcapStack.onContinue(continueIndication);

                        }
                    } catch (IncorrectSyntaxException ex) {
                        try {
                            LOGGER.error("ErrorOccured: " + dialogueId + " " + this.state, ex);
                            TCPAbort tcPAbort = new TCPAbort();
                            tcPAbort.setDialogue(this);
                            tcPAbort.setpAbort(PAbortCause.ABNORMAL_DIALOGUE);
                            tcapStack.onPAbort(tcPAbort);

                            DialogueAbortPDUImpl dialogueAbortAPDUImpl = DialogueFactory.createDialogueAbort(AbortSource.DIALOGUE_SERVICE_PROVIDER);
                            DialoguePortionImpl dialoguePortion = new DialoguePortionImpl();
                            dialoguePortion.setOidValue(ObjectIdentifiers.DIALOGUE_AS_ID.value());
                            dialoguePortion.setAsn(true);
                            dialoguePortion.setOid(true);
                            dialoguePortion.setDialogAPDU(dialogueAbortAPDUImpl);

                            AsnOutputStream aos = new AsnOutputStream();
                            dialoguePortion.encode(aos);
                        } catch (IncorrectSyntaxException _ex) {
                            LOGGER.error("ErrorOccured: ", _ex);
                        }
                        tcapStack.dialogueTerminated(this);
                        //Discard components awaiting transmission
                        components.clear();
                    }
                    break;

                case ACTIVE:
                    if (continueMessage.getDialogPortion() != null
                            && continueMessage.getDialogPortion().length > 0) {
                        DialoguePortionImpl dialoguePortion;
                        try {
                            dialoguePortion = DialogueFactory.createDialoguePortion(new AsnInputStream(continueMessage.getDialogPortion()));
                            /**
                             * No dialogue control APDUs are exchanged during
                             * this stage of the transaction. If dialogue
                             * control APDUs were exchanged during the
                             * establishment of the dialogue, the Application
                             * Context Name sent in the AARE APDU is assumed to
                             * be the application context in place between the
                             * TC-users for the life-time of the dialogue. TC
                             * does not verify this except in that it treats the
                             * presence of a dialogue control APDU during this
                             * stage of the dialogue as an abnormal event.
                             * During this phase, a dialogue portion with a
                             * user-defined abstract syntax may optionally be
                             * present.
                             */
                            if (dialoguePortion.isAsn()) {
                                throw new IncorrectSyntaxException();
                            }

                            if (this.isAcModeSet()) {
                                TCContinue tcContinueIndication = new TCContinue();
                                tcContinueIndication.setApplicationContextName(this.ac);
                                tcContinueIndication.setDialogue(this);

                                if (continueMessage.getComponents() != null
                                        && continueMessage.getComponents().length > 0) {
                                    tcContinueIndication.setComponents(this.doComponents(continueMessage.getComponents()));
                                }

                                this.state = TCAPDialogueState.ACTIVE;
                                tcapStack.onContinue(tcContinueIndication);

                                //no needing to persist data , because no any data changes occured
                            } else {
                                throw new IncorrectSyntaxException();
                            }

                        } catch (IncorrectSyntaxException ex) {
                            LOGGER.error("ErrorOccured: " + dialogueId + " " + this.state, ex);
                            TCPAbort tcPAbort = new TCPAbort();
                            tcPAbort.setDialogue(this);
                            tcPAbort.setpAbort(PAbortCause.ABNORMAL_DIALOGUE);
                            tcapStack.onPAbort(tcPAbort);

                            try {
                                DialogueAbortPDUImpl dialogueAbortAPDUImpl = DialogueFactory.createDialogueAbort(AbortSource.DIALOGUE_SERVICE_PROVIDER);
                                dialoguePortion = new DialoguePortionImpl();
                                dialoguePortion.setOidValue(ObjectIdentifiers.DIALOGUE_AS_ID.value());
                                dialoguePortion.setAsn(true);
                                dialoguePortion.setOid(true);
                                dialoguePortion.setDialogAPDU(dialogueAbortAPDUImpl);

                                AsnOutputStream aos = new AsnOutputStream();
                                dialoguePortion.encode(aos);

                                AbortMessageImpl abortMessage = MessageFactory.createAbort();
                                abortMessage.setDestinationTransactionId(this.remoteTransactionId);

                                abortMessage.setUAbortCause(aos.toByteArray());

                                aos.reset();

                                abortMessage.encode(aos);

                                this.tcapStack.getProvider().send(this.callingParty,
                                        this.calledParty, qos, aos.toByteArray());

                            } catch (IncorrectSyntaxException _ex) {
                                LOGGER.error("ErrorOccured: ", _ex);
                            }

                            tcapStack.dialogueTerminated(this);
                            //Discard components awaiting transmission
                            components.clear();

                        }
                    } else {
                        TCContinue tcContinueIndication = new TCContinue();
                        tcContinueIndication.setApplicationContextName(this.ac);
                        tcContinueIndication.setDialogue(this);

                        if (continueMessage.getComponents() != null
                                && continueMessage.getComponents().length > 0) {
                            tcContinueIndication.setComponents(this.doComponents(continueMessage.getComponents()));
                        }

                        this.state = TCAPDialogueState.ACTIVE;

//                        this.tcapStack.dialogueStorage.store(this);
                        tcapStack.onContinue(tcContinueIndication);
                    }
                    break;
            }
        } finally {
            mutex.unlock();
        }
    }

    public void endReceived(EndMessageImpl endMessage, SCCPAddress callingParty, QoS qos) {
        try {
            mutex.lock();
            switch (this.state) {
                case INITIATION_SENT:
                    try {
                        TCEnd tcEndIndication = new TCEnd();
                        this.calledParty = callingParty;//Remote address

                        if (endMessage.getDialogPortion() != null
                                && endMessage.getDialogPortion().length > 0) {
                            if (this.isAcModeSet()) {
                                DialoguePortionImpl dialoguePortion = DialogueFactory.createDialoguePortion(new AsnInputStream(endMessage.getDialogPortion()));
                                DialoguePDU dialoguePDU = dialoguePortion.getDialogueAPDU();

                                if (dialoguePDU.getDialogueType() != DialoguePDU.DIALOGUE_RESPONSE_APDU_TAG) {
                                    throw new IncorrectSyntaxException();
                                }

                                DialogueResponsePDUImpl dialogueAARE = (DialogueResponsePDUImpl) dialoguePDU;

                                tcEndIndication.setApplicationContextName(dialogueAARE.getApplicationContext());
                                tcEndIndication.setDialogue(this);
                                tcEndIndication.setUserInformation(dialogueAARE.getUserInformation());

                                if (endMessage.getComponents() != null
                                        && endMessage.getComponents().length > 0) {
                                    tcEndIndication.setComponents(this.doComponents(endMessage.getComponents()));
                                }

                                tcapStack.onEnd(tcEndIndication);
                                //no needing to data persistance, because dialogue is releasing
                            } else {
                                throw new IncorrectSyntaxException();
                            }
                        } else {
                            if (this.isAcModeSet()) {
                                throw new IncorrectSyntaxException();
                            }

                            tcEndIndication.setDialogue(this);

                            if (endMessage.getComponents() != null
                                    && endMessage.getComponents().length > 0) {
                                tcEndIndication.setComponents(this.doComponents(endMessage.getComponents()));
                            }

                            tcapStack.onEnd(tcEndIndication);
                        }

                    } catch (IncorrectSyntaxException ex) {
                        TCPAbort tcpAbort = new TCPAbort();
                        tcpAbort.setDialogue(this);

                        tcpAbort.setpAbort(PAbortCause.ABNORMAL_DIALOGUE);
                        this.tcapStack.onPAbort(tcpAbort);
                    }
                    break;
                case ACTIVE:
                    if (endMessage.getDialogPortion() != null
                            && endMessage.getDialogPortion().length > 0) {
                        TCPAbort tcpAbort = new TCPAbort();
                        tcpAbort.setDialogue(this);

                        tcpAbort.setpAbort(PAbortCause.ABNORMAL_DIALOGUE);
                        tcapStack.onPAbort(tcpAbort);
                    } else {
                        TCEnd tcEndIndication = new TCEnd();
                        tcEndIndication.setDialogue(this);

                        if (endMessage.getComponents() != null
                                && endMessage.getComponents().length > 0) {
                            tcEndIndication.setComponents(this.doComponents(endMessage.getComponents()));
                        }

                        tcapStack.onEnd(tcEndIndication);
                        //no needing to data persistance, because dialogue is releasing
                    }
                    break;
            }
        } finally {
            tcapStack.dialogueTerminated(this);
            //Discard components awaiting transmission
            components.clear();
            mutex.unlock();
        }
    }

    public void abortReceived(AbortMessageImpl abortMessage, SCCPAddress callingParty, QoS qos) {

        try {
            mutex.lock();
            PAbortCause pAbortCause = abortMessage.getPAbortCause();

            //it's TR-U-ABORT
            if (pAbortCause == null) {
                LOGGER.info("TCAP User Abort received. dialog = " + dialogueId + " state = " + state + "isAcMode = " + isAcModeSet());

                switch (this.state) {
                    case INITIATION_SENT:
                        try {
                            this.calledParty = callingParty;
                            if (isAcModeSet()) {
                                //Is userdata included?
                                if (abortMessage.getUAbortCause() == null
                                        || abortMessage.getUAbortCause().length == 0) {
                                    LOGGER.info("TCAP Abort received. PAbortCause = null and UAbortCause == null. Fire PAbort. dialog = " + dialogueId);
                                    TCPAbort tcPAbort = new TCPAbort();
                                    tcPAbort.setpAbort(PAbortCause.ABNORMAL_DIALOGUE);
                                    tcPAbort.setDialogue(this);
                                    this.tcapStack.onPAbort(tcPAbort);
                                    return;
                                }

                                DialoguePortionImpl dialoguePortion = DialogueFactory.createDialoguePortion(new AsnInputStream(abortMessage.getUAbortCause()));

                                //Is abstract syntax = dialogue-PDU AS
                                if (ObjectIdentifiers.getInstance(dialoguePortion.getOidValue()) != ObjectIdentifiers.DIALOGUE_AS_ID
                                        || !dialoguePortion.isAsn()) {
                                    LOGGER.info("TCAP Abort received dialog portion is not DialogASID. Fire PAbort. dialog = " + dialogueId);
                                    TCPAbort tcPAbort = new TCPAbort();
                                    tcPAbort.setpAbort(PAbortCause.ABNORMAL_DIALOGUE);
                                    tcPAbort.setDialogue(this);
                                    this.tcapStack.onPAbort(tcPAbort);
                                    return;
                                }

                                //Is PDU Type ABRT or AARE(rejected) if not then abort
                                DialoguePDU dialoguePDU = dialoguePortion.getDialogueAPDU();
                                if (!(dialoguePDU.getDialogueType() == DialoguePDU.DIALOGUE_ABORT_APDU_TAG
                                        || (dialoguePDU.getDialogueType() == DialoguePDU.DIALOGUE_RESPONSE_APDU_TAG
                                        && ((DialogueResponsePDUImpl) dialoguePDU).getResult().getResult() == Result.REJECT_PERMANENT))) {
                                    TCPAbort tcPAbort = new TCPAbort();
                                    tcPAbort.setpAbort(PAbortCause.ABNORMAL_DIALOGUE);
                                    tcPAbort.setDialogue(this);
                                    this.tcapStack.onPAbort(tcPAbort);
                                    return;
                                }

                                if (dialoguePDU.getDialogueType() == DialoguePDU.DIALOGUE_ABORT_APDU_TAG) {
                                    DialogueAbortPDU dialogueAbortPDU = (DialogueAbortPDU) dialoguePDU;
                                    TCUAbort tcuAbort = new TCUAbort();
                                    tcuAbort.setDialogueId(dialogueId);
                                    tcuAbort.setApplicationContextName(ac);
                                    tcuAbort.setUserInformation(dialogueAbortPDU.getUserInformation());
                                    if (dialogueAbortPDU.getUserInformation() != null) {
                                        tcuAbort.setAbortReason(AbortReason.USER_SPECIFIC);
                                    } else {
                                        tcuAbort.setAbortReason(AbortReason.NO_REASON_GIVEN);
                                    }
                                    tcapStack.onUAbort(tcuAbort);
                                } else if (dialoguePDU.getDialogueType() == DialoguePDU.DIALOGUE_RESPONSE_APDU_TAG) {
                                    LOGGER.info("DialogResponse PDU . dialog = " + dialogueId + " state = " + state);
                                    DialogueResponsePDU dialogueResponsePDU = (DialogueResponsePDU) dialoguePDU;
                                    if (dialogueResponsePDU.getResutlSourceDiagnostic() != null
                                            && dialogueResponsePDU
                                            .getResutlSourceDiagnostic()
                                            .getDialogueServiceUserDiagnosticValue() != null) {

                                        DialogueServiceUserDiagnostic dialogueServiceUserDiagnostic =
                                                dialogueResponsePDU.getResutlSourceDiagnostic()
                                                        .getDialogueServiceUserDiagnosticValue();
                                        LOGGER.info("DialogServiceUserDiagnostic. dialog = " + dialogueId + " state = " + state + " diagnostic = " + dialogueServiceUserDiagnostic);
                                        TCUAbort tcuAbort = new TCUAbort();
                                        tcuAbort.setDialogueId(dialogueId);
                                        tcuAbort.setUserInformation(dialogueResponsePDU.getUserInformation());
                                        tcuAbort.setApplicationContextName(dialogueResponsePDU.getApplicationContext());
                                        if (dialogueServiceUserDiagnostic == DialogueServiceUserDiagnostic.APPLICATION_CONTEXT_NAME_NOT_SUPPORTED) {
                                            tcuAbort.setAbortReason(AbortReason.APPLICATION_CONTEXT_NAME_NOT_SUPPORTED);
                                        } else if (dialogueServiceUserDiagnostic == DialogueServiceUserDiagnostic.NO_REASON_GIVEN) {
                                            tcuAbort.setAbortReason(AbortReason.NO_REASON_GIVEN);
                                        } else {
                                            tcuAbort.setAbortReason(AbortReason.NULL);
                                        }
                                        LOGGER.info("AbortReason. dialog = " + dialogueId + " state = " + state + " diagnostic = " + dialogueServiceUserDiagnostic + " abortReason = " + tcuAbort.getAbortReason());
                                        tcapStack.onUAbort(tcuAbort);
                                    } else if (dialogueResponsePDU.getResutlSourceDiagnostic() != null
                                            && dialogueResponsePDU.getResutlSourceDiagnostic().getDialogueServiceProviderDiagnosticValue() != null
                                            && dialogueResponsePDU.getResutlSourceDiagnostic().getDialogueServiceProviderDiagnosticValue() == DialogueServiceProviderDiagnostic.NO_COMMON_DIALOGUE_PORTION) {
                                        TCPAbort tcPAbort = new TCPAbort();
                                        tcPAbort.setDialogue(this);
                                        tcPAbort.setpAbort(PAbortCause.NO_COMMON_DIALOGUE_PORTION);
                                        this.tcapStack.onPAbort(tcPAbort);
                                    } else {
                                        TCPAbort tcPAbort = new TCPAbort();
                                        tcPAbort.setpAbort(PAbortCause.ABNORMAL_DIALOGUE);
                                        tcPAbort.setDialogue(this);
                                        this.tcapStack.onPAbort(tcPAbort);
                                    }

                                }
                            } else if (abortMessage.getUAbortCause() != null
                                    && abortMessage.getUAbortCause().length > 0) {//DialogPortion present?
                                TCPAbort tcPAbort = new TCPAbort();
                                tcPAbort.setpAbort(PAbortCause.ABNORMAL_DIALOGUE);
                                tcPAbort.setDialogue(this);
                                this.tcapStack.onPAbort(tcPAbort);
                            } else {//Dialog portion not present?
                                TCUAbort tcUAbort = new TCUAbort();
                                tcUAbort.setDialogueId(this.dialogueId);
                                tcUAbort.setAbortReason(AbortReason.USER_SPECIFIC);
                                tcUAbort.setApplicationContextName(ac);
                                tcapStack.onUAbort(tcUAbort);
                            }
                        } catch (IncorrectSyntaxException ex) {
                            TCPAbort tcPAbort = new TCPAbort();
                            tcPAbort.setpAbort(PAbortCause.ABNORMAL_DIALOGUE);
                            tcPAbort.setDialogue(this);
                            this.tcapStack.onPAbort(tcPAbort);
                        }
                        break;
                    case ACTIVE:
                        DialoguePortionImpl dialoguePortion;
                        try {
                            if (this.isAcModeSet()) {
                                if (abortMessage.getUAbortCause() != null
                                        && abortMessage.getUAbortCause().length > 0) {
                                    dialoguePortion = DialogueFactory.createDialoguePortion(new AsnInputStream(abortMessage.getUAbortCause()));

                                    if (!dialoguePortion.isAsn() || !dialoguePortion.isOid()) {
                                        throw new IncorrectSyntaxException();
                                    }

                                    DialoguePDU dialoguePdu = dialoguePortion.getDialogueAPDU();
                                    if (dialoguePdu.getDialogueType() == DialoguePDU.DIALOGUE_ABORT_APDU_TAG
                                            && ((DialogueAbortPDUImpl) dialoguePdu).getAbortSource() == AbortSource.DIALOGUE_SERVICE_USER) {
                                        TCUAbort tcUAbort = new TCUAbort();
                                        tcUAbort.setDialogueId(this.dialogueId);
                                        tcUAbort.setApplicationContextName(this.ac);
                                        tcUAbort.setAbortReason(AbortReason.USER_SPECIFIC);
                                        tcUAbort.setUserInformation(dialoguePdu.getUserInformation());

                                        this.tcapStack.onUAbort(tcUAbort);

                                    } else {
                                        throw new IncorrectSyntaxException();
                                    }
                                } else {
                                    throw new IncorrectSyntaxException();
                                }
                            } else //AC mode not set
                                if (abortMessage.getUAbortCause() != null
                                        && abortMessage.getUAbortCause().length > 0) {
                                    throw new IncorrectSyntaxException();
                                } else {
                                    TCUAbort tcUAbort = new TCUAbort();
                                    tcUAbort.setDialogueId(this.dialogueId);
                                    tcUAbort.setAbortReason(AbortReason.USER_SPECIFIC);

                                    tcapStack.onUAbort(tcUAbort);
                                }
                        } catch (IncorrectSyntaxException ex) {
                            TCPAbort tcPAbort = new TCPAbort();
                            tcPAbort.setDialogue(this);
                            tcPAbort.setpAbort(PAbortCause.ABNORMAL_DIALOGUE);

                            this.tcapStack.onPAbort(tcPAbort);
                        }
                        break;
                }

            } else {

                switch (state) {
                    case INITIATION_SENT:
                    case ACTIVE:
                        TCPAbort tcPAbort = new TCPAbort();
                        tcPAbort.setDialogue(this);
                        tcPAbort.setpAbort(pAbortCause);
                        tcapStack.onPAbort(tcPAbort);
                        break;
                }
            }
        } finally {
            tcapStack.dialogueTerminated(this);
            //Discard components awaiting transmission
            components.clear();
            mutex.unlock();
        }
    }

    public void localAbort(PAbortCause pAbortCause) {
        try {
            mutex.lock();
            switch (this.state) {
                case INITIATION_SENT:
                case ACTIVE:
                    TCPAbort tcPAbort = new TCPAbort();
                    tcPAbort.setDialogue(this);
                    tcPAbort.setpAbort(pAbortCause);

                    tcapStack.onPAbort(tcPAbort);
                    break;
            }
        } finally {
            //Discard components awaiting transmission
            components.clear();
            tcapStack.dialogueTerminated(this);
            mutex.unlock();
        }

    }

    /**
     * @return the state
     */
    public TCAPDialogueState getState() {
        return this.state;
    }

    /**
     * @param state the state to set
     */
    public void setState(TCAPDialogueState state) {
        this.state = state;

    }

    public void onNotice(Long origTransactionId, SCCPAddress callingParty, SCCPAddress calledParty, ErrorReason errorReason) {
        try {
            mutex.lock();
            switch (this.state) {
                case INITIATION_SENT:
                    TRNotice tRNotice = new TRNotice(errorReason);
                    tRNotice.setOrigTransactionId(origTransactionId);
                    tRNotice.setCallingParty(callingParty);
                    tRNotice.setCalledParty(calledParty);
                    switch (state) {
                        case INITIATION_SENT:
                        case ACTIVE:
                            this.tcapStack.onNotice(tRNotice);
                            break;
                    }
                    break;
            }
        } finally {
            mutex.unlock();
        }
    }

    public void sendBegin(TCBegin request) {
        //No need to mutex lock, tcap user(map, cap) should acquire lock
        try {

            this.localInitiated = true;

            if (this.state == TCAPDialogueState.IDLE) {
                BeginMessageImpl beginMessage = MessageFactory.createBegin(dialogueId);

                //Dialogue info. included?
                if (request.getApplicationContextName() != null) {

                    //Set application context mode
                    this.ac = request.getApplicationContextName();

                    DialogueRequest dialogueRequest = DialogueFactory.createDialogueRequest();
                    dialogueRequest.setApplicationContext(request.getApplicationContextName());
                    dialogueRequest.setUserInformation(request.getUserInformation());

                    DialoguePortionImpl dialoguePortion = new DialoguePortionImpl();
                    dialoguePortion.setAsn(true);
                    dialoguePortion.setOid(true);
                    dialoguePortion.setOidValue(ObjectIdentifiers.DIALOGUE_AS_ID.value());
                    dialoguePortion.setDialogAPDU(dialogueRequest);

                    AsnOutputStream aos = new AsnOutputStream();
                    dialoguePortion.encode(aos);

                    beginMessage.setDialoguePortion(aos.toByteArray());
                }

                byte[] data = this.requestComponents();

                beginMessage.setComponents(data);

//                tcapProvider.storeDialogue(dialogueId, this);
                this.state = TCAPDialogueState.INITIATION_SENT;
                try {
                    AsnOutputStream aos = new AsnOutputStream();
                    beginMessage.encode(aos);
                    this.tcapStack.getProvider().send(this.calledParty,
                            this.callingParty,
                            this.qos, aos.toByteArray());
                } catch (IncorrectSyntaxException ex) {
                    LOGGER.error("{}", ex);
                }

//                this.tcapStack.dialogueStorage.store(this);
            } else {
                LOGGER.error(String.format("No BEGIN message sent. STM is not in IDLE state. STATE = %s", state));
            }
        } catch (IncorrectSyntaxException exception) {
            LOGGER.error("{}", exception);
        } finally {
//            dialogueLock.unlock();
//            mutex.unlock();
        }
    }

    /**
     * If the TC-user has received an Application Context Name parameter in the
     * TC-BEGIN indication primitive, and this Application Context is
     * acceptable, the TC-user should include the same value in the first
     * backward TC-CONTINUE request primitive. This causes a Dialogue Response
     * (AARE) APDU to be sent concatenated with any components in a Continue
     * message.
     *
     * @param request
     */
    public void sendContinue(TCContinue request) {
        try {
//            mutex.lock();
            switch (this.state) {
                case INITIATION_RECEIVED:
                    ContinueMessageImpl continueMessage = MessageFactory.createContinue();
                    continueMessage.setDestinationTransactionId(this.remoteTransactionId);
                    continueMessage.setOriginatingTransactionId(dialogueId);

                    //Dialogue info included?
                    if (request.getApplicationContextName() != null) {

                        AssociateSourceDiagnosticImpl associateSourceDiagnosticImpl = new AssociateSourceDiagnosticImpl(DialogueServiceUserDiagnostic.NULL);
                        AssociateResult associatedResult = ParameterFactory.createAssociateResult(Result.ACCEPTED);
                        DialogueResponsePDU dialogueResponsePDUImpl = DialogueFactory.createDialogueResponse(request.getApplicationContextName(), associatedResult, associateSourceDiagnosticImpl);
                        dialogueResponsePDUImpl.setUserInformation(request.getUserInformations());

                        DialoguePortionImpl dialoguePortion = new DialoguePortionImpl();
                        dialoguePortion.setAsn(true);
                        dialoguePortion.setOid(true);
                        dialoguePortion.setOidValue(ObjectIdentifiers.DIALOGUE_AS_ID.value());
                        dialoguePortion.setDialogAPDU(dialogueResponsePDUImpl);

                        AsnOutputStream aos = new AsnOutputStream();
                        dialoguePortion.encode(aos);

                        continueMessage.setDialoguePortion(aos.toByteArray());
                    }

                    byte[] data = this.requestComponents();

//                    trContinue.setOriginationAddress(request.getOriginatingAddress());
                    continueMessage.setComponents(data);

                    try {
                        AsnOutputStream aos = new AsnOutputStream();
                        continueMessage.encode(aos);

                        this.state = TCAPDialogueState.ACTIVE;
                        this.tcapStack.getProvider().send(this.callingParty,
                                this.calledParty,
                                this.qos, aos.toByteArray());

//                        this.tcapStack.dialogueStorage.store(this);
                    } catch (IncorrectSyntaxException ex) {
                        LOGGER.error("Error occured on continue transaction. {}", ex);
                    }

                    break;
                case ACTIVE:
                    continueMessage = MessageFactory.createContinue();

                    data = this.requestComponents();
                    continueMessage.setComponents(data);

                    continueMessage.setDestinationTransactionId(this.remoteTransactionId);
                    continueMessage.setOriginatingTransactionId(dialogueId);

                    try {
                        AsnOutputStream aos = new AsnOutputStream();
                        continueMessage.encode(aos);

                        this.state = TCAPDialogueState.ACTIVE;
                        if (this.localInitiated) {
                            this.tcapStack.getProvider().send(calledParty,
                                    callingParty,
                                    qos, aos.toByteArray());
                        } else {
                            this.tcapStack.getProvider().send(callingParty,
                                    calledParty,
                                    qos, aos.toByteArray());
                        }

//                        this.tcapStack.dialogueStorage.store(this);
                    } catch (IncorrectSyntaxException ex) {
                        LOGGER.error("Error occured on continue transaction. {}", ex);
                    }

                    break;
            }
        } catch (IncorrectSyntaxException exception) {
            LOGGER.error("{}", exception);
        } finally {
//            mutex.unlock();
//            dialogueLock.unlock();
        }
    }

    public void sendEnd(TCEnd request) {
//        dialogueLock.mutex();
        try {
//            mutex.lock();
            switch (this.state) {
                case INITIATION_RECEIVED:
                case ACTIVE:
                    switch (request.getTermination()) {
                        case PREARRANGED:
                            break;
                        case BASIC:
                            EndMessageImpl endMessage = MessageFactory.createEnd();
                            //Dialogue info included?
                            if (request.getApplicationContextName() != null) {
                                AssociateSourceDiagnosticImpl sourceDiagnostic = new AssociateSourceDiagnosticImpl(DialogueServiceUserDiagnostic.NULL);
                                AssociateResult result = ParameterFactory.createAssociateResult(Result.ACCEPTED);

                                DialogueResponsePDU dialogueResponsePDUImpl = DialogueFactory.createDialogueResponse(request.getApplicationContextName(), result, sourceDiagnostic);
                                dialogueResponsePDUImpl.setUserInformation(request.getUserInformation());

                                DialoguePortionImpl dialoguePortion = new DialoguePortionImpl();
                                dialoguePortion.setAsn(true);
                                dialoguePortion.setOid(true);
                                dialoguePortion.setOidValue(ObjectIdentifiers.DIALOGUE_AS_ID.value());
                                dialoguePortion.setDialogAPDU(dialogueResponsePDUImpl);

                                AsnOutputStream aos = new AsnOutputStream();
                                dialoguePortion.encode(aos);

                                endMessage.setDialoguePortion(aos.toByteArray());
                            }

                            byte[] data = this.requestComponents();
                            endMessage.setComponents(data);

                            endMessage.setDestinationTransactionId(this.remoteTransactionId);

                            try {
                                AsnOutputStream aos = new AsnOutputStream();
                                endMessage.encode(aos);
                                if (localInitiated) {
                                    this.tcapStack.getProvider().send(calledParty,
                                            callingParty,
                                            qos, aos.toByteArray());
                                } else {
                                    this.tcapStack.getProvider().send(callingParty,
                                            calledParty,
                                            qos, aos.toByteArray());
                                }
                            } catch (IncorrectSyntaxException ex) {
                                LOGGER.error("Error occured on continue transaction. {}", ex);
                            }
                            break;
                    }
                    break;
                case INITIATION_SENT:
                    break;
                default:
                    LOGGER.error(String.format("End message not sent. TSM is incorrect state. STATE = %s", state));
                    break;
            }
        } catch (IncorrectSyntaxException exception) {
            LOGGER.error("{}", exception);
        } finally {
            //Discard components awaiting transmission
            components.clear();
            tcapStack.dialogueTerminated(this);
//            mutex.unlock();
        }
    }

    @SuppressWarnings("")
    public void sendUAbort(TCUAbort request) {
//        dialogueLock.mutex();
        try {

            switch (this.state) {
                case INITIATION_RECEIVED:
                    AbortMessageImpl abortMessage = MessageFactory.createAbort();

                    if (this.isAcModeSet()) {
                        //Abort reason present and = ac-name not supported or dialogue refused
                        if (request.getAbortReason() != null
                                && (request.getAbortReason() == AbortReason.APPLICATION_CONTEXT_NAME_NOT_SUPPORTED
                                || request.getAbortReason() == AbortReason.DIALOGUE_REFUSED)) {
                            DialogueServiceUserDiagnostic userReason;

                            switch (request.getAbortReason()) {
                                case APPLICATION_CONTEXT_NAME_NOT_SUPPORTED:
                                    userReason = DialogueServiceUserDiagnostic.APPLICATION_CONTEXT_NAME_NOT_SUPPORTED;
                                    break;
                                case NULL:
                                    userReason = DialogueServiceUserDiagnostic.NULL;
                                    break;
                                default:
                                    userReason = DialogueServiceUserDiagnostic.NO_REASON_GIVEN;
                                    break;
                            }

                            //This is applicable only before the dialogue is established (i.e. before the
                            //first backward continue message) and only if the "abort reason" parameter in the
                            //TC-U-ABORT request primitive is set to "application-content-name-not-supported"
                            //or "dialogue-refused".
                            AssociateSourceDiagnosticImpl resultSourceDiagnostic = new AssociateSourceDiagnosticImpl(userReason);
                            AssociateResult result = ParameterFactory.createAssociateResult(Result.REJECT_PERMANENT);

                            DialogueResponsePDU dialogueResponsePDUImpl = DialogueFactory.createDialogueResponse(request.getApplicationContextName(), result, resultSourceDiagnostic);
                            dialogueResponsePDUImpl.setUserInformation(request.getUserInformation());

                            DialoguePortionImpl dialoguePortion = new DialoguePortionImpl();
                            dialoguePortion.setAsn(true);
                            dialoguePortion.setOid(true);
                            dialoguePortion.setOidValue(ObjectIdentifiers.DIALOGUE_AS_ID.value());
                            dialoguePortion.setDialogAPDU(dialogueResponsePDUImpl);

                            AsnOutputStream aos = new AsnOutputStream();
                            dialoguePortion.encode(aos);

                            abortMessage.setUAbortCause(aos.toByteArray());
                        } else {
                            DialogueAbortPDUImpl dialogueAbortAPDUImpl = DialogueFactory.createDialogueAbort(AbortSource.DIALOGUE_SERVICE_USER);
                            dialogueAbortAPDUImpl.setUserInformation(request.getUserInformation());

                            DialoguePortionImpl dialoguePortion = new DialoguePortionImpl();
                            dialoguePortion.setAsn(true);
                            dialoguePortion.setOid(true);
                            dialoguePortion.setOidValue(ObjectIdentifiers.DIALOGUE_AS_ID.value());
                            dialoguePortion.setDialogAPDU(dialogueAbortAPDUImpl);

                            AsnOutputStream aos = new AsnOutputStream();
                            dialoguePortion.encode(aos);

                            abortMessage.setUAbortCause(aos.toByteArray());
                        }
                    }

                    abortMessage.setDestinationTransactionId(this.remoteTransactionId);

                    try {
                        AsnOutputStream aos = new AsnOutputStream();
                        abortMessage.encode(aos);
                        if (localInitiated) {
                            this.tcapStack.getProvider().send(calledParty,
                                    callingParty,
                                    qos, aos.toByteArray());
                        } else {
                            this.tcapStack.getProvider().send(callingParty,
                                    calledParty,
                                    qos, aos.toByteArray());
                        }

                    } catch (IncorrectSyntaxException ex) {
                        LOGGER.error("{}", ex);
                    }

                    break;
                case INITIATION_SENT:
                    //Local action
                    //In this state no any package sent to peer. Just release resources
                    break;
                case ACTIVE:
                    /**
                     * If a TC-U-ABORT request primitive is issued during the
                     * active state of a dialogue, with the "Abort reason"
                     * parameter absent or set to "user-defined", a Dialogue
                     * Abort (ABRT) APDU is formatted only for the case when the
                     * AARQ/AARE APDUs were used during the dialogue
                     * establishment state. User data provided in the primitive
                     * is then transferred in the User Information field of the
                     * ABRT APDU.
                     */
                    abortMessage = MessageFactory.createAbort();
                    abortMessage.setDestinationTransactionId(remoteTransactionId);

                    if (this.isAcModeSet()) {
                        DialogueAbortPDUImpl dialogueAbortAPDUImpl = DialogueFactory.createDialogueAbort(AbortSource.DIALOGUE_SERVICE_USER);
                        dialogueAbortAPDUImpl.setUserInformation(request.getUserInformation());

                        DialoguePortionImpl dialoguePortion = new DialoguePortionImpl();
                        dialoguePortion.setAsn(true);
                        dialoguePortion.setOid(true);
                        dialoguePortion.setOidValue(ObjectIdentifiers.DIALOGUE_AS_ID.value());
                        dialoguePortion.setDialogAPDU(dialogueAbortAPDUImpl);

                        AsnOutputStream aos = new AsnOutputStream();
                        dialoguePortion.encode(aos);
                        abortMessage.setUAbortCause(aos.toByteArray());

                        aos.reset();

                        try {
                            abortMessage.encode(aos);
                            if (localInitiated) {
                                this.tcapStack.getProvider().send(calledParty,
                                        callingParty,
                                        qos, aos.toByteArray());
                            } else {
                                this.tcapStack.getProvider().send(callingParty,
                                        calledParty,
                                        qos, aos.toByteArray());
                            }

                        } catch (IncorrectSyntaxException ex) {
                            LOGGER.error("{}", ex);
                        }
                    } else {
                        try {
                            AsnOutputStream aos = new AsnOutputStream();
                            abortMessage.encode(aos);

                            if (localInitiated) {
                                this.tcapStack.getProvider().send(calledParty,
                                        callingParty,
                                        qos, aos.toByteArray());
                            } else {
                                this.tcapStack.getProvider().send(callingParty,
                                        calledParty,
                                        qos, aos.toByteArray());
                            }

                        } catch (IncorrectSyntaxException ex) {
                            LOGGER.error("{}", ex);
                        }
                    }
                    break;
            }
        } catch (IncorrectSyntaxException exception) {
            LOGGER.error("{}", exception);
        } finally {
            //Discard components awaiting transmission
            components.clear();
            tcapStack.dialogueTerminated(this);
//            mutex.unlock();
        }
    }

    private boolean isAcModeSet() {
        return ac != null;
    }

    public Long getDialogueId() {
        return dialogueId;
    }

    public SCCPAddress getCallingParty() {
        return callingParty;
    }

    public void setCallingParty(SCCPAddress callingParty) {
        this.callingParty = callingParty;
    }

    public SCCPAddress getCalledParty() {
        return calledParty;
    }

    public void setCalledParty(SCCPAddress calledParty) {
        this.calledParty = calledParty;
    }

    //    public ComponentCoordinator getComponentCoordinator() {
//        return componentCoordinator;
//    }
    public QoS getQos() {
        return qos;
    }

    public ApplicationContext getAppLicationContext() {
        return ac;
    }

    //Component coordination part
    public void tcURejectRequest(Short invokeId, InvokeProblem invokeProblem) {

        RejectImpl reject = new RejectImpl();
        reject.setInvokeId(invokeId);
        reject.setProblem(new ProblemImpl(invokeProblem));
        components.add(reject);

    }

    public void tcURejectRequest(Short invokeId, ReturnResultProblem returnResultProblem) {

        RejectImpl reject = new RejectImpl();
        reject.setInvokeId(invokeId);
        reject.setProblem(new ProblemImpl(returnResultProblem));

        this.terminateIsm(invokeId);
        components.add(reject);

    }

    public void tcURejectRequest(Short invokeId, ReturnErrorProblem returnErrorProblem) {

        RejectImpl reject = new RejectImpl();
        reject.setInvokeId(invokeId);
        reject.setProblem(new ProblemImpl(returnErrorProblem));

        this.terminateIsm(invokeId);
        components.add(reject);

    }

    public void sendUserError(Short invokeId, int errorCode) {
        this.sendUserError(invokeId, errorCode, null);
    }

    public void sendUserError(Short invokeId, int errorCode, ParameterImpl parameter) {

        ReturnErrorImpl returnError = ComponentFactory.createReturnErrorComponent();
        returnError.setErrorCode(new ErrorCodeImpl(errorCode));
        returnError.setInvokeId(invokeId);
        returnError.setParameter(parameter);
        components.add(returnError);

    }

    public void sendResult(Short invokeId, int opcode, ParameterImpl parameter) {

        ReturnResultLastImpl returnResultLast = ComponentFactory.createReturnResultLastComponent();
        returnResultLast.setInvokeId(invokeId);
        returnResultLast.setOperationCode(new OperationCodeImpl(opcode));
        returnResultLast.setParameter(parameter);
        components.add(returnResultLast);

    }

    public void sendResult(Short invokeId, ParameterImpl parameter) {

        ReturnResultLastImpl returnResultLast = ComponentFactory.createReturnResultLastComponent();
        returnResultLast.setInvokeId(invokeId);
        returnResultLast.setParameter(parameter);
        components.add(returnResultLast);

    }

    public void sendResult(Short invokeId) {

        ReturnResultLastImpl returnResultLast = ComponentFactory.createReturnResultLastComponent();
        returnResultLast.setInvokeId(invokeId);
        components.add(returnResultLast);

    }

    public void sendRequest(Short invokeId, int operationCode, ParameterImpl parameters) throws Exception {


        ReturnResultNotLastImpl returnResultNotLast = ComponentFactory.createReturnResultComponent();
        returnResultNotLast.setInvokeId(invokeId);
        returnResultNotLast.setOperationCode(new OperationCodeImpl(operationCode));
        returnResultNotLast.setParameter(parameters);
        components.add(returnResultNotLast);

    }

    public Short sendInvoke(int operationCode, OperationClass opClass, ParameterImpl parameters, long timeout) {


        Short invokeId = (short) invokeIdCounter.getAndIncrement();
        OperationCodeImpl _operationCode = new OperationCodeImpl(operationCode);
        InvokeImpl invoke = ComponentFactory.createInvokeComponent();
        invoke.setInvokeClass(opClass);
        invoke.setInvokeId(invokeId);
        invoke.setOpCode(_operationCode);
        invoke.setParameter(parameters);
        invoke.setInvokeTimeOut(timeout);
        this.components.add(invoke);
        return invokeId;

    }

    public Short sendInvoke(int operationCode, OperationClass opClass, long timeout) {
        return this.sendInvoke(operationCode, opClass, null, timeout);
    }

    //on request case
    private byte[] requestComponents() {
        mutex.lock();
        if (components == null || components.size() <= 0) {
            return null;
        }

        AsnOutputStream aos = new AsnOutputStream();
        try {
            try {
                aos.writeTag(Component.COMPONENT_PORTION_TAG_CLASS, false, Component.COMPONENT_PORTION_TAG);
            } catch (Exception ex) {
                LOGGER.error("Error occured while assemble component. " + this, ex);
                return null;
            }

            int position = aos.StartContentDefiniteLength();

            Iterator<Component> componentIter = this.components.iterator();
            while (componentIter.hasNext()) {
                Component component = componentIter.next();

                if (component.getComponentType() == ComponentType.INVOKE) {
                    InvokeImpl invoke = (InvokeImpl) component;
                    InvocationStateMachine invocationStateMachine = new InvocationStateMachine(tcapStack, this, invoke.getInvokeId(), invoke.getInvokeTimeOut(), invoke.getInvokeClass(), invoke.getOpCode().getLocalValue());
                    invocationStateMachine.startInvocationTimer();
                    this.ismTable.put(invoke.getInvokeId(), invocationStateMachine);
                }

                try {
                    component.encode(aos);
                } catch (IncorrectSyntaxException ex) {
                    LOGGER.error("Error occured during assemble component. " + this, ex);
                }
            }

            aos.FinalizeContent(position);
            return aos.toByteArray();
        } finally {
            components.clear();
            mutex.unlock();
        }
    }

    private List<Operation> doComponents(byte[] components) {
        List<Operation> operations = new ArrayList<>();
        AsnInputStream ais = new AsnInputStream(components);

        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();

                switch (tag) {
                    case InvokeImpl.INVOKE_TAG:
                        try {
                            getProvider().tcapStat.receivedInvokeCount.incrementAndGet();
                            InvokeImpl invoke = ComponentFactory.createInvokeComponent(ais);

                            TCInvoke tcInvoke = new TCInvoke();
                            tcInvoke.setDialogue(this);
                            tcInvoke.setInvokeID(invoke.getInvokeId());
                            tcInvoke.setLinkedId(invoke.getLinkedInvokeId());
                            tcInvoke.setOperation(invoke.getOpCode().getLocalValue());
                            tcInvoke.setParameter(invoke.getParameter());
                            if (invoke.getLinkedInvokeId() != null) {
                                InvocationStateMachine invocationStateMachine = this.ismTable.get(invoke.getLinkedInvokeId());
                                if (invocationStateMachine == null) {
                                    ProblemImpl problem = new ProblemImpl(InvokeProblem.UNRECOGNIZED_LINKED_ID);
                                    TCLReject tcLReject = this.createTCReject(invoke.getInvokeId(), problem);
                                    operations.add(tcLReject);

                                    this.sendReject(invoke.getInvokeId(), problem);
                                    break;
                                }
                            }
                            operations.add(tcInvoke);

                        } catch (IncorrectSyntaxException ex) {
                            //Syntax error
                            IncorrectComponent incorrectComponent = new IncorrectComponent();
                            incorrectComponent.decode(ais);

                            ProblemImpl problem = new ProblemImpl(GeneralProblem.MISTYPED_COMPONENT);

                            TCLReject tcLReject = this.createTCReject(incorrectComponent.getInvokeId(), problem);
                            //Discard this and subsequent components
                            operations.clear();
                            operations.add(tcLReject);

                            this.sendReject(incorrectComponent.getInvokeId(), problem);
                            LOGGER.error("", ex);

                            return operations;
                        } catch (AsnException | IOException ex) {
                            //Syntax error
                            IncorrectComponent incorrectComponent = new IncorrectComponent();
                            incorrectComponent.decode(ais);
                            ProblemImpl problem = new ProblemImpl(GeneralProblem.BADLY_STRUCTURED_COMPONENT);

                            TCLReject tcLReject = this.createTCReject(incorrectComponent.getInvokeId(), problem);

                            //Discard this and subsequent components
                            operations.clear();
                            operations.add(tcLReject);

                            this.sendReject(incorrectComponent.getInvokeId(), problem);
                            LOGGER.error("", ex);

                            return operations;
                        }
                        break;
                    case ReturnResultNotLastImpl.RETURN_RESULT_NOT_LAST:

                        try {
                            getProvider().tcapStat.receivedReturnResultNotLastCount.incrementAndGet();
                            ReturnResultNotLastImpl returnResultNotLast = ComponentFactory.createReturnResultComponent(ais);

                            InvocationStateMachine invocationStateMachine = this.ismTable.get(returnResultNotLast.getInvokeId());

                            if (invocationStateMachine == null) {
                                ProblemImpl problem = new ProblemImpl(ReturnResultProblem.UNRECOGNIZED_INVOKE_ID);
                                TCLReject tcLReject = this.createTCReject(returnResultNotLast.getInvokeId(), problem);
                                operations.add(tcLReject);

                                this.sendReject(returnResultNotLast.getInvokeId(), problem);
                                break;
                            }

                            TCResultNotLast tcResultNotLast = new TCResultNotLast();
                            tcResultNotLast.setDialogue(this);
                            tcResultNotLast.setInvokeId(returnResultNotLast.getInvokeId());
                            tcResultNotLast.setOperationCode(returnResultNotLast.getOperationCode());
                            tcResultNotLast.setParameters(returnResultNotLast.getParameter());

                            switch (invocationStateMachine.operationClass) {
                                case CLASS1:
                                    operations.add(tcResultNotLast);
                                    break;
                                case CLASS2:
                                case CLASS4:
                                    TCLReject tcLReject = this.createTCReject(returnResultNotLast.getInvokeId(), new ProblemImpl(ReturnResultProblem.RETURN_RESULT_UNEXPECTED));
                                    operations.add(tcLReject);
                                    this.terminateIsm(returnResultNotLast.getInvokeId());
                                    break;
                                case CLASS3:
                                    operations.add(tcResultNotLast);
                                    break;
                            }

                        } catch (IncorrectSyntaxException ex) {
                            LOGGER.error("", ex);
                            IncorrectComponent incorrectComponent = new IncorrectComponent();
                            incorrectComponent.decode(ais);

                            this.terminateAllIsms();

                            ProblemImpl problem = new ProblemImpl(GeneralProblem.MISTYPED_COMPONENT);
                            TCLReject tcLReject = this.createTCReject(incorrectComponent.getInvokeId(), problem);
                            //Discard this and subsequent components
                            operations.clear();
                            operations.add(tcLReject);
                            this.sendReject(incorrectComponent.getInvokeId(), problem);

                            return operations;
                        } catch (AsnException | IOException ex) {
                            LOGGER.error("", ex);
                            IncorrectComponent incorrectComponent = new IncorrectComponent();
                            incorrectComponent.decode(ais);

                            this.terminateAllIsms();

                            ProblemImpl problem = new ProblemImpl(GeneralProblem.BADLY_STRUCTURED_COMPONENT);
                            TCLReject tcLReject = this.createTCReject(incorrectComponent.getInvokeId(), problem);
                            //Discard this and subsequent components
                            operations.clear();
                            operations.add(tcLReject);
                            this.sendReject(incorrectComponent.getInvokeId(), problem);

                            return operations;
                        }
                        break;
                    case ReturnResultLastImpl.RETURN_RESULT_LAST_TAG:
                        try {
                            getProvider().tcapStat.receivedReturnResultLastCount.incrementAndGet();
                            ReturnResultLastImpl returnResultLast = ComponentFactory.createReturnResultLastComponent(ais);

                            InvocationStateMachine invocationStateMachine = this.ismTable.get(returnResultLast.getInvokeId());

                            if (invocationStateMachine == null) {
                                ProblemImpl problem = new ProblemImpl(ReturnResultProblem.UNRECOGNIZED_INVOKE_ID);

                                TCLReject tcLReject = this.createTCReject(returnResultLast.getInvokeId(), problem);
                                operations.add(tcLReject);

                                this.sendReject(returnResultLast.getInvokeId(), problem);
                                break;
                            }

                            TCResult tcResultLast = new TCResult();
                            tcResultLast.setDialogue(this);
                            tcResultLast.setInvokeId(returnResultLast.getInvokeId());

                            tcResultLast.setOperationCode(returnResultLast.getOperationCode());
                            tcResultLast.setParameters(returnResultLast.getParameter());

                            switch (invocationStateMachine.operationClass) {
                                case CLASS1:
                                    invocationStateMachine.stopInvocationTimer();
                                    invocationStateMachine.startRejectTimer();
                                    operations.add(tcResultLast);
                                    break;
                                case CLASS2:
                                case CLASS4:
                                    this.terminateIsm(returnResultLast.getInvokeId());
                                    TCLReject tcLReject = createTCReject(returnResultLast.getInvokeId(), new ProblemImpl(ReturnResultProblem.RETURN_RESULT_UNEXPECTED));
                                    operations.add(tcLReject);
                                    break;
                                case CLASS3:
                                    invocationStateMachine.stopInvocationTimer();
                                    invocationStateMachine.startRejectTimer();
                                    operations.add(tcResultLast);
                                    break;
                            }

                        } catch (IncorrectSyntaxException ex) {
                            IncorrectComponent incorrectComponent = new IncorrectComponent();
                            incorrectComponent.decode(ais);

                            this.terminateAllIsms();
                            ProblemImpl problem = new ProblemImpl(GeneralProblem.MISTYPED_COMPONENT);
                            TCLReject tcLReject = this.createTCReject(incorrectComponent.getInvokeId(), problem);
                            //Discard this and subsequent components
                            operations.clear();
                            operations.add(tcLReject);

                            this.sendReject(incorrectComponent.getInvokeId(), problem);
                            LOGGER.error("", ex);

                            return operations;
                        } catch (AsnException | IOException ex) {
                            IncorrectComponent incorrectComponent = new IncorrectComponent();
                            incorrectComponent.decode(ais);

                            this.terminateAllIsms();
                            ProblemImpl problem = new ProblemImpl(GeneralProblem.BADLY_STRUCTURED_COMPONENT);
                            TCLReject tcLReject = this.createTCReject(incorrectComponent.getInvokeId(), problem);
                            //Discard this and subsequent components
                            operations.clear();
                            operations.add(tcLReject);

                            this.sendReject(incorrectComponent.getInvokeId(), problem);
                            LOGGER.error("", ex);

                            return operations;
                        }

                        break;
                    case ReturnErrorImpl.RETURN_ERROR_TAG:
                        try {
                            getProvider().tcapStat.receivedReturnErrorCount.incrementAndGet();
                            ReturnErrorImpl returnError = ComponentFactory.createReturnErrorComponent(ais);
                            InvocationStateMachine invocationStateMachine = this.ismTable.get(returnError.getInvokeId());

                            if (invocationStateMachine == null) {
                                ProblemImpl problem = new ProblemImpl(ReturnErrorProblem.UNRECOGNIZED_INVOKE_ID);
                                TCLReject tcLReject = this.createTCReject(returnError.getInvokeId(), problem);
                                operations.add(tcLReject);

                                this.sendReject(returnError.getInvokeId(), problem);
                                break;
                            }

                            TCUError tcUError = new TCUError();

                            tcUError.setDialogue(this);
                            tcUError.setInvokeId(returnError.getInvokeId());
                            tcUError.setError(returnError.getErrorCode());
                            tcUError.setParameter(returnError.getParameter());

                            switch (invocationStateMachine.operationClass) {
                                case CLASS1:
                                case CLASS2:
                                    operations.add(tcUError);
                                    invocationStateMachine.stopInvocationTimer();
                                    invocationStateMachine.startRejectTimer();
                                    break;
                                case CLASS3:
                                case CLASS4:
                                    TCLReject tcLReject = this.createTCReject(returnError.getInvokeId(), new ProblemImpl(ReturnErrorProblem.RETURN_ERROR_UNEXPECTED));
                                    operations.add(tcLReject);
                                    this.terminateIsm(returnError.getInvokeId());
                                    break;
                            }

                        } catch (IncorrectSyntaxException ex) {
                            IncorrectComponent incorrectComponent = new IncorrectComponent();
                            incorrectComponent.decode(ais);

                            this.terminateAllIsms();

                            ProblemImpl problem = new ProblemImpl(GeneralProblem.MISTYPED_COMPONENT);
                            TCLReject tcLReject = this.createTCReject(incorrectComponent.getInvokeId(), problem);
                            //Discard this and subsequent components
                            operations.clear();
                            operations.add(tcLReject);

                            this.sendReject(incorrectComponent.getInvokeId(), problem);
                            LOGGER.error("", ex);

                            return operations;
                        } catch (AsnException | IOException ex) {
                            IncorrectComponent incorrectComponent = new IncorrectComponent();
                            incorrectComponent.decode(ais);

                            this.terminateAllIsms();

                            ProblemImpl problem = new ProblemImpl(GeneralProblem.BADLY_STRUCTURED_COMPONENT);
                            TCLReject tcLReject = this.createTCReject(incorrectComponent.getInvokeId(), problem);
                            //Discard this and subsequent components
                            operations.clear();
                            operations.add(tcLReject);

                            this.sendReject(incorrectComponent.getInvokeId(), problem);
                            LOGGER.error("", ex);

                            return operations;
                        }
                        break;

                    case RejectImpl.REJECT_TAG:
                        try {
                            getProvider().tcapStat.receivedRejectCount.incrementAndGet();
                            RejectImpl rejectImpl = ComponentFactory.createRejectComponent(ais);
                            if (rejectImpl.isInvokeProblem()) {
                                if (!this.terminateIsm(rejectImpl.getInvokeId())) {
                                    LOGGER.error(String.format("REJECT Component received. No ISM found. DialogueId = %s, %s", this, rejectImpl));
                                    break;
                                }
                            }

                            //Look at ITU Q.772 3.7 for determine TC-R-REJECT or TC-U-REJECT must be
                            if (rejectImpl.isGeneralProblem()
                                    || (rejectImpl.isInvokeProblem() && rejectImpl.getProblem().getInvokeProblem() == InvokeProblem.UNRECOGNIZED_LINKED_ID)
                                    || (rejectImpl.isReturnResultProblem() && (rejectImpl.getProblem().getReturnResultProblem() == ReturnResultProblem.UNRECOGNIZED_INVOKE_ID
                                    || rejectImpl.getProblem().getReturnResultProblem() == ReturnResultProblem.RETURN_RESULT_UNEXPECTED))
                                    || (rejectImpl.isReturnErrorProblem() && (rejectImpl.getProblem().getReturnErrorProblem() == ReturnErrorProblem.UNRECOGNIZED_INVOKE_ID
                                    || rejectImpl.getProblem().getReturnErrorProblem() == ReturnErrorProblem.RETURN_ERROR_UNEXPECTED))) {
                                TCRReject tcRReject = new TCRReject();
                                tcRReject.setDialogue(this);
                                tcRReject.setInvokeId(rejectImpl.getInvokeId());
                                tcRReject.setProblem(rejectImpl.getProblem());
                                operations.add(tcRReject);
                            } else {
                                TCUReject tcUReject = new TCUReject();
                                tcUReject.setDialogue(this);
                                tcUReject.setInvokeId(rejectImpl.getInvokeId());
                                tcUReject.setProblem(rejectImpl.getProblem());
                                operations.add(tcUReject);
                            }

                        } catch (IncorrectSyntaxException | AsnException | IOException ex) {
                            //inform local tc-user
                            LOGGER.error(String.format("RejectComponent: Incorrect syntax %d", this), ex);
                            operations.clear();
                            return operations;
                        }
                    default:
                        //Component type is not derivable
                        getProvider().tcapStat.receivedMalformedOperationCount.incrementAndGet();
                        UnknownComponent unknownComponent;
                        try {
                            unknownComponent = ComponentFactory.createUnknownComponent(ais);

                            ProblemImpl problem = new ProblemImpl(GeneralProblem.UNRECOGNIZED_COMPONENT);
                            TCLReject tcLReject = this.createTCReject(unknownComponent.getInvokeId(), problem);
                            //Discard all subsequent components
                            operations.clear();
                            operations.add(tcLReject);

                            this.createTCReject(unknownComponent.getInvokeId(), problem);
                            return operations;
                        } catch (IncorrectSyntaxException ex) {
                            LOGGER.error("", ex);
                        }
                }

            }
        } catch (IOException ex) {
            LOGGER.error("", ex);
            getProvider().tcapStat.receivedMalformedOperationCount.incrementAndGet();
            ProblemImpl problem = new ProblemImpl(GeneralProblem.BADLY_STRUCTURED_COMPONENT);
            TCLReject tcLReject = this.createTCReject(null, problem);
            operations.clear();
            operations.add(tcLReject);

            this.sendReject(null, problem);
            return operations;
        }
        return operations;
    }

    private TCLReject createTCReject(Short invokeId, ProblemImpl problem) {
        TCLReject tcLReject = new TCLReject();
        tcLReject.setDialogue(this);
        tcLReject.setInvokeId(invokeId);
        tcLReject.setProblem(problem);

        return tcLReject;
    }

    private void sendReject(Short invokeId, ProblemImpl problem) {
        RejectImpl invokeReject = new RejectImpl();
        invokeReject.setInvokeId(invokeId);
        invokeReject.setProblem(problem);
        this.components.add(invokeReject);

    }

    protected final boolean terminateIsm(Short invokeId) {
        InvocationStateMachine ism = this.ismTable.remove(invokeId);
        if (ism != null) {
            ism.terminate();
            return true;
        }
        return false;
    }

    protected final void terminateAllIsms() {
        ismTable.values().forEach((ism) -> {
            ism.terminate();
        });
        ismTable.clear();
    }

    public InvocationStateMachine getInvocationStateMachine(Short invokeId) {
        return ismTable.get(invokeId);
    }

    public enum TCAPDialogueState {

        UNKNOWN,
        IDLE,
        INITIATION_RECEIVED,
        INITIATION_SENT,
        ACTIVE;

        public static TCAPDialogueState getInstance(int value) {
            switch (value) {
                case 0:
                    return UNKNOWN;
                case 1:
                    return IDLE;
                case 2:
                    return INITIATION_RECEIVED;
                case 3:
                    return INITIATION_SENT;
                case 4:
                    return ACTIVE;
                default:
                    return UNKNOWN;
            }
        }
    }

    public final byte[] serialize() {
        try {
            AsnOutputStream aos = new AsnOutputStream();
            if (callingParty != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 1, callingParty.encode());
            }
            if (calledParty != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 2, calledParty.encode());
            }
            if (qos != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 3, qos.encode());
            }
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 4, state.ordinal());
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 5, remoteTransactionId);
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 6, dialogueId);
            if (localInitiated) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 7);
            }
            if (ac != null) {
                aos.writeObjectIdentifier(Tag.CLASS_CONTEXT_SPECIFIC, 8, ac.getOid());
            }
            return aos.toByteArray();
        } catch (Exception ex) {
            LOGGER.error("error:", ex);
            return null;
        }
    }

    public final void deseraialize(byte[] data) throws Exception {
        AsnInputStream ais = new AsnInputStream(data);
        while (ais.available() > 0) {
            int tag = ais.readTag();
            switch (tag) {
                case 1:
                    this.callingParty = new SCCPAddress(ais.readOctetString());
                    break;
                case 2:
                    this.calledParty = new SCCPAddress(ais.readOctetString());
                    break;
                case 3:
                    this.qos = new QoS(ais.readOctetString());
                    break;
                case 4:
                    this.state = TCAPDialogueState.getInstance((int) ais.readInteger());
                    break;
                case 5:
                    this.remoteTransactionId = ais.readInteger();
                    break;
                case 6:
                    this.dialogueId = ais.readInteger();
                    break;
                case 7:
                    this.localInitiated = true;
                    ais.readNull();
                    break;
                case 8:
                    this.ac = new ApplicationContextImpl(ais.readObjectIdentifier());
                    break;
            }
        }
    }

    public void setApplicationContext(ApplicationContextImpl ac) {
        this.ac = ac;
    }

    @Override
    public void run() {
        tcapStack.onDialogueTimeOut(this.dialogueId);
    }

    public ReentrantLock getLock() {
        return mutex;
    }

    public TCAPProviderImpl getProvider() {
        return tcapStack.getProvider();
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    public byte[] getRawData() {
        return rawData;
    }

    @Override
    public String toString() {
        return String.format("TCAPDialogue[DialogueId = %s; remoteTransactionId = %s; "
                        + "ClngPrty = %s; CldPrty = %s; QoS = %s; ApplicationContext = %s; "
                        + "State = %s]", dialogueId, remoteTransactionId, callingParty,
                calledParty, qos, ac, state);
    }

    protected void terminated() {
        mutex.lock();
        try {
            this.terminateAllIsms();
            this.keepAliveTask.cancel(false);
        } finally {
            mutex.unlock();
        }
    }
}
