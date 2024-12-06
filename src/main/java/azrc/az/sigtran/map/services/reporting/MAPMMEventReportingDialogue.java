/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.reporting;

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
import java.util.concurrent.ExecutorService;

/**
 * @author eatakishiyev
 */
public class MAPMMEventReportingDialogue extends MAPDialogue {

    private final Logger logger = LoggerFactory.getLogger(MAPMMEventReportingDialogue.class);

    public MAPMMEventReportingDialogue(MAPStackImpl stack,
                                       SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                       ExtensionContainer specificInformation, MessageHandling messageHandling,
                                       boolean sequenceControl) throws ResourceLimitationException {
        super(stack,
                new MAPApplicationContextImpl(MAPApplicationContextName.MM_EVENT_REPORTING_CONTEXT, MAPApplicationContextVersion.VERSION_3),
                destinationAddress, null, originatingAddress, null, specificInformation,
                messageHandling, sequenceControl);
    }

    public MAPMMEventReportingDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue) {
        super(stack, tcapDialogue, new MAPApplicationContextImpl(MAPApplicationContextName.MM_EVENT_REPORTING_CONTEXT, MAPApplicationContextVersion.VERSION_3));
    }

    public Short sendMAPNoteMMEventRequest(NoteMMEventArg arg, long timeOut) throws IncorrectSyntaxException, UnexpectedDataException {
        if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
            OperationCodes opCode = OperationCodes.NOTE_MM_EVENT;

            AsnOutputStream aos = new AsnOutputStream();
            arg.encode(aos);

            ParameterImpl parameter = new ParameterImpl(aos.toByteArray());
            Short invokeId = this.getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);
            return invokeId;
        }
        return null;
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke) throws IncorrectSyntaxException, NoServiceParameterAvailableException, UnexpectedDataException {
        OperationCodes operationCode = OperationCodes.getInstance(invoke.getOperationCode());
        switch (operationCode) {
            case NOTE_MM_EVENT:
                ParameterImpl parameter = invoke.getParameter();
                if (parameter == null) {
                    throw new NoServiceParameterAvailableException();
                }

                NoteMMEventArg noteMmEventArg = new NoteMMEventArg();
                noteMmEventArg.decode(parameter.getData());

                ExecutorService worker = stack.findWorker(this.getDialogueId());
                worker.submit(() -> {
                    ((MAPMMEventReportingContextListener) getMAPUser()).onMAPNoteMMEventIndication(invoke.getInvokeID(),
                            noteMmEventArg, this);
                });

                break;
        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int opCode) {
        try {
            OperationCodes operationCode = OperationCodes.getInstance(opCode);
            switch (operationCode) {
                case NOTE_MM_EVENT:
                    NoteMMEventRes response = null;
                    if (indication.getParameter() != null) {
                        response = new NoteMMEventRes();
                        response.decode(indication.getParameter().getData());
                    }
                    this.onMAPNoteMMEventConfirmation(indication.getInvokeId(),
                            response, null, null);
                    break;
            }
        } catch (IncorrectSyntaxException ex) {
            logger.error("Error Occured: ", ex);
            this.onMAPNoteMMEventConfirmation(indication.getInvokeId(), null,
                    ProviderError.INVALID_RESPONSE_RECEIVED, null);

            getTcapDialogue().tcURejectRequest(indication.getInvokeId(),
                    ReturnResultProblem.MISTYPED_PARAMETER);
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, MAPUserError mapUserError, Short invokeId) {
        OperationCodes operationCode = OperationCodes.getInstance(operation);
        switch (operationCode) {
            case NOTE_MM_EVENT:
                this.onMAPNoteMMEventConfirmation(invokeId, null, null, mapUserError);
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, ProviderError providerError, Short invokeId) {
        OperationCodes operationCode = OperationCodes.getInstance(operation);
        switch (operationCode) {
            case NOTE_MM_EVENT:
                this.onMAPNoteMMEventConfirmation(invokeId, null, providerError, null);
                break;
        }
    }

    private void onMAPNoteMMEventConfirmation(Short invokeId, NoteMMEventRes response,
                                              ProviderError providerError, MAPUserError userError) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());
        worker.submit(() -> {
            ((MAPMMEventReportingContextListener) getMAPUser()).onMAPNoteMMEventConfirmation(invokeId, response, this,
                    userError, providerError);
        });
    }
}
