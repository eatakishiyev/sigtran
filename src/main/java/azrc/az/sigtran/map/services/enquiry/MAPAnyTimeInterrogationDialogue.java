/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.enquiry;

import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
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
public class MAPAnyTimeInterrogationDialogue extends MAPDialogue {

    private static final Logger logger = LoggerFactory.getLogger(MAPAnyTimeInterrogationDialogue.class);

    public MAPAnyTimeInterrogationDialogue(MAPStackImpl stack,
                                           SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                           ExtensionContainer specificInformation, MessageHandling messageHandling,
                                           boolean sequenceControl) throws ResourceLimitationException {
//        This package is v3 only.
        super(stack, new MAPApplicationContextImpl(MAPApplicationContextName.ANY_TIME_ENQUIRY_CONTEXT, MAPApplicationContextVersion.VERSION_3),
                destinationAddress, null, originatingAddress, null, specificInformation, messageHandling, sequenceControl);
    }

    public MAPAnyTimeInterrogationDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        super(stack, tcapDialogue, mapApplicationContext);
    }

    public Short sendMAPAnyTimeInterrogationRequest(AnyTimeInterrogationArg anyTimeInterrogationArg, long timeOut) throws
            IncorrectSyntaxException, UnexpectedDataException {

        if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
            OperationCodes opCode = OperationCodes.ANY_TIME_INTERROGATION;

            AsnOutputStream aos = new AsnOutputStream();
            anyTimeInterrogationArg.encode(aos);

            ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

            Short invokeId = this.getTcapDialogue().sendInvoke(opCode.value(),
                    opCode.operationClass(), parameter, timeOut);
            return invokeId;
        }
        return null;
    }

    public void sendMAPAnyTimeInterrogationResponse(Short invokeId,
                                                    AnyTimeInterrogationRes anyTimeInterrogationRes) throws IOException,
            IncorrectSyntaxException, UnexpectedDataException, IllegalNumberFormatException {
        if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {

            if (!this.isPerformingSSMExists(invokeId)) {
                throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
            }

            AsnOutputStream aos = new AsnOutputStream();
            anyTimeInterrogationRes.encode(aos);

            ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

            this.terminatePerformingSSM(invokeId);
            getTcapDialogue().sendResult(invokeId, OperationCodes.ANY_TIME_INTERROGATION.value(), parameter);
        }
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke) throws
            IncorrectSyntaxException, IncorrectSyntaxException,
            NoServiceParameterAvailableException, UnexpectedDataException {
        OperationCodes operationCode = OperationCodes.getInstance(invoke.getOperationCode());
        switch (operationCode) {
            case ANY_TIME_INTERROGATION:
                this.processAnyTimeInterrogationIndication(invoke);
                break;
        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int opCode) {
        OperationCodes operationCode = OperationCodes.getInstance(opCode);
        switch (operationCode) {
            case ANY_TIME_INTERROGATION:
                this.processAnyTimeInterrogationConfirmation(indication);
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, MAPUserError mapUserError,
                                            Short invokeId) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());
        OperationCodes operationCode = OperationCodes.getInstance(operation);
        switch (operationCode) {
            case ANY_TIME_INTERROGATION:
                worker.submit(() -> {
                    ((MAPAnyTimeEnquiryContextListener) getMAPUser()).onMAPAnyTimeInterrogationConfirmation(invokeId,
                            null, this, mapUserError, null);
                });
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, ProviderError providerError,
                                            Short invokeId) {
        OperationCodes operationCode = OperationCodes.getInstance(operation);
        switch (operationCode) {
            case ANY_TIME_INTERROGATION:
                this.onMAPAnyTimeInterrogationConfirmation(invokeId, providerError);
                break;
        }
    }

    private void processAnyTimeInterrogationIndication(TCInvoke invoke) throws
            NoServiceParameterAvailableException, IncorrectSyntaxException,
            UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();
        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        AnyTimeInterrogationArg anyTimeInterrogationArg = new AnyTimeInterrogationArg();

        anyTimeInterrogationArg.decode(parameter.getData());

        ExecutorService worker = stack.findWorker(this.getDialogueId());
        worker.submit(() -> {
            ((MAPAnyTimeEnquiryContextListener) getMAPUser()).onMAPAnyTimeInterrogationIndication(invoke.getInvokeID(),
                    anyTimeInterrogationArg, this);
        });
    }

    private void processAnyTimeInterrogationConfirmation(TCResult indication) {
        try {
            if (indication.getParameter() == null) {
                throw new UnexpectedDataException();
            }

            AnyTimeInterrogationRes anyTimeInterrogationRes = new AnyTimeInterrogationRes();
            anyTimeInterrogationRes.decode(indication.getParameter().getData());

            ExecutorService worker = stack.findWorker(this.getDialogueId());
            worker.submit(() -> {
                ((MAPAnyTimeEnquiryContextListener) getMAPUser()).onMAPAnyTimeInterrogationConfirmation(indication.getInvokeId(),
                        anyTimeInterrogationRes, this, null, null);
            });

        } catch (IncorrectSyntaxException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPAnyTimeInterrogationConfirmation(indication.getInvokeId(),
                    ProviderError.INVALID_RESPONSE_RECEIVED);
            getTcapDialogue().tcURejectRequest(indication.getInvokeId(),
                    ReturnResultProblem.MISTYPED_PARAMETER);
        } catch (UnexpectedDataException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPAnyTimeInterrogationConfirmation(indication.getInvokeId(),
                    ProviderError.INVALID_RESPONSE_RECEIVED);
        }
    }

    private void onMAPAnyTimeInterrogationConfirmation(Short invokeId, ProviderError providerError) {
        {
            ExecutorService worker = stack.findWorker(this.getDialogueId());
            worker.submit(() -> {
                ((MAPAnyTimeEnquiryContextListener) getMAPUser())
                        .onMAPAnyTimeInterrogationConfirmation(invokeId, null, this, null, providerError);
            });
        }
    }

}
