/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

/**
 *
 * @author eatakishiyev
 */
public enum NetworkIndicator {

    INTERNATIONAL(0),
    INTERNATIONAL_RESERVE(1),
    NATIONAL(2),
    NATIONAL_RESERVE(3),
    UNKNOWN(-1);
    private final int value;

    private NetworkIndicator(int value) {
        this.value = value;
    }

    public static NetworkIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return INTERNATIONAL;
            case 1:
                return INTERNATIONAL_RESERVE;
            case 2:
                return NATIONAL;
            case 3:
                return NATIONAL_RESERVE;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
