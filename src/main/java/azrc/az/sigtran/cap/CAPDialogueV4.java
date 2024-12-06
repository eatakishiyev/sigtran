/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap;

import java.io.IOException;

import azrc.az.sigtran.cap.errors.CAPUserError;
import azrc.az.sigtran.cap.parameters.CapGPRSReferenceNumber;
import azrc.az.sigtran.tcap.ResourceLimitationException;
import azrc.az.sigtran.tcap.TCAPDialogue;
import azrc.az.sigtran.cap.callcontrol.general.ApplyChargingReportArg;
import azrc.az.sigtran.cap.callcontrol.general.AssistRequestInstructionArg;
import azrc.az.sigtran.cap.callcontrol.v4.ApplyChargingArg;
import azrc.az.sigtran.cap.callcontrol.v4.ConnectArgImpl;
import azrc.az.sigtran.cap.callcontrol.v4.EstablishTemporaryConnectionArg;
import azrc.az.sigtran.cap.callcontrol.v4.EventReportBCSMV4ArgImpl;
import azrc.az.sigtran.cap.callcontrol.v4.InitialDpArgImpl;
import azrc.az.sigtran.cap.callcontrol.v4.PlayAnnouncementArg;
import azrc.az.sigtran.cap.callcontrol.v4.ReleaseCallArg;
import azrc.az.sigtran.cap.callcontrol.v4.RequestReportBCSMEventArgImpl;
import azrc.az.sigtran.cap.messages.circuit.switched.call.control.bcsm.ConnectToResourceArg;
import azrc.az.sigtran.cap.smscontrol.general.ConnectSMSArg;
import azrc.az.sigtran.cap.smscontrol.general.ReleaseSMSArg;
import azrc.az.sigtran.cap.smscontrol.v4.EventReportSMSArg;
import azrc.az.sigtran.cap.smscontrol.v4.InitialDpSMSArgImpl;
import azrc.az.sigtran.cap.smscontrol.v4.RequestReportSMSEventArg;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.NoServiceParameterAvailableException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.common.exceptions.UnexpectedDialogueStateException;
import azrc.az.sigtran.common.exceptions.UnidentifableServiceException;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.messages.MessageHandling;
import azrc.az.sigtran.tcap.parameters.ParameterImpl;
import azrc.az.sigtran.tcap.primitives.tc.TCInvoke;
import azrc.az.sigtran.tcap.primitives.tc.TCResult;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author eatakishiyev
 */
public class CAPDialogueV4 extends CAPDialogue {

    private final Logger logger = LoggerFactory.getLogger(CAPDialogueV4.class);

    public CAPDialogueV4(CAPStackImpl stack, CAPApplicationContexts capApplicationContext,
            SCCPAddress destinationAddress, SCCPAddress originationAddress,
            CapGPRSReferenceNumber capGprsReferenceNumber,
            MessageHandling messageHandling, boolean sequenceControl) throws ResourceLimitationException {
        super(stack, capApplicationContext, destinationAddress, originationAddress,
                capGprsReferenceNumber, messageHandling, sequenceControl);
    }

