/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.dialoguePDU;

/**
 *
 * @author eatakishiyev
 */
public enum MapProviderAbortReason {

    ABNORMAL_DIALOGUE(0),
    INVALID_PDU(1),
    UNKNOWN(-1);
    private int value;

    private MapProviderAbortReason(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static MapProviderAbortReason getInstance(int value) {
        switch (value) {
            case 0:
                return ABNORMAL_DIALOGUE;
            case 1:
                return INVALID_PDU;
            default:
                return UNKNOWN;
        }
    }
}
