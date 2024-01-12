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
public enum ChargeIndicator {

    NO_INDICATION(0),
    NO_CHARGE(1),
    CHARGE(2),
    UNKNOWN(-1);

    private final int value;

    private ChargeIndicator(int value) {
        this.value = value;
    }

    public static ChargeIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_INDICATION;
            case 1:
                return NO_CHARGE;
            case 2:
                return CHARGE;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return value;
    }
}
