/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors.params;

/**
 *
 * @author eatakishiyev
 */
public enum CallBarringCause {

    BARRING_SERVICE_ACTIVE(0),
    OPERATOR_BARRING(1),
    UNKNOWN(-1);
    private int value;

    private CallBarringCause(int value) {
        this.value = value;
    }

    public static CallBarringCause getInstance(int value) {
        switch (value) {
            case 0:
                return BARRING_SERVICE_ACTIVE;
            case 1:
                return OPERATOR_BARRING;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
