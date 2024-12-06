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
import azrc.az.sigtran.map.parameters.*;
import azrc.az.sigtran.map.parameters.depricated.Time;
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
public class MAPShortMessageRelayDialogue extends MAPDialogue {

    private static final Logger logger = LoggerFactory.getLogger(MAPShortMessageRelayDialogue.class);

    public MAPShortMessageRelayDialogue(MAPStackImpl stack,
                                        MAPApplicationContextName acName, MAPApplicationContextVersion acVersion,
                                        SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                        ExtensionContainer specificInformation, MessageHandling messageHandling, boolean sequenceControl) throws ResourceLimitationException {

        super(stack, new MAPApplicationContextImpl(acName, acVersion),
                destinationAddress, null, originatingAddress, null,
                specificInformation, messageHandling, sequenceControl);
    }

    public MAPShortMessageRelayDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        super(stack, tcapDialogue, mapApplicationContext);
    }

    /**
     * @param smRPDA             Mandatory parameter
     * @param smRPOA             Mandatory parameter
     * @param signalInfo         Mandatory parameter
     * @param extensionContainer Optional parameter. May be null
     * @param imsi               Optional parameter. May be null
     * @param timeOut            invocation timeOut
     * @return
     */
    public Short sendMAPMOForwardSMRequest(SM_RP_DA smRPDA, SM_RP_OA smRPOA, SignalInfo signalInfo, ExtensionContainer extensionContainer, IMSI imsi, long timeOut) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.FORWARD_SM;

                MOForwardSMArg moForwardSMReq = new MOForwardSMArg(smRPDA, smRPOA, signalInfo);
                moForwardSMReq.setExtensionContainer(extensionContainer);
                moForwardSMReq.setImsi(imsi);

                AsnOutputStream aos = new AsnOutputStream();
                moForwardSMReq.encode(aos);
                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);
                return invokeId;
            }
            return null;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void sendMAPForwardSMResponse(Short invokeId, SignalInfo signalInfo, ExtensionContainer extensionContainer) throws IOException, IncorrectSyntaxException {
        this.sendMAPMOForwardSMResponse(invokeId, signalInfo, extensionContainer);
    }

    public void sendMAPMOForwardSMResponse(Short invokeId, SignalInfo signalInfo, ExtensionContainer extensionContainer) throws IOException, IncorrectSyntaxException {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                    || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {

                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }

                MAPForwardSMResponse moForwardRes = new MAPForwardSMResponse();
                moForwardRes.setSmRPUI(signalInfo);
                moForwardRes.setExtensionContainer(extensionContainer);

                AsnOutputStream aos = new AsnOutputStream();
                moForwardRes.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                this.terminatePerformingSSM(invokeId);
                getTcapDialogue().sendResult(invokeId, parameter);

            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void sendMAPForwardSMResponse(Short invokeId, MAPUserError userError) throws Exception {
        this.sendMAPMOForwardSMResponse(invokeId, userError);
    }

    public void sendMAPMOForwardSMResponse(Short invokeId, MAPUserError userError) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                    || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {

                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }

                AsnOutputStream aos = new AsnOutputStream();
                userError.encode(aos);

                ParameterImpl parameter = null;

                if (aos.size() > 0) {
                    parameter = new ParameterImpl(aos.toByteArray());
                }

                this.terminatePerformingSSM(invokeId);
                getTcapDialogue().sendUserError(invokeId, userError.getMAPUserErrorValue().value(), parameter);
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    /**
     * @param sm_rp_da
     * @param sm_rp_oa
     * @param sm_rp_ui
     * @param moreMessageToSend
     * @param sMDeliveryTimer
     * @param time
     * @param timeOut           invocation timeOut in seconds
     * @return
     * @throws Exception
     */
    public Short sendMAPMTForwardSMRequest(SM_RP_DA sm_rp_da, SM_RP_OA sm_rp_oa, SignalInfo sm_rp_ui, boolean moreMessageToSend, SMDeliveryTimerValue sMDeliveryTimer, Time time, long timeOut) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.MT_FORWARD_SM;

                if (getMapApplicationContext().getMapApplicationContextVersion()
                        == MAPApplicationContextVersion.VERSION_1
                        || getMapApplicationContext().getMapApplicationContextVersion()
                        == MAPApplicationContextVersion.VERSION_2) {
                    opCode = OperationCodes.FORWARD_SM;
                }

                AsnOutputStream aos = new AsnOutputStream();
                MTForwardSMArg mtForwardSMArg = new MTForwardSMArg(sm_rp_da, sm_rp_oa, sm_rp_ui);
                mtForwardSMArg.setMoreMessageToSend(moreMessageToSend);
                mtForwardSMArg.setSmDeliveryStartTime(time);
                mtForwardSMArg.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());
                Short invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);

                return invokeId;
            }
            return null;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void sendMAPMTForwardSMResponse(Short invokeId, SignalInfo signalInfo, ExtensionContainer extensionContainer) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }

                MAPForwardSMResponse mtForwardRes = new MAPForwardSMResponse();
                mtForwardRes.setSmRPUI(signalInfo);
                mtForwardRes.setExtensionContainer(extensionContainer);

                AsnOutputStream aos = new AsnOutputStream();
                mtForwardRes.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                this.terminatePerformingSSM(invokeId);
                getTcapDialogue().sendResult(invokeId, parameter);
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    /**
     * @return
     * @throws Exception
     */
    //MT/MO FORWARD for MAP Version 1,2
