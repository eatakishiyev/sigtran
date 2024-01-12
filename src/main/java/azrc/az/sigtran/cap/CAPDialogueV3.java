/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap;

import azrc.az.sigtran.cap.errors.CAPUserError;
import azrc.az.sigtran.tcap.ResourceLimitationException;
import azrc.az.sigtran.tcap.TCAPDialogue;
import azrc.az.sigtran.cap.callcontrol.general.ApplyChargingReportArg;
import azrc.az.sigtran.cap.callcontrol.general.ReleaseCallArg;
import azrc.az.sigtran.cap.callcontrol.v3.ApplyChargingArg;
import azrc.az.sigtran.cap.callcontrol.v3.ConnectArgImpl;
import azrc.az.sigtran.cap.callcontrol.v3.EstablishTemporaryConnectionArg;
import azrc.az.sigtran.cap.callcontrol.v3.EventReportBCSMArgImpl;
import azrc.az.sigtran.cap.callcontrol.v3.InitialDpArgImpl;
import azrc.az.sigtran.cap.callcontrol.v3.RequestReportBCSMEventArgImpl;
import azrc.az.sigtran.cap.smscontrol.general.ConnectSMSArg;
import azrc.az.sigtran.cap.smscontrol.general.ReleaseSMSArg;
import azrc.az.sigtran.cap.smscontrol.v3.EventReportSMSArg;
import azrc.az.sigtran.cap.smscontrol.v3.InitialDpSMSArgImpl;
import azrc.az.sigtran.cap.smscontrol.v3.RequestReportSMSEventArg;
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
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class CAPDialogueV3 extends CAPDialogue {

    public CAPDialogueV3(CAPStackImpl stack, CAPApplicationContexts capApplicationContext,
            SCCPAddress destinationAddress, SCCPAddress originationAddress,
            MessageHandling messageHandling, boolean sequenceControl) throws ResourceLimitationException {
        super(stack, capApplicationContext, destinationAddress, originationAddress,
                null, messageHandling, sequenceControl);
    }

    public CAPDialogueV3(TCAPDialogue tcapDialogue, CAPStackImpl stack) {
        super(tcapDialogue, stack);
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke, OperationCodes opCode) throws IncorrectSyntaxException, IncorrectSyntaxException, UnidentifableServiceException, NoServiceParameterAvailableException, UnexpectedDataException, ParameterOutOfRangeException, IllegalNumberFormatException {
        switch (opCode) {
            case INITIAL_DP:
                this.processInitialDp(invoke);
                break;
            case APPLY_CHARGING:
                this.processApplyCharging(invoke);
                break;
            case APPLY_CHARGING_REPORT:
                this.processApplyChargingReport(invoke);
                break;
            case REQUEST_REPORT_BCSM_EVENT:
                this.processRequestReportBCSMEvent(invoke);
                break;
            case EVENT_REPORT_BCSM:
                this.processEventReportBCSM(invoke);
                break;
            case RELEASE_CALL:
                this.processReleaseCall(invoke);
                break;
            case INITIAL_DP_SMS:
                this.processInitialDpSMS(invoke);
                break;
            case EVENT_REPORT_SMS:
                this.processEventReportSMS(invoke);
                break;
            case REQUEST_REPORT_SMS_EVENT:
                this.processRequestReportSMSEvent(invoke);
                break;
            case CONTINUE_SMS:
                this.processContinueSMS(invoke);
                break;
            case CONNECT_SMS:
                this.processConnectSMS(invoke);
                break;
            case RELEASE_SMS:
                this.processReleaseSMS(invoke);
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
    protected void onNegativeResultReceived(OperationCodes operation, CAPUserError userError, Short invokeId) {
        switch (operation) {
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
    protected void onNegativeResultReceived(OperationCodes operation, ProviderError providerError, Short invokeId) {
        switch (operation) {
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

    private void processInitialDpSMS(TCInvoke invoke) throws NoServiceParameterAvailableException, UnexpectedDataException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        InitialDpSMSArgImpl initialDpSMSArg = new InitialDpSMSArgImpl();
        initialDpSMSArg.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;

        stack.onCAPInitialDpSmsIndication(invoke.getInvokeID(), initialDpSMSArg, this);
    }

    private void processEventReportSMS(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException {
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

    private void processContinueSMS(TCInvoke invoke) {
        stack.onCAPContinueSms(invoke.getInvokeID(), this);
    }

    private void processConnectSMS(TCInvoke invoke) throws UnexpectedDataException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();

        ConnectSMSArg connectSMSArg = new ConnectSMSArg();
        if (parameter != null) {
            connectSMSArg.decode(new AsnInputStream(parameter.getData()));
        }

        long timeOut = 0;

        stack.onCAPConnectSMS(invoke.getInvokeID(), connectSMSArg, this);
    }

    private void processReleaseSMS(TCInvoke invoke) throws IncorrectSyntaxException, NoServiceParameterAvailableException {
        ParameterImpl parameter = invoke.getParameter();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ReleaseSMSArg releaseSMSArg = new ReleaseSMSArg();
        releaseSMSArg.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;

        stack.onCAPReleaseSMS(invoke.getInvokeID(), releaseSMSArg, this);
    }

    private void processInitialDp(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException, ParameterOutOfRangeException, UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        InitialDpArgImpl initialDp = new InitialDpArgImpl();
        initialDp.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;

        stack.onCAPInitialDpIndication(invoke.getInvokeID(), initialDp, this);
    }

    private void processApplyCharging(TCInvoke invoke) throws NoServiceParameterAvailableException, ParameterOutOfRangeException, IncorrectSyntaxException {
        ParameterImpl parameter = new ParameterImpl();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ApplyChargingArg applyCharging = new ApplyChargingArg();
        applyCharging.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;
        stack.onCAPApplyCharging(invoke.getInvokeID(), applyCharging, this);
    }

    private void processApplyChargingReport(TCInvoke invoke) throws ParameterOutOfRangeException, NoServiceParameterAvailableException, IncorrectSyntaxException {
        ParameterImpl parameter = new ParameterImpl();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ApplyChargingReportArg applyChargingReport = new ApplyChargingReportArg();
        applyChargingReport.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;
        stack.onCAPApplyChargingReport(invoke.getInvokeID(), applyChargingReport, this);
    }

    private void processRequestReportBCSMEvent(TCInvoke invoke) throws NoServiceParameterAvailableException, ParameterOutOfRangeException, IncorrectSyntaxException {
        ParameterImpl parameter = new ParameterImpl();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        RequestReportBCSMEventArgImpl requestReportBCSMEvent = new RequestReportBCSMEventArgImpl();
        requestReportBCSMEvent.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;
        stack.onCAPRequestReportBCSM(invoke.getInvokeID(), requestReportBCSMEvent, this);

    }

    private void processEventReportBCSM(TCInvoke invoke) throws NoServiceParameterAvailableException, ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDataException {
        ParameterImpl parameter = new ParameterImpl();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        EventReportBCSMArgImpl eventReportBCSM = new EventReportBCSMArgImpl();
        eventReportBCSM.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;
        stack.onCAPEventReportBCSM(invoke.getInvokeID(), eventReportBCSM, this);
    }

    private void processReleaseCall(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException {
        ParameterImpl parameter = new ParameterImpl();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ReleaseCallArg releaseCall = new ReleaseCallArg();
        releaseCall.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;
        stack.onCAPReleaseCall(invoke.getInvokeID(), releaseCall, this);

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

    public Short sendCAPEventReportBCSM(EventReportBCSMArgImpl eventReportBCSM) throws UnexpectedDialogueStateException, IncorrectSyntaxException, IllegalNumberFormatException, UnexpectedDataException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.EVENT_REPORT_BCSM;

                AsnOutputStream aos = new AsnOutputStream();
                eventReportBCSM.encode(aos);
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

    public Short sendCAPConnect(ConnectArgImpl connectArg) throws UnexpectedDialogueStateException, IncorrectSyntaxException, ParameterOutOfRangeException, UnexpectedDataException {
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
