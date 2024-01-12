/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum SCCPMethodIndicator {
    NO_INDICATION(0),
    CONNECTIONLESS_METHOD_AVAILABLE(1),
    CONNECTION_ORIENTED_METHOD_AVAILABLE(2),
    CONNECTIONLESS_AND_CONNECTION_ORIENTED_METHODS_AVAILABLE(3),
    UNKNOWN(-1);

    private final int value;

    private SCCPMethodIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static SCCPMethodIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_INDICATION;
            case 1:
                return CONNECTIONLESS_METHOD_AVAILABLE;
            case 2:
                return CONNECTION_ORIENTED_METHOD_AVAILABLE;
            case 3:
                return CONNECTIONLESS_AND_CONNECTION_ORIENTED_METHODS_AVAILABLE;
            default:
                return UNKNOWN;
        }
    }
}
