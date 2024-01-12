/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum DefaultCallHandling {

    CONTINUE_CALL(0),
    RELEASE_CALL(1);
    private final int value;

    private DefaultCallHandling(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static DefaultCallHandling getInstance(int value) {
        switch (value) {
            case 0:
                return CONTINUE_CALL;
            case 1:
                return RELEASE_CALL;
            default:
                return null;
        }
    }
}
