/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum StatusType {

    APPLICATION_SERVER_STATE_CHANGE(1),
    OTHER(2);
    private final int value;

    private StatusType(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int value() {
        return value;
    }

    public static StatusType getInstance(int value) {
        switch (value) {
            case 1:
                return APPLICATION_SERVER_STATE_CHANGE;
            case 2:
                return OTHER;
            default:
                return null;
        }
    }
}
