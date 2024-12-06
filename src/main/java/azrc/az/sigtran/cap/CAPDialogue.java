/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap;

import azrc.az.sigtran.cap.api.IReleaseCallArg;
import azrc.az.sigtran.cap.errors.CAPErrorFactory;
import azrc.az.sigtran.cap.errors.CAPUserError;
import azrc.az.sigtran.cap.errors.SystemFailure;
import azrc.az.sigtran.cap.errors.UnknownCapErrorException;
import azrc.az.sigtran.cap.parameters.CapGPRSReferenceNumber;
import azrc.az.sigtran.cap.parameters.CapUAbortReason;
import azrc.az.sigtran.common.exceptions.*;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.messages.MessageHandling;
import azrc.az.sigtran.tcap.InvocationStateMachine;
import azrc.az.sigtran.tcap.QoS;
import azrc.az.sigtran.tcap.ResourceLimitationException;
import azrc.az.sigtran.tcap.TCAPDialogue;
import azrc.az.sigtran.tcap.parameters.*;
import azrc.az.sigtran.tcap.parameters.interfaces.ApplicationContext;
import azrc.az.sigtran.tcap.primitives.AbortReason;
import azrc.az.sigtran.tcap.primitives.Termination;
import azrc.az.sigtran.tcap.primitives.tc.*;
import azrc.az.sigtran.tcap.primitives.tr.TRNotice;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author eatakishiyev
 */
public abstract class CAPDialogue<T> {

    private final Logger logger = LoggerFactory.getLogger(CAPDialogue.class);
    protected DialogueState state = DialogueState.IDLE;
    private CapGPRSReferenceNumber capGPRSReferenceNumber;
    private CAPApplicationContexts capApplicationContext;

    protected TCAPDialogue tcapDialogue;
    protected CAPStackImpl stack;
    private T attachment;
    private final HashMap<Short, Future> performingSSMTable = new HashMap<>();


    private CAPDialogue(CAPStackImpl stack) {
        this.stack = stack;
    }

    protected CAPDialogue(CAPStackImpl stack,
                          CAPApplicationContexts capApplicationContext,
                          SCCPAddress destinationAddress,
                          SCCPAddress originationAddress,
                          CapGPRSReferenceNumber capGPRSReferenceNumber,
                          MessageHandling messageHandling,
                          boolean sequenceControl) throws ResourceLimitationException {
        this(stack);
        this.tcapDialogue = stack.getTcapProvider().createDialogue(new QoS(messageHandling, sequenceControl));
        tcapDialogue.setCalledParty(destinationAddress);
        tcapDialogue.setCallingParty(originationAddress);

        this.capApplicationContext = capApplicationContext;
        this.capGPRSReferenceNumber = capGPRSReferenceNumber;
        this.state = DialogueState.WAIT_FOR_USER_REQUESTS;
    }

    protected CAPDialogue(TCAPDialogue tcapDialogue, CAPStackImpl stack) {
        this(stack);
        this.tcapDialogue = tcapDialogue;
    }

    public CAPApplicationContexts getCapApplicationContext() {
        return this.capApplicationContext;
    }

