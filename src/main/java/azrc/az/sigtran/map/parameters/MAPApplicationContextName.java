/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import azrc.az.sigtran.map.OperationCodes;

/**
 *
 * @author root
 */
public enum MAPApplicationContextName {

    NETWORK_LOCUP_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 1, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case UPDATE_LOCATION:
                case FORWARD_CHECK_SS_INDICATION:
                case INSERT_SUBSCRIBER_DATA:
                case ACTIVATE_TRACE_MODE:
                    if (version == MAPApplicationContextVersion.VERSION_4) {
                        return false;
                    }
                    return true;
                case SEND_PARAMETERS:
                    return version == MAPApplicationContextVersion.VERSION_1;
                case RESTORE_DATA:
                    return version == MAPApplicationContextVersion.VERSION_2
                            || version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 1;
        }

    },
    LOCATION_CANCELLATION_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 2, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.CANCEL_LOCATION
                    && (version == MAPApplicationContextVersion.VERSION_1
                    || version == MAPApplicationContextVersion.VERSION_2
                    || version == MAPApplicationContextVersion.VERSION_3);
        }

        @Override
        public int getOpCode() {
            return 2;
        }
    },
    ROAMING_NUMBER_ENQUIRY_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 3, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.PROVIDE_ROAMING_NUMBER
                    && (version == MAPApplicationContextVersion.VERSION_1
                    || version == MAPApplicationContextVersion.VERSION_2
                    || version == MAPApplicationContextVersion.VERSION_3);
        }

        @Override
        public int getOpCode() {
            return 3;
        }
    },
    LOCATION_INFO_RETRIEVAL_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 5, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.SEND_ROUTING_INFO
                    && (version == MAPApplicationContextVersion.VERSION_1
                    || version == MAPApplicationContextVersion.VERSION_2
                    || version == MAPApplicationContextVersion.VERSION_3);
        }

        @Override
        public int getOpCode() {
            return 5;
        }
    },
    CALL_CONTROL_TRANSFER_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 6, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.RESUME_CALL_HANDLING
                    && (version == MAPApplicationContextVersion.VERSION_3
                    || version == MAPApplicationContextVersion.VERSION_4);
        }

        @Override
        public int getOpCode() {
            return 6;
        }
    },
    RESET_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 10, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.RESET
                    && (version == MAPApplicationContextVersion.VERSION_1
                    || version == MAPApplicationContextVersion.VERSION_2);
        }

        @Override
        public int getOpCode() {
            return 10;
        }
    },
    HANDOVER_CONTROL_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 11, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case PERFORM_HANDOVER:
                    return version == MAPApplicationContextVersion.VERSION_1;
                case PREPARE_HANDOVER:
                    return version == MAPApplicationContextVersion.VERSION_2
                            || version == MAPApplicationContextVersion.VERSION_3;
                case FORWARD_ACCESS_SIGNALLING:
                    return version == MAPApplicationContextVersion.VERSION_1
                            || version == MAPApplicationContextVersion.VERSION_2
                            || version == MAPApplicationContextVersion.VERSION_3;
                case TRACE_SUBSCRIBER_ACTIVITY:
                    return version == MAPApplicationContextVersion.VERSION_1;
                case SEND_END_SIGNAL:
                    return version == MAPApplicationContextVersion.VERSION_1
                            || version == MAPApplicationContextVersion.VERSION_2
                            || version == MAPApplicationContextVersion.VERSION_3;
                case NOTE_INTERNAL_HANDOVER:
                    return version == MAPApplicationContextVersion.VERSION_1;
                case PROCESS_ACCESS_SIGNALLING:
                    return version == MAPApplicationContextVersion.VERSION_1
                            || version == MAPApplicationContextVersion.VERSION_2
                            || version == MAPApplicationContextVersion.VERSION_3;
                case PERFORM_SUBSEQUENT_HANDOVER:
                    return version == MAPApplicationContextVersion.VERSION_1;
                case PREPARE_SUBSEQUENT_HANDOVER:
                    return version == MAPApplicationContextVersion.VERSION_2
                            || version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 11;
        }
    },
    IMSI_RETRIEVAL_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 26, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.SEND_IMSI && version == MAPApplicationContextVersion.VERSION_2;
        }

        @Override
        public int getOpCode() {
            return 26;
        }
    },
    EQUIPMENT_MGNT_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 13, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.CHECK_IMEI
                    && (version == MAPApplicationContextVersion.VERSION_1
                    || version == MAPApplicationContextVersion.VERSION_2
                    || version == MAPApplicationContextVersion.VERSION_3);
        }

        @Override
        public int getOpCode() {
            return 13;
        }
    },
    INFO_RETRIEVAL_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 14, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case SEND_PARAMETERS:
                    return version == MAPApplicationContextVersion.VERSION_1;
                case SEND_AUTHENTICATION_INFO:
                    return version == MAPApplicationContextVersion.VERSION_2
                            || version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 14;
        }
    },
    INTER_VLR_INFO_RETRIEVAL_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 15, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case SEND_PARAMETERS:
                    return version == MAPApplicationContextVersion.VERSION_1;
                case SEND_IDENTIFICATION:
                    return version == MAPApplicationContextVersion.VERSION_2
                            || version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 15;
        }
    },
    SUBSCRIBER_DATA_MGNT_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 16, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case INSERT_SUBSCRIBER_DATA:
                case DELETE_SUBSCRIBER_DATA:
                    return version == MAPApplicationContextVersion.VERSION_1
                            || version == MAPApplicationContextVersion.VERSION_2
                            || version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 16;
        }
    },
    TRACING_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 17, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case ACTIVATE_TRACE_MODE:
                case DEACTIVATE_TRACE_MODE:
                    return version == MAPApplicationContextVersion.VERSION_1
                            || version == MAPApplicationContextVersion.VERSION_2
                            || version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 17;
        }
    },
    NETWORK_FUNCTIONAL_SS_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 18, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case REGISTER_SS:
                case ERASE_SS:
                case ACTIVATE_SS:
                case DEACTIVATE_SS:
                case REGISTER_PASSWORD:
                case INTERROGATE_SS:
                case GET_PASSWORD:
                    return version == MAPApplicationContextVersion.VERSION_1
                            || version == MAPApplicationContextVersion.VERSION_2;
                case PROCESS_UNSTRUCTURED_SS_DATA:
                case BEGIN_SUBSCRIBER_ACTIVITY:
                    return version == MAPApplicationContextVersion.VERSION_1;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 18;
        }
    },
    NETWORK_UNSTRUCTURED_SS_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 19, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case PROCESS_UNSTRUCTURED_SS_DATA:
                    return version == MAPApplicationContextVersion.VERSION_1;
                case PROCESS_UNSTRUCTURED_SS_REQUEST:
                case UNSTRUCTURED_SS_REQUEST:
                case UNSTRUCTURED_SS_NOTIFY:
                    return version == MAPApplicationContextVersion.VERSION_2;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 19;
        }
    },
    SHORT_MSG_GATEWAY_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 20, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case SEND_ROUTING_INFO_FOR_SM:
                case REPORT_SM_DELIVERY_STATUS:
                    return version == MAPApplicationContextVersion.VERSION_1
                            || version == MAPApplicationContextVersion.VERSION_2
                            || version == MAPApplicationContextVersion.VERSION_3;
                case INFORM_SERVICE_CENTRE:
                    return version == MAPApplicationContextVersion.VERSION_2
                            || version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 20;
        }
    },
    SHORT_MSG_MO_RELAY_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 21, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case FORWARD_SM://MO-FORWARD-SM uses same operation code with FORWARD-SM
                    return version == MAPApplicationContextVersion.VERSION_1
                            || version == MAPApplicationContextVersion.VERSION_2
                            || version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 21;
        }
    },
    SHORT_MSG_MT_RELAY_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 25, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case FORWARD_SM:
                    return version == MAPApplicationContextVersion.VERSION_1
                            || version == MAPApplicationContextVersion.VERSION_2;
                case MT_FORWARD_SM:
                    return version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 25;
        }
    },
    SUBSCRIBER_DATA_MODIFICATION_NOTIFICATION_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 22, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.NOTE_SUBSCRIBER_DATA_MODIFIED && version == MAPApplicationContextVersion.VERSION_3;
        }

        @Override
        public int getOpCode() {
            return 22;
        }
    },
    SHORT_MSG_ALERT_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 23, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case ALERT_SERVICE_CENTER_WITHOUT_RESULT:
                    return version == MAPApplicationContextVersion.VERSION_1;
                case ALERT_SERVICE_CENTRE:
                    return version == MAPApplicationContextVersion.VERSION_2;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 23;
        }
    },
    MWD_MGNT_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 24, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case NOTE_SUBSCRIBER_PRESENT:
                    return version == MAPApplicationContextVersion.VERSION_1;
                case READY_FOR_SM:
                    return version == MAPApplicationContextVersion.VERSION_2
                            || version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 24;
        }
    },
    MS_PURGING_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 27, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.PURGE_MS && (version == MAPApplicationContextVersion.VERSION_2
                    || version == MAPApplicationContextVersion.VERSION_3);
        }

        @Override
        public int getOpCode() {
            return 27;
        }
    },
    SUBSCRIBER_INFO_ENQUIRY_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 28, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.PROVIDE_SUBSCRIBER_INFO
                    && version == MAPApplicationContextVersion.VERSION_3;
        }

        @Override
        public int getOpCode() {
            return 28;
        }
    },
    ANY_TIME_ENQUIRY_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 29, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.ANY_TIME_INTERROGATION && version == MAPApplicationContextVersion.VERSION_3;
        }

        @Override
        public int getOpCode() {
            return 29;
        }
    },
    GROUP_CALL_CONTROL_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 31, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case PREPARE_GROUP_CALL:
                case PROCESS_GROUP_CALL_SIGNALLING:
                case FORWARD_GROUP_CALL_SIGNALLING:
                case SEND_GROUP_CALL_END_SIGNAL:
                    return version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 31;
        }
    },
    GROUP_CALL_INFO_RET_CONTROL_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 45, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.SEND_GROUP_CALL_INFO && version == MAPApplicationContextVersion.VERSION_3;
        }

        @Override
        public int getOpCode() {
            return 45;
        }
    },
    GPRS_LOCATION_UPDATE_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 32, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case UPDATE_GPRS_LOCATION:
                case INSERT_SUBSCRIBER_DATA:
                case ACTIVATE_TRACE_MODE:
                    return version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 32;
        }
    },
    GPRS_LOCATION_INFO_RETRIEVAL_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 33, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.SEND_ROUTING_INFO_FOR_GPRS
                    && (version == MAPApplicationContextVersion.VERSION_3 || version == MAPApplicationContextVersion.VERSION_4);
        }

        @Override
        public int getOpCode() {
            return 33;
        }
    },
    FAILURE_REPORT_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 34, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.FAILURE_REPORT && version == MAPApplicationContextVersion.VERSION_3;
        }

        @Override
        public int getOpCode() {
            return 34;
        }
    },
    GPRS_NOTIFY_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 35, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.NOTE_MS_PRESENT_FOR_GPRS && version == MAPApplicationContextVersion.VERSION_3;
        }

        @Override
        public int getOpCode() {
            return 35;
        }
    },
    SS_INVOCATION_NOTIFICATION_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 36, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.SS_INVOCATION_NOTIFICATION && version == MAPApplicationContextVersion.VERSION_3;
        }

        @Override
        public int getOpCode() {
            return 36;
        }
    },
    REPORTING_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 7, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case SET_REPORTING_STATE:
                case STATUS_REPORT:
                case REMOTE_USER_FREE:
                    return version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 7;
        }
    },
    CALL_COMPLETION_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 8, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case REGISTER_CC_ENTRY:
                case ERASE_CC_ENTRY:
                    return version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 8;
        }
    },
    LOCATION_SVC_GATEWAY_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 37, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.SEND_ROUTING_INFO_FOR_LCS && version == MAPApplicationContextVersion.VERSION_3;
        }

        @Override
        public int getOpCode() {
            return 37;
        }
    },
    LOCATION_SVC_ENQUIRY_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 38, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case PROVIDE_SUBSCRIBER_LOCATION:
                case SUBSCRIBER_LOCATION_REPORT:
                    return version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 38;
        }
    },
    IST_ALERTING_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 4, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.IST_ALERT && version == MAPApplicationContextVersion.VERSION_3;
        }

        @Override
        public int getOpCode() {
            return 4;
        }
    },
    SERVICE_TERMINATION_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 9, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.IST_COMMAND && version == MAPApplicationContextVersion.VERSION_3;
        }

        @Override
        public int getOpCode() {
            return 9;
        }
    },
    MM_EVENT_REPORTING_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 42, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.NOTE_MM_EVENT && version == MAPApplicationContextVersion.VERSION_3;
        }

        @Override
        public int getOpCode() {
            return 42;
        }
    },
    ANY_TIME_INFOHANDLING_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 43, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            switch (opCode) {
                case ANY_TIME_SUBSCRIPTION_INTERROGATION:
                case ANY_TIME_MODIFICATION:
                    return version == MAPApplicationContextVersion.VERSION_3;
                default:
                    return false;
            }
        }

        @Override
        public int getOpCode() {
            return 43;
        }
    },
    AUTHENTICATION_FAILURE_REPORTING_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 39, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.AUTHENTICATION_FAILURE_REPORT && version == MAPApplicationContextVersion.VERSION_3;
        }

        @Override
        public int getOpCode() {
            return 39;
        }
    },
    RESOURCE_MANAGEMENT_CONTET(new long[]{0, 4, 0, 0, 1, 0, 44, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.RELEASE_RESOURCES && version == MAPApplicationContextVersion.VERSION_3;
        }

        @Override
        public int getOpCode() {
            return 44;
        }
    },
    SHORT_MSG_MT_RELAY_VGCS_CONTEXT(new long[]{0, 4, 0, 0, 1, 0, 41, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.MT_FORWARD_SM_VGSS && version == MAPApplicationContextVersion.VERSION_3;
        }

        @Override
        public int getOpCode() {
            return 41;
        }
    },
    ERI_MOBILITY_MNGT_IN(new long[]{1, 2, 826, 0, 1249, 58, 1, 1, 3, 0}) {
        @Override
        public boolean isUserInformationRequired() {
            return false;
        }

        @Override
        public boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version) {
            return opCode == OperationCodes.MOBILITY_MNGT_IN_TRIGGERING && version == MAPApplicationContextVersion.VERSION_2;
        }

        @Override
        public int getOpCode() {
            return 3;
        }

    };

    private final long[] value;

    private MAPApplicationContextName(long[] value) {
        this.value = value;
    }

    private static boolean isOidsEquals(long[] oid1, long[] oid2) {
        if (oid1 == oid2) {
            return true;
        }
        if (oid1 == null || oid2 == null) {
            return false;
        }
        for (int i = 0; i < oid1.length; i++) {
            if (!(oid1[i] == oid2[i])) {
                return false;
            }
        }
        return true;
    }

    public static MAPApplicationContextName getInstance(long[] oid) {
        //Standart 3GPP MAP AC
        if (isOidsEquals(_3GPP_MAP_ACS_OID, oid)) {
            int ac = (int) oid[6];
            switch (ac) {
                case 1:
                    return NETWORK_LOCUP_CONTEXT;
                case 2:
                    return LOCATION_CANCELLATION_CONTEXT;
                case 3:
                    return ROAMING_NUMBER_ENQUIRY_CONTEXT;
                case 5:
                    return LOCATION_INFO_RETRIEVAL_CONTEXT;
                case 6:
                    return CALL_CONTROL_TRANSFER_CONTEXT;
                case 10:
                    return RESET_CONTEXT;
                case 11:
                    return HANDOVER_CONTROL_CONTEXT;
                case 26:
                    return IMSI_RETRIEVAL_CONTEXT;
                case 13:
                    return EQUIPMENT_MGNT_CONTEXT;
                case 14:
                    return INFO_RETRIEVAL_CONTEXT;
                case 15:
                    return INTER_VLR_INFO_RETRIEVAL_CONTEXT;
                case 16:
                    return SUBSCRIBER_DATA_MGNT_CONTEXT;
                case 17:
                    return TRACING_CONTEXT;
                case 18:
                    return NETWORK_FUNCTIONAL_SS_CONTEXT;
                case 19:
                    return NETWORK_UNSTRUCTURED_SS_CONTEXT;
                case 20:
                    return SHORT_MSG_GATEWAY_CONTEXT;
                case 21:
                    return SHORT_MSG_MO_RELAY_CONTEXT;
                case 22:
                    return SUBSCRIBER_DATA_MODIFICATION_NOTIFICATION_CONTEXT;
                case 23:
                    return SHORT_MSG_ALERT_CONTEXT;
                case 24:
                    return MWD_MGNT_CONTEXT;
                case 25:
                    return SHORT_MSG_MT_RELAY_CONTEXT;
                case 27:
                    return MS_PURGING_CONTEXT;
                case 28:
                    return SUBSCRIBER_INFO_ENQUIRY_CONTEXT;
                case 29:
                    return ANY_TIME_ENQUIRY_CONTEXT;
                case 31:
                    return GROUP_CALL_CONTROL_CONTEXT;
                case 45:
                    return GROUP_CALL_INFO_RET_CONTROL_CONTEXT;
                case 32:
                    return GPRS_LOCATION_UPDATE_CONTEXT;
                case 33:
                    return GPRS_LOCATION_INFO_RETRIEVAL_CONTEXT;
                case 34:
                    return FAILURE_REPORT_CONTEXT;
                case 35:
                    return GPRS_NOTIFY_CONTEXT;
                case 36:
                    return SS_INVOCATION_NOTIFICATION_CONTEXT;
                case 7:
                    return REPORTING_CONTEXT;
                case 8:
                    return CALL_COMPLETION_CONTEXT;
                case 37:
                    return LOCATION_SVC_GATEWAY_CONTEXT;
                case 38:
                    return LOCATION_SVC_ENQUIRY_CONTEXT;
                case 4:
                    return IST_ALERTING_CONTEXT;
                case 9:
                    return SERVICE_TERMINATION_CONTEXT;
                case 42:
                    return MM_EVENT_REPORTING_CONTEXT;
                case 43:
                    return ANY_TIME_INFOHANDLING_CONTEXT;
                case 39:
                    return AUTHENTICATION_FAILURE_REPORTING_CONTEXT;
                case 44:
                    return RESOURCE_MANAGEMENT_CONTET;
                case 41:
                    return SHORT_MSG_MT_RELAY_VGCS_CONTEXT;
                default:
                    return null;
            }
        } else if (isOidsEquals(ERICSSON_MAP_ACS_OID, oid)) {
            int ac = (int) oid[8];
            switch (ac) {
                case 3:
                    return ERI_MOBILITY_MNGT_IN;
                default:
                    return null;
            }
        }

        return null;
    }

    public abstract boolean isUserInformationRequired();

    public abstract boolean isOperationSupported(OperationCodes opCode, MAPApplicationContextVersion version);

    public abstract int getOpCode();

    public long[] value() {
        return this.value;
    }
    public static final long[] _3GPP_MAP_ACS_OID = new long[]{0, 4, 0, 0, 1, 0};
    public static final long[] ERICSSON_MAP_ACS_OID = new long[]{1, 2, 826, 0, 1249, 58, 1, 1};
}
