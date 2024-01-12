/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.primitives.tc;

/**
 *
 * @author root
 */
public enum PAbortCause {

    UNRECOGNIZED_MESSAGE_TYPE(0),
    UNRECOGNIZED_TRANSACTION_ID(1),
    BADDLY_FORMATTED_TRANSACTION_PORTION(2),
    INCORRECT_TRANSACTION_PORTION(3),
    RESOURCE_LIMITATION(4),
    ABNORMAL_DIALOGUE(5),
    NO_COMMON_DIALOGUE_PORTION(6);
    private int value;

    private PAbortCause(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static PAbortCause getInstance(int i) {
        switch (i) {
            case 0:
                return UNRECOGNIZED_MESSAGE_TYPE;
            case 1:
                return UNRECOGNIZED_TRANSACTION_ID;
            case 2:
                return BADDLY_FORMATTED_TRANSACTION_PORTION;
            case 3:
                return INCORRECT_TRANSACTION_PORTION;
            case 4:
                return RESOURCE_LIMITATION;
            case 5:
                return ABNORMAL_DIALOGUE;
            case 6:
                return NO_COMMON_DIALOGUE_PORTION;
            default:
                return null;
        }
    }
}