    public void requestDelimiter() {
        try {
            getTcapDialogue().getLock().lock();
            switch (this.state) {
                case WAIT_FOR_USER_REQUESTS:
                    TCBegin tcBegin = new TCBegin(this.tcapDialogue);
                    tcBegin.setApplicationContext(new ApplicationContextImpl(this.capApplicationContext.oid()));
                    this.getTcapDialogue().sendBegin(tcBegin);

                    this.state = DialogueState.DIALOGUE_INITIATED;
                    break;
                case DIALOGUE_ACCEPTED:

                    TCContinue tCContinue = new TCContinue();
                    tCContinue.setApplicationContextName(new ApplicationContextImpl(this.capApplicationContext.oid()));
                    tCContinue.setDialogue(getTcapDialogue());
                    getTcapDialogue().sendContinue(tCContinue);
                    this.state = DialogueState.DIALOGUE_ESTABLISHED;
                    break;
                case DIALOGUE_ESTABLISHED:
                    tCContinue = new TCContinue();
                    tCContinue.setDialogue(this.getTcapDialogue());
                    getTcapDialogue().sendContinue(tCContinue);
                    this.state = DialogueState.DIALOGUE_ESTABLISHED;
                    break;
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void requestCancel(Short invokeId) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            switch (state) {
                case WAIT_FOR_USER_REQUESTS:
                case DIALOGUE_ACCEPTED:
                case DIALOGUE_ESTABLISHED:
                case DIALOGUE_INITIATED:
                case DIALOGUE_PENDING:
                    tcapDialogue.userCancel(invokeId);
                    terminatePerformingSSM(invokeId);
                    break;
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void requestUAbort() {
        try {
            getTcapDialogue().getLock().lock();
            switch (state) {
                case DIALOGUE_INITIATED:
                case DIALOGUE_PENDING:
                case DIALOGUE_ACCEPTED:
                case DIALOGUE_ESTABLISHED:
                    TCUAbort tcUAbort = new TCUAbort();
                    tcUAbort.setAbortReason(AbortReason.NO_REASON_GIVEN);
                    tcUAbort.setDialogueId(getDialogueId());

                    this.stack.releaseCapDialogue(getDialogueId());
                    this.getTcapDialogue().sendUAbort(tcUAbort);
                    break;
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void requestUAbort(CapUAbortReason reason) throws IncorrectSyntaxException {
        try {
            tcapDialogue.getLock().lock();
            TCUAbort tcUAbort = new TCUAbort();
            tcUAbort.setAbortReason(AbortReason.USER_SPECIFIC);
            tcUAbort.setDialogueId(getDialogueId());

            AsnOutputStream aos = new AsnOutputStream();
            CAPUserAbortData capUserAbortData = new CAPUserAbortData(reason);
            capUserAbortData.encode(aos);

            UserInformationImpl userInformation = new UserInformationImpl();
            userInformation.setDirectReference(CAPUserAbortData.USER_ABORT_DATA_OID);
            userInformation.setEncoding(UserInformationImpl.Encoding.SINGLE_ASN_1);
            userInformation.setExternalData(aos.toByteArray());

            tcUAbort.setUserInformation(userInformation);

            this.stack.releaseCapDialogue(getDialogueId());
            this.getTcapDialogue().sendUAbort(tcUAbort);
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void requestClose(Termination type) {
        try {
            getTcapDialogue().getLock().lock();
            switch (this.state) {
                case DIALOGUE_INITIATED:
                    TCEnd tcEnd = new TCEnd();
                    tcEnd.setDialogue(getTcapDialogue());
                    tcEnd.setTermination(Termination.PREARRANGED);
                    tcEnd.setApplicationContextName(new ApplicationContextImpl(capApplicationContext.oid()));

                    stack.releaseCapDialogue(getDialogueId());

                    getTcapDialogue().sendEnd(tcEnd);
                    break;
                case DIALOGUE_ACCEPTED:
                    tcEnd = new TCEnd();
                    tcEnd.setDialogue(getTcapDialogue());
                    tcEnd.setTermination(type);
                    tcEnd.setApplicationContextName(new ApplicationContextImpl(capApplicationContext.oid()));

                    stack.releaseCapDialogue(getDialogueId());
                    getTcapDialogue().sendEnd(tcEnd);

                    break;
                case DIALOGUE_PENDING:
                case DIALOGUE_ESTABLISHED:
                    tcEnd = new TCEnd();
                    tcEnd.setTermination(type);
                    tcEnd.setDialogue(this.getTcapDialogue());
                    stack.releaseCapDialogue(getDialogueId());
                    getTcapDialogue().sendEnd(tcEnd);
                    break;
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public final Short sendCAPDisconnectForwardConnection() throws UnexpectedDialogueStateException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.DISCONNECT_FORWARD_CONNECTION;

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), 0);
                return invokeId;
            }

            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public final Short sendCAPReleaseCall(IReleaseCallArg releaseCallArgument) throws UnexpectedDialogueStateException, IncorrectSyntaxException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.RELEASE_CALL;
                AsnOutputStream aos = new AsnOutputStream();

                releaseCallArgument.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());
                Short invokId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), parameter, 0);
                return invokId;
            }

            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }

    }

    public final Short sendCAPActivityTest(long timeOut) throws UnexpectedDialogueStateException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.ACTIVITY_TEST;

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), timeOut);
                return invokeId;
            }

            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public final Short sendCAPContinue() throws UnexpectedDialogueStateException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.CONTINUE;
                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), 0);

                return invokeId;
            }
            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void sendCAPActivityTestRsp(Short invokeId) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {

                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %s InvokeId = %d",
                            this.getDialogueId(), invokeId));
                }

                this.terminatePerformingSSM(invokeId);
                tcapDialogue.sendResult(invokeId);

            } else {
                throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                        this.getState()));
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    protected void onTCBegin(byte[] rawData, TCBegin indication) {
        try {
            getTcapDialogue().getLock().lock();
            //AC not included to the TCAP Begin message
            //Send U-ABORT Request to the Peer
            if (!indication.isApplicationContextIncluded()) {
                sendUAbort(AbortReason.NO_COMMON_DIALOGUE_PORTION);
                logger.error(String.format("TCBegin:No AC received. DialogueId = %s", this.getTcapDialogue().getDialogueId()));
            } else {
                this.capApplicationContext = CAPApplicationContexts.getInstance(indication.getApplicationContextName().getOid());
                //AC Supported?
                if (this.capApplicationContext == null) {
                    sendUAbort(AbortReason.APPLICATION_CONTEXT_NAME_NOT_SUPPORTED);
                    logger.error(String.format("TCBegin:Incorrect CAP AC received. DialogueId = %s", this.getTcapDialogue().getDialogueId()));
                    return;
                }

                if (indication.getUserInformation() != null
                        && indication.getUserInformation().getDirectReference() == CapGPRSReferenceNumber.CAP_GPRS_REFERENCE_NUMBER_OID) {
                    AsnInputStream ais = new AsnInputStream(indication.getUserInformation().getExternalData());
                    try {
                        int tag = ais.readTag();
                        if (tag == Tag.SEQUENCE
                                && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                            this.capGPRSReferenceNumber = new CapGPRSReferenceNumber();
                            this.capGPRSReferenceNumber.decode(ais.readSequenceStream());
                        }
                    } catch (IOException ex) {
                        this.sendUAbortRequest(CapUAbortReason.INVALID_REFERENCE);
                        return;
                    } catch (AsnException ex) {
                        this.sendUAbortRequest(CapUAbortReason.INVALID_REFERENCE);
                        return;
                    } catch (IncorrectSyntaxException ex) {
                        this.sendUAbortRequest(CapUAbortReason.INVALID_REFERENCE);
                        return;
                    } catch (ParameterOutOfRangeException ex) {
                        this.sendUAbortRequest(CapUAbortReason.INVALID_REFERENCE);
                        return;
                    }
                }

                //Camel Phase4. Subclause 14.1.4.1.2
                //When the gprsSSF sends the first operation for a new GPRS dialogue(InitialDPGPRS), the gprsSSF shall
                //include a GPRS Reference Number in the TC message            
                if (indication.getUserInformation() == null
                        && this.capApplicationContext == CAPApplicationContexts.CAP_V4_GPRSSSF_TO_GSMSCF_AC) {
                    this.sendUAbortRequest(CapUAbortReason.MISSING_REFERENCE);
                    return;
                }

                this.state = DialogueState.DIALOGUE_ACCEPTED;
                this.stack.fireDialogueAccepted(this);

                if (indication.getComponents() != null
                        && indication.getComponents().size() > 0) {
                    List<Operation> operations = indication.getComponents();
                    for (Operation operation : operations) {
                        this.onOperation(operation);
                    }
                }

                this.stack.fireDelimiter(this);
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    protected void onTCContinue(TCContinue indication) {
        try {
            getTcapDialogue().getLock().lock();
            switch (this.state) {
                case DIALOGUE_INITIATED:
                    //On receipt of the first TC-CONTINUE indication primitive
                    //for a dialogue, TC-USER shall check the value of the AC parameter
                    //If this value matches the one used in the TC-BEGIN request primitive,
                    //then TC-USER shall process the following TC component handling indication
                    //primitives as described in subclause 14.1.1.4.1, otherwise it shall 
                    //issue a TC-U-ABORT request primitive
                    if (!this.isACNameUnchanged(indication.getApplicationContextName())) {
                        logger.error(String.format("AC not matched with AC in TC-BEGIN one. Dialogue = %s StoredAC = %s ReceveidAC = %s",
                                this.getDialogueId(), this.capApplicationContext, indication.getApplicationContextName()));

                        this.stack.fireProviderAbort(CAPGeneralAbortReasons.ABNORMAL_DIALOGUE, ProblemSource.CAP_PROBLEM, this);
                        this.sendUAbortRequest(CapUAbortReason.ABNORMAL_PROCESSING);

                        this.stack.releaseCapDialogue(getDialogueId());
                    } else {
                        this.state = DialogueState.DIALOGUE_ESTABLISHED;
                        if (indication.getComponents() != null
                                && indication.getComponents().size() > 0) {
                            List<Operation> operations = indication.getComponents();
                            for (Operation operation : operations) {
                                this.onOperation(operation);
                            }
                            this.stack.fireDelimiter(this);
                        }
                    }
                    break;
                case DIALOGUE_ESTABLISHED:
                    if (indication.getComponents() != null
                            && indication.getComponents().size() > 0) {
                        List<Operation> operations = indication.getComponents();
                        for (Operation operation : operations) {
                            this.onOperation(operation);
                        }
                        this.stack.fireDelimiter(this);
                    }
                    break;
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    void onTCEnd(TCEnd indication) {
        try {
            getTcapDialogue().getLock().lock();
            switch (this.state) {
                case DIALOGUE_INITIATED:
                    if (!this.isACNameUnchanged(indication.getApplicationContextName())) {
                        logger.error(String.format("AC not matched with AC in TC-BEGIN one. Dialogue = %s StoredAC = %s ReceveidAC = %s",
                                this.getDialogueId(), this.capApplicationContext, indication.getApplicationContextName()));

                        this.stack.fireProviderAbort(CAPGeneralAbortReasons.ABNORMAL_DIALOGUE, ProblemSource.CAP_PROBLEM, this);
                        this.stack.releaseCapDialogue(getDialogueId());
                    } else if (indication.getComponents() != null
                            && indication.getComponents().size() > 0) {
                        List<Operation> operations = indication.getComponents();
                        for (Operation operation : operations) {
                            this.onOperation(operation);
                        }
                    }
                    break;
                case DIALOGUE_ESTABLISHED:
                    if (indication.getComponents() != null
                            && indication.getComponents().size() > 0) {
                        List<Operation> operations = indication.getComponents();
                        for (Operation operation : operations) {
                            this.onOperation(operation);
                        }
                    }

                    break;
            }
        } finally {
            this.stack.fireCloseIndication(this);
            this.stack.releaseCapDialogue(this.getDialogueId());
            getTcapDialogue().getLock().unlock();
        }
    }

    public void onPAbort(TCPAbort indication) {
        try {
            getTcapDialogue().getLock().lock();
            switch (indication.getpAbort()) {
                case NO_COMMON_DIALOGUE_PORTION:
                    this.stack.fireProviderAbort(CAPGeneralAbortReasons.VERSION_IMCOMPATIBILITY, ProblemSource.TC_PROBLEM, this);
                    break;
                case UNRECOGNIZED_MESSAGE_TYPE:
                case INCORRECT_TRANSACTION_PORTION:
                case BADDLY_FORMATTED_TRANSACTION_PORTION:
                case ABNORMAL_DIALOGUE:
                    this.stack.fireProviderAbort(CAPGeneralAbortReasons.PROVIDER_MALFUNCTION, ProblemSource.TC_PROBLEM, this);
                    break;
                case UNRECOGNIZED_TRANSACTION_ID:
                    this.stack.fireProviderAbort(CAPGeneralAbortReasons.SUPPORTING_DIALOGUE_RELEASED, ProblemSource.TC_PROBLEM, this);
                    break;
                case RESOURCE_LIMITATION:
                    this.stack.fireProviderAbort(CAPGeneralAbortReasons.RESOURCE_LIMITATION, ProblemSource.TC_PROBLEM, this);
                    break;
            }
        } finally {
            this.stack.releaseCapDialogue(this.getDialogueId());
            getTcapDialogue().getLock().unlock();
        }

    }

    public void onTCUAbort(TCUAbort indication) {
        try {
            getTcapDialogue().getLock().lock();
            switch (indication.getAbortReason()) {
                case APPLICATION_CONTEXT_NAME_NOT_SUPPORTED:
                    this.stack.fireUserAbort(CAPGeneralAbortReasons.APPLICATION_CONTEXT_NOT_SUPPORTED, this);
                    break;
                case USER_SPECIFIC:
                    if (indication.getUserInformation() != null) {
                        if (indication.getUserInformation().getDirectReference() == CAPUserAbortData.USER_ABORT_DATA_OID) {
                            AsnInputStream ais = new AsnInputStream(indication.getUserInformation().getExternalData());
                            try {
                                int tag = ais.readTag();
                                if (tag == Tag.ENUMERATED
                                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                                    CAPUserAbortData capUserAbortData = new CAPUserAbortData();
                                    capUserAbortData.decode(ais);

                                    CAPGeneralAbortReasons refuseReason = CAPGeneralAbortReasons.NO_REASON_GIVEN;

                                    switch (capUserAbortData.getReason()) {
                                        case ABNORMAL_PROCESSING:
                                            refuseReason = CAPGeneralAbortReasons.ABNORMAL_PROCESSING;
                                            break;
                                        case APPLICATION_TIMER_EXPIRED:
                                            refuseReason = CAPGeneralAbortReasons.APPLICATION_TIMER_EXPIRED;
                                            break;
                                        case CONGESTION:
                                            refuseReason = CAPGeneralAbortReasons.CONGESTION;
                                            break;
                                        case INVALID_REFERENCE:
                                            refuseReason = CAPGeneralAbortReasons.INVALID_REFERENCE;
                                            break;
                                        case MISSING_REFERENCE:
                                            refuseReason = CAPGeneralAbortReasons.MISSING_REFERENCE;
                                            break;
                                        case NOT_ALLOWED_PROCEDURES:
                                            refuseReason = CAPGeneralAbortReasons.NOT_ALLOWED_PROCEDURES;
                                            break;
                                        case OVERLAPPING_DIALOGUE:
                                            refuseReason = CAPGeneralAbortReasons.OVERLAPPING_DIALOGUE;
                                            break;
                                    }

                                    this.stack.fireUserAbort(refuseReason, this);

                                } else {
                                    logger.error(String.format("Expecting Tag[ENUM] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                                    this.stack.fireUserAbort(CAPGeneralAbortReasons.ABNORMAL_PROCESSING, this);
                                }
                            } catch (IOException ex) {
                                logger.error("ErrorOccured: ", ex);
                                this.stack.fireUserAbort(CAPGeneralAbortReasons.ABNORMAL_PROCESSING, this);

                            } catch (IncorrectSyntaxException ex) {
                                logger.error("ErrorOccured: ", ex);
                                this.stack.fireUserAbort(CAPGeneralAbortReasons.ABNORMAL_PROCESSING, this);

                            }
                        }
                    }
            }
        } finally {
            this.stack.releaseCapDialogue(getDialogueId());
            getTcapDialogue().getLock().unlock();
        }
    }

    public DialogueState getState() {
        return this.state;
    }

    /**
     * @return the userData
     */
    public CapGPRSReferenceNumber getCapGprsReferenceNumber() {
        return capGPRSReferenceNumber;
    }

    /**
     * @param capGPRSReferenceNumber
     */
    public void setUserData(CapGPRSReferenceNumber capGPRSReferenceNumber) {
        this.capGPRSReferenceNumber = capGPRSReferenceNumber;
    }

    private void sendUAbort(AbortReason reason) {
        TCUAbort tcUabort = new TCUAbort();
        tcUabort.setAbortReason(reason);
        if (reason == AbortReason.APPLICATION_CONTEXT_NAME_NOT_SUPPORTED) {
            tcUabort.setApplicationContextName(new ApplicationContextImpl(this.capApplicationContext.oid()));
        }
        tcUabort.setDialogueId(getDialogueId());
        this.getTcapDialogue().sendUAbort(tcUabort);
        this.stack.releaseCapDialogue(getDialogueId());
    }

    public void sendUAbortRequest(CapUAbortReason reason) {
        try {
            getTcapDialogue().getLock().lock();
            TCUAbort tcUAbort = new TCUAbort();
            tcUAbort.setAbortReason(AbortReason.USER_SPECIFIC);
            tcUAbort.setDialogueId(getDialogueId());

            AsnOutputStream aos = new AsnOutputStream();
            CAPUserAbortData capUserAbortData = new CAPUserAbortData(reason);
            capUserAbortData.encode(aos);

            UserInformationImpl userInformation = new UserInformationImpl();
            userInformation.setDirectReference(CAPUserAbortData.USER_ABORT_DATA_OID);
            userInformation.setEncoding(UserInformationImpl.Encoding.SINGLE_ASN_1);
            userInformation.setExternalData(aos.toByteArray());

            tcUAbort.setUserInformation(userInformation);
            this.tcapDialogue.sendUAbort(tcUAbort);
        } catch (IncorrectSyntaxException ex) {
            logger.error("ErrorOccured:", ex);
        } finally {
            this.stack.releaseCapDialogue(this.getDialogueId());
            getTcapDialogue().getLock().unlock();
        }
    }

    public Long getDialogueId() {
        return this.getTcapDialogue().getDialogueId();
    }

    private void onOperation(Operation operation) {
        switch (operation.getOperation()) {
            case TCINVOKE:
                this.onTCInvoke((TCInvoke) operation);
                break;
            case TCRETURNRESULTNOTLAST:
                this.getTcapDialogue().tcURejectRequest(((TCResultNotLast) operation).getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);
                break;
//            case TCLCANCEL:
//                this.onTCLCancel((TCLCancel) operation);
//                break;
            case TCLREJECT:
                this.onTCLReject((TCLReject) operation);
                break;
            case TCRETURNRESULTLAST:
                this.onTCReturnLast((TCResult) operation);
                break;
            case TCRREJECT:
                this.onTCRReject((TCRReject) operation);
                break;
            case TCUERROR:
                this.onTCUError((TCUError) operation);
                break;
            case TCUREJECT:
                this.onTCUReject((TCUReject) operation);
                break;
        }
    }


    private boolean isACNameUnchanged(ApplicationContext applicationContextName) {
        if (applicationContextName == null) {
            return false;
        }

        if (applicationContextName.getOid().length <= 0) {
            return false;
        }

        CAPApplicationContexts _capApplicationContext = CAPApplicationContexts.getInstance(applicationContextName.getOid());
        if (_capApplicationContext == null) {
            return false;
        }

        return _capApplicationContext == this.capApplicationContext;

    }

    void onNotice(TRNotice notice) {
        this.stack.fireNotice(Notifications.NO_RESPONSE_FROM_PEER, this);
//        this.stack.releaseCapDialogue(this.getDialogueId());
        requestClose(Termination.BASIC);
    }

    private void onTCInvoke(TCInvoke tcInvoke) {
        OperationCodes opCode = OperationCodes.getInstance(tcInvoke.getOperationCode());

        if (performingSSMTable.containsKey(tcInvoke.getInvokeID())) {//Invoke id already assigned
            logger.error(String.format("TCInvoke received."
                    + "InvokeId already assigned. Generate Reject. DialogueId[%d]: %s", this.getDialogueId(), tcInvoke));

            this.getTcapDialogue().tcURejectRequest(tcInvoke.getInvokeID(), InvokeProblem.DUPLICATE_INVOKE_ID);
            this.stack.fireNotice(Notifications.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
        } else if (this.capApplicationContext.isOperationExists(opCode)) {

            try {
                if (tcInvoke.getLinkedId() == null) {
                    this.processInvocation(opCode, tcInvoke);
                } else {
                    logger.warn("Linked operation received " + getDialogueId());
//                    this.processLinkedInvocation(opCode, tcInvoke);
                }
            } catch (IncorrectSyntaxException ex) {
                logger.error("MistypedParameterException. " + this, ex);
                tcapDialogue.tcURejectRequest(tcInvoke.getInvokeID(), InvokeProblem.MISTYPED_PARAMETER);

                this.stack.fireNotice(Notifications.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
            } catch (ParameterOutOfRangeException ex) {
                logger.error("MistypedParameterException." + this, ex);
                tcapDialogue.tcURejectRequest(tcInvoke.getInvokeID(), InvokeProblem.MISTYPED_PARAMETER);

                this.stack.fireNotice(Notifications.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
            } catch (IllegalNumberFormatException ex) {
                logger.error("MistypedParameterException. " + this, ex);
                tcapDialogue.tcURejectRequest(tcInvoke.getInvokeID(), InvokeProblem.MISTYPED_PARAMETER);

                this.stack.fireNotice(Notifications.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
            } catch (UnidentifableServiceException ex) {
                logger.error("UnidentifableServiceException. " + this, ex);

                if (opCode.getOperationClass() == OperationClass.CLASS3 || opCode.getOperationClass() == OperationClass.CLASS4) {
                    return;
                }

                this.tcapDialogue.sendUserError(tcInvoke.getInvokeID(), CAPUserErrorCodes.UNEXPECTED_DATA_VALUE.value());

                this.stack.fireNotice(Notifications.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);

            } catch (UnexpectedDataException ex) {
                logger.error("UnidentifableServiceException. " + this, ex);

                if (opCode.getOperationClass() == OperationClass.CLASS3 || opCode.getOperationClass() == OperationClass.CLASS4) {
                    return;
                }

                this.tcapDialogue.sendUserError(tcInvoke.getInvokeID(), CAPUserErrorCodes.UNEXPECTED_DATA_VALUE.value());

                this.stack.fireNotice(Notifications.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
            } catch (NoServiceParameterAvailableException ex) {
                logger.error("DataMissingException. " + this, ex);

                if (opCode.getOperationClass() == OperationClass.CLASS3 || opCode.getOperationClass() == OperationClass.CLASS4) {
                    return;
                }

                tcapDialogue.sendUserError(tcInvoke.getInvokeID(), CAPUserErrorCodes.MISSING_PARAMETER.value());
                stack.fireNotice(Notifications.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
            }
        } else {
            logger.error(String.format("TCInvoke received. Operation is not supported for AC. Operation = %s, AC = %s",
                    opCode, this.capApplicationContext));

            getTcapDialogue().tcURejectRequest(tcInvoke.getInvokeID(), InvokeProblem.UNRECOGNIZED_OPERATION);
        }
    }

    private void onTCReturnLast(TCResult indication) {
        InvocationStateMachine requestingSSM = tcapDialogue.getInvocationStateMachine(indication.getInvokeId());
        if (requestingSSM == null) {
            logger.error(String.format("TCResultLast received: RequestingSSM not found. DialogueId = %s InvokeId = %s",
                    this.getDialogueId(), indication.getInvokeId()));

            this.getTcapDialogue().tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);
            this.stack.fireNotice(Notifications.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("TCResultLast received: DialogueId = %s InvokeId = %s", this.getDialogueId(), indication.getInvokeId()));
            }
            this.onResultReceived(indication, requestingSSM.getOperationCode());
        }
    }

    private void onTCLReject(TCLReject indication) {
        if (!indication.isInvokeIdPresent()) {
            this.stack.fireNotice(Notifications.ABNORMAL_EVENT_DETECTED_BY_PEER, this);
            return;
        }

        ProblemImpl problem = indication.getProblem();

        if (problem.isInvokeProblem()) {
            InvocationStateMachine requestingSSM
                    = tcapDialogue.getInvocationStateMachine(indication.getInvokeId());

            if (requestingSSM == null) {
                stack.fireNotice(Notifications.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
            } else {
                this.onNegativeResultReceived(OperationCodes.getInstance(requestingSSM.getOperationCode()),
                        ProviderError.SERVICE_COMPLETION_FAILURE, indication.getInvokeId());
            }
        } else {
            stack.fireNotice(Notifications.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
        }

    }

    private void onTCRReject(TCRReject indication) {
        if (indication.getProblem().isInvokeProblem()) {
            InvocationStateMachine requestingSSM = tcapDialogue.getInvocationStateMachine(indication.getInvokeId());
            if (requestingSSM == null) {
                stack.fireNotice(Notifications.ABNORMAL_EVENT_DETECTED_BY_PEER, this);
            } else {
                this.onNegativeResultReceived(OperationCodes.getInstance(requestingSSM.getOperationCode()),
                        ProviderError.SERVICE_COMPLETION_FAILURE, indication.getInvokeId());
            }
        } else {
            Notifications mapDiagnostics = Notifications.RESPONSE_REJECTED_BY_PEER;
            if (indication.getProblem().isGeneralProblem()) {
                mapDiagnostics = Notifications.ABNORMAL_EVENT_DETECTED_BY_PEER;
            }
            stack.fireNotice(mapDiagnostics, this);
        }

    }

    private void onTCUReject(TCUReject indication) {
        if (indication.getProblem().isInvokeProblem()) {
            InvocationStateMachine requestingSSM = tcapDialogue.getInvocationStateMachine(indication.getInvokeId());
            if (requestingSSM == null) {
                stack.fireNotice(Notifications.ABNORMAL_EVENT_DETECTED_BY_PEER, this);
            } else {
                this.onNegativeResultReceived(OperationCodes.getInstance(requestingSSM.getOperationCode()),
                        ProviderError.SERVICE_COMPLETION_FAILURE, indication.getInvokeId());
            }
        } else {
            stack.fireNotice(Notifications.RESPONSE_REJECTED_BY_PEER, this);
        }
    }

    private void onTCUError(TCUError indication) {
        InvocationStateMachine requestingSSM = tcapDialogue.getInvocationStateMachine(indication.getInvokeId());

        if (requestingSSM == null) {
            tcapDialogue.tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.UNRECOGNIZED_INVOKE_ID);
            stack.fireNotice(Notifications.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
        } else {
            OperationCodes operationCode = OperationCodes.getInstance(requestingSSM.getOperationCode());

            try {
                CAPUserError capUserError = CAPErrorFactory.createCapUserError(indication.getError(), indication.getParameter());
                this.onNegativeResultReceived(operationCode, capUserError, indication.getInvokeId());
            } catch (UnknownCapErrorException ex) {
                logger.error("TCUError received: Unknown CAP error: " + this, ex);
                this.onNegativeResultReceived(operationCode, ProviderError.INVALID_RESPONSE_RECEIVED, indication.getInvokeId());
                tcapDialogue.tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);
            } catch (IncorrectSyntaxException ex) {
                logger.error("TCUError received: CAP Incorrect syntax error: " + this, ex);
                this.onNegativeResultReceived(operationCode, ProviderError.INVALID_RESPONSE_RECEIVED, indication.getInvokeId());
                tcapDialogue.tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);
            } catch (NoServiceParameterAvailableException ex) {
                logger.error("TCUError received: CAP No Service parameter available error: " + this, ex);
                this.onNegativeResultReceived(operationCode, ProviderError.INVALID_RESPONSE_RECEIVED, indication.getInvokeId());
                tcapDialogue.tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);
            } catch (UnexpectedDataException ex) {
                logger.error("TCUError received: : CAP Unexpected Data error: " + this, ex);
                this.onNegativeResultReceived(operationCode, ProviderError.INVALID_RESPONSE_RECEIVED, indication.getInvokeId());
                tcapDialogue.tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);
            }
        }
    }

    public void onTCLCancel(TCLCancel indication) {
        InvocationStateMachine requestingSSM = tcapDialogue.getInvocationStateMachine(indication.getInvokeId());

        if (requestingSSM != null) {
            this.onTimerExpiry(indication.getInvokeId(), OperationCodes.getInstance(requestingSSM.getOperationCode()));
        }
    }

    /**
     * @return the tcapDialogue
     */
    public TCAPDialogue getTcapDialogue() {
        return tcapDialogue;
    }

    protected void processActivityTest(Short invokeId) {
        stack.onCAPActivityTest(invokeId, this);
    }

    public void onDialogueTimeOut() {
        this.stack.releaseCapDialogue(this.getDialogueId());
        this.stack.fireCAPDialogueTimeOut(this);
    }

    public void setRespondingAddress(SCCPAddress respondingAddress) {
        if (state == DialogueState.IDLE
                || state == DialogueState.DIALOGUE_ESTABLISHED
                || state == DialogueState.DIALOGUE_ACCEPTED) {
            this.tcapDialogue.setCalledParty(respondingAddress);
        }
    }

    public void setCapApplicationContext(CAPApplicationContexts capApplicationContext) {
        this.capApplicationContext = capApplicationContext;
    }

    public void setState(DialogueState state) {
        this.state = state;
    }

    protected final void onTimerExpiry(Short invokeId, OperationCodes opCode) {
        switch (opCode.getOperationClass()) {
            case CLASS2:
//                if (stack.capCache.isLinkedOperationInvoke(getDialogueId(), invokeId)) {
//                    if (stack.capCache.isImplicitConfirm(getDialogueId(), invokeId)) {
//                        this.onNegativeResultReceived(opCode, new SystemFailure(), invokeId);
//                    } else {
//                        this.onNegativeResultReceived(opCode, ProviderError.NO_RESPONSE_FROM_PEER, invokeId);
//                    }
                ///}else
            {
                this.onNegativeResultReceived(opCode, new SystemFailure(), invokeId);
            }
            break;
            case CLASS1:
            case CLASS3:
                this.onNegativeResultReceived(opCode, ProviderError.NO_RESPONSE_FROM_PEER, invokeId);
                break;

        }
    }


    private void processInvocation(OperationCodes opCode, TCInvoke indication) throws IncorrectSyntaxException, UnidentifableServiceException, NoServiceParameterAvailableException, UnexpectedDataException, ParameterOutOfRangeException, IllegalNumberFormatException {

        Short invokeId = indication.getInvokeID();
        if (opCode.getOperationClass() != OperationClass.CLASS4) {
            Future future = stack.getGuardTimer().schedule(new GuardTimerTask(this, invokeId),
                    stack.getGuardTimerValue(), TimeUnit.SECONDS);
            performingSSMTable.put(invokeId, future);
        }

        this.onServiceInvocationReceived(indication, opCode);
    }
//
//    private void processLinkedInvocation(OperationCodes opCode, TCInvoke indication) throws IncorrectSyntaxException, UnidentifableServiceException, NoServiceParameterAvailableException, UnexpectedDataException, ParameterOutOfRangeException, IllegalNumberFormatException {
//        RequestingSSM requestingSSM = stack.capCache.getRequestingSSM(getDialogueId(), indication.getLinkedId());
//        if (requestingSSM == null) {
//            tcapDialogue.tcURejectRequest(indication.getInvokeID(), InvokeProblem.UNRECOGNIZED_LINKED_ID);
//            stack.fireNotice(Notifications.ABNORMAL_EVENT_RECEIVED_FROM_PEER, this);
//            logger.error(String.format("LinkedId not assigned. %s, %s", indication, this));
//        } else if (indication.getOperationClass() == OperationClass.CLASS4) {
//            stack.capCache.linkedOperationInvoked(getDialogueId(), indication.getInvokeID());
//            if (requestingSSM.getOperation().isLinkedOperationAllowed(opCode)) {
//                this.onLinkedRequestReceived(indication);
//            } else {
//                this.onNegativeResultReceived(opCode, ProviderError.INVALID_RESPONSE_RECEIVED, indication.getInvokeID());
//                this.getTcapDialogue().tcURejectRequest(indication.getInvokeID(), ReturnResultProblem.MISTYPED_PARAMETER);
//            }
//        } else {
//            stack.capCache.linkedOperationInvoked(getDialogueId(), indication.getLinkedId());
//            stack.capCache.implicitConfirm(getDialogueId(), indication.getLinkedId());
//
//            this.onLinkedServiceInvoked(indication);
//            stack.capCache.initiatePSSM(this.getDialogueId(), indication.getInvokeID(), stack.getGuardTimerValue());
//            this.onServiceInvocationReceived(indication, opCode);
//
//        }
//    }

    public enum Result {

        ACCEPTED,
        REFUSED;
    }

    public void attach(T attachment) {
        this.attachment = attachment;
    }

    public T getAttachment() {
        return attachment;
    }

    //TODO Should be overrided for linked service
    protected void onLinkedRequestReceived(TCInvoke indication) {

    }

    //TODO Should be override for linked service
    protected void onLinkedServiceInvoked(TCInvoke indication) {

    }

    protected void terminatePerformingSSM(Short invokeId) {
        Future future = performingSSMTable.remove(invokeId);
        if (future != null) {
            future.cancel(false);
        }
    }

    protected void terminateAllPerformingSSMs() {
        tcapDialogue.getLock().lock();
        try {
            performingSSMTable.values().forEach((future) -> {
                future.cancel(false);
            });

            performingSSMTable.clear();
        } finally {
            tcapDialogue.getLock().unlock();
        }
    }

    protected boolean isPerformingSSMExists(Short invokeId) {
        return performingSSMTable.containsKey(invokeId);
    }


    protected abstract void onServiceInvocationReceived(TCInvoke invoke, OperationCodes opCode) throws IncorrectSyntaxException, IncorrectSyntaxException, UnidentifableServiceException, NoServiceParameterAvailableException, UnexpectedDataException, ParameterOutOfRangeException, IllegalNumberFormatException;

    protected abstract void onResultReceived(TCResult indication, int opCode);

    protected abstract void onNegativeResultReceived(OperationCodes operation, CAPUserError userError, Short invokeId);

    protected abstract void onNegativeResultReceived(OperationCodes operation, ProviderError providerError, Short invokeId);

}