    public CAPDialogueV4(TCAPDialogue tcapDialogue, CAPStackImpl stack) {
        super(tcapDialogue, stack);
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke, OperationCodes operationCode) throws IncorrectSyntaxException, IncorrectSyntaxException, UnidentifableServiceException, NoServiceParameterAvailableException, UnexpectedDataException, ParameterOutOfRangeException, IllegalNumberFormatException {
        if (logger.isDebugEnabled()) {
            logger.debug("CAP ServiceInvocationReceived: " + operationCode + " dialogueId " + getDialogueId());
        }

        switch (operationCode) {
            case INITIAL_DP:
                this.processInitialDp(invoke);
                break;
            case EVENT_REPORT_BCSM:
                this.processEventReportBCSM(invoke);
                break;
            case REQUEST_REPORT_BCSM_EVENT:
                this.processRequestReportBCSMEvent(invoke);
                break;
            case RELEASE_CALL:
                this.processReleaseCall(invoke);
                break;
            case CONNECT:
                this.processConnect(invoke);
                break;
            case CONTINUE:
                this.processContinue(invoke);
                break;
            case APPLY_CHARGING:
                this.processApplyCharging(invoke);
                break;
            case APPLY_CHARGING_REPORT:
                this.processApplyChargingReport(invoke);
                break;
            case PLAY_ANNOUNCEMENT:
                this.processPlayAnnouncement(invoke);
                break;
            case CONNECT_TO_RESOURCE:
                this.processConnectToResource(invoke);
                break;
            case ACTIVITY_TEST:
                this.processActivityTest(invoke.getInvokeID());
                break;
            case ASSIST_REQUEST_INSTRUCTIONS:
                this.processAssistRequestInstructions(invoke);
                break;
            case ESTABLISH_TEMPORARY_CONNECTION:
                this.processEstablishTemporaryConnection(invoke);
                break;
            case INITIAL_DP_SMS:
                this.processInitialDpSms(invoke);
                break;
            case EVENT_REPORT_SMS:
                this.processEventReportSms(invoke);
                break;
            case REQUEST_REPORT_SMS_EVENT:
                this.processRequestReportSMSEvent(invoke);
                break;
            case CONTINUE_SMS:
                this.processContinueSms(invoke);
                break;
            case CONNECT_SMS:
                this.processConnectSms(invoke);
                break;
            case RELEASE_SMS:
                this.processReleaseSms(invoke);
                break;
        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int opCode) {
        OperationCodes _opCode = OperationCodes.getInstance(opCode);
        switch (_opCode) {
            case ACTIVITY_TEST:
                stack.onCAPActivityTestConfirmation(indication.getInvokeId(), this);
                break;
            default:
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    protected void onNegativeResultReceived(OperationCodes operationCode, CAPUserError userError, Short invokeId) {

        switch (operationCode) {
            case INITIAL_DP:
                stack.onCAPInitialDpError(invokeId, this, userError);
                break;
            case EVENT_REPORT_BCSM:
                stack.onCAPEventReportBCSMError(invokeId, this, userError);
                break;
            case REQUEST_REPORT_BCSM_EVENT:
                stack.onCAPRequestReportBCSMEventError(invokeId, this, userError);
                break;
            case CONNECT:
                stack.onCAPConnectError(invokeId, this, userError);
                break;
            case APPLY_CHARGING:
                stack.onCAPApplyChargingError(invokeId, this, userError);
                break;
            case APPLY_CHARGING_REPORT:
                stack.onCAPApplyChargingReportError(invokeId, this, userError);
                break;
            case PLAY_ANNOUNCEMENT:
                stack.onCAPPlayAnnouncementError(invokeId, this, userError);
                break;
            case CONNECT_TO_RESOURCE:
                stack.onCAPConnectToResourceError(invokeId, this, userError);
                break;
            case ASSIST_REQUEST_INSTRUCTIONS:
                stack.onCAPAssistRequestInstructionsError(invokeId, this, userError);
                break;
            case ESTABLISH_TEMPORARY_CONNECTION:
                stack.onCAPEstablishTemporaryConnectionError(invokeId, this, userError);
                break;
            case INITIAL_DP_SMS:
                stack.onCAPInitialDpSmsError(invokeId, this, userError);
                break;
            case EVENT_REPORT_SMS:
                stack.onCAPEventReportSMSError(invokeId, this, userError);
                break;
            case REQUEST_REPORT_SMS_EVENT:
                stack.onCAPRequestReportSMSError(invokeId, this, userError);
                break;
            case CONNECT_SMS:
                stack.onCAPConnectSMSError(invokeId, this, userError);
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(OperationCodes operationCode, ProviderError providerError, Short invokeId) {
        switch (operationCode) {
            case INITIAL_DP:
                stack.onCAPInitialDpError(invokeId, this, providerError);
                break;
            case EVENT_REPORT_BCSM:
                stack.onCAPEventReportBCSMError(invokeId, this, providerError);
                break;
            case REQUEST_REPORT_BCSM_EVENT:
                stack.onCAPRequestReportBCSMEventError(invokeId, this, providerError);
                break;
            case CONNECT:
                stack.onCAPConnectError(invokeId, this, providerError);
                break;
            case APPLY_CHARGING:
                stack.onCAPApplyChargingError(invokeId, this, providerError);
                break;
            case APPLY_CHARGING_REPORT:
                stack.onCAPApplyChargingReportError(invokeId, this, providerError);
                break;
            case PLAY_ANNOUNCEMENT:
                stack.onCAPPlayAnnouncementError(invokeId, this, providerError);
                break;
            case CONNECT_TO_RESOURCE:
                stack.onCAPConnectToResourceError(invokeId, this, providerError);
                break;
            case ASSIST_REQUEST_INSTRUCTIONS:
                stack.onCAPAssistRequestInstructionsError(invokeId, this, providerError);
                break;
            case ESTABLISH_TEMPORARY_CONNECTION:
                stack.onCAPEstablishTemporaryConnectionError(invokeId, this, providerError);
                break;
            case INITIAL_DP_SMS:
                stack.onCAPInitialDpSmsError(invokeId, this, providerError);
                break;
            case EVENT_REPORT_SMS:
                stack.onCAPEventReportSMSError(invokeId, this, providerError);
                break;
            case REQUEST_REPORT_SMS_EVENT:
                stack.onCAPRequestReportSMSError(invokeId, this, providerError);
                break;
            case CONNECT_SMS:
                stack.onCAPConnectSMSError(invokeId, this, providerError);
                break;
        }
    }

//    
    private void processInitialDp(TCInvoke invoke) throws NoServiceParameterAvailableException, ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDataException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        InitialDpArgImpl initialDpArg = new InitialDpArgImpl();
        initialDpArg.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;

        stack.onCAPInitialDpIndication(invoke.getInvokeID(), initialDpArg, this);
    }

    private void processEventReportBCSM(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException, UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        EventReportBCSMV4ArgImpl eventReportBCSMArg = new EventReportBCSMV4ArgImpl();
        eventReportBCSMArg.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;

        stack.onCAPEventReportBCSM(invoke.getInvokeID(), eventReportBCSMArg, this);
    }

    private void processRequestReportBCSMEvent(TCInvoke invoke) throws NoServiceParameterAvailableException, ParameterOutOfRangeException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        RequestReportBCSMEventArgImpl requestReportBCSMEventArg = new RequestReportBCSMEventArgImpl();
        requestReportBCSMEventArg.decode(new AsnInputStream(parameter.getData()));

        int timeOut = 0;

        stack.onCAPRequestReportBCSM(invoke.getInvokeID(), requestReportBCSMEventArg, this);
    }

    private void processReleaseCall(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ReleaseCallArg releaseCallArg = new ReleaseCallArg();
        releaseCallArg.decode(new AsnInputStream(parameter.getData()));

        int timeOut = 0;

        stack.onCAPReleaseCall(invoke.getInvokeID(), releaseCallArg, this);
    }

    private void processConnect(TCInvoke invoke) throws NoServiceParameterAvailableException, ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ConnectArgImpl connectArg = new ConnectArgImpl();
        connectArg.decode(new AsnInputStream(parameter.getData()));

        int timeOut = 0;

        stack.onCAPConnect(invoke.getInvokeID(), connectArg, this);
    }

    private void processContinue(TCInvoke invoke) {
        stack.onCAPContinue(invoke.getInvokeID(), this);
    }

    private void processApplyCharging(TCInvoke invoke) throws NoServiceParameterAvailableException, ParameterOutOfRangeException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ApplyChargingArg applyChargingArg = new ApplyChargingArg();
        applyChargingArg.decode(new AsnInputStream(parameter.getData()));

        int timeOut = 0;
        stack.onCAPApplyCharging(invoke.getInvokeID(), applyChargingArg, this);
    }

    private void processApplyChargingReport(TCInvoke invoke) throws NoServiceParameterAvailableException, ParameterOutOfRangeException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ApplyChargingReportArg applyChargingReportArg = new ApplyChargingReportArg();
        applyChargingReportArg.decode(new AsnInputStream(parameter.getData()));

        int timeOut = 0;
        stack.onCAPApplyChargingReport(invoke.getInvokeID(), applyChargingReportArg, this);
    }

