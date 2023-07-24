/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map;

import java.util.List;

import org.apache.logging.log4j.*;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.ISDNAddressString;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextImpl;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextName;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextVersion;
import dev.ocean.sigtran.map.services.enquiry.MAPAnyTimeInterrogationDialogue;
import dev.ocean.sigtran.map.services.reporting.MAPMMEventReportingDialogue;
import dev.ocean.sigtran.map.services.purging.MAPPurgeMSDialogue;
import dev.ocean.sigtran.map.services.enquiry.MAPRoamingNumberEnquiryDialogue;
import dev.ocean.sigtran.map.services.enquiry.MAPSubscriberInformationEnquiryDialogue;
import dev.ocean.sigtran.map.services.equipment.management.MAPEquipmentManagementDialogue;
import dev.ocean.sigtran.map.services.ericsson.mobility.management.IN.MAPEricssonMobilityMngtINTriggeringDialogue;
import dev.ocean.sigtran.map.services.errors.IncorrectAcVersion;
import dev.ocean.sigtran.map.services.location.cancellation.MAPLocationCancellationDialogue;
import dev.ocean.sigtran.map.services.location.info.retrieval.MAPLocationInfoRetrievalDialogue;
import dev.ocean.sigtran.map.services.mobility.sms.MAPShortMessageAlertDialogue;
import dev.ocean.sigtran.map.services.mobility.sms.MAPShortMessageGatewayDialogue;
import dev.ocean.sigtran.map.services.mobility.sms.MAPShortMessageRelayDialogue;
import dev.ocean.sigtran.map.services.mobility.ussd.MAPNetworkUnstructuredSsDialogue;
import dev.ocean.sigtran.map.services.network.location.update.MAPNetworkLocationUpdateDialogue;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.messages.MessageHandling;
import dev.ocean.sigtran.tcap.ResourceLimitationException;
import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.parameters.ApplicationContextImpl;
import dev.ocean.sigtran.tcap.primitives.AbortReason;
import dev.ocean.sigtran.tcap.primitives.tc.Operation;
import dev.ocean.sigtran.tcap.primitives.tc.TCBegin;
import dev.ocean.sigtran.tcap.primitives.tc.TCInvoke;
import dev.ocean.sigtran.tcap.primitives.tc.TCUAbort;

/**
 * @author eatakishiyev
 */
public class MAPDialogueFactory {

    private final Logger logger = LogManager.getLogger(MAPDialogueFactory.class);
    private final MAPStackImpl stack;

    protected MAPDialogueFactory(MAPStackImpl stack) {
        this.stack = stack;
    }

    public MAPRoamingNumberEnquiryDialogue createMAPRoamingNumberEnquiryDialogue(MAPApplicationContextVersion version,
                                                                                 SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                                                                 ExtensionContainer specificInformation, MessageHandling messageHandling,
                                                                                 boolean sequenceControl) throws ResourceLimitationException {

        return new MAPRoamingNumberEnquiryDialogue(stack,
                version, destinationAddress, originatingAddress,
                specificInformation, messageHandling, sequenceControl);

    }

    private MAPRoamingNumberEnquiryDialogue createMAPRoamingNumberEnquiryDialogue(TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        return new MAPRoamingNumberEnquiryDialogue(stack, tcapDialogue, mapApplicationContext);
    }

    //
    public MAPLocationInfoRetrievalDialogue createMAPLocationInfoRetrievalDialogue(MAPApplicationContextVersion version,
                                                                                   SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                                                                   ExtensionContainer specificInformation, MessageHandling messageHandling,
                                                                                   boolean sequenceControl) throws ResourceLimitationException {

        return new MAPLocationInfoRetrievalDialogue(stack, version,
                destinationAddress, originatingAddress, specificInformation,
                messageHandling, sequenceControl);
    }

    private MAPLocationInfoRetrievalDialogue createMAPLocationInfoRetrievalDialogue(TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        return new MAPLocationInfoRetrievalDialogue(stack, tcapDialogue, mapApplicationContext);
    }
//    

    public MAPShortMessageAlertDialogue createMAPAlertScDialogue(MAPApplicationContextVersion version,
                                                                 SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                                                 ExtensionContainer specificInformation, MessageHandling messageHandling,
                                                                 boolean sequenceControl) throws ResourceLimitationException {

        return new MAPShortMessageAlertDialogue(stack, version, destinationAddress,
                originatingAddress, specificInformation, messageHandling, sequenceControl);
    }

