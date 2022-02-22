/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.service.subscriber.information.enquiry;

import java.io.IOException;

import dev.ocean.sigtran.map.*;
import org.apache.logging.log4j.*;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.NoServiceParameterAvailableException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextImpl;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextName;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextVersion;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.messages.MessageHandling;
import dev.ocean.sigtran.tcap.ResourceLimitationException;
import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.parameters.Parameter;
import dev.ocean.sigtran.tcap.parameters.ParameterImpl;
import dev.ocean.sigtran.tcap.parameters.ReturnResultProblem;
import dev.ocean.sigtran.tcap.primitives.tc.TCInvoke;
import dev.ocean.sigtran.tcap.primitives.tc.TCResult;

import java.util.concurrent.ExecutorService;

import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * subscriberInformationEnquiryPackage-v3 OPERATION-PACKAGE ::= { -- Supplier is
 * VLR or SGSN if Consumer is HLR CONSUMER INVOKES { provideSubscriberInfo} }
 * This package is v3 only.
 *
 * @author eatakishiyev
 */
public class MAPSubscriberInformationEnquiryDialogue extends MAPDialogue {

    private static final Logger logger = LogManager.getLogger(MAPSubscriberInformationEnquiryDialogue.class);

    public MAPSubscriberInformationEnquiryDialogue(MAPStackImpl stack,
                                                   SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                                   ExtensionContainer specificInformation, MessageHandling messageHandling,
                                                   boolean sequenceControl) throws ResourceLimitationException {

        super(stack, new MAPApplicationContextImpl(MAPApplicationContextName.SUBSCRIBER_INFO_ENQUIRY_CONTEXT, MAPApplicationContextVersion.VERSION_3),
                destinationAddress, null,
                originatingAddress, null,
                specificInformation, messageHandling, sequenceControl);
    }

    public MAPSubscriberInformationEnquiryDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue) {
        super(stack, tcapDialogue, new MAPApplicationContextImpl(MAPApplicationContextName.LOCATION_CANCELLATION_CONTEXT, MAPApplicationContextVersion.VERSION_3));
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke) throws IncorrectSyntaxException, NoServiceParameterAvailableException, UnexpectedDataException {
        OperationCodes opCode = OperationCodes.getInstance(invoke.getOperationCode());
        switch (opCode) {
            case PROVIDE_SUBSCRIBER_INFO:
                this.processProvideSubscriberInfoIndication(invoke);
                break;
        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int operationCode) {
        OperationCodes opCode = OperationCodes.getInstance(operationCode);
        switch (opCode) {
            case PROVIDE_SUBSCRIBER_INFO:
                this.processProvideSubscriberInfoConfirmation(indication);
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, MAPUserError mapUserError, Short invokeId) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());
        OperationCodes opCode = OperationCodes.getInstance(operation);
        switch (opCode) {
            case PROVIDE_SUBSCRIBER_INFO:
                worker.submit(() -> {
                    ((MAPSubscriberInfoEnquiryContextListener) getMAPUser())
                            .onMAPProvideSubscriberInfoConfirmation(invokeId, null, this, mapUserError, null);
                });
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, ProviderError providerError, Short invokeId) {
        OperationCodes opCode = OperationCodes.getInstance(operation);
        switch (opCode) {
            case PROVIDE_SUBSCRIBER_INFO:
                this.onMAPProvideSubscriberInfoConfirmation(invokeId, providerError);
                break;
        }

    }

    private void processProvideSubscriberInfoIndication(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException, UnexpectedDataException {
        Parameter parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ProvideSubscriberInfoArg provideSubscriberInfoArg = new ProvideSubscriberInfoArg();

        provideSubscriberInfoArg.decode(parameter.getData());

        ExecutorService worker = stack.findWorker(this.getDialogueId());
        worker.submit(() -> {
            ((MAPSubscriberInfoEnquiryContextListener) getMAPUser()).onMAPProvideSubscriberInfoIndication(invoke.getInvokeID(),
                    provideSubscriberInfoArg, this);
        });

    }

    private void processProvideSubscriberInfoConfirmation(TCResult indication) {
        try {
            ProvideSubscriberInfoRes provideSubscriberInfoRes = new ProvideSubscriberInfoRes();
            if (indication.getParameter() != null) {
                provideSubscriberInfoRes.decode(indication.getParameter().getData());
            }

            ExecutorService worker = stack.findWorker(this.getDialogueId());
            worker.submit(() -> {
                ((MAPSubscriberInfoEnquiryContextListener) getMAPUser()).onMAPProvideSubscriberInfoConfirmation(indication.getInvokeId(),
                        provideSubscriberInfoRes, this, null, null);
            });

        } catch (IncorrectSyntaxException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPProvideSubscriberInfoConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);

            getTcapDialogue().tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);
        } catch (UnexpectedDataException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPProvideSubscriberInfoConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);
        }
    }

    public Short sendMAPProvideSubscriberInfoRequest(ProvideSubscriberInfoArg provideSubscriberInfoArg, long timeOut) throws IncorrectSyntaxException, UnexpectedDataException {
        if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
            OperationCodes opCode = OperationCodes.PROVIDE_SUBSCRIBER_INFO;

            AsnOutputStream aos = new AsnOutputStream();
            provideSubscriberInfoArg.encode(aos);

            ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

            Short invokeId = this.getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);

            return invokeId;
        }
        return null;
    }

    public void sendMAPProvideSubscriberInfoResponse(Short invokeId, ProvideSubscriberInfoRes provideSubscriberInfoRes) throws Exception {
        if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {

            if (!this.isPerformingSSMExists(invokeId)) {

                throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
            }

            OperationCodes opCode = OperationCodes.PROVIDE_SUBSCRIBER_INFO;
            AsnOutputStream aos = new AsnOutputStream();
            provideSubscriberInfoRes.encode(aos);

            ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

            this.terminatePerformingSSM(invokeId);
            getTcapDialogue().sendResult(invokeId, opCode.value(), parameter);
        }

    }

    private void onMAPProvideSubscriberInfoConfirmation(Short invokeId, ProviderError providerError) {
        {
            ExecutorService worker = stack.findWorker(this.getDialogueId());
            worker.submit(() -> {
                ((MAPSubscriberInfoEnquiryContextListener) getMAPUser())
                        .onMAPProvideSubscriberInfoConfirmation(invokeId, null, this, null, providerError);
            });
        }
    }

}
