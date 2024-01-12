/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum Result {

    ACCEPTED(0),
    REJECT_PERMANENT(1),
    UNKNOWN(-1);
    private int value;

    private Result(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static Result getInstance(int value) {
        switch (value) {
            case 0:
                return ACCEPTED;
            case 1:
                return REJECT_PERMANENT;
            default:
                return UNKNOWN;
        }
    }
}
