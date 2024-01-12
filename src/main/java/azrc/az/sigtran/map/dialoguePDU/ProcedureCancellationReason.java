/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.dialoguePDU;

/**
 *
 * @author eatakishiyev
 */
public enum ProcedureCancellationReason {

    HANDOVER_CANCELLATION(0),
    RADIO_CHANNEL_RELEASE(1),
    NETWORK_PATH_RELEASE(2),
    CALL_RELEASE(3),
    ASSOCIATED_PROCEDURE_FAILURE(4),
    TANDEM_DIALOGUE_RELEASE(5),
    REMOTE_OPERATION_FAILURE(6),
    UNKNOWN(-1);
    private int value;

    private ProcedureCancellationReason(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static ProcedureCancellationReason getInstance(int value) {
        switch (value) {
            case 0:
                return HANDOVER_CANCELLATION;
            case 1:
                return RADIO_CHANNEL_RELEASE;
            case 2:
                return NETWORK_PATH_RELEASE;
            case 3:
                return CALL_RELEASE;
            case 4:
                return ASSOCIATED_PROCEDURE_FAILURE;
            case 5:
                return TANDEM_DIALOGUE_RELEASE;
            case 6:
                return REMOTE_OPERATION_FAILURE;
            default:
                return UNKNOWN;
        }
    }
}
