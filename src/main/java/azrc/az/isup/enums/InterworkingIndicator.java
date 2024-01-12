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
public enum InterworkingIndicator {
    NO_INTERWORKING(0),
    INTERWORKING(1),
    UNKNOWN(-1);

    private final int value;

    private InterworkingIndicator(int value) {
        this.value = value;
    }

    public static InterworkingIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_INTERWORKING;
            case 1:
                return INTERWORKING;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
