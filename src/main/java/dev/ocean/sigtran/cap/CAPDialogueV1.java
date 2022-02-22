/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap;

import dev.ocean.sigtran.cap.callcontrol.v1.ConnectArgImpl;
import dev.ocean.sigtran.cap.callcontrol.v1.InitialDpArgImpl;
import dev.ocean.sigtran.cap.callcontrol.general.ReleaseCallArg;
import dev.ocean.sigtran.cap.callcontrol.v1.RequestReportBCSMEventArgImpl;
import dev.ocean.sigtran.cap.errors.CAPUserError;
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
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class CAPDialogueV1 extends CAPDialogue {

    public CAPDialogueV1(CAPStackImpl stack, SCCPAddress destinationAddress,
            SCCPAddress originationAddress, MessageHandling messageHandling, boolean sequenceControl) throws ResourceLimitationException {
        super(stack, CAPApplicationContexts.CAP_V1_GSMSSF_TO_GSMSCF_AC, destinationAddress,
                originationAddress, null, messageHandling, sequenceControl);
    }

    public CAPDialogueV1(TCAPDialogue tcapDialogue, CAPStackImpl stack) {
        super(tcapDialogue, stack);
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke, OperationCodes opCode) throws IncorrectSyntaxException, IncorrectSyntaxException, UnidentifableServiceException, NoServiceParameterAvailableException, UnexpectedDataException, ParameterOutOfRangeException, IllegalNumberFormatException {
        switch (opCode) {
            case INITIAL_DP:
                this.processInitialDp(invoke);
                break;
            case CONNECT:
                this.processConnect(invoke);
                break;
            case RELEASE_CALL:
                this.processReleaseCall(invoke);
                break;
            case REQUEST_REPORT_BCSM_EVENT:
                this.processRequestReportBCSMEvent(invoke);
                break;
            case CONTINUE:
                this.processContinue(invoke);
                break;
            case ACTIVITY_TEST:
                this.processActivityTest(invoke.getInvokeID());
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
            case CONNECT:
                stack.onCAPConnectError(invokeId, this, userError);
                break;
            case REQUEST_REPORT_BCSM_EVENT:
                stack.onCAPRequestReportBCSMEventError(invokeId, this, userError);
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(OperationCodes operation, ProviderError providerError, Short invokeId) {
        switch (operation) {
            case INITIAL_DP:
                stack.onCAPInitialDpError(invokeId, this, providerError);
                break;
            case CONNECT:
                stack.onCAPConnectError(invokeId, this, providerError);
                break;
            case REQUEST_REPORT_BCSM_EVENT:
                stack.onCAPRequestReportBCSMEventError(invokeId, this, providerError);
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

    public Short sendCAPRequestReportBCSMEvent(RequestReportBCSMEventArgImpl requestReportBCSMEventArg) throws IncorrectSyntaxException, UnexpectedDialogueStateException {
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

    public Short sendCAPActivityTest() throws UnexpectedDialogueStateException {
        try {
            getTcapDialogue().getLock().lock();
            if (this.getState() == DialogueState.WAIT_FOR_USER_REQUESTS
                    || this.getState() == DialogueState.DIALOGUE_ACCEPTED
                    || this.getState() == DialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.ACTIVITY_TEST;

                Short invokeId = tcapDialogue.sendInvoke(opCode.value(), opCode.getOperationClass(), 0);
                return invokeId;
            }
            throw new UnexpectedDialogueStateException(String.format("Dialogue is in unexpected state [%s]. Must be in WAIT_FOR_USER_REQUESTS | DIALOGUE_ACCEPTED | DIALOGUE_ESTABLISHED",
                    this.getState()));
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    private void processInitialDp(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException, ParameterOutOfRangeException, UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        InitialDpArgImpl initialDpArg = new InitialDpArgImpl();
        initialDpArg.decode(new AsnInputStream(parameter.getData()));

        long timeOut = 0;
        stack.onCAPInitialDpIndication(invoke.getInvokeID(), initialDpArg, this);
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

    private void processContinue(TCInvoke invoke) {
        stack.onCAPContinue(invoke.getInvokeID(), this);
    }
}