//    public Short sendMAPForwardSMRequest(SM_RP_DA sm_rp_da, SM_RP_OA sm_rp_oa, SignalInfo sm_rp_ui, boolean moreMessageToSend, SMDeliveryTimerValue sMDeliveryTimer, Time time, long timeOut) throws Exception {
//        try {
//            getTcapDialogue().getLock().lock();
//            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
//                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
//                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
//
//                OperationCodes opCode = OperationCodes.FORWARD_SM;
//
//                AsnOutputStream aos = new AsnOutputStream();
//                MTForwardSMArg mtForwardSMArg = new MTForwardSMArg(sm_rp_da, sm_rp_oa, sm_rp_ui);
//                mtForwardSMArg.setMoreMessageToSend(moreMessageToSend);
//                mtForwardSMArg.encode(aos);
//
//                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());
//                Short invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);
//                return invokeId;
//            }
//            return null;
//        } finally {
//            getTcapDialogue().getLock().unlock();
//        }
//    }
    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke) throws IncorrectSyntaxException, UnexpectedDataException, NoServiceParameterAvailableException {
        switch (invoke.getOperationCode()) {
            case 46://MO_FORWARD_SM:
                this.processForwardSMIndication(invoke);
                break;
            case 44://MT_FORWARD_SM:
                this.processMtForwardSMIndication(invoke);
                break;

        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int operationCode) {
        if (logger.isDebugEnabled()) {
            logger.debug("ResultReceived. Operation = " + operationCode + " Dialogue = " + this);
        }

        switch (operationCode) {
            case 44: //MT_FORWARD_SM
            case 46: //MO_FORWARD_SM/FORWARD_SM:
                this.processForwardSMCnf(indication);
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, MAPUserError mapUserError, Short invokeId) {

        switch (operation) {
            case 44://MT_FORWARD_SM
            case 46://MO_FORWARD_SM/FORWARD_SM:
                ExecutorService worker = stack.findWorker(this.getDialogueId());

                worker.submit(() -> {
                    ((MAPShortMessageRelayContextListener) getMAPUser())
                            .onMAPForwardSmConfirmation(invokeId, null, this, mapUserError, null);
                });

                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, ProviderError providerError, Short invokeId) {
        switch (operation) {
            case 44://MT_FORWARD_SM
            case 46://MO_FORWARD_SM/FORWARD_SM:

                ExecutorService worker = stack.findWorker(this.getDialogueId());

                worker.submit(() -> {
                    ((MAPShortMessageRelayContextListener) getMAPUser()).
                            onMAPForwardSmConfirmation(invokeId, null, this, null, providerError);
                });

                break;
        }
    }

    private void processForwardSMIndication(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException, UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        MOForwardSMArg forwardSmArg = new MOForwardSMArg();

        forwardSmArg.decode((parameter.getData()));

        this.onMAPForwardSmIndication(invoke.getInvokeID(), forwardSmArg);
    }

    private void processForwardSMCnf(TCResult indication) {
        try {
            MAPForwardSMResponse forwardSmRes = new MAPForwardSMResponse();

            if (indication.getParameter() != null) {
                forwardSmRes.decode((indication.getParameter().getData()));
            }

            ExecutorService worker = stack.findWorker(getDialogueId());

            worker.submit(() -> {
                ((MAPShortMessageRelayContextListener) getMAPUser()).
                        onMAPForwardSmConfirmation(indication.getInvokeId(),
                                forwardSmRes, this, null, null);
            });

        } catch (IncorrectSyntaxException ex) {
            logger.error("ErrorOccured: ", ex);
            ((MAPShortMessageRelayContextListener) getMAPUser()).onMAPForwardSmConfirmation(indication.getInvokeId(),
                    null, this, null, ProviderError.INVALID_RESPONSE_RECEIVED);
        }
    }

    private void processMtForwardSMIndication(TCInvoke invoke) throws NoServiceParameterAvailableException,
            IncorrectSyntaxException, UnexpectedDataException {

        ParameterImpl parameter = invoke.getParameter();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        MTForwardSMArg mtForwardSmArg = new MTForwardSMArg();
        mtForwardSmArg.decode((parameter.getData()));

        this.onMAPMtForwardSmIndication(invoke.getInvokeID(), mtForwardSmArg);
    }

    private void onMAPForwardSmIndication(Short invokeID, MAPForwardSmArg arg) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());

        worker.submit(() -> {
            ((MAPShortMessageRelayContextListener) getMAPUser()).
                    onMAPForwardSmIndication(invokeID, arg, this);
        });

    }

    private void onMAPMtForwardSmIndication(Short invokeID, MTForwardSMArg arg) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());

        worker.submit(() -> {
            ((MAPShortMessageRelayContextListener) getMAPUser()).
                    onMAPMtForwardSmIndication(invokeID, arg, this);
        });

    }
}