    private MAPShortMessageAlertDialogue createMAPAlertingDialogue(TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        return new MAPShortMessageAlertDialogue(stack, tcapDialogue, mapApplicationContext);
    }
//    

    public MAPShortMessageGatewayDialogue createMAPShortMessageGatewayDialogue(MAPApplicationContextVersion version,
                                                                               SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                                                               ExtensionContainer specificInformation, MessageHandling messageHandling,
                                                                               boolean sequenceControl) throws ResourceLimitationException {

        return new MAPShortMessageGatewayDialogue(stack, version,
                destinationAddress, originatingAddress, specificInformation,
                messageHandling, sequenceControl);
    }

    private MAPShortMessageGatewayDialogue createMAPShortMessageGatewayDialogue(TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        return new MAPShortMessageGatewayDialogue(stack, tcapDialogue, mapApplicationContext);
    }
//    

    public MAPAnyTimeInterrogationDialogue createMAPAnyTimeInterrogationDialogue(SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                                                                 ExtensionContainer specificInformation, MessageHandling messageHandling, boolean sequenceControl) throws ResourceLimitationException {
        return new MAPAnyTimeInterrogationDialogue(stack, destinationAddress,
                originatingAddress, specificInformation, messageHandling, sequenceControl);
    }

    private MAPAnyTimeInterrogationDialogue createMAPAnyTimeInterrogationDialogue(TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        return new MAPAnyTimeInterrogationDialogue(stack, tcapDialogue, mapApplicationContext);
    }
//    

    //
    public MAPShortMessageRelayDialogue createMAPShortMessageRelayDialogue(MAPApplicationContextName acName, MAPApplicationContextVersion version,
                                                                           SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                                                           ExtensionContainer specificInformation, MessageHandling messageHandling,
                                                                           boolean sequenceControl) throws ResourceLimitationException {

        return new MAPShortMessageRelayDialogue(stack, acName, version,
                destinationAddress, originatingAddress, specificInformation,
                messageHandling, sequenceControl);
    }

    private MAPShortMessageRelayDialogue createMAPShortMessageRelayDialogue(TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        return new MAPShortMessageRelayDialogue(stack, tcapDialogue, mapApplicationContext);
    }
//    

    public MAPNetworkUnstructuredSsDialogue createMAPUssdDialogue(MAPApplicationContextVersion version,
                                                                  SCCPAddress destinationAddress, ISDNAddressString destinationReference,
                                                                  SCCPAddress originatingAddress, ISDNAddressString originatingReference,
                                                                  ExtensionContainer specificInformation, MessageHandling messageHandling,
                                                                  boolean sequenceControl) throws ResourceLimitationException {

        return new MAPNetworkUnstructuredSsDialogue(stack, version,
                destinationAddress, destinationReference,
                originatingAddress, originatingReference,
                specificInformation, messageHandling, sequenceControl);
    }

    private MAPNetworkUnstructuredSsDialogue createMAPUssdDialogue(TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        return new MAPNetworkUnstructuredSsDialogue(stack, tcapDialogue, mapApplicationContext);
    }
//

    public MAPNetworkLocationUpdateDialogue createMAPNetworkLocationUpdateDialogue(MAPApplicationContextImpl mapApplicationContext,
                                                                                   SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                                                                   ExtensionContainer specificInformation, MessageHandling messageHandling,
                                                                                   boolean sequenceControl) throws ResourceLimitationException {
        return new MAPNetworkLocationUpdateDialogue(stack, mapApplicationContext,
                destinationAddress, originatingAddress, specificInformation,
                messageHandling, sequenceControl);
    }

    private MAPNetworkLocationUpdateDialogue createMAPNetworkLocationUpdateDialogue(TCAPDialogue tcapDialogue, MAPApplicationContextImpl mapApplicationContext) {
        return new MAPNetworkLocationUpdateDialogue(stack, tcapDialogue, mapApplicationContext);
    }

    //
    public MAPLocationCancellationDialogue createMAPLocationCancellationDialogue(MAPApplicationContextVersion version,
                                                                                 SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                                                                 ExtensionContainer specificInformation, MessageHandling messageHandling,
                                                                                 boolean sequenceControl) throws ResourceLimitationException {
        return new MAPLocationCancellationDialogue(stack, version, destinationAddress, originatingAddress, specificInformation, messageHandling, sequenceControl);
    }

