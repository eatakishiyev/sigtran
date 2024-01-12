/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap;

import azrc.az.sigtran.tcap.primitives.tc.OperationClass;

/**
 *
 * @author eatakishiyev
 */
public enum OperationCodes {

    INITIAL_DP(0) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }

    },
    ASSIST_REQUEST_INSTRUCTIONS(16) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    ESTABLISH_TEMPORARY_CONNECTION(17) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    DISCONNECT_FORWARD_CONNECTION(18) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    DISCONNECT_FORWARD_CONNECTION_WITH_ARGUMENT(86) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CONNECT_TO_RESOURCE(19) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CONNECT(20) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    RELEASE_CALL(22) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS4;
        }

        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    REQUEST_REPORT_BCSM_EVENT(23) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    EVENT_REPORT_BCSM(24) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS4;
        }

        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    COLLECT_INFORMATION(27) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CONTINUE(31) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS4;
        }

        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    INITIATE_CALL_ATTEMPT(32) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS1;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    RESET_TIMER(33) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    FURNISH_CHARGING_INFORMATION(34) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    APPLY_CHARGING(35) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    APPLY_CHARGING_REPORT(36) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CALL_GAP(41) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS4;
        }

        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CALL_INFORMATION_REPORT(44) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS4;
        }

        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CALL_INFORMATION_REQUEST(45) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SEND_CHARGING_INFORMATION(46) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PLAY_ANNOUNCEMENT(47) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return opCode == SPECIALISED_RESOURCE_REPORT;
        }
    },
    PROMPT_AND_COLLECT_USER_INFORMATION(48) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS1;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SPECIALISED_RESOURCE_REPORT(49) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS4;
        }

        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CANCEL(53) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    ACTIVITY_TEST(55) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS3;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CONTINUE_WITH_ARGUMENT(88) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    DISCONNECT_LEG(90) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS1;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    MOVE_LEG(93) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS1;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SPLIT_LEG(95) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS1;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    ENTITY_RELEASED(96) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS4;
        }

        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    PLAY_TONE(97) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    INITIAL_DP_SMS(60) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    FURNISH_CHARGING_INFORMATION_SMS(61) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CONNECT_SMS(62) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    REQUEST_REPORT_SMS_EVENT(63) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    EVENT_REPORT_SMS(64) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS4;
        }

        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CONTINUE_SMS(65) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    RELEASE_SMS(66) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS4;
        }

        @Override
        public boolean isConfirmed() {
            return false;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    RESET_TIMER_SMS(67) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    ACTIVITY_TEST_GPRS(70) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS3;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    APPLY_CHARGING_GPRS(71) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    APPLY_CHARGING_REPORT_GPRS(72) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS1;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CANCEL_GPRS(73) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CONNECT_GPRS(74) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    CONTINUE_GPRS(75) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    ENTITY_RELEASED_GPRS(76) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS1;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    FURNISH_CHARGING_INFORMATION_GPRS(77) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    INITIAL_DP_GPRS(78) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    RELEASE_GPRS(79) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    EVENT_REPORT_GPRS(80) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS1;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    REQUEST_REPORT_GPRS_EVENT(81) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    RESET_TIMER_GPRS(82) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    },
    SEND_CHARGING_INFORMATION_GPRS(83) {
        @Override
        public OperationClass getOperationClass() {
            return OperationClass.CLASS2;
        }

        @Override
        public boolean isConfirmed() {
            return true;
        }

        @Override
        public boolean isLinkedOperationAllowed(OperationCodes opCode) {
            return false;
        }
    };
    private final int value;

    public abstract boolean isLinkedOperationAllowed(OperationCodes opCode);

    public abstract boolean isConfirmed();

    private OperationCodes(int value) {
        this.value = value;
    }

    public abstract OperationClass getOperationClass();

    public static OperationCodes getInstance(int value) {
        switch (value) {
            case 0:
                return INITIAL_DP;
            case 16:
                return ASSIST_REQUEST_INSTRUCTIONS;
            case 17:
                return ESTABLISH_TEMPORARY_CONNECTION;
            case 18:
                return DISCONNECT_FORWARD_CONNECTION;
            case 86:
                return DISCONNECT_FORWARD_CONNECTION_WITH_ARGUMENT;
            case 19:
                return CONNECT_TO_RESOURCE;
            case 20:
                return CONNECT;
            case 22:
                return RELEASE_CALL;
            case 23:
                return REQUEST_REPORT_BCSM_EVENT;
            case 24:
                return EVENT_REPORT_BCSM;
            case 27:
                return COLLECT_INFORMATION;
            case 31:
                return CONTINUE;
            case 32:
                return INITIATE_CALL_ATTEMPT;
            case 33:
                return RESET_TIMER;
            case 34:
                return FURNISH_CHARGING_INFORMATION;
            case 35:
                return APPLY_CHARGING;
            case 36:
                return APPLY_CHARGING_REPORT;
            case 41:
                return CALL_GAP;
            case 44:
                return CALL_INFORMATION_REPORT;
            case 45:
                return CALL_INFORMATION_REQUEST;
            case 46:
                return SEND_CHARGING_INFORMATION;
            case 47:
                return PLAY_ANNOUNCEMENT;
            case 48:
                return PROMPT_AND_COLLECT_USER_INFORMATION;
            case 49:
                return SPECIALISED_RESOURCE_REPORT;
            case 53:
                return CANCEL;
            case 55:
                return ACTIVITY_TEST;
            case 88:
                return CONTINUE_WITH_ARGUMENT;
            case 90:
                return DISCONNECT_LEG;
            case 93:
                return MOVE_LEG;
            case 95:
                return SPLIT_LEG;
            case 96:
                return ENTITY_RELEASED;
            case 97:
                return PLAY_TONE;
            case 60:
                return INITIAL_DP_SMS;
            case 61:
                return FURNISH_CHARGING_INFORMATION_SMS;
            case 62:
                return CONNECT_SMS;
            case 63:
                return REQUEST_REPORT_SMS_EVENT;
            case 64:
                return EVENT_REPORT_SMS;
            case 65:
                return CONTINUE_SMS;
            case 66:
                return RELEASE_SMS;
            case 67:
                return RESET_TIMER_SMS;
            case 70:
                return ACTIVITY_TEST_GPRS;
            case 71:
                return APPLY_CHARGING_GPRS;
            case 72:
                return APPLY_CHARGING_REPORT_GPRS;
            case 73:
                return CANCEL_GPRS;
            case 74:
                return CONNECT_GPRS;
            case 75:
                return CONTINUE_GPRS;
            case 76:
                return ENTITY_RELEASED_GPRS;
            case 77:
                return FURNISH_CHARGING_INFORMATION_GPRS;
            case 78:
                return INITIAL_DP_GPRS;
            case 79:
                return RELEASE_GPRS;
            case 80:
                return EVENT_REPORT_GPRS;
            case 81:
                return REQUEST_REPORT_GPRS_EVENT;
            case 82:
                return RESET_TIMER_GPRS;
            case 83:
                return SEND_CHARGING_INFORMATION_GPRS;
            default:
                return null;
        }
    }

    public int value() {
        return value;
    }
}
