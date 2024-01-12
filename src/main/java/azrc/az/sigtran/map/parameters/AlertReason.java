/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum AlertReason {

    MS_PRESENT(0),
    MEMORY_AVAILABLE(1);
    private final int value;

    private AlertReason(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static AlertReason getInstance(int value) {
        switch (value) {
            case 0:
                return MS_PRESENT;
            case 1:
                return MEMORY_AVAILABLE;
            default:
                return null;
        }
    }

}
