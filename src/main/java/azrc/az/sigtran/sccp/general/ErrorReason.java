/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general;

/**
 *
 * @author eatakishiyev
 */
public enum ErrorReason {

    NONE(-1),
    NO_TRANSLATION_FOR_ADDRESS_SUCH_NATURE(0),
    NO_TRANSLATION_FOR_SPECIFIC_ADDRESS(1),
    SUBSYSTEM_CONGESTION(2),
    SUBSYSTEM_FAILURE(3),
    UNEQUIPPED_USER(4),
    MTP_FAILURE(5),
    NETWORK_CONGESTION(6),
    UNQUALIFIED(7),
    ERROR_IN_MESSAGE_TRANSFER(8),
    ERROR_IN_LOCAL_PROCESSING(9),
    DESTINATION_CANNOT_PERFORM_REASSEMBLY(10),
    SCCP_FAILURE(11),
    HOP_COUNTER_VIOLATION(12),
    SEGMENTATION_NOT_SUPPORTED(13),
    SEGMENTATION_FAILURE(14),
    ABNORMAL_EVENT_DETECTED_BY_PEER(-1),
    RESPONSE_REJECTED_BY_PEER(-1),
    ABNORMAL_EVENT_RECEIVED_FROM_PEER(-1),
    MESSAGE_CANNOT_BE_DELIVERED_TO_PEER(-1);
    private int value;

    public static ErrorReason getInstance(int value) {
        switch (value) {
            case 0:
                return NO_TRANSLATION_FOR_ADDRESS_SUCH_NATURE;
            case 1:
                return NO_TRANSLATION_FOR_SPECIFIC_ADDRESS;
            case 2:
                return SUBSYSTEM_CONGESTION;
            case 3:
                return SUBSYSTEM_FAILURE;
            case 4:
                return UNEQUIPPED_USER;
            case 5:
                return MTP_FAILURE;
            case 6:
                return NETWORK_CONGESTION;
            case 7:
                return UNQUALIFIED;
            case 8:
                return ERROR_IN_MESSAGE_TRANSFER;
            case 9:
                return ERROR_IN_LOCAL_PROCESSING;
            case 10:
                return DESTINATION_CANNOT_PERFORM_REASSEMBLY;
            case 11:
                return SCCP_FAILURE;
            case 12:
                return HOP_COUNTER_VIOLATION;
            case 13:
                return SEGMENTATION_NOT_SUPPORTED;
            case 14:
                return SEGMENTATION_FAILURE;
            default:
                return NONE;
        }
    }

    private ErrorReason(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
