/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.purging;

import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.NoServiceParameterAvailableException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.*;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.MAPApplicationContextImpl;
import azrc.az.sigtran.map.parameters.MAPApplicationContextName;
import azrc.az.sigtran.map.parameters.MAPApplicationContextVersion;
import azrc.az.sigtran.map.services.errors.IncorrectAcVersion;
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
public class MAPPurgeMSDialogue extends MAPDialogue {

    private final static Logger logger = LoggerFactory.getLogger(MAPPurgeMSDialogue.class);

    public MAPPurgeMSDialogue(MAPStackImpl stack, MAPApplicationContextVersion acVersion,
                              SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                              ExtensionContainer specificInformation, MessageHandling messageHandling,
                              boolean sequenceControl) throws ResourceLimitationException, IncorrectAcVersion {
        super(stack, new MAPApplicationContextImpl(MAPApplicationContextName.MS_PURGING_CONTEXT,
                        acVersion), destinationAddress, null, originatingAddress, null,
                specificInformation, messageHandling, sequenceControl);
        if (acVersion != MAPApplicationContextVersion.VERSION_2
                && acVersion != MAPApplicationContextVersion.VERSION_3) {
            throw new IncorrectAcVersion("MsPurgingContext supporting in V2 and V3");
        }

    }

    public MAPPurgeMSDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue,
                              MAPApplicationContextImpl mapApplicationContext) {
        super(stack, tcapDialogue, mapApplicationContext);
    }

    public Short sendMAPPurgeMSRequest(PurgeMSArg request, long timeOut) throws IncorrectSyntaxException,
            UnexpectedDataException,
            IllegalNumberFormatException {
        if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
            OperationCodes opCode = OperationCodes.PURGE_MS;

            AsnOutputStream aos = new AsnOutputStream();
            request.encode(aos);

            ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

            Short invokeId = this.getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);

            return invokeId;
        }
        return null;
    }

    public void sendMAPPurgeMSResponse(Short invokeId, PurgeMSRes response) throws IOException,
            Exception {
        if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {

            if (!this.isPerformingSSMExists(invokeId)) {
                throw new IOException(String.format("No performing SSM found. "
                        + "DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
            }

            OperationCodes opCode = OperationCodes.PURGE_MS;
            AsnOutputStream aos = new AsnOutputStream();
            response.encode(aos);

            ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

            this.terminatePerformingSSM(invokeId);
            getTcapDialogue().sendResult(invokeId, opCode.value(), parameter);
        }
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke) throws IncorrectSyntaxException,
            NoServiceParameterAvailableException, UnexpectedDataException {
        OperationCodes operatinCode = OperationCodes.getInstance(invoke.getOperationCode());
        switch (operatinCode) {
            case PURGE_MS:
                ParameterImpl parameter = invoke.getParameter();
                if (parameter == null) {
                    throw new NoServiceParameterAvailableException();
                }

                PurgeMSArg purgeMSArg = new PurgeMSArg();
                purgeMSArg.decode(parameter.getData());

                ExecutorService worker = stack.findWorker(this.getDialogueId());
                worker.submit(() -> {
                    ((MAPPurgeMSContextListener) getMAPUser()).onMAPPurgeMSIndication(invoke.getInvokeID(), purgeMSArg, this);
                });

                break;
        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int operationCode) {
        try {
            if (indication.getParameter() == null) {
                throw new UnexpectedDataException();
            }

            PurgeMSRes response = new PurgeMSRes();
            response.decode(indication.getParameter().getData());

            this.onMAPPurgeMSConfirmation(indication.getInvokeId(), null, null,
                    response);
        } catch (IncorrectSyntaxException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPPurgeMSConfirmation(indication.getInvokeId(),
                    ProviderError.INVALID_RESPONSE_RECEIVED, null, null);

            getTcapDialogue().tcURejectRequest(indication.getInvokeId(),
                    ReturnResultProblem.MISTYPED_PARAMETER);
        } catch (UnexpectedDataException ex) {
            logger.error("ErrorOccured: ", ex);
            this.onMAPPurgeMSConfirmation(indication.getInvokeId(),
                    ProviderError.INVALID_RESPONSE_RECEIVED, null, null);
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, ProviderError providerError, Short invokeId) {
        OperationCodes operationCode = OperationCodes.getInstance(operation);
        switch (operationCode) {
            case PURGE_MS:
                this.onMAPPurgeMSConfirmation(invokeId, providerError, null, null);
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, MAPUserError mapUserError, Short invokeId) {
        OperationCodes operationCode = OperationCodes.getInstance(operation);
        switch (operationCode) {
            case PURGE_MS:
                this.onMAPPurgeMSConfirmation(invokeId, null, mapUserError, null);
                break;
        }
    }

    private void onMAPPurgeMSConfirmation(Short invokeId, ProviderError providerError,
                                          MAPUserError userError, PurgeMSRes response) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());
        worker.submit(() -> {
            ((MAPPurgeMSContextListener) getMAPUser()).onMAPPurgeMSConfirmation(invokeId, response, this, userError, providerError);
        });

    }
}
