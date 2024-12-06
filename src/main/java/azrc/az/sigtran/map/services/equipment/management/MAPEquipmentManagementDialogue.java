/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.equipment.management;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.NoServiceParameterAvailableException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.*;
import azrc.az.sigtran.map.parameters.*;
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
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author eatakishiyev
 */
public class MAPEquipmentManagementDialogue extends MAPDialogue {

    private static final Logger logger = LoggerFactory.getLogger(MAPEquipmentManagementDialogue.class);

    public MAPEquipmentManagementDialogue(MAPStackImpl stack, MAPApplicationContextVersion mapApplicationContextVersion,
                                          SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                          ExtensionContainer specificInformation, MessageHandling messageHandling, boolean sequenceControl) throws ResourceLimitationException {

        super(stack, new MAPApplicationContextImpl(MAPApplicationContextName.EQUIPMENT_MGNT_CONTEXT, mapApplicationContextVersion),
                destinationAddress, null,
                originatingAddress, null,
                specificInformation, messageHandling, sequenceControl);
    }

    public MAPEquipmentManagementDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue, MAPApplicationContextVersion version) {
        super(stack, tcapDialogue, new MAPApplicationContextImpl(MAPApplicationContextName.EQUIPMENT_MGNT_CONTEXT, version));
    }

    public Short sendMAPCheckIMEIRequest(IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
                                         ExtensionContainer extensionContainer, long timeOut) throws IncorrectSyntaxException, UnexpectedDataException {
        ReentrantLock mutex = getTcapDialogue().getLock();
        mutex.lock();
        try {
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes operationCodes = OperationCodes.CHECK_IMEI;

                AsnOutputStream aos = new AsnOutputStream();
                CheckIMEIArg arg = new CheckIMEIArg(imei, requestedEquipmentInfo);
                arg.setExtensionContainer(extensionContainer);
                arg.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());
                return getTcapDialogue().sendInvoke(operationCodes.value(), operationCodes.operationClass(), parameter, timeOut);
            }
            return null;
        } finally {
            mutex.unlock();
        }
    }

    public void sendMAPCheckIMEIResponse(Short invokeId, CheckIMEIResponse response) throws IOException, IncorrectSyntaxException, UnexpectedDataException {
        ReentrantLock mutex = getTcapDialogue().getLock();
        mutex.lock();
        try {
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                    || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {

                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }

                OperationCodes opCode = OperationCodes.CHECK_IMEI;
                AsnOutputStream aos = new AsnOutputStream();
                response.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                this.terminatePerformingSSM(invokeId);
                getTcapDialogue().sendResult(invokeId, opCode.value(), parameter);
            }
        } finally {
            mutex.unlock();
        }
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke) throws IncorrectSyntaxException, NoServiceParameterAvailableException, UnexpectedDataException {
        if (invoke.getOperationCode() == OperationCodes.CHECK_IMEI.value()) {
            this.processCheckImeiIndication(invoke);
        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int operationCode) {
        if (operationCode == OperationCodes.CHECK_IMEI.value()) {
            this.processCheckImeiConfirmation(indication);
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, MAPUserError mapUserError, Short invokeId) {
        if (operation == OperationCodes.CHECK_IMEI.value()) {
            ExecutorService worker = stack.findWorker(this.getDialogueId());
            worker.submit(() -> {
                ((MAPEquipmentManagementContextListener) getMAPUser())
                        .onMAPCheckIMEIConfirmation(invokeId, null, this, mapUserError, null);
            });
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, ProviderError providerError, Short invokeId) {
        if (operation == OperationCodes.CHECK_IMEI.value()) {
            this.onMAPCheckIMEIConfirmation(invokeId, providerError);
        }
    }

    private void processCheckImeiIndication(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException, UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        CheckIMEIArg arg = new CheckIMEIArg();
        arg.decode(parameter.getData());

        ExecutorService worker = stack.findWorker(this.getDialogueId());
        worker.submit(() -> {
            ((MAPEquipmentManagementContextListener) getMAPUser()).onMAPCheckIMEIIndication(invoke.getInvokeID(), arg, this);
        });
    }

    private void processCheckImeiConfirmation(TCResult indication) {
        try {
            CheckIMEIResponse response = new CheckIMEIResponse();
            if (indication.getParameter() != null) {
                response.decode(indication.getParameter().getData());
            }

            ExecutorService worker = stack.findWorker(this.getDialogueId());
            worker.submit(() -> {
                ((MAPEquipmentManagementContextListener) getMAPUser()).onMAPCheckIMEIConfirmation(indication.getInvokeId(),
                        response, this, null, null);
            });
        } catch (IncorrectSyntaxException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPCheckIMEIConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);
            getTcapDialogue().tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);

        } catch (UnexpectedDataException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPCheckIMEIConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);
        }
    }

    private void onMAPCheckIMEIConfirmation(Short invokeId, ProviderError providerError) {
        {
            ExecutorService worker = stack.findWorker(this.getDialogueId());
            worker.submit(() -> {
                ((MAPEquipmentManagementContextListener) getMAPUser())
                        .onMAPCheckIMEIConfirmation(invokeId, null, this, null, providerError);
            });

        }
    }

}