    private MAPLocationCancellationDialogue createMAPLocationCancellationDialogue(TCAPDialogue dialogue, MAPApplicationContextImpl mapApplicationContext) {
        return new MAPLocationCancellationDialogue(stack, dialogue, mapApplicationContext.getMapApplicationContextVersion());
    }
//

    public MAPSubscriberInformationEnquiryDialogue createMAPSubscriberInformationEnquiryDialogue(SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                                                                                 ExtensionContainer specificInformation, MessageHandling messageHandling,
                                                                                                 boolean sequenceControl) throws ResourceLimitationException {
        return new MAPSubscriberInformationEnquiryDialogue(stack, destinationAddress, originatingAddress, specificInformation, messageHandling, sequenceControl);
    }

    private MAPSubscriberInformationEnquiryDialogue createMAPSubscriberInformationEnquiryDialogue(TCAPDialogue dialogue) {
        return new MAPSubscriberInformationEnquiryDialogue(stack, dialogue);
    }
//

    public MAPEquipmentManagementDialogue createMAPEquipmentManagementDialogue(MAPApplicationContextVersion version,
                                                                               SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                                                               ExtensionContainer extensionContainer, MessageHandling messageHandling,
                                                                               boolean sequenceControl) throws ResourceLimitationException {
        return new MAPEquipmentManagementDialogue(stack, version, destinationAddress, originatingAddress, extensionContainer, messageHandling, sequenceControl);
    }

    private MAPEquipmentManagementDialogue createMAPEquipmentManagementDialogue(TCAPDialogue dialogue, MAPApplicationContextImpl mapApplicationContext) {
        return new MAPEquipmentManagementDialogue(stack, dialogue, mapApplicationContext.getMapApplicationContextVersion());
    }
//    

