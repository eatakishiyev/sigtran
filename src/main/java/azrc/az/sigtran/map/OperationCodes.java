/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map;

import azrc.az.sigtran.tcap.parameters.OperationCodeType;
import azrc.az.sigtran.tcap.primitives.tc.OperationClass;

/**
 *
 * @author root
 */
public enum OperationCodes {

    UPDATE_LOCATION(2, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CANCEL_LOCATION(3, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PURGE_MS(67, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SEND_IDENTIFICATION(55, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    UPDATE_GPRS_LOCATION(23, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PROVIDE_SUBSCRIBER_INFO(70, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    ANY_TIME_INTERROGATION(71, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    ANY_TIME_SUBSCRIPTION_INTERROGATION(62, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    ANY_TIME_MODIFICATION(65, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    NOTE_SUBSCRIBER_DATA_MODIFIED(5, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PREPARE_HANDOVER(68, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SEND_END_SIGNAL(29, OperationCodeType.LOCAL, OperationClass.CLASS3) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PROCESS_ACCESS_SIGNALLING(33, OperationCodeType.LOCAL, OperationClass.CLASS4) {
        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    FORWARD_ACCESS_SIGNALLING(34, OperationCodeType.LOCAL, OperationClass.CLASS4) {
        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PREPARE_SUBSEQUENT_HANDOVER(69, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SEND_AUTHENTICATION_INFO(56, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    AUTHENTICATION_FAILURE_REPORT(15, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CHECK_IMEI(43, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    INSERT_SUBSCRIBER_DATA(7, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    DELETE_SUBSCRIBER_DATA(8, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    RESET(37, OperationCodeType.LOCAL, OperationClass.CLASS4) {
        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    FORWARD_CHECK_SS_INDICATION(38, OperationCodeType.LOCAL, OperationClass.CLASS4) {
        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    RESTORE_DATA(57, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SEND_ROUTING_INFO_FOR_GPRS(24, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    FAILURE_REPORT(25, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    NOTE_MS_PRESENT_FOR_GPRS(26, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    NOTE_MM_EVENT(89, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    ACTIVATE_TRACE_MODE(50, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    DEACTIVATE_TRACE_MODE(51, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SEND_IMSI(58, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SEND_ROUTING_INFO(22, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PROVIDE_ROAMING_NUMBER(4, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    RESUME_CALL_HANDLING(6, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SET_REPORTING_STATE(73, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    STATUS_REPORT(74, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    REMOTE_USER_FREE(75, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    IST_ALERT(87, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    IST_COMMAND(88, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    RELEASE_RESOURCES(20, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    REGISTER_SS(10, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    ERASE_SS(11, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    ACTIVATE_SS(12, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    DEACTIVATE_SS(13, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    INTERROGATE_SS(14, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PROCESS_UNSTRUCTURED_SS_REQUEST(59, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    UNSTRUCTURED_SS_REQUEST(60, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    UNSTRUCTURED_SS_NOTIFY(61, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    REGISTER_PASSWORD(17, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return opCode == GET_PASSWORD;
        }

    },
    GET_PASSWORD(18, OperationCodeType.LOCAL, OperationClass.CLASS3) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SS_INVOCATION_NOTIFICATION(72, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    REGISTER_CC_ENTRY(76, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    ERASE_CC_ENTRY(77, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SEND_ROUTING_INFO_FOR_SM(45, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    FORWARD_SM(46, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    MT_FORWARD_SM(44, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    REPORT_SM_DELIVERY_STATUS(47, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    ALERT_SERVICE_CENTRE(64, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    INFORM_SERVICE_CENTRE(63, OperationCodeType.LOCAL, OperationClass.CLASS4) {
        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    MT_FORWARD_SM_VGSS(21, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    READY_FOR_SM(66, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PREPARE_GROUP_CALL(39, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SEND_GROUP_CALL_END_SIGNAL(40, OperationCodeType.LOCAL, OperationClass.CLASS3) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PROCESS_GROUP_CALL_SIGNALLING(41, OperationCodeType.LOCAL, OperationClass.CLASS4) {
        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    FORWARD_GROUP_CALL_SIGNALLING(42, OperationCodeType.LOCAL, OperationClass.CLASS4) {
        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SEND_GROUP_CALL_INFO(84, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SEND_ROUTING_INFO_FOR_LCS(85, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PROVIDE_SUBSCRIBER_LOCATION(83, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SUBSCRIBER_LOCATION_REPORT(86, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SEND_PARAMETERS(9, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PROCESS_UNSTRUCTURED_SS_DATA(19, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PERFORM_HANDOVER(28, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PERFORM_SUBSEQUENT_HANDOVER(30, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PROVIDE_SIWFS_NUMBER(31, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SIWFS_SIGNALLING_MODIFY(32, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    NOTE_INTERNAL_HANDOVER(35, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    NOTE_SUBSCRIBER_PRESENT(48, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    ALERT_SERVICE_CENTER_WITHOUT_RESULT(49, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    TRACE_SUBSCRIBER_ACTIVITY(52, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    BEGIN_SUBSCRIBER_ACTIVITY(54, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    /**
     * Ericsson proprietary op code to handle MM events 
     */
    MOBILITY_MNGT_IN_TRIGGERING(-8, OperationCodeType.LOCAL, OperationClass.CLASS1) {//248
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    UNKNOWN(-1, OperationCodeType.LOCAL, OperationClass.CLASS1) {
        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    };
//    
    private final int value;
    private final OperationCodeType operationCodeType;
    private final OperationClass operationClass;

    OperationCodes(int operationCode, OperationCodeType operationCodeType, OperationClass operationClass) {
        this.value = operationCode;
        this.operationCodeType = operationCodeType;
        this.operationClass = operationClass;
    }

    public abstract boolean isConfirmed();

    public abstract boolean isLinkedOperationAllowed(OperationCodes opCode);

    public int value() {
        return this.value;
    }

    public OperationCodeType getOperationCodeType() {
        return this.operationCodeType;
    }

    public OperationClass operationClass() {
        return this.operationClass;
    }

    public static OperationCodes getInstance(int opCode) {
        switch (opCode) {
            case 2:
                return UPDATE_LOCATION;
            case 3:
                return CANCEL_LOCATION;
            case 67:
                return PURGE_MS;
            case 55:
                return SEND_IDENTIFICATION;
            case 23:
                return UPDATE_GPRS_LOCATION;
            case 70:
                return PROVIDE_SUBSCRIBER_INFO;
            case 71:
                return ANY_TIME_INTERROGATION;
            case 62:
                return ANY_TIME_SUBSCRIPTION_INTERROGATION;
            case 65:
                return ANY_TIME_MODIFICATION;
            case 5:
                return NOTE_SUBSCRIBER_DATA_MODIFIED;
            case 68:
                return PREPARE_HANDOVER;
            case 29:
                return SEND_END_SIGNAL;
            case 33:
                return PROCESS_ACCESS_SIGNALLING;
            case 34:
                return FORWARD_ACCESS_SIGNALLING;
            case 69:
                return PREPARE_SUBSEQUENT_HANDOVER;
            case 56:
                return SEND_AUTHENTICATION_INFO;
            case 15:
                return AUTHENTICATION_FAILURE_REPORT;
            case 43:
                return CHECK_IMEI;
            case 7:
                return INSERT_SUBSCRIBER_DATA;
            case 8:
                return DELETE_SUBSCRIBER_DATA;
            case 37:
                return RESET;
            case 38:
                return FORWARD_CHECK_SS_INDICATION;
            case 57:
                return RESTORE_DATA;
            case 24:
                return SEND_ROUTING_INFO_FOR_GPRS;
            case 25:
                return FAILURE_REPORT;
            case 26:
                return NOTE_MS_PRESENT_FOR_GPRS;
            case 89:
                return NOTE_MM_EVENT;
            case 50:
                return ACTIVATE_TRACE_MODE;
            case 51:
                return DEACTIVATE_TRACE_MODE;
            case 58:
                return SEND_IMSI;
            case 22:
                return SEND_ROUTING_INFO;
            case 4:
                return PROVIDE_ROAMING_NUMBER;
            case 6:
                return RESUME_CALL_HANDLING;
            case 73:
                return SET_REPORTING_STATE;
            case 74:
                return STATUS_REPORT;
            case 75:
                return REMOTE_USER_FREE;
            case 87:
                return IST_ALERT;
            case 88:
                return IST_COMMAND;
            case 20:
                return RELEASE_RESOURCES;
            case 10:
                return REGISTER_SS;
            case 11:
                return ERASE_SS;
            case 12:
                return ACTIVATE_SS;
            case 13:
                return DEACTIVATE_SS;
            case 14:
                return INTERROGATE_SS;
            case 59:
                return PROCESS_UNSTRUCTURED_SS_REQUEST;
            case 60:
                return UNSTRUCTURED_SS_REQUEST;
            case 61:
                return UNSTRUCTURED_SS_NOTIFY;
            case 17:
                return REGISTER_PASSWORD;
            case 18:
                return GET_PASSWORD;
            case 72:
                return SS_INVOCATION_NOTIFICATION;
            case 76:
                return REGISTER_CC_ENTRY;
            case 77:
                return ERASE_CC_ENTRY;
            case 44:
                return MT_FORWARD_SM;
            case 45:
                return SEND_ROUTING_INFO_FOR_SM;
            case 46:
                return FORWARD_SM;
            case 47:
                return REPORT_SM_DELIVERY_STATUS;
            case 64:
                return ALERT_SERVICE_CENTRE;
            case 63:
                return INFORM_SERVICE_CENTRE;
            case 21:
                return MT_FORWARD_SM_VGSS;
            case 66:
                return READY_FOR_SM;
            case 39:
                return PREPARE_GROUP_CALL;
            case 40:
                return SEND_GROUP_CALL_END_SIGNAL;
            case 41:
                return PROCESS_GROUP_CALL_SIGNALLING;
            case 42:
                return FORWARD_GROUP_CALL_SIGNALLING;
            case 84:
                return SEND_GROUP_CALL_INFO;
            case 85:
                return SEND_ROUTING_INFO_FOR_LCS;
            case 83:
                return PROVIDE_SUBSCRIBER_LOCATION;
            case 86:
                return SUBSCRIBER_LOCATION_REPORT;
            case 9:
                return SEND_PARAMETERS;
            case 19:
                return PROCESS_UNSTRUCTURED_SS_DATA;
            case 28:
                return PERFORM_HANDOVER;
            case 30:
                return PERFORM_SUBSEQUENT_HANDOVER;
            case 31:
                return PROVIDE_SIWFS_NUMBER;
            case 32:
                return SIWFS_SIGNALLING_MODIFY;
            case 35:
                return NOTE_INTERNAL_HANDOVER;
            case 48:
                return NOTE_SUBSCRIBER_PRESENT;
            case 49:
                return ALERT_SERVICE_CENTER_WITHOUT_RESULT;
            case 52:
                return TRACE_SUBSCRIBER_ACTIVITY;
            case 54:
                return BEGIN_SUBSCRIBER_ACTIVITY;
            case 248:
            case -8://Ericsson eriMobilityMngtINContext|v2 MobilityMngtINTriggering| opcode
                return MOBILITY_MNGT_IN_TRIGGERING;
            default:
                return UNKNOWN;

        }
    }
}
