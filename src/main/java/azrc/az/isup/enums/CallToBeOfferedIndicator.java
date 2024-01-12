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
public enum CallToBeOfferedIndicator {
    NO_INDICATION(0),
    CALL_OFFERING_NOT_ALLOWED(1),
    CALL_OFFERING_ALLOWED(2),
    UNKNOWN(-1);

    private final int value;

    private CallToBeOfferedIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static CallToBeOfferedIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_INDICATION;
            case 1:
                return CALL_OFFERING_NOT_ALLOWED;
            case 2:
                return CALL_OFFERING_ALLOWED;
            default:
                return UNKNOWN;
        }
    }
}
