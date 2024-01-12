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
public class MAPShortMessageGatewayDialogue extends MAPDialogue {


    private final static Logger logger = LoggerFactory.getLogger(MAPShortMessageGatewayDialogue.class);

    public MAPShortMessageGatewayDialogue(MAPStackImpl stack, MAPApplicationContextVersion mapApplicationContextVersion,
                                          SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                          ExtensionContainer specificInformation, MessageHandling messageHandling, boolean sequenceControl) throws ResourceLimitationException {

        super(stack, new MAPApplicationContextImpl(MAPApplicationContextName.SHORT_MSG_GATEWAY_CONTEXT, mapApplicationContextVersion),
                destinationAddress, null,
                originatingAddress, null,
                specificInformation, messageHandling, sequenceControl);
    }

    public MAPShortMessageGatewayDialogue(MAPStackImpl stack, TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        super(stack, tcapDialogue, mapApplicationContext);
    }

    public Short sendMAPSendRoutingInfoForSMRequest(SendRoutingInfoForSMArg arg, long timeOut) throws IncorrectSyntaxException {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.SEND_ROUTING_INFO_FOR_SM;

                AsnOutputStream aos = new AsnOutputStream();
                arg.encode(aos);
                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);
                return invokeId;
            }
            return null;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    /**
     * @param msisdn                   Mandatory parameter
     * @param smRPPRI                  Mandatory parameter
     * @param serviceCentreAddress     Mandatory parameter
     * @param extensionContainer       Optional paramter
     * @param gprsSupportIndicator     Optional paramter
     * @param smRPMTI                  Optional paramter
     * @param smRPSMEA                 Optional paramter
     * @param smDeliveryNotIntended    Optional paramter
     * @param ipsSMGwGuidanceIndicator Optional paramter
     * @param timeOut                  invocation timeOut in seconds
     * @return invokeId
     * @throws IncorrectSyntaxException
     */
    public Short sendMAPSendRoutingInfoForSMRequest(ISDNAddressString msisdn, boolean smRPPRI,
                                                    AddressStringImpl serviceCentreAddress, ExtensionContainer extensionContainer,
                                                    boolean gprsSupportIndicator, SM_RP_MTI smRPMTI, SM_RP_SMEA smRPSMEA,
                                                    SM_DeliveryNotIntended smDeliveryNotIntended, boolean ipsSMGwGuidanceIndicator,
                                                    long timeOut) throws IncorrectSyntaxException {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.SEND_ROUTING_INFO_FOR_SM;

                AsnOutputStream aos = new AsnOutputStream();

                SendRoutingInfoForSMArg sendRoutingInfoForSMArg = new SendRoutingInfoForSMArg(msisdn, smRPPRI, serviceCentreAddress);
                sendRoutingInfoForSMArg.setExtensionContainer(extensionContainer);
                sendRoutingInfoForSMArg.setGprsSupportIndicator(gprsSupportIndicator);
                sendRoutingInfoForSMArg.setSmRPMTI(smRPMTI);
                sendRoutingInfoForSMArg.setSmRPSMEA(smRPSMEA);
                sendRoutingInfoForSMArg.setsM_DeliveryNotIntended(smDeliveryNotIntended);
                sendRoutingInfoForSMArg.setIpSMGWGuidanceIndicator(ipsSMGwGuidanceIndicator);
                sendRoutingInfoForSMArg.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());
                Short invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);
                return invokeId;
            }
            return null;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    /**
     * @param storedMSISDN
     * @param mwStatus
     * @param extensionContainer
     * @param absentSubscriberDiagnosticSM
     * @param additionalAbsentSubscriberDiagnosticsSM
     * @return
     * @throws Exception
     */
    public Short sendMAPInformServiceCentreRequest(ISDNAddressString storedMSISDN, MWStatus mwStatus, ExtensionContainer extensionContainer, AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM, AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticsSM) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.INFORM_SERVICE_CENTRE;

                InformServiceCentreArg informServiceCentreArg = new InformServiceCentreArg();
                informServiceCentreArg.setStoredMsisdn(storedMSISDN);
                informServiceCentreArg.setMwStatus(mwStatus);
                informServiceCentreArg.setExtensionContainer(extensionContainer);
                informServiceCentreArg.setAbsentSubscriberDiagnosticSM(absentSubscriberDiagnosticSM);
                informServiceCentreArg.setAdditionalAbsentSubscriberDiagnosticSM(additionalAbsentSubscriberDiagnosticsSM);

                AsnOutputStream aos = new AsnOutputStream();
                informServiceCentreArg.encode(aos);

                ParameterImpl parameter = null;
                if (aos.size() > 0) {
                    parameter = new ParameterImpl(aos.toByteArray());
                }

                Short invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, 0);
                return invokeId;
            }
            return null;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public Short sendMAPInformServiceCentreRequest(InformServiceCentreArg informServiceCentreArg) {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {

                OperationCodes opCode = OperationCodes.INFORM_SERVICE_CENTRE;
                ParameterImpl parameter = new ParameterImpl();
                parameter.setData(informServiceCentreArg.getRequestData());
                Short invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, 0);
                return invokeId;
            }
            return null;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void sendMAPSendRoutingInfoForSMResponse(Short invokeId, IMSI imsi, LocationInfoWithLMSI localtionInfoWithLMSI, ExtensionContainer extensionContainer, IP_SM_GW_Guidance ipSMGwGuidance) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                    || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {

                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }

                SendRoutingInfoForSMRes sendRoutingInfoForSMRes = new SendRoutingInfoForSMRes(imsi, localtionInfoWithLMSI);
                sendRoutingInfoForSMRes.setExtensionContainer(extensionContainer);
                sendRoutingInfoForSMRes.setIpSMGWGuidance(ipSMGwGuidance);

                AsnOutputStream aos = new AsnOutputStream();
                sendRoutingInfoForSMRes.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                this.terminatePerformingSSM(invokeId);
                getTcapDialogue().sendResult(invokeId, OperationCodes.SEND_ROUTING_INFO_FOR_SM.value(), parameter);
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    /**
     * @param msisdn
     * @param serviceCentreAddress
     * @param smDeliveryOutcome
     * @param absentSubscriberDiagnosticSM
     * @param extensionContainer
     * @param gprsSupportIndicator
     * @param deliveryOutcomeIndicator
     * @param additionalSMDeliveryOutcome
     * @param additionalAbsentSubscriberDiagnosticSM
     * @param ipSMGwIndicator
     * @param ipSMGwSMDeliveryOutcome
     * @param ipSMGwAbsnetAbsentSubscriberDiagnosticSM
     * @param timeOut                                  incocation timeOut in seconds
     * @return
     * @throws Exception
     */
    public Short sendMAPReportSMDeliveryStatusRequest(ISDNAddressString msisdn, AddressStringImpl serviceCentreAddress, SMDeliveryOutcome smDeliveryOutcome, AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM,
                                                      ExtensionContainer extensionContainer, boolean gprsSupportIndicator, boolean deliveryOutcomeIndicator, SMDeliveryOutcome additionalSMDeliveryOutcome,
                                                      AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM, boolean ipSMGwIndicator, SMDeliveryOutcome ipSMGwSMDeliveryOutcome, AbsentSubscriberDiagnosticSM ipSMGwAbsnetAbsentSubscriberDiagnosticSM, long timeOut) throws Exception {
        try {

            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.WAIT_FOR_USER_REQUESTS
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED) {
                OperationCodes opCode = OperationCodes.REPORT_SM_DELIVERY_STATUS;

                AsnOutputStream aos = new AsnOutputStream();

                ReportSMDeliveryStatusArg reportSMDeliveryStatusArg = new ReportSMDeliveryStatusArg(msisdn, serviceCentreAddress, smDeliveryOutcome);
                reportSMDeliveryStatusArg.setAbsentSubscriberDiagnosticSM(absentSubscriberDiagnosticSM);
                reportSMDeliveryStatusArg.setExtensionContainer(extensionContainer);
                reportSMDeliveryStatusArg.setGprsSupportIndicator(gprsSupportIndicator);
                reportSMDeliveryStatusArg.setDeliveryOutcomeIndicator(deliveryOutcomeIndicator);
                reportSMDeliveryStatusArg.setAdditionalDeliveryOutcome(additionalSMDeliveryOutcome);
                reportSMDeliveryStatusArg.setAdditionalAbsentSubscriberDiagnosticSM(additionalAbsentSubscriberDiagnosticSM);
                reportSMDeliveryStatusArg.setIpSmGwIndicator(ipSMGwIndicator);
                reportSMDeliveryStatusArg.setIpSmGwSmDeliveryOutcome(ipSMGwSMDeliveryOutcome);
                reportSMDeliveryStatusArg.setIpSmGwAbsentSubscriberDiagnosticSM(ipSMGwAbsnetAbsentSubscriberDiagnosticSM);

                reportSMDeliveryStatusArg.encode(aos);

                ParameterImpl parameter = new ParameterImpl(aos.toByteArray());

                Short invokeId = getTcapDialogue().sendInvoke(opCode.value(), opCode.operationClass(), parameter, timeOut);
                return invokeId;
            }
            return null;
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    public void sendMAPReportSMDeliveryStatusResponse(Short invokeId, ISDNAddressString storedMsisdn, ExtensionContainer extensionContainer) throws Exception {
        try {
            getTcapDialogue().getLock().lock();
            if (getMAPDialogueState() == MAPDialogueState.DIALOGUE_ACCEPTED
                    || getMAPDialogueState() == MAPDialogueState.DIALOGUE_ESTABLISHED
                    || getMAPDialogueState() == MAPDialogueState.WAIT_FOR_INIT_DATA) {

                if (!this.isPerformingSSMExists(invokeId)) {
                    throw new IOException(String.format("No performing SSM found. DialogueId = %d InvokeId = %d", getDialogueId(), invokeId));
                }

                ReportSMDeliveryStatusRes reportSMDeliveryStatusRes = null;
                ParameterImpl parameter = null;
                if (storedMsisdn != null || extensionContainer != null) {
                    reportSMDeliveryStatusRes = new ReportSMDeliveryStatusRes();
                    reportSMDeliveryStatusRes.setStoredMsisdn(storedMsisdn);
                    reportSMDeliveryStatusRes.setExtensionContainer(extensionContainer);

                    AsnOutputStream aos = new AsnOutputStream();
                    reportSMDeliveryStatusRes.encode(aos);

                    parameter = new ParameterImpl(aos.toByteArray());

                    this.terminatePerformingSSM(invokeId);
                    getTcapDialogue().sendResult(invokeId, OperationCodes.REPORT_SM_DELIVERY_STATUS.value(), parameter);
                } else {

                    this.terminatePerformingSSM(invokeId);
                    getTcapDialogue().sendResult(invokeId);
                }
            }
        } finally {
            getTcapDialogue().getLock().unlock();
        }
    }

    @Override
    protected void onServiceInvocationReceived(TCInvoke invoke) throws IncorrectSyntaxException, UnexpectedDataException, NoServiceParameterAvailableException {
        switch (invoke.getOperationCode()) {

            case 45://SEND_ROUTING_INFO_FOR_SM
                this.processSriForSmIndication(invoke);
                break;
            case 47://REPORT_SM_DELIVERY_STATUS:
                this.processReportSMDeliveryStatusIndication(invoke);
                break;
            case 63://INFORM_SERVICE_CENTRE:
                this.processInformServiceCentreIndication(invoke);
                break;
        }
    }

    @Override
    protected void onResultReceived(TCResult indication, int operationCode) {

        if (logger.isDebugEnabled()) {
            logger.debug("ResultReceived. Operation = " + operationCode + " Dialogue = " + this);
        }

        switch (operationCode) {
            case 45:// SEND_ROUTING_INFO_FOR_SM:
                this.processSriForSMConfirmation(indication);
                break;
            case 47://REPORT_SM_DELIVERY_STATUS:
                this.processReportSMDeliveryStatusCnf(indication);
                break;
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, MAPUserError mapUserError, Short invokeId) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());

        switch (operation) {
            case 45://SEND_ROUTING_INFO_FOR_SM:
                worker.submit(() -> {
                    ((MAPShortMessageGatewayContextListener) getMAPUser()).onMAPSendRoutingInfoSmConfirmation(invokeId, null, this,
                            mapUserError, null);
                });

                break;
            case 47://REPORT_SM_DELIVERY_STATUS:
                worker.submit(() -> {
                    ((MAPShortMessageGatewayContextListener) getMAPUser()).onMAPReportSmDeliveryStatusConfirmation(invokeId, null, this, mapUserError, null);
                });
        }
    }

    @Override
    protected void onNegativeResultReceived(int operation, ProviderError providerError, Short invokeId) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());
        switch (operation) {
            case 45://SEND_ROUTING_INFO_FOR_SM:
                worker.submit(() -> {
                    ((MAPShortMessageGatewayContextListener) getMAPUser()).onMAPSendRoutingInfoSmConfirmation(invokeId, null, this,
                            null, providerError);
                });
                break;
            case 47://REPORT_SM_DELIVERY_STATUS:
                this.onMAPReportSmDeliveryStatusConfirmation(invokeId, providerError);
                break;
        }
    }

    private void processSriForSmIndication(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException, UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        SendRoutingInfoForSMArg sendRoutingInfoForSMArg = new SendRoutingInfoForSMArg();

        sendRoutingInfoForSMArg.decode((parameter.getData()));

        ExecutorService worker = stack.findWorker(this.getDialogueId());

        worker.submit(() -> {
            ((MAPShortMessageGatewayContextListener) getMAPUser()).onMAPSendRoutingInfoSmIndication(invoke.getInvokeID(), sendRoutingInfoForSMArg, this);
        });

    }

    private void processReportSMDeliveryStatusIndication(TCInvoke invoke) throws NoServiceParameterAvailableException, IncorrectSyntaxException, UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();

        if (parameter == null) {
            throw new NoServiceParameterAvailableException();
        }

        ReportSMDeliveryStatusArg reportSmDeliveryStatusArg = new ReportSMDeliveryStatusArg();

        reportSmDeliveryStatusArg.decode((parameter.getData()));

        ExecutorService worker = stack.findWorker(this.getDialogueId());

        worker.submit(() -> {
            ((MAPShortMessageGatewayContextListener) getMAPUser()).onMAPReportSmDeliveryStatusIndication(invoke.getInvokeID(), reportSmDeliveryStatusArg, this);
        });

    }

    private void processInformServiceCentreIndication(TCInvoke invoke) throws IncorrectSyntaxException, UnexpectedDataException {
        ParameterImpl parameter = invoke.getParameter();

        InformServiceCentreArg informServiceCentreArg = new InformServiceCentreArg();
        if (parameter != null) {
            informServiceCentreArg.decode((parameter.getData()));
        }

        ExecutorService worker = stack.findWorker(this.getDialogueId());
        worker.submit(() -> {
            ((MAPShortMessageGatewayContextListener) getMAPUser()).onMAPInformScIndication(invoke.getInvokeID(), informServiceCentreArg, this);
        });

    }

    private void processSriForSMConfirmation(TCResult indication) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());
        try {
            if (indication.getParameter() == null) {
                throw new UnexpectedDataException();
            }

            SendRoutingInfoForSMRes sendRoutingInfoForSMRes = new SendRoutingInfoForSMRes();

            sendRoutingInfoForSMRes.decode((indication.getParameter().getData()));

            worker.submit(() -> {
                logger.debug("SendRoutingInfoForSmConfirmation submitted to user {}", getMAPUser());
                ((MAPShortMessageGatewayContextListener) getMAPUser()).onMAPSendRoutingInfoSmConfirmation(indication.getInvokeId(),
                        sendRoutingInfoForSMRes, this, null, null);
            });

        } catch (Throwable ex) {
            logger.error("ErrorOccurred: ", ex);

            worker.submit(() -> {
                ((MAPShortMessageGatewayContextListener) getMAPUser()).onMAPSendRoutingInfoSmConfirmation(indication.getInvokeId(),
                        null, this, null, ProviderError.INVALID_RESPONSE_RECEIVED);
            });

        }
    }

    private void processReportSMDeliveryStatusCnf(TCResult indication) {
        ReportSMDeliveryStatusRes reportSmDeliveryStatusRes = new ReportSMDeliveryStatusRes();
        try {
            if (indication.getParameter() != null) {
                reportSmDeliveryStatusRes.decode((indication.getParameter().getData()));
            }

            ExecutorService worker = stack.findWorker(this.getDialogueId());

            worker.submit(() -> {
                ((MAPShortMessageGatewayContextListener) getMAPUser()).
                        onMAPReportSmDeliveryStatusConfirmation(indication.getInvokeId(),
                                reportSmDeliveryStatusRes, this, null, null);
            });

        } catch (IncorrectSyntaxException ex) {
            this.onMAPReportSmDeliveryStatusConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);
            getTcapDialogue().tcURejectRequest(indication.getInvokeId(), ReturnResultProblem.MISTYPED_PARAMETER);

        } catch (UnexpectedDataException ex) {
            this.onMAPReportSmDeliveryStatusConfirmation(indication.getInvokeId(), ProviderError.INVALID_RESPONSE_RECEIVED);
        }
    }

    private void onMAPReportSmDeliveryStatusConfirmation(Short invokeId, ProviderError providerError) {
        ExecutorService worker = stack.findWorker(this.getDialogueId());
        worker.submit(() -> {
            ((MAPShortMessageGatewayContextListener) getMAPUser())
                    .onMAPReportSmDeliveryStatusConfirmation(invokeId, null,
                            this, null, providerError);
        });
    }

}
