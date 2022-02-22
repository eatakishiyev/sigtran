/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.network.location.update;

import java.io.IOException;

import dev.ocean.sigtran.map.*;
import org.apache.logging.log4j.*;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.NoServiceParameterAvailableException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextImpl;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.messages.MessageHandling;
import dev.ocean.sigtran.tcap.ResourceLimitationException;
import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.parameters.ParameterImpl;
import dev.ocean.sigtran.tcap.parameters.ReturnResultProblem;
import dev.ocean.sigtran.tcap.primitives.tc.TCInvoke;
import dev.ocean.sigtran.tcap.primitives.tc.TCResult;
import dev.ocean.sigtran.utils.ByteUtils;

import java.util.concurrent.ExecutorService;

import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * networkLocUpContext-v3 APPLICATION-CONTEXT ::= { -- Responder is HLR if
 * Initiator is VLR INITIATOR CONSUMER OF { locationUpdatingPackage-v3 | *
 * dataRestorationPackage-v3} * RESPONDER CONSUMER OF {
 * subscriberDataMngtPackage-v3 | tracingPackage-v3} ID {map-ac networkLocUp(1)
 * version3(3)} }
 *
 * @author eatakishiyev
 */
public class MAPNetworkLocationUpdateDialogue extends MAPDialogue {

    private static final Logger logger = LogManager.getLogger(MAPNetworkLocationUpdateDialogue.class);

    public MAPNetworkLocationUpdateDialogue(MAPStackImpl stack, MAPApplicationContextImpl mapApplicationContext,
                                            SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                            ExtensionContainer specificInformation, MessageHandling messageHandling, boolean sequenceControl) throws ResourceLimitationException {
        super(stack, mapApplicationContext,
                destinationAddress,
                null,
                originatingAddress,
                null,
                specificInformation,
                messageHandling, sequenceControl);
    }

    public MAPNetworkLocationUpdateDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue, MAPApplicationContextImpl mAPApplicationContext) {
        super(stack, tcapDialogue, mAPApplicationContext);
    }

    public Short sendMAPUpdateLocationRequest(UpdateLocationArg updateLocationArg, long timeOut) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.UPDATE_LOCATION;

                AsnOutputStream aos = new AsnOutputStream();
                updateLocationArg.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);

                return invokeId;
            }
            return null;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void sendMAPUpdateLocationResponse(Short invokeId, UpdateLocationRes updateLocationRes) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                    || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {

                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }

                OperationCodes opCode = OperationCodes.UPDATE_LOCATION;
                AsnOutputStream aos = new AsnOutputStream();
                updateLocationRes.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                this.terminatePerformingSSM(invokeId);
                getTcapDialogue().sendResult(invokeId, opCode.value(), parameter);
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendMAPInsertSubscriberDataRequest(InsertSubscriberDataArg insertSubscriberDataArg, long timeOut) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.INSERT_SUBSCRIBER_DATA;

                AsnOutputStream aos = new AsnOutputStream();
                insertSubscriberDataArg.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);
                return invokeId;
            }
            return null;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void sendMAPInsertSubscriberDataResponse(Short invokeId, InsertSubscriberDataRes insertSubscriberDataRes) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                    || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {

                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }

                OperationCodes opCode = OperationCodes.INSERT_SUBSCRIBER_DATA;
                AsnOutputStream aos = new AsnOutputStream();
                insertSubscriberDataRes.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());
                logger.info("InsertSubscriberDataResponse: " + ByteUtils.bytes2Hex(aos.toByteArray()));
                this.terminatePerformingSSM(invokeId);
                getTcapDialogue().sendResult(invokeId, opCode.value(), parameter);
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke) throws IncorrectSyntaxException, IncorrectSyntaxException, NoServiceParameterAvailableException, UnexpectedDataException {
        switch (invoke.getOperationCode()) {
            case 2://OperationCodes.UPDATE_LOCATION:
                this.processUpdateLocationInd(invoke);
                break;
            case 7://OperationCodes.INSERT_SUBSCRIBER_DATA
                this.processInsertSubscriberDataInd(invoke);
                break;
        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int operationCode) {
        OperationCodes opCode = OperationCodes.getInstance(operationCode);

        switch (opCode) {
            case UPDATE_LOCATION://OperationCodes.UPDATE_LOCATION:
                this.processUpdateLocationConf(indication);
                break;
            case INSERT_SUBSCRIBER_DATA:
                this.processInsertSubscriberDataConf(indication);
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, MAPUserError mapUserError, Short invokeId) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());
        switch (operation) {
            case 2://OperationCodes.UPDATE_LOCATION:

                worker.submit(() -> {
                    ((MAPNetworkLocationUpdateContextListener) getMAPUser())
                            .onMAPUpdateLocationConfirmation(invokeId, null, this, mapUserError, null);
                });

                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, ProviderError providerError, Short invokeId) {
        switch (operation) {
            case 2://OperationCodes.UPDATE_LOCATION:
                this.onMAPUpdateLocationConfirmation(invokeId, providerError);
                break;
        }
    }

    private void processUpdateLocationInd(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException, UnexpectedDataException {

        ParameterImpl parameter = invoke.getParameter();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        UpdateLocationArg updateLocationArg = new UpdateLocationArg();
        updateLocationArg.decode(parameter.getData());

        ExecutorService worker = stack.findWorker(this.getDialogueId());

        worker.submit(() -> {
            ((MAPNetworkLocationUpdateContextListener) getMAPUser())
                    .onMAPUpdateLocationIndication(invoke.getInvokeID(), updateLocationArg, this);
        });

    }

    private void processInsertSubscriberDataInd(TCInvoke invoke) throws IncorrectSyntaxException, UnexpectedDataException {

        ParameterImpl parameter = invoke.getParameter();
        InsertSubscriberDataArg insertSubscriberDataArg = new InsertSubscriberDataArg();

        if (parameter != null) {
            insertSubscriberDataArg.decode((parameter.getData()));
        }

        ExecutorService worker = stack.findWorker(this.getDialogueId());

        worker.submit(() -> {
            ((MAPNetworkLocationUpdateContextListener) getMAPUser())
                    .onMAPInsertSubscriberDataIndication(invoke.getInvokeID(), insertSubscriberDataArg, this);
        });

    }

    private void processUpdateLocationConf(TCResult indication) {
        try {
            if (indication.getParameter() == null) {
                throw new UnexpectedDataException();
            }

            UpdateLocationRes updateLocationRes = new UpdateLocationRes();
            updateLocationRes.decode(indication.getParameter().getData());

            ExecutorService worker = stack.findWorker(this.getDialogueId());

            worker.submit(() -> {
                ((MAPNetworkLocationUpdateContextListener) getMAPUser())
                        .onMAPUpdateLocationConfirmation(indication.getInvokeId(), updateLocationRes, this, null, null);
            });

        } catch (IncorrectSyntaxException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPUpdateLocationConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);
            getTcapDialogue().tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);
        } catch (UnexpectedDataException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPUpdateLocationConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);
        }
    }

    private void processInsertSubscriberDataConf(TCResult indication) {
        InsertSubscriberDataRes insertSubscriberDataRes = new InsertSubscriberDataRes();
        try {
            if (indication.getParameter() != null) {
                insertSubscriberDataRes.decode((indication.getParameter().getData()));
            }

            ExecutorService worker = stack.findWorker(this.getDialogueId());

            worker.submit(() -> {
                ((MAPNetworkLocationUpdateContextListener) getMAPUser()).
                        onMAPInsertSubscriberDataConfirmation(indication.getInvokeId(), insertSubscriberDataRes, this, null, null);
            });

        } catch (IncorrectSyntaxException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPInsertSubscriberDataConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);
            getTcapDialogue().tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);

        } catch (UnexpectedDataException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPInsertSubscriberDataConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);

        }
    }

    private void onMAPUpdateLocationConfirmation(Short invokeId, ProviderError providerError) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());
        worker.submit(() -> {
            ((MAPNetworkLocationUpdateContextListener) getMAPUser()).
                    onMAPUpdateLocationConfirmation(invokeId, null, this, null, providerError);
        });

    }

    private void onMAPInsertSubscriberDataConfirmation(Short invokeId, ProviderError providerError) {
        {
            ExecutorService worker = stack.findWorker(this.getDialogueId());
            worker.submit(() -> {
                ((MAPNetworkLocationUpdateContextListener) getMAPUser()).
                        onMAPInsertSubscriberDataConfirmation(invokeId, null, this, providerError, null);
            });
        }
    }
}
