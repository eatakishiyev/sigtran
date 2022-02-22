/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.mobility.sms;

import java.io.IOException;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.NoServiceParameterAvailableException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.*;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextImpl;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextName;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextVersion;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.messages.MessageHandling;
import dev.ocean.sigtran.tcap.ResourceLimitationException;
import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.parameters.ParameterImpl;
import dev.ocean.sigtran.tcap.primitives.tc.TCInvoke;
import dev.ocean.sigtran.tcap.primitives.tc.TCResult;

import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.*;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * @author eatakishiyev
 */
public class MAPShortMessageAlertDialogue extends MAPDialogue {

    private final static Logger logger = LogManager.getLogger(MAPShortMessageAlertDialogue.class);

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
