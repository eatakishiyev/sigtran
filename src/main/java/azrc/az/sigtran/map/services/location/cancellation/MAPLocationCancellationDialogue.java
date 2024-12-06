/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.location.cancellation;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.NoServiceParameterAvailableException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.*;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.MAPApplicationContextImpl;
import azrc.az.sigtran.map.parameters.MAPApplicationContextName;
import azrc.az.sigtran.map.parameters.MAPApplicationContextVersion;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.messages.MessageHandling;
import azrc.az.sigtran.tcap.ResourceLimitationException;
import azrc.az.sigtran.tcap.TCAPDialogue;
import azrc.az.sigtran.tcap.parameters.Parameter;
import azrc.az.sigtran.tcap.parameters.ParameterImpl;
import azrc.az.sigtran.tcap.parameters.ReturnResultProblem;
import azrc.az.sigtran.tcap.primitives.tc.TCInvoke;
import azrc.az.sigtran.tcap.primitives.tc.TCResult;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * locationCancellationContext-v3 APPLICATION-CONTEXT ::= { -- Responder is VLR
 * or SGSN if Initiator is HLR INITIATOR CONSUMER OF {
 * locationCancellationPackage-v3} ID {map-ac locationCancel(2) version3(3)} }
 *
 * @author eatakishiyev
 */
public class MAPLocationCancellationDialogue extends MAPDialogue {

    private static final Logger logger = LoggerFactory.getLogger(MAPLocationCancellationDialogue.class);

    public MAPLocationCancellationDialogue(MAPStackImpl stack, MAPApplicationContextVersion mapApplicationContextVersion,
                                           SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                           ExtensionContainer specificInformation, MessageHandling messageHandling,
                                           boolean sequenceControl) throws ResourceLimitationException {

        super(stack, new MAPApplicationContextImpl(MAPApplicationContextName.LOCATION_CANCELLATION_CONTEXT, mapApplicationContextVersion),
                destinationAddress, null,
                originatingAddress, null,
                specificInformation, messageHandling, sequenceControl);
    }

    public MAPLocationCancellationDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue, MAPApplicationContextVersion version) {
        super(stack, tcapDialogue, new MAPApplicationContextImpl(MAPApplicationContextName.LOCATION_CANCELLATION_CONTEXT, version));
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke) throws IncorrectSyntaxException, NoServiceParameterAvailableException, UnexpectedDataException {
        OperationCodes operationCode = OperationCodes.getInstance(invoke.getOperationCode());
        switch (operationCode) {
            case CANCEL_LOCATION:
                this.processCancelLocation(invoke);
                break;
        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int opCode) {
        OperationCodes operationCode = OperationCodes.getInstance(opCode);
        switch (operationCode) {
            case CANCEL_LOCATION:
                this.processCancelLocationConfirmation(indication);
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, MAPUserError mapUserError, Short invokeId) {
        OperationCodes operationCode = OperationCodes.getInstance(operation);
        switch (operationCode) {
            case CANCEL_LOCATION:
                ExecutorService worker = stack.findWorker(this.getDialogueId());
                worker.submit(() -> {
                    ((MAPLocationCancellationContextListener) getMAPUser())
                            .onMAPCancelLocationConfirmation(invokeId, null, this, mapUserError, null);
                });
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, ProviderError providerError, Short invokeId) {
        OperationCodes operationCode = OperationCodes.getInstance(operation);
        switch (operationCode) {
            case CANCEL_LOCATION:
                this.onMAPCancelLocationConfirmation(invokeId, providerError);
                break;
        }
    }

    private void processCancelLocation(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException, UnexpectedDataException {
        Parameter parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        CancelLocationArg cancelLocationArg = new CancelLocationArg();
        cancelLocationArg.decode((parameter.getData()));

        ExecutorService worker = stack.findWorker(this.getDialogueId());

        worker.submit(() -> {
            ((MAPLocationCancellationContextListener) getMAPUser())
                    .onMAPCancelLocationIndication(invoke.getInvokeID(), cancelLocationArg, this);
        });

    }

    private void processCancelLocationConfirmation(TCResult indication) {
        try {
            CancelLocationRes cancelLocationRes = new CancelLocationRes();
            if (indication.getParameter() != null) {
                cancelLocationRes.decode((indication.getParameter().getData()));
            }

            ExecutorService worker = stack.findWorker(this.getDialogueId());

            worker.submit(() -> {
                ((MAPLocationCancellationContextListener) getMAPUser())
                        .onMAPCancelLocationConfirmation(indication.getInvokeId(), cancelLocationRes, this, null, null);
            });

        } catch (IncorrectSyntaxException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPCancelLocationConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);

            getTcapDialogue().tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);
        } catch (UnexpectedDataException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPCancelLocationConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);
        }
    }

    public Short sendMAPCancelLocationRequest(CancelLocationArg cancelLocationArg, long timeOut) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.CANCEL_LOCATION;

                AsnOutputStream aos = new AsnOutputStream();
                cancelLocationArg.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = this.getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);
                return invokeId;
            }
            return null;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void sendMAPCancelLocationResponse(Short invokeId, CancelLocationRes cancelLocationRes) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                    || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {

                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }

                OperationCodes opCode = OperationCodes.CANCEL_LOCATION;
                AsnOutputStream aos = new AsnOutputStream();
                cancelLocationRes.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                this.terminatePerformingSSM(invokeId);
                getTcapDialogue().sendResult(invokeId, opCode.value(), parameter);
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    private void onMAPCancelLocationConfirmation(Short invokeId, ProviderError providerError) {
        {
            ExecutorService worker = stack.findWorker(this.getDialogueId());

            worker.submit(() -> {
                ((MAPLocationCancellationContextListener) getMAPUser())
                        .onMAPCancelLocationConfirmation(invokeId, null, this, null, providerError);
            });

        }
    }

}
