/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.location.info.retrieval;

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
 * @author eatakishiyev
 */
public class MAPLocationInfoRetrievalDialogue extends MAPDialogue {

    private static final Logger logger = LoggerFactory.getLogger(MAPLocationInfoRetrievalDialogue.class);

    public MAPLocationInfoRetrievalDialogue(MAPStackImpl stack, MAPApplicationContextVersion mapApplicationContextVersion,
                                            SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                            ExtensionContainer specificInformation, MessageHandling messageHandling,
                                            boolean sequenceControl) throws ResourceLimitationException {

        super(stack, new MAPApplicationContextImpl(MAPApplicationContextName.LOCATION_INFO_RETRIEVAL_CONTEXT, mapApplicationContextVersion),
                destinationAddress, null,
                originatingAddress, null,
                specificInformation, messageHandling, sequenceControl);
    }

    public MAPLocationInfoRetrievalDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        super(stack, tcapDialogue, mapApplicationContext);
    }

    public Short sendMAPSendRoutingInfoRequest(SendRoutingInfoArg sendRoutingInfoArg, long timeOut) throws IncorrectSyntaxException {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.SEND_ROUTING_INFO;

                AsnOutputStream aos = new AsnOutputStream();
                sendRoutingInfoArg.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());
                Short invokeId = this.getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);
                return invokeId;
            }
            return null;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void sendMAPSendRoutingInfoResponse(Short invokeId, SendRoutingInfoRes sendRoutingInfoRes) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                    || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {

                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }

                AsnOutputStream aos = new AsnOutputStream();
                sendRoutingInfoRes.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                this.terminatePerformingSSM(invokeId);
                getTcapDialogue().sendResult(invokeId, OperationCodes.SEND_ROUTING_INFO.value(), parameter);
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke) throws IncorrectSyntaxException, IncorrectSyntaxException, NoServiceParameterAvailableException, UnexpectedDataException {
        OperationCodes operationCode = OperationCodes.getInstance(invoke.getOperationCode());
        switch (operationCode) {
            case SEND_ROUTING_INFO:
                this.processSendRoutingInfoIndication(invoke);
                break;
        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int opCode) {
        OperationCodes operationCode = OperationCodes.getInstance(opCode);
        switch (operationCode) {
            case SEND_ROUTING_INFO:
                this.processSendRoutingInfoConfirmation(indication);
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, MAPUserError mapUserError, Short invokeId) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());

        OperationCodes operationCode = OperationCodes.getInstance(operation);
        switch (operationCode) {
            case SEND_ROUTING_INFO:
                worker.submit(() -> {
                    ((MAPLocationInfoRetrievalContextListener) getMAPUser()).
                            onMAPSendRoutingInfoConfirmation(invokeId, null, this, mapUserError, null);
                });

                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, ProviderError providerError, Short invokeId) {
        OperationCodes operationCode = OperationCodes.getInstance(operation);
        switch (operationCode) {
            case SEND_ROUTING_INFO:
                this.onMAPSendRoutingInfoConfirmation(invokeId, providerError);
                break;
        }
    }

    private void processSendRoutingInfoIndication(TCInvoke invoke) throws NoServiceParameterAvailableException,
            UnexpectedDataException, IncorrectSyntaxException {

        if (invoke.getParameter() == null) {
            throw new NoServiceParameterAvailableException();
        }

        SendRoutingInfoArg sendRoutingInfoArg = new SendRoutingInfoArg();
        sendRoutingInfoArg.decode((invoke.getParameter().getData()));

        ExecutorService worker = stack.findWorker(this.getDialogueId());
        worker.submit(() -> {
            ((MAPLocationInfoRetrievalContextListener) getMAPUser())
                    .onMAPSendRoutingInfoIndication(invoke.getInvokeID(), sendRoutingInfoArg, this);
        });
    }

    private void processSendRoutingInfoConfirmation(TCResult indication) {
        try {
            SendRoutingInfoRes sendRoutingInfoRes = new SendRoutingInfoRes();

            if (indication.getParameter() != null) {
                sendRoutingInfoRes.decode((indication.getParameter().getData()));
            }

            ExecutorService worker = stack.findWorker(this.getDialogueId());

            worker.submit(() -> {
                ((MAPLocationInfoRetrievalContextListener) getMAPUser()).onMAPSendRoutingInfoConfirmation(indication.getInvokeId(),
                        sendRoutingInfoRes, this, null, null);
            });

        } catch (IncorrectSyntaxException ex) {
            logger.error("ErrorOccured:", ex);
            this.onMAPSendRoutingInfoConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);
            getTcapDialogue().tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);
        } catch (UnexpectedDataException ex) {
            logger.error("ErrorOccured:", ex);
            this.onMAPSendRoutingInfoConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);
        }
    }

    private void onMAPSendRoutingInfoConfirmation(Short invokeId, ProviderError providerError) {
        {
            ExecutorService worker = stack.findWorker(this.getDialogueId());
            worker.submit(() -> {
                ((MAPLocationInfoRetrievalContextListener) getMAPUser())
                        .onMAPSendRoutingInfoConfirmation(invokeId, null, this, null, providerError);
            });

        }
    }

}
