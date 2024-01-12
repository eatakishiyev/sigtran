/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map;

/**
 *
 * @author eatakishiyev
 */
public enum MAPUserErrorValues {

    UNKNOWN_SUBSCRIBER(1),
    UNKNOWN_MSC(3),
    UNIDENTIFIED_SUBSCRIBER(5),
    ABSENT_SUBSCRIBER_SM(6),
    UNKNOWN_EQUIPMENT(7),
    ROAMING_NOT_ALLOWED(8),
    ILLEGAL_SUBSCRIBER(9),
    BEAREAR_SERVICE_NOT_PROVISIONED(10),
    TELESERVICE_NOT_PROVISIONED(11),
    ILLEGAL_EQUIPMENT(12),
    CALL_BARRED(13),
    FORWARDING_VIOLATION(14),
    CUG_REJECT(15),
    ILLEGAL_SS_OPERATION(16),
    SS_ERROR_STATUS(17),
    SS_NOT_ALLOWED(18),
    SS_SUBSCRIPTION_VIOLATION(19),
    SS_INCOMPATIBILITY(20),
    FACILITY_NOT_SUPPORTED(21),
    ONGOING_GROUP_CALL(22),
    NO_HANDOVER_NUMBER_AVAILABLE(25),
    SUBSEQUENT_HANDOVER_FAILURE(26),
    ABSENT_SUBSCRIBER(27),
    INCOMPATIBLE_TERMINAL(28),
    SHORT_TERM_DENIAL(29),
    LONG_TERM_DENIAL(30),
    SUBSCRIBER_BUSY_FOR_MT_SMS(31),
    SM_DELIVERY_FAILURE(32),
    MESSAGE_WAITING_LIST_FULL(33),
    SYSTEM_FAILURE(34),
    DATA_MISSING(35),
    UNEXPECTED_DATA_VALUE(36),
    PW_REGISTRATION_FAILURE(37),
    NEGATIVE_PW_CHECK(38),
    NO_ROAMING_NUMBER_AVAILABLE(39),
    TRACING_BUFFER_FULL(40),
    TARGET_CELL_OUTSIDE_GROUP_CELL_AREA(42),
    NUMBER_OF_PW_ATTEMPTS_VIOLATION(43),
    NUMBER_CHANGED(44),
    BUSY_SUBSCRIBER(45),
    NO_SUBSCRIBER_REPLY(46),
    FORWARDING_FAILED(47),
    OR_NOT_ALLOWED(48),
    ATI_NOT_ALLOWED(49),
    NO_GROUP_CALL_NUMBER_AVAILABLE(50),
    RESOURCE_LIMITATION(51),
    UNAUTHORIZED_REQUESTING_NETWORK(52),
    UNAUTHORIZED_LCS_CLIENT(53),
    POSITION_METHOD_FAILURE(54),
    UNKNOWN_OR_UNREACHABLE_CS_CLIENT(58),
    MM_EVENT_NOT_SUPPORTED(59),
    ATSI_NOT_ALLOWED(60),
    ATM_NOT_ALLOWED(61),
    INFORMATION_NOT_AVAILABLE(62),
    UNKNOWN_ALPHABET(71),
    USSD_BUSY(72),
    INITIATING_RELEASE(253),
    LOC_UPDATE_TYPE_NOT_SUPPORTED(254),
    MOB_MNGT_DP_NOT_SUPPORTED(255),
    UNKNOWN(-1);
    private final int value;

    private MAPUserErrorValues(int value) {
        this.value = value;
    }

    public static MAPUserErrorValues getInstance(Integer value) {
        if (value == null) {
            return null;
        }
        switch (value) {
            case 1:
                return UNKNOWN_SUBSCRIBER;
            case 3:
                return UNKNOWN_MSC;
            case 5:
                return UNIDENTIFIED_SUBSCRIBER;
            case 6:
                return ABSENT_SUBSCRIBER_SM;
            case 7:
                return UNKNOWN_EQUIPMENT;
            case 8:
                return ROAMING_NOT_ALLOWED;
            case 9:
                return ILLEGAL_SUBSCRIBER;
            case 10:
                return BEAREAR_SERVICE_NOT_PROVISIONED;
            case 11:
                return TELESERVICE_NOT_PROVISIONED;
            case 12:
                return ILLEGAL_EQUIPMENT;
            case 13:
                return CALL_BARRED;
            case 14:
                return FORWARDING_VIOLATION;
            case 15:
                return CUG_REJECT;
            case 16:
                return ILLEGAL_SS_OPERATION;
            case 17:
                return SS_ERROR_STATUS;
            case 18:
                return SS_NOT_ALLOWED;
            case 19:
                return SS_SUBSCRIPTION_VIOLATION;
            case 20:
                return SS_INCOMPATIBILITY;
            case 21:
                return FACILITY_NOT_SUPPORTED;
            case 22:
                return ONGOING_GROUP_CALL;
            case 25:
                return NO_HANDOVER_NUMBER_AVAILABLE;
            case 26:
                return SUBSEQUENT_HANDOVER_FAILURE;
            case 27:
                return ABSENT_SUBSCRIBER;
            case 28:
                return INCOMPATIBLE_TERMINAL;
            case 29:
                return SHORT_TERM_DENIAL;
            case 30:
                return LONG_TERM_DENIAL;
            case 31:
                return SUBSCRIBER_BUSY_FOR_MT_SMS;
            case 32:
                return SM_DELIVERY_FAILURE;
            case 33:
                return MESSAGE_WAITING_LIST_FULL;
            case 34:
                return SYSTEM_FAILURE;
            case 35:
                return DATA_MISSING;
            case 36:
                return UNEXPECTED_DATA_VALUE;
            case 37:
                return PW_REGISTRATION_FAILURE;
            case 38:
                return NEGATIVE_PW_CHECK;
            case 39:
                return NO_ROAMING_NUMBER_AVAILABLE;
            case 40:
                return TRACING_BUFFER_FULL;
            case 42:
                return TARGET_CELL_OUTSIDE_GROUP_CELL_AREA;
            case 43:
                return NUMBER_OF_PW_ATTEMPTS_VIOLATION;
            case 44:
                return NUMBER_CHANGED;
            case 45:
                return BUSY_SUBSCRIBER;
            case 46:
                return NO_SUBSCRIBER_REPLY;
            case 47:
                return FORWARDING_FAILED;
            case 48:
                return OR_NOT_ALLOWED;
            case 49:
                return ATI_NOT_ALLOWED;
            case 50:
                return NO_GROUP_CALL_NUMBER_AVAILABLE;
            case 51:
                return RESOURCE_LIMITATION;
            case 52:
                return UNAUTHORIZED_REQUESTING_NETWORK;
            case 53:
                return UNAUTHORIZED_LCS_CLIENT;
            case 54:
                return POSITION_METHOD_FAILURE;
            case 58:
                return UNKNOWN_OR_UNREACHABLE_CS_CLIENT;
            case 59:
                return MM_EVENT_NOT_SUPPORTED;
            case 60:
                return ATSI_NOT_ALLOWED;
            case 61:
                return ATM_NOT_ALLOWED;
            case 62:
                return INFORMATION_NOT_AVAILABLE;
            case 71:
                return UNKNOWN_ALPHABET;
            case 72:
                return USSD_BUSY;
            case 254:
                return LOC_UPDATE_TYPE_NOT_SUPPORTED;
            case 255:
                return MOB_MNGT_DP_NOT_SUPPORTED;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
