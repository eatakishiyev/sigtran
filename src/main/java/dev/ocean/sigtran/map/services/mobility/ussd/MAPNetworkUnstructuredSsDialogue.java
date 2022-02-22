/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.mobility.ussd;

import java.io.IOException;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.NoServiceParameterAvailableException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.*;
import dev.ocean.sigtran.map.parameters.AlertingPattern;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.ISDNAddressString;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextImpl;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextName;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextVersion;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.messages.MessageHandling;
import dev.ocean.sigtran.tcap.ResourceLimitationException;
import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.parameters.ParameterImpl;
import dev.ocean.sigtran.tcap.parameters.ReturnResultProblem;
import dev.ocean.sigtran.tcap.primitives.tc.TCInvoke;
import dev.ocean.sigtran.tcap.primitives.tc.TCResult;

import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.*;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * @author eatakishiyev
 */
public class MAPNetworkUnstructuredSsDialogue extends MAPDialogue {

    private final static Logger logger = LogManager.getLogger(MAPNetworkUnstructuredSsDialogue.class);

    public MAPNetworkUnstructuredSsDialogue(MAPStackImpl stack, MAPApplicationContextVersion mapApplicationContextVersion,
                                            SCCPAddress destinationAddress, ISDNAddressString destinationReference,
                                            SCCPAddress originatingAddress, ISDNAddressString originatingReference,
                                            ExtensionContainer specificInformation, MessageHandling messageHandling, boolean sequenceControl) throws ResourceLimitationException {

        super(stack, new MAPApplicationContextImpl(MAPApplicationContextName.NETWORK_UNSTRUCTURED_SS_CONTEXT, mapApplicationContextVersion),
                destinationAddress, destinationReference,
                originatingAddress, originatingReference, specificInformation, messageHandling, sequenceControl);
    }

    public MAPNetworkUnstructuredSsDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        super(stack, tcapDialogue, mapApplicationContext);
    }

    /**
     * @param ussdDataCodingScheme
     * @param ussdString
     * @param alertingPatter
     * @param msisdn
     * @param timeOut
     * @return
     * @throws Exception
     */
    public Short sendMAPProcessUnstructuredSsRequest(Integer ussdDataCodingScheme, byte[] ussdString, AlertingPattern alertingPatter, ISDNAddressString msisdn,
                                                     long timeOut) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            Short invokeId = null;
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.PROCESS_UNSTRUCTURED_SS_REQUEST;

                AsnOutputStream aos = new AsnOutputStream();

                UssdArg ussd = new UssdArg(ussdDataCodingScheme, ussdString);
                ussd.setAlertingPatter(alertingPatter);
                ussd.setMsisdn(msisdn);
                ussd.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);
            }
            return invokeId;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    /**
     * @param ussdDataCodingScheme
     * @param ussdString
     * @param invokeId
     * @throws Exception
     */
    public void sendMAPProcessUnstructuredSsRequestResponse(int ussdDataCodingScheme, byte[] ussdString, Short invokeId) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }

                OperationCodes opCode = OperationCodes.PROCESS_UNSTRUCTURED_SS_REQUEST;
                AsnOutputStream aos = new AsnOutputStream();

                UssdRes ussdResp = new UssdRes(ussdDataCodingScheme, ussdString);
                ussdResp.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                this.terminatePerformingSSM(invokeId);
                getTcapDialogue().sendResult(invokeId, opCode.value(), parameter);
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    /**
     * @param ussdDataCodingScheme
     * @param ussdString
     * @param alertingPatter
     * @param msisdn
     * @param timeOut
     * @return
     * @throws Exception
     */
    public Short sendMAPUnstructuredSsRequest(Integer ussdDataCodingScheme, byte[] ussdString, AlertingPattern alertingPatter, ISDNAddressString msisdn,
                                              long timeOut) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            Short invokeId = null;

            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.UNSTRUCTURED_SS_REQUEST;

                AsnOutputStream aos = new AsnOutputStream();

                UssdArg ussd = new UssdArg(ussdDataCodingScheme, ussdString);
                ussd.setAlertingPatter(alertingPatter);
                ussd.setMsisdn(msisdn);

                ussd.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                TCInvoke invoke = new TCInvoke();
                invoke.setDialogue(getTcapDialogue());
                invoke.setInvokeID(invokeId);
                invoke.setOperation(opCode.value());
                invoke.setOperationClass(opCode.operationClass());
                invoke.setParameter(parameter);
                invoke.setTimeout(timeOut);

                invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);
            }

            return invokeId;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    /**
     * @param ussdDataCodingScheme
     * @param ussdString
     * @param invokeId
     * @throws Exception
     */
    public void sendMAPUnstructuredSsRequestResponse(int ussdDataCodingScheme, byte[] ussdString, Short invokeId) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }

                OperationCodes opCode = OperationCodes.UNSTRUCTURED_SS_REQUEST;

                UssdRes ussdRsp = new UssdRes(ussdDataCodingScheme, ussdString);
                AsnOutputStream aos = new AsnOutputStream();
                ussdRsp.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                this.terminatePerformingSSM(invokeId);
                getTcapDialogue().sendResult(invokeId, opCode.value(), parameter);
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendMAPUnstructuredSSNotifyRequest(Integer ussdDataCodingScheme, byte[] ussdString, AlertingPattern alertingPatter, ISDNAddressString msisdn,
                                                    long timeOut) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            Short invokeId = null;

            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.UNSTRUCTURED_SS_NOTIFY;

                AsnOutputStream aos = new AsnOutputStream();

                UssdArg ussd = new UssdArg(ussdDataCodingScheme, ussdString);
                ussd.setAlertingPatter(alertingPatter);
                ussd.setMsisdn(msisdn);

                ussd.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());
                invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);
            }

            return invokeId;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    /**
     * @param invokeId
     * @throws Exception
     */
    public void sendMAPUnstructuredSsNotifyResponse(Short invokeId) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }
                this.terminatePerformingSSM(invokeId);
                getTcapDialogue().sendResult(invokeId);
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke) throws IncorrectSyntaxException, IncorrectSyntaxException, NoServiceParameterAvailableException, UnexpectedDataException {

        switch (invoke.getOperationCode()) {
            case 59://PROCESS_UNSTRUCTURED_SS_REQUEST
                this.handleProcessUnstructuredSsRequest(invoke);
                break;
            case 60://UNSTRUCTURED_SS_REQUEST
                this.handleUnstructuredSsRequest(invoke);
                break;
            case 61:
                this.handleUnstructuredSsNotify(invoke);

        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int operationCode) {

        switch (operationCode) {
            case 59://PROCESS_UNSTRUCTURED_SS_REQUEST
                this.handleProcessUnstructuredSsRequestConfirmation(indication);
                break;
            case 60://UNSTRUCTURED_SS_REQUEST
                this.handleUnstructuredSsRequestConfirmation(indication);
                break;
            case 61:
                this.handleUnstructuredSsNotifyConfirmation(indication);
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, MAPUserError mapUserError, Short invokeId) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());
        switch (operation) {
            case 59://PROCESS_UNSTRUCTURED_SS_REQUEST

                worker.submit(() -> {
                    ((MAPNetworkUnstructuredSsContextListener) getMAPUser()).
                            onMAPProcessUnstructuredSsRequestConfirmation(invokeId, null, this, mapUserError, null);
                });

                break;
            case 60://UNSTRUCTURED_SS_REQUEST

                worker.submit(() -> {
                    ((MAPNetworkUnstructuredSsContextListener) getMAPUser()).
                            onMAPUnstructuredSsRequestConfirmation(invokeId, null, this, null, mapUserError);
                });

                break;

            case 61:

                worker.submit(() -> {
                    ((MAPNetworkUnstructuredSsContextListener) getMAPUser()).
                            onMAPUnstructuredSsNotifyConfirmation(invokeId, this, null, mapUserError);
                });

                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, ProviderError providerError, Short invokeId) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());
        switch (operation) {
            case 59://PROCESS_UNSTRUCTURED_SS_REQUEST

                worker.submit(() -> {
                    ((MAPNetworkUnstructuredSsContextListener) getMAPUser()).
                            onMAPProcessUnstructuredSsRequestConfirmation(invokeId, null, this, null, providerError);
                });

                break;
            case 60://UNSTRUCTURED_SS_REQUEST

                worker.submit(() -> {
                    ((MAPNetworkUnstructuredSsContextListener) getMAPUser()).
                            onMAPUnstructuredSsRequestConfirmation(invokeId, null, this, providerError, null);
                });

                break;
            case 61:

                worker.submit(() -> {
                    ((MAPNetworkUnstructuredSsContextListener) getMAPUser()).
                            onMAPUnstructuredSsNotifyConfirmation(invokeId, this, providerError, null);
                });

                break;
        }
    }

    private void handleProcessUnstructuredSsRequest(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        UssdArg ussdArg = new UssdArg();
        ussdArg.decode((parameter.getData()));

        ExecutorService worker = stack.findWorker(this.getDialogueId());

        worker.submit(() -> {
            ((MAPNetworkUnstructuredSsContextListener) getMAPUser()).
                    onMAPProcessUnstructuredSsRequestIndication(invoke.getInvokeID(), ussdArg, this);
        });

    }

    private void handleUnstructuredSsNotify(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        UssdArg ussdArg = new UssdArg();
        ussdArg.decode((parameter.getData()));

        ExecutorService worker = stack.findWorker(this.getDialogueId());

        worker.submit(() -> {
            ((MAPNetworkUnstructuredSsContextListener) getMAPUser()).
                    onMAPUnstructuredSsNotifyIndication(invoke.getInvokeID(), ussdArg, this);
        });

    }

    private void handleUnstructuredSsRequest(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException {
        ParameterImpl parameter = invoke.getParameter();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        UssdArg ussdArg = new UssdArg();
        ussdArg.decode((parameter.getData()));

        ExecutorService worker = stack.findWorker(this.getDialogueId());

        worker.submit(() -> {
            ((MAPNetworkUnstructuredSsContextListener) getMAPUser()).
                    onMAPUnstructuredSsRequestIndication(invoke.getInvokeID(), ussdArg, this);
        });

    }

    private void handleProcessUnstructuredSsRequestConfirmation(TCResult indication) {
        try {
            if (indication.getParameter() == null) {
                throw new UnexpectedDataException();
            }

            UssdRes ussdRes = new UssdRes();
            ussdRes.decode((indication.getParameter().getData()));

            ExecutorService worker = stack.findWorker(this.getDialogueId());

            worker.submit(() -> {
                ((MAPNetworkUnstructuredSsContextListener) getMAPUser()).
                        onMAPProcessUnstructuredSsRequestConfirmation(indication.getInvokeId(), ussdRes, this, null, null);
            });

        } catch (IncorrectSyntaxException ex) {
            this.onMAPProcessUnstructuredSsRequestConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);
            getTcapDialogue().tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);
            logger.info("Error Occured: ", ex);

        } catch (UnexpectedDataException ex) {
            this.onMAPProcessUnstructuredSsRequestConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);
            logger.info("Error Occured: ", ex);
        }
    }

    private void handleUnstructuredSsRequestConfirmation(TCResult indication) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());
        UssdRes ussdRes = new UssdRes();
        try {
            if (indication.getParameter() != null) {
                ussdRes.decode((indication.getParameter().getData()));
            }

            worker.submit(() -> {
                ((MAPNetworkUnstructuredSsContextListener) getMAPUser()).
                        onMAPUnstructuredSsRequestConfirmation(indication.getInvokeId(),
                                ussdRes, this, null, null);
            });

        } catch (IncorrectSyntaxException ex) {

            worker.submit(() -> {
                ((MAPNetworkUnstructuredSsContextListener) getMAPUser()).
                        onMAPUnstructuredSsRequestConfirmation(indication.getInvokeId(),
                                null, this, ProviderError.INVALID_RESPONSE_RECEIVED, null);
            });

            getTcapDialogue().tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);
            logger.error("Error Occured: ", ex);
        }
    }

    private void handleUnstructuredSsNotifyConfirmation(TCResult indication) {
        UssdRes ussdRes = new UssdRes();

        ExecutorService worker = stack.findWorker(this.getDialogueId());

        worker.submit(() -> {
            ((MAPNetworkUnstructuredSsContextListener) getMAPUser()).onMAPUnstructuredSsNotifyConfirmation(indication.getInvokeId(), this, null, null);
        });

    }

    private void onMAPProcessUnstructuredSsRequestConfirmation(Short invokeId, ProviderError providerError) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());

        worker.submit(() -> {
            ((MAPNetworkUnstructuredSsContextListener) getMAPUser()).
                    onMAPProcessUnstructuredSsRequestConfirmation(invokeId, null, this, null, providerError);
        });

    }

}