    private MAPDialogue createMAPAnyTimeInfoHandlingDialogue(TCAPDialogue dialogue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
//    

    public MAPMMEventReportingDialogue createMAPMMEventReportingDialogue(SCCPAddress destinationAddress,
                                                                         SCCPAddress originatingAddress, ExtensionContainer extensionContainer,
                                                                         MessageHandling messageHandling, boolean sequenceControl) throws ResourceLimitationException {
        return new MAPMMEventReportingDialogue(stack, destinationAddress, originatingAddress,
                extensionContainer, messageHandling, sequenceControl);
    }

    private MAPMMEventReportingDialogue createMAPMMEventReportingDialogue(TCAPDialogue dialogue) {
        return new MAPMMEventReportingDialogue(stack, dialogue);
    }

    public MAPEricssonMobilityMngtINTriggeringDialogue createMAPEricssonMobilityMngtINTriggeringDialogue(SCCPAddress destinationAddress,
                                                                                                         SCCPAddress originatingAddress, ExtensionContainer extensionContainer,
                                                                                                         MessageHandling messageHandling, boolean sequenceControl) throws ResourceLimitationException {
        return new MAPEricssonMobilityMngtINTriggeringDialogue(stack, destinationAddress,
                originatingAddress, messageHandling, sequenceControl);
    }

    private MAPEricssonMobilityMngtINTriggeringDialogue createMAPEricssonMngtINTriggeringDialogue(TCAPDialogue dialog) {
        return new MAPEricssonMobilityMngtINTriggeringDialogue(stack, dialog);
    }

    public MAPPurgeMSDialogue createMAPPurgeMSDialogue(MAPApplicationContextVersion version,
                                                       SCCPAddress destinationAddress, SCCPAddress originatingAddress,
                                                       ExtensionContainer extensionContainer, MessageHandling messageHandling,
                                                       boolean sequenceControl) throws ResourceLimitationException, IncorrectAcVersion {
        return new MAPPurgeMSDialogue(stack, version, destinationAddress,
                originatingAddress, extensionContainer, messageHandling, sequenceControl);
    }

    private MAPPurgeMSDialogue createMAPPurgeMSDialogue(TCAPDialogue dialog, MAPApplicationContextImpl mapAc) {
        return new MAPPurgeMSDialogue(stack, dialog, mapAc);
    }
//    

    protected MAPDialogue createMAPDialogue(TCBegin indication) {
        if (indication.isApplicationContextIncluded()) {
            MAPApplicationContextImpl mapApplicationContext
                    = new MAPApplicationContextImpl(indication.getApplicationContextName().getOid());
            int version = mapApplicationContext.getMapApplicationContextVersion().value();
            try {
//                isACSupporting(mapApplicationContext);
                switch (mapApplicationContext.getMapApplicationContextName()) {
                    case ROAMING_NUMBER_ENQUIRY_CONTEXT:
                        if (version >= 1 && version <= 3) {
                            return this.createMAPRoamingNumberEnquiryDialogue(indication.getDialogue(), mapApplicationContext);
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.ROAMING_NUMBER_ENQUIRY_CONTEXT, MAPApplicationContextVersion.VERSION_3));
                        }
                    case LOCATION_INFO_RETRIEVAL_CONTEXT:
                        if (version >= 1 && version <= 3) {
                            return this.createMAPLocationInfoRetrievalDialogue(indication.getDialogue(), mapApplicationContext);
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.LOCATION_INFO_RETRIEVAL_CONTEXT, MAPApplicationContextVersion.VERSION_3));
                        }
                    case SHORT_MSG_ALERT_CONTEXT:
                        if (version >= 1 && version <= 2) {
                            return this.createMAPAlertingDialogue(indication.getDialogue(), mapApplicationContext);
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.SHORT_MSG_ALERT_CONTEXT, MAPApplicationContextVersion.VERSION_2));
                        }
                    case SHORT_MSG_GATEWAY_CONTEXT:
                        if (version >= 1 && version <= 3) {
                            return this.createMAPShortMessageGatewayDialogue(indication.getDialogue(), mapApplicationContext);
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.SHORT_MSG_GATEWAY_CONTEXT, MAPApplicationContextVersion.VERSION_3));
                        }
                    case SHORT_MSG_MO_RELAY_CONTEXT:
                        if (version >= 1 && version <= 3) {
                            return this.createMAPShortMessageRelayDialogue(indication.getDialogue(), mapApplicationContext);
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.SHORT_MSG_MO_RELAY_CONTEXT, MAPApplicationContextVersion.VERSION_3));
                        }
                    case SHORT_MSG_MT_RELAY_CONTEXT:
                        if (version >= 1 && version <= 3) {
                            return this.createMAPShortMessageRelayDialogue(indication.getDialogue(), mapApplicationContext);
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.SHORT_MSG_MT_RELAY_CONTEXT, MAPApplicationContextVersion.VERSION_3));
                        }
                    case NETWORK_UNSTRUCTURED_SS_CONTEXT:
                        if (version == 1 || version == 2) {
                            return this.createMAPUssdDialogue(indication.getDialogue(), mapApplicationContext);
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.NETWORK_UNSTRUCTURED_SS_CONTEXT, MAPApplicationContextVersion.VERSION_2));
                        }
                    case NETWORK_LOCUP_CONTEXT:
                        if (version >= 1 && version <= 3) {
                            return this.createMAPNetworkLocationUpdateDialogue(indication.getDialogue(), mapApplicationContext);
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.NETWORK_LOCUP_CONTEXT, MAPApplicationContextVersion.VERSION_3));

                        }
                    case ANY_TIME_ENQUIRY_CONTEXT:
                        if (version == 3) {
                            return this.createMAPAnyTimeInterrogationDialogue(indication.getDialogue(), mapApplicationContext);
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.ANY_TIME_ENQUIRY_CONTEXT, MAPApplicationContextVersion.VERSION_3));
                        }
                    case ANY_TIME_INFOHANDLING_CONTEXT:
                        if (version == 3) {
                            return this.createMAPAnyTimeInfoHandlingDialogue(indication.getDialogue());
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.ANY_TIME_INFOHANDLING_CONTEXT, MAPApplicationContextVersion.VERSION_3));
                        }
                    case LOCATION_CANCELLATION_CONTEXT:
                        if (version >= 1 && version <= 3) {
                            return this.createMAPLocationCancellationDialogue(indication.getDialogue(), mapApplicationContext);
                        }
                        throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s", mapApplicationContext),
                                new MAPApplicationContextImpl(MAPApplicationContextName.LOCATION_CANCELLATION_CONTEXT, MAPApplicationContextVersion.VERSION_3));
                    case SUBSCRIBER_INFO_ENQUIRY_CONTEXT:
                        //This application-context is v3 only.
                        if (version == 3) {
                            return this.createMAPSubscriberInformationEnquiryDialogue(indication.getDialogue());
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.SUBSCRIBER_INFO_ENQUIRY_CONTEXT, MAPApplicationContextVersion.VERSION_3));

                        }
                    case EQUIPMENT_MGNT_CONTEXT:
                        if (version >= 1 && version <= 3) {
                            return this.createMAPEquipmentManagementDialogue(indication.getDialogue(), mapApplicationContext);
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.EQUIPMENT_MGNT_CONTEXT, MAPApplicationContextVersion.VERSION_3));
                        }
                    case MM_EVENT_REPORTING_CONTEXT:
                        //This application-context is v3 only.
                        if (version == 3) {
                            return this.createMAPMMEventReportingDialogue(indication.getDialogue());
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.MM_EVENT_REPORTING_CONTEXT, MAPApplicationContextVersion.VERSION_3));
                        }
                    case ERI_MOBILITY_MNGT_IN:
                        if (version == 2) {
                            return this.createMAPEricssonMngtINTriggeringDialogue(indication.getDialogue());
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.ERI_MOBILITY_MNGT_IN, MAPApplicationContextVersion.VERSION_2));
                        }
                    case MS_PURGING_CONTEXT:
                        if (version == 2 || version == 3) {
                            return this.createMAPPurgeMSDialogue(indication.getDialogue(), mapApplicationContext);
                        } else {
                            throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext),
                                    new MAPApplicationContextImpl(MAPApplicationContextName.MS_PURGING_CONTEXT, MAPApplicationContextVersion.VERSION_3));
                        }
                    default:
                        throw new UnsupportedMapApplicationContext(String.format("Unsupported MAP version received. %s.", mapApplicationContext));
                }
            } catch (UnsupportedMapApplicationContext ex) {
                logger.error("AC not supported. Abort dialogue" + indication.getDialogue());
                TCUAbort tcUAbort = new TCUAbort();
                tcUAbort.setAbortReason(AbortReason.APPLICATION_CONTEXT_NAME_NOT_SUPPORTED);
                if (ex.getAlternativeAc() != null) {
                    tcUAbort.setApplicationContextName(new ApplicationContextImpl(ex.getAlternativeAc().getOid()));
                } else {
                    tcUAbort.setApplicationContextName(indication.getApplicationContextName());
                }
                indication.getDialogue().sendUAbort(tcUAbort);
            } catch (Exception ex) {
                logger.error("Internal exception occured. Abort dialogue" + indication.getDialogue(), ex);
                TCUAbort tcUAbort = new TCUAbort();
                tcUAbort.setAbortReason(AbortReason.NO_REASON_GIVEN);
                indication.getDialogue().sendUAbort(tcUAbort);
            }
        } else if (indication.getComponents() != null
                && indication.getComponents().size() > 0
                && (indication.getComponents().get(0) instanceof TCInvoke)) {

            if (logger.isDebugEnabled()) {
                logger.debug("MAP Version 1 message received");
            }

            List<Operation> components = indication.getComponents();

            //All invocations must be in context of selected ApplicationContext
            //because we must could getDialogue any invocation and derive ApplicationContext
            TCInvoke invoke = (TCInvoke) components.get(0);
            MAPDialogue mapDialogue = this.deriveV1AC(invoke.getOperationCode(), indication.getDialogue());

            if (mapDialogue == null) {
                TCUAbort tcUAbort = new TCUAbort();
//                tcUAbort.setDialogue(indication.getDialogue());
                tcUAbort.setAbortReason(AbortReason.NULL);
                indication.getDialogue().sendUAbort(tcUAbort);
                logger.error("Can not derive MAP V1 Dialogue: " + indication.getDialogue());
            } else {
                return mapDialogue;
            }

        } else {
            logger.error("Dialogue V1 with empty Component list received. Abort dialogue" + this);

            TCUAbort tcUAbort = new TCUAbort();
//            tcUAbort.setDialogue(indication.getDialogue());
            tcUAbort.setAbortReason(AbortReason.NULL);
            indication.getDialogue().sendUAbort(tcUAbort);
        }
        return null;
    }

    private MAPDialogue deriveV1AC(Integer operation, TCAPDialogue tcapDialogue) {
        OperationCodes operationCode = OperationCodes.getInstance(operation);
        switch (operationCode) {
            case UPDATE_LOCATION:
                return this.createMAPNetworkLocationUpdateDialogue(tcapDialogue,
                        new MAPApplicationContextImpl(MAPApplicationContextName.NETWORK_LOCUP_CONTEXT,
                                MAPApplicationContextVersion.VERSION_1));
            case CANCEL_LOCATION:
                return this.createMAPLocationCancellationDialogue(tcapDialogue,
                        new MAPApplicationContextImpl(MAPApplicationContextName.LOCATION_CANCELLATION_CONTEXT,
                                MAPApplicationContextVersion.VERSION_1));
            case PROVIDE_ROAMING_NUMBER:
                return this.createMAPRoamingNumberEnquiryDialogue(tcapDialogue,
                        new MAPApplicationContextImpl(MAPApplicationContextName.ROAMING_NUMBER_ENQUIRY_CONTEXT,
                                MAPApplicationContextVersion.VERSION_1));
            case INSERT_SUBSCRIBER_DATA:
            case DELETE_SUBSCRIBER_DATA:
                return this.createMAPNetworkLocationUpdateDialogue(tcapDialogue,
                        new MAPApplicationContextImpl(MAPApplicationContextName.SUBSCRIBER_DATA_MGNT_CONTEXT,
                                MAPApplicationContextVersion.VERSION_1));
            //subscriberDataMngtPackage-v1
//                return null;
            case BEGIN_SUBSCRIBER_ACTIVITY:
                return this.createMAPUssdDialogue(tcapDialogue,
                        new MAPApplicationContextImpl(MAPApplicationContextName.NETWORK_FUNCTIONAL_SS_CONTEXT,
                                MAPApplicationContextVersion.VERSION_1));
            //networkFunctionalSsContext
//                return null;
            case SEND_ROUTING_INFO:
                return this.createMAPLocationInfoRetrievalDialogue(tcapDialogue,
                        new MAPApplicationContextImpl(MAPApplicationContextName.LOCATION_INFO_RETRIEVAL_CONTEXT,
                                MAPApplicationContextVersion.VERSION_1));
            case PERFORM_HANDOVER:
//                return new MAPApplicationContextImpl(MAPApplicationContextName.HANDOVER_CONTROL_CONTEXT, MAPApplicationContextVersion.VERSION_1);
                return null;
            case RESET:
//                return new MAPApplicationContextImpl(MAPApplicationContextName.RESET_CONTEXT, MAPApplicationContextVersion.VERSION_1);
                return null;
            case ACTIVATE_TRACE_MODE:
            case DEACTIVATE_TRACE_MODE:
//                return new MAPApplicationContextImpl(MAPApplicationContextName.TRACING_CONTEXT, MAPApplicationContextVersion.VERSION_1);
                return null;
            case SEND_ROUTING_INFO_FOR_SM:
            case REPORT_SM_DELIVERY_STATUS:
                return this.createMAPShortMessageGatewayDialogue(tcapDialogue,
                        new MAPApplicationContextImpl(MAPApplicationContextName.SHORT_MSG_GATEWAY_CONTEXT,
                                MAPApplicationContextVersion.VERSION_1));
            case FORWARD_SM:
                return this.createMAPShortMessageRelayDialogue(tcapDialogue,
                        new MAPApplicationContextImpl(MAPApplicationContextName.SHORT_MSG_MO_RELAY_CONTEXT,
                                MAPApplicationContextVersion.VERSION_1));
            case NOTE_SUBSCRIBER_PRESENT:
//                return  new MAPApplicationContextImpl(MAPApplicationContextName.MWD_MGNT_CONTEXT, MAPApplicationContextVersion.VERSION_1);
                return null;
            case CHECK_IMEI:
                return this.createMAPEquipmentManagementDialogue(tcapDialogue,
                        new MAPApplicationContextImpl(MAPApplicationContextName.EQUIPMENT_MGNT_CONTEXT,
                                MAPApplicationContextVersion.VERSION_1));
            case ALERT_SERVICE_CENTER_WITHOUT_RESULT:
                return this.createMAPAlertingDialogue(tcapDialogue,
                        new MAPApplicationContextImpl(MAPApplicationContextName.SHORT_MSG_ALERT_CONTEXT, MAPApplicationContextVersion.VERSION_1));
            default:
                return null;
        }
    }

}
