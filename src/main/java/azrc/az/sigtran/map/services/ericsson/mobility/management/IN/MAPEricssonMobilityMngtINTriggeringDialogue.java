/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.ericsson.mobility.management.IN;

import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.NoServiceParameterAvailableException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.*;
import azrc.az.sigtran.map.parameters.MAPApplicationContextImpl;
import azrc.az.sigtran.map.parameters.MAPApplicationContextName;
import azrc.az.sigtran.map.parameters.MAPApplicationContextVersion;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.messages.MessageHandling;
import azrc.az.sigtran.tcap.ResourceLimitationException;
import azrc.az.sigtran.tcap.TCAPDialogue;
import azrc.az.sigtran.tcap.parameters.ParameterImpl;
import azrc.az.sigtran.tcap.primitives.tc.TCInvoke;
import azrc.az.sigtran.tcap.primitives.tc.TCResult;
import org.mobicents.protocols.asn.AsnOutputStream;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author eatakishiyev
 */
public class MAPEricssonMobilityMngtINTriggeringDialogue extends MAPDialogue {


    public MAPEricssonMobilityMngtINTriggeringDialogue(MAPStackImpl stack,
                                                       SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                                       MessageHandling messageHandling, boolean sequenceControl) throws ResourceLimitationException {
        super(stack, new MAPApplicationContextImpl(MAPApplicationContextName.ERI_MOBILITY_MNGT_IN,
                        MAPApplicationContextVersion.VERSION_2), destinationAddress, null,
                originatingAddress, null, null, messageHandling, sequenceControl);
    }

    public MAPEricssonMobilityMngtINTriggeringDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue) {
        super(stack, tcapDialogue, new MAPApplicationContextImpl(MAPApplicationContextName.ERI_MOBILITY_MNGT_IN,
                MAPApplicationContextVersion.VERSION_2));
    }

    public Short sendMAPMobilityMngtINTriggeringRequest(MobilityMngtINTriggeringArg arg,
                                                        long timeOut) throws IncorrectSyntaxException, UnexpectedDataException,
            IllegalNumberFormatException {
        ReentrantLock mutex = getTcapDialogue().getLock();
        mutex.lock();
        try {
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes operationCodes = OperationCodes.MOBILITY_MNGT_IN_TRIGGERING;

                AsnOutputStream aos = new AsnOutputStream();
                arg.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());
                return getTcapDialogue().sendInvoke(operationCodes.value(), operationCodes.operationClass(), parameter, timeOut);
            }
            return null;
        } finally {
            mutex.unlock();
        }
    }

    public void sendMAPMobilityMngtINTriggeringResponse(Short invokeId) throws IOException {
        ReentrantLock mutex = getTcapDialogue().getLock();
        mutex.lock();
        try {
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                    || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {
                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = InvokeId = %d", getDialogueId(), invokeId));
                }

                this.terminatePerformingSSM(invokeId);
                getTcapDialogue().sendResult(invokeId);
            }
        } finally {
            mutex.unlock();
        }
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke) throws IncorrectSyntaxException, NoServiceParameterAvailableException, UnexpectedDataException {
        if (invoke.getOperationCode() == OperationCodes.MOBILITY_MNGT_IN_TRIGGERING.value()) {
            ParameterImpl parameter = invoke.getParameter();
            if (parameter == null) {
                throw new NoServiceParameterAvailableException();
            }

            MobilityMngtINTriggeringArg mobilityMngtINTriggeringArg
                    = new MobilityMngtINTriggeringArg();
            mobilityMngtINTriggeringArg.decode(parameter.getData());

            ExecutorService worker = stack.findWorker(this.getDialogueId());
            worker.submit(() -> {
                ((MAPEricssonMobilityMngtINTriggeringContextListener) getMAPUser())
                        .onMAPEricssonMobilityManagementINTriggeringIndication(invoke.getInvokeID(),
                                mobilityMngtINTriggeringArg, this);
            });
        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int operationCode) {
        if (operationCode == OperationCodes.MOBILITY_MNGT_IN_TRIGGERING.value()) {
            ExecutorService worker = stack.findWorker(this.getDialogueId());
            worker.submit(() -> {
                ((MAPEricssonMobilityMngtINTriggeringContextListener) getMAPUser())
                        .onMAPEricssonMobilityManagementINTriggeringConfirmation(indication.getInvokeId(),
                                this, null, null);
            });
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, MAPUserError mapUserError, Short invokeId) {
        if (operation == OperationCodes.MOBILITY_MNGT_IN_TRIGGERING.value()) {
            ExecutorService worker = stack.findWorker(this.getDialogueId());
            worker.submit(() -> {
                ((MAPEricssonMobilityMngtINTriggeringContextListener) getMAPUser())
                        .onMAPEricssonMobilityManagementINTriggeringConfirmation(invokeId, this,
                                mapUserError, null);
            });
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, ProviderError providerError, Short invokeId) {
        if (operation == OperationCodes.MOBILITY_MNGT_IN_TRIGGERING.value()) {
            ExecutorService worker = stack.findWorker(this.getDialogueId());
            worker.submit(() -> {
                ((MAPEricssonMobilityMngtINTriggeringContextListener) getMAPUser())
                        .onMAPEricssonMobilityManagementINTriggeringConfirmation(invokeId, this,
                                null, providerError);
            });
        }
    }

}
