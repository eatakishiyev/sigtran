/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap;

import java.io.IOException;
import dev.ocean.sigtran.cap.callcontrol.general.ApplyChargingReportArg;
import dev.ocean.sigtran.cap.callcontrol.general.AssistRequestInstructionArg;
import dev.ocean.sigtran.cap.callcontrol.general.CAMELCallResult;
import dev.ocean.sigtran.cap.callcontrol.general.ReleaseCallArg;
import dev.ocean.sigtran.cap.callcontrol.v2.ApplyChargingArg;
import dev.ocean.sigtran.cap.callcontrol.v2.ConnectArgImpl;
import dev.ocean.sigtran.cap.callcontrol.v2.EstablishTemporaryConnectionArg;
import dev.ocean.sigtran.cap.callcontrol.v2.EventReportBCSMArgImpl;
import dev.ocean.sigtran.cap.callcontrol.v2.InitialDpArgImpl;
import dev.ocean.sigtran.cap.callcontrol.v2.PlayAnnouncementArg;
import dev.ocean.sigtran.cap.callcontrol.v2.RequestReportBCSMEventArgImpl;
import dev.ocean.sigtran.cap.errors.CAPUserError;
import dev.ocean.sigtran.cap.messages.circuit.switched.call.control.bcsm.ConnectToResourceArg;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.NoServiceParameterAvailableException;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDialogueStateException;
import dev.ocean.sigtran.common.exceptions.UnidentifableServiceException;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.messages.MessageHandling;
import dev.ocean.sigtran.tcap.ResourceLimitationException;
import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.parameters.ParameterImpl;
import dev.ocean.sigtran.tcap.primitives.tc.TCInvoke;
import dev.ocean.sigtran.tcap.primitives.tc.TCResult;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class CAPDialogueV2 extends CAPDialogue {

    public CAPDialogueV2(CAPStackImpl stack, CAPApplicationContexts capApplicationContext,
            SCCPAddress destinationAddress, SCCPAddress originationAddress, MessageHandling messageHandling,
            boolean sequenceControl) throws ResourceLimitationException {
        super(stack, capApplicationContext, destinationAddress, originationAddress,
                null, messageHandling, sequenceControl);
    }

    public CAPDialogueV2(TCAPDialogue tcapDialogue, CAPStackImpl stack) {
        super(tcapDialogue, stack);
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke, OperationCodes opCode) throws IncorrectSyntaxException, IncorrectSyntaxException, UnidentifableServiceException, NoServiceParameterAvailableException, UnexpectedDataException, ParameterOutOfRangeException, IllegalNumberFormatException {
        switch (opCode) {
            case APPLY_CHARGING:
                this.processApplyCharging(invoke);
                break;
            case APPLY_CHARGING_REPORT:
                this.processApplyChargingReport(invoke);
                break;
            case ASSIST_REQUEST_INSTRUCTIONS:
                this.processAssitRequestInstructions(invoke);
                break;
            case CONNECT:
                this.processConnect(invoke);
                break;
            case DISCONNECT_FORWARD_CONNECTION:
                this.processDisconnectForwardConnection(invoke);
                break;
            case ESTABLISH_TEMPORARY_CONNECTION:
                this.processEstablishTemporaryConnection(invoke);
                break;
            case EVENT_REPORT_BCSM:
                this.processEventReportBCSM(invoke);
                break;
            case INITIAL_DP:
                this.processInitialDp(invoke);
                break;
            case PLAY_ANNOUNCEMENT:
                this.processPlayAnnouncement(invoke);
                break;
            case RELEASE_CALL:
                this.processReleaseCall(invoke);
                break;
            case REQUEST_REPORT_BCSM_EVENT:
                this.processRequestReportBCSMEvent(invoke);
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
        }
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

    public Short sendCAPEventReportBCSM(EventReportBCSMArgImpl eventReportBCSMArg) throws UnexpectedDialogueStateException, IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDataException {
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

    public Short sendCAPRequestReportBCSMEvent(RequestReportBCSMEventArgImpl requestReportBCSMEventArg) throws IncorrectSyntaxException, ParameterOutOfRangeException, UnexpectedDialogueStateException {
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

    public Short sendCAPConnect(ConnectArgImpl connectArg) throws ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDataException, UnexpectedDialogueStateException {
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

    public Short sendCAPPlayAnnouncement(PlayAnnouncementArg playAnnouncementArg) throws AsnException, ParameterOutOfRangeException, IllegalNumberFormatException, UnexpectedDialogueStateException, IOException {
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

    public Short sendCAPCOnnectToResource(ConnectToResourceArg connectToResourceArg) throws IncorrectSyntaxException, IllegalNumberFormatException, UnexpectedDialogueStateException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.CONNECT_TO_RESOURCE;

                AsnOutputStream aos = new AsnOutputStream();
                connectToResourceArg.encode(aos);

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

    public Short sendCAPEstablishTemporaryConnection(EstablishTemporaryConnectionArg establishTemporaryConnectionArg) throws IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDialogueStateException {
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

        ApplyChargingReportArg applyChargingReportArg = new ApplyChargingReportArg(new CAMELCallResult());
        applyChargingReportArg.decode(new AsnInputStream(parameter.getData()));

        int timeOut = 0;
        stack.onCAPApplyChargingReport(invoke.getInvokeID(), applyChargingReportArg, this);
    }

    private void processAssitRequestInstructions(TCInvoke invoke) throws NoServiceParameterAvailableException, IllegalNumberFormatException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        AssistRequestInstructionArg assistRequestInstruction
                = new AssistRequestInstructionArg();
        assistRequestInstruction.decode(new AsnInputStream(parameter.getData()));

        int timeOut = 0;
        stack.onCAPAssistRequestInstructions(invoke.getInvokeID(), assistRequestInstruction, this);
    }

    private void processConnect(TCInvoke invoke) throws NoServiceParameterAvailableException, ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ConnectArgImpl connect = new ConnectArgImpl();
        connect.decode(new AsnInputStream(parameter.getData()));

        int timeOut = 0;
        stack.onCAPConnect(invoke.getInvokeID(), connect, this);
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

    private void processEventReportBCSM(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException, UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        EventReportBCSMArgImpl eventReportBCSM = new EventReportBCSMArgImpl();
        eventReportBCSM.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;

        stack.onCAPEventReportBCSM(invoke.getInvokeID(), eventReportBCSM, this);
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

    private void processPlayAnnouncement(TCInvoke invoke) throws NoServiceParameterAvailableException, ParameterOutOfRangeException, IllegalNumberFormatException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        PlayAnnouncementArg playAnnouncement = new PlayAnnouncementArg();
        playAnnouncement.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;

        stack.onCAPPlayAnnouncement(invoke.getInvokeID(), playAnnouncement, this);
    }

    private void processReleaseCall(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ReleaseCallArg releaseCall = new ReleaseCallArg();
        releaseCall.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;

        stack.onCAPReleaseCall(invoke.getInvokeID(), releaseCall, this);
    }

    private void processDisconnectForwardConnection(TCInvoke invoke) {
        stack.onCAPDisconnectForwardConnection(invoke.getInvokeID(), this);
    }

    private void processRequestReportBCSMEvent(TCInvoke invoke) throws NoServiceParameterAvailableException, ParameterOutOfRangeException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        RequestReportBCSMEventArgImpl requestReportBCSMEvent = new RequestReportBCSMEventArgImpl();
        requestReportBCSMEvent.decode(new AsnInputStream(parameter.getData()));

        int timeOut = 0;

        stack.onCAPRequestReportBCSM(invoke.getInvokeID(), requestReportBCSMEvent, this);
    }

}
