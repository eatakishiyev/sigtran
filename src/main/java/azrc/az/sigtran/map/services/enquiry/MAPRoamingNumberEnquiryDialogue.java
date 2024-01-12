/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.enquiry;

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
public class MAPRoamingNumberEnquiryDialogue extends MAPDialogue {

    private static final Logger logger = LoggerFactory.getLogger(MAPRoamingNumberEnquiryDialogue.class);

    public MAPRoamingNumberEnquiryDialogue(MAPStackImpl stack, MAPApplicationContextVersion acVersion,
                                           SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                           ExtensionContainer specificInformation, MessageHandling messageHandling, boolean sequenceControl) throws ResourceLimitationException {
        super(stack, new MAPApplicationContextImpl(MAPApplicationContextName.ROAMING_NUMBER_ENQUIRY_CONTEXT, acVersion),
                destinationAddress, null, originatingAddress,
                null, specificInformation, messageHandling, sequenceControl);
    }

    public MAPRoamingNumberEnquiryDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        super(stack, tcapDialogue, mapApplicationContext);
    }

    public Short sendMAPProvideRoamingNumberRequest(ProvideRoamingNumberArg provideRoamingNumber, long timeOut) throws IncorrectSyntaxException, UnexpectedDataException {
        if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
            OperationCodes opCode = OperationCodes.PROVIDE_ROAMING_NUMBER;

            AsnOutputStream aos = new AsnOutputStream();
            provideRoamingNumber.encode(aos);

            ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

            Short invokeId = this.getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);

            return invokeId;
        }
        return null;
    }

    public void sendMAPProvideRoamingNumberResponse(Short invokeId, ProvideRoamingNumberRes provideRoamingNumberRes) throws Exception {
        if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {

            if (!this.isPerformingSSMExists(invokeId)) {
                throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
            }

            OperationCodes opCode = OperationCodes.PROVIDE_ROAMING_NUMBER;
            AsnOutputStream aos = new AsnOutputStream();
            provideRoamingNumberRes.encode(aos);

            ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

            this.terminatePerformingSSM(invokeId);
            getTcapDialogue().sendResult(invokeId, opCode.value(), parameter);
        }
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke) throws IncorrectSyntaxException, IncorrectSyntaxException, NoServiceParameterAvailableException, UnexpectedDataException {
        OperationCodes operationCode = OperationCodes.getInstance(invoke.getOperationCode());
        switch (operationCode) {
            case PROVIDE_ROAMING_NUMBER:
                this.processProvideRoamingNumberIndication(invoke);
                break;
        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int opCode) {
        OperationCodes operationCode = OperationCodes.getInstance(opCode);
        switch (operationCode) {
            case PROVIDE_ROAMING_NUMBER:
                this.processProvideRoamingNumberConfirmation(indication);
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, MAPUserError mapUserError, Short invokeId) {
        OperationCodes operationCode = OperationCodes.getInstance(operation);
        switch (operationCode) {
            case PROVIDE_ROAMING_NUMBER:
                this.onMAPProvideRoamingNumberConfirmation(invokeId, null, mapUserError,
                        null);
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, ProviderError providerError, Short invokeId) {
        OperationCodes operationCode = OperationCodes.getInstance(operation);
        switch (operationCode) {
            case PROVIDE_ROAMING_NUMBER:
                this.onMAPProvideRoamingNumberConfirmation(invokeId, providerError,
                        null, null);
                break;
        }
    }

    private void processProvideRoamingNumberIndication(TCInvoke invoke) throws NoServiceParameterAvailableException,
            IncorrectSyntaxException, UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ProvideRoamingNumberArg provideRoamingNumberArg = new ProvideRoamingNumberArg();
        provideRoamingNumberArg.decode(parameter.getData());

        ExecutorService worker = stack.findWorker(this.getDialogueId());
        worker.submit(() -> {
            ((MAPRoamingNumberEnquiryContextListener) getMAPUser()).onMAPProvideRoamingNumberIndication(invoke.getInvokeID(),
                    provideRoamingNumberArg, this);
        });
    }

    private void processProvideRoamingNumberConfirmation(TCResult indication) {
        try {
            if (indication.getParameter() == null) {
                throw new UnexpectedDataException();
            }

            ProvideRoamingNumberRes provideRoamingNumberRes = new ProvideRoamingNumberRes();
            provideRoamingNumberRes.decode(indication.getParameter().getData());

            this.onMAPProvideRoamingNumberConfirmation(indication.getInvokeId(),
                    null, null, provideRoamingNumberRes);

        } catch (IncorrectSyntaxException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPProvideRoamingNumberConfirmation(indication.getInvokeId(),
                    ProviderError.INVALID_RESPONSE_RECEIVED, null, null);

            getTcapDialogue().tcURejectRequest(indication.getInvokeId(),
                    ReturnResultProblem.MISTYPED_PARAMETER);
        } catch (UnexpectedDataException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPProvideRoamingNumberConfirmation(indication.getInvokeId(),
                    ProviderError.INVALID_RESPONSE_RECEIVED, null, null);
        }
    }

    private void onMAPProvideRoamingNumberConfirmation(Short invokeId,
                                                       ProviderError providerError, MAPUserError userError, ProvideRoamingNumberRes response) {
        {
            ExecutorService worker = stack.findWorker(this.getDialogueId());
            worker.submit(() -> {
                ((MAPRoamingNumberEnquiryContextListener) getMAPUser()).onMAPProvideRoamingNumberConfirmation(invokeId, response,
                        this, userError, providerError);
            });
        }
    }

}
