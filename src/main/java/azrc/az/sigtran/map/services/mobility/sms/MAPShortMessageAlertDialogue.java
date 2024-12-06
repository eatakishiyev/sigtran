/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.mobility.sms;

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
public class MAPShortMessageAlertDialogue extends MAPDialogue {

    private final static Logger logger = LoggerFactory.getLogger(MAPShortMessageAlertDialogue.class);

    public MAPShortMessageAlertDialogue(MAPStackImpl stack, MAPApplicationContextVersion version,
                                        SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                        ExtensionContainer specificInformation, MessageHandling messageHandling,
                                        boolean sequenceControl) throws ResourceLimitationException {

        super(stack, new MAPApplicationContextImpl(MAPApplicationContextName.SHORT_MSG_ALERT_CONTEXT, version),
                destinationAddress, null,
                originatingAddress, null,
                specificInformation, messageHandling, sequenceControl);
    }

    public MAPShortMessageAlertDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        super(stack, tcapDialogue, mapApplicationContext);
    }

    public Short sendMAPAlertServiceCentreReq(AlertServiceCentreArg alertServiceCentreArg, long timeOut) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.ALERT_SERVICE_CENTRE;

                AsnOutputStream aos = new AsnOutputStream();

                alertServiceCentreArg.encode(aos);
                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);
                return invokeId;
            }
            return null;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void sendMAPAlertServiceCentreRsp(Short invokeId) throws Exception {
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

    public Short sendMAPAlertServiceCentreWithoutResultReq(AlertServiceCentreArg alertServiceCentreArg, long timeOut) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.ALERT_SERVICE_CENTRE;

                AsnOutputStream aos = new AsnOutputStream();

                alertServiceCentreArg.encode(aos);
                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);
                return invokeId;
            }
            return null;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke) throws IncorrectSyntaxException, UnexpectedDataException,
            NoServiceParameterAvailableException {
        switch (invoke.getOperationCode()) {
            case 64://ALERT_SERVICE_CENTRE:
                this.processAlertServiceCentreIndication(invoke);
                break;
            case 49:
                this.processAlertServiceCentreWithoutResultIndication(invoke);
                break;
        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int opCode) {
        switch (opCode) {
            case 64:
                ExecutorService worker = stack.findWorker(this.getDialogueId());
                worker.submit(() -> {
                    ((MAPShortMessageAlertContextListener) getMAPUser()).onMAPAlertScConfirmation(indication.getInvokeId(),
                            this, null, null);
                });
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, MAPUserError mapUserError, Short invokeId) {
        switch (operation) {
            case 64:
                ExecutorService worker = stack.findWorker(this.getDialogueId());
                worker.submit(() -> {
                    ((MAPShortMessageAlertContextListener) getMAPUser()).onMAPAlertScConfirmation(invokeId,
                            this, mapUserError, null);
                });
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, ProviderError providerError, Short invokeId) {
        switch (operation) {
            case 64:
                ExecutorService worker = stack.findWorker(this.getDialogueId());
                worker.submit(() -> {
                    ((MAPShortMessageAlertContextListener) getMAPUser()).onMAPAlertScConfirmation(invokeId,
                            this, null, providerError);
                });
                break;
        }
    }

    private void processAlertServiceCentreIndication(TCInvoke invoke) throws NoServiceParameterAvailableException,
            IncorrectSyntaxException, UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        AlertServiceCentreArg alertServiceCentreArg = new AlertServiceCentreArg();
        alertServiceCentreArg.decode((parameter.getData()));

        this.onMAPAlertScIndication(invoke.getInvokeID(), alertServiceCentreArg);
    }

    private void processAlertServiceCentreWithoutResultIndication(TCInvoke invoke) throws NoServiceParameterAvailableException,
            IncorrectSyntaxException, UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        AlertServiceCentreArg alertServiceCentreArg = new AlertServiceCentreArg();
        alertServiceCentreArg.setWithoutResult(true);

        alertServiceCentreArg.decode((parameter.getData()));

        this.onMAPAlertScIndication(invoke.getInvokeID(), alertServiceCentreArg);
    }

    public void onMAPAlertScIndication(Short invokeId, AlertServiceCentreArg alertServiceCentreArg) {
        ExecutorService worker = stack.findWorker(getDialogueId());
        worker.submit(() -> {
            ((MAPShortMessageAlertContextListener) getMAPUser()).onMAPAlertScIndication(invokeId, alertServiceCentreArg, this);
        });

    }

}