    private void processPlayAnnouncement(TCInvoke invoke) throws NoServiceParameterAvailableException, ParameterOutOfRangeException, IllegalNumberFormatException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        PlayAnnouncementArg playAnnouncementArg = new PlayAnnouncementArg();
        playAnnouncementArg.decode(new AsnInputStream(parameter.getData()));

        int timeOut = 0;
        stack.onCAPPlayAnnouncement(invoke.getInvokeID(), playAnnouncementArg, this);
    }

    private void processConnectToResource(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException, IllegalNumberFormatException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ConnectToResourceArg connectToResourceArg
                = new ConnectToResourceArg();
        connectToResourceArg.decode(new AsnInputStream(parameter.getData()));

        int timeOut = 0;

        stack.onCAPConnectToResource(invoke.getInvokeID(), connectToResourceArg, this);
    }

    private void processAssistRequestInstructions(TCInvoke invoke) throws NoServiceParameterAvailableException, IllegalNumberFormatException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        AssistRequestInstructionArg assistRequestInstructionArg
                = new AssistRequestInstructionArg();
        assistRequestInstructionArg.decode(new AsnInputStream(parameter.getData()));

        int timeOut = 0;

        stack.onCAPAssistRequestInstructions(invoke.getInvokeID(), assistRequestInstructionArg, this);
    }

    private void processEstablishTemporaryConnection(TCInvoke invoke) throws NoServiceParameterAvailableException, IllegalNumberFormatException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        EstablishTemporaryConnectionArg establishTemporaryConnection = new EstablishTemporaryConnectionArg();
        establishTemporaryConnection.decode(new AsnInputStream(parameter.getData()));

        int timeOut = 0;

        stack.onCAPEstablishTemporaryConnection(invoke.getInvokeID(), establishTemporaryConnection, this);
    }

    private void processInitialDpSms(TCInvoke invoke) throws NoServiceParameterAvailableException, UnexpectedDataException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        InitialDpSMSArgImpl initialDpSMSArg = new InitialDpSMSArgImpl();
        initialDpSMSArg.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;

        stack.onCAPInitialDpSmsIndication(invoke.getInvokeID(), initialDpSMSArg, this);
    }

    private void processEventReportSms(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        EventReportSMSArg eventReportSMSArg = new EventReportSMSArg();
        eventReportSMSArg.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;

        stack.onCAPEventReportSmsIndication(invoke.getInvokeID(), eventReportSMSArg, this);
    }

    private void processRequestReportSMSEvent(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        RequestReportSMSEventArg requestReportSMSEventArg = new RequestReportSMSEventArg();
        requestReportSMSEventArg.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;

        stack.onCAPRequestReportSMSEvent(invoke.getInvokeID(), requestReportSMSEventArg, this);
    }

    private void processContinueSms(TCInvoke invoke) {
        stack.onCAPContinueSms(invoke.getInvokeID(), this);
    }

    private void processConnectSms(TCInvoke invoke) throws UnexpectedDataException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();

        ConnectSMSArg connectSMSArg = new ConnectSMSArg();
        if (parameter != null) {
            connectSMSArg.decode(new AsnInputStream(parameter.getData()));
        }

        long timeOut = 0;

        stack.onCAPConnectSMS(invoke.getInvokeID(), connectSMSArg, this);
    }

    private void processReleaseSms(TCInvoke invoke) throws IncorrectSyntaxException, NoServiceParameterAvailableException {
        ParameterImpl parameter = invoke.getParameter();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ReleaseSMSArg releaseSMSArg = new ReleaseSMSArg();
        releaseSMSArg.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;

        stack.onCAPReleaseSMS(invoke.getInvokeID(), releaseSMSArg, this);
    }

    public Short sendCAPInitialDp(InitialDpArgImpl initialDpArg) throws ParameterOutOfRangeException, IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDataException, UnexpectedDialogueStateException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.INITIAL_DP;

                AsnOutputStream aos = new AsnOutputStream();
                initialDpArg.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), parameter, 0);
                return invokeId;
            }
            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendCAPEventReportBCSM(EventReportBCSMV4ArgImpl eventReportBCSMArg) throws IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDialogueStateException, UnexpectedDataException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.EVENT_REPORT_BCSM;

                AsnOutputStream aos = new AsnOutputStream();
                eventReportBCSMArg.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), parameter, 0);
                return invokeId;
            }

            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));

        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendCAPApplyChargingReport(ApplyChargingReportArg applyChargingReportArg) throws ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDialogueStateException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.APPLY_CHARGING_REPORT;

                AsnOutputStream aos = new AsnOutputStream();
                applyChargingReportArg.encode(aos);
                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), parameter, 0);
                return invokeId;

            }

            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendCAPApplyCharging(ApplyChargingArg applyChargingArg) throws ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDialogueStateException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.APPLY_CHARGING;

                AsnOutputStream aos = new AsnOutputStream();
                applyChargingArg.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), parameter, 0);
                return invokeId;
            }

            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendCAPConnect(ConnectArgImpl connectArg) throws UnexpectedDialogueStateException, ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDataException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.CONNECT;

                AsnOutputStream aos = new AsnOutputStream();
                connectArg.encode(aos);
                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), parameter, 0);
                return invokeId;
            }
            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendCAPRequestReportBCSMEvent(RequestReportBCSMEventArgImpl requestReportBCSMEventArg) throws IncorrectSyntaxException, UnexpectedDialogueStateException, ParameterOutOfRangeException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.REQUEST_REPORT_BCSM_EVENT;

                AsnOutputStream aos = new AsnOutputStream();
                requestReportBCSMEventArg.encode(aos);
                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), parameter, 0);
                return invokeId;
            }
            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendCAPAssistRequestInstructions(AssistRequestInstructionArg assistRequestInstructionArg) throws IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDialogueStateException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.ASSIST_REQUEST_INSTRUCTIONS;

                AsnOutputStream aos = new AsnOutputStream();
                assistRequestInstructionArg.encode(aos);
                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), parameter, 0);
                return invokeId;

            }
            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendCAPEstablishTemporaryConnection(EstablishTemporaryConnectionArg establishTemporaryConnectionArg) throws UnexpectedDialogueStateException, IllegalNumberFormatException, IncorrectSyntaxException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.ESTABLISH_TEMPORARY_CONNECTION;

                AsnOutputStream aos = new AsnOutputStream();
                establishTemporaryConnectionArg.encode(aos);
                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), parameter, 0);
                return invokeId;
            }
            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendPlayAnnouncement(PlayAnnouncementArg playAnnouncementArg) throws AsnException, ParameterOutOfRangeException, IllegalNumberFormatException, IOException, UnexpectedDialogueStateException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.PLAY_ANNOUNCEMENT;

                AsnOutputStream aos = new AsnOutputStream();
                playAnnouncementArg.encode(aos);
                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), parameter, 0);
                return invokeId;
            }
            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendCAPInitialDpSMS(InitialDpSMSArgImpl initialDpSMSArg) throws UnexpectedDialogueStateException, IncorrectSyntaxException, IllegalNumberFormatException, UnexpectedDataException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.INITIAL_DP_SMS;

                AsnOutputStream aos = new AsnOutputStream();
                initialDpSMSArg.encode(aos);
                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), parameter, 0);
                return invokeId;
            }
            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendCAPContinueSMS() throws UnexpectedDialogueStateException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.CONTINUE_SMS;

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), 0);
                return invokeId;
            }
            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendCAPReleaseSMS(byte cause) throws UnexpectedDialogueStateException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.RELEASE_SMS;

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), 0);
                return invokeId;
            }
            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendCAPRequestReportSMSEvent(RequestReportSMSEventArg requestReportSMSEventArg) throws IncorrectSyntaxException, UnexpectedDialogueStateException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.REQUEST_REPORT_SMS_EVENT;

                AsnOutputStream aos = new AsnOutputStream();
                requestReportSMSEventArg.encode(aos);
                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), parameter, 0);
                return invokeId;
            }
            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendCAPEventReportSMS(EventReportSMSArg eventReportSMSArg) throws UnexpectedDialogueStateException, IncorrectSyntaxException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.EVENT_REPORT_SMS;

                AsnOutputStream aos = new AsnOutputStream();
                eventReportSMSArg.encode(aos);
                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), parameter, 0);
                return invokeId;
            }
            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendCAPConnectSMS(ConnectSMSArg connectSMSArg) throws UnexpectedDialogueStateException, IncorrectSyntaxException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.CONNECT_SMS;

                AsnOutputStream aos = new AsnOutputStream();
                connectSMSArg.encode(aos);
                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), parameter, 0);
                return invokeId;
            }
            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }
}
