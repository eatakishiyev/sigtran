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
public enum RedirectStatusIndicator {
    NOT_USED(0),
    ACKNOWLEDGMENT_OF_REDIRECTION(1),
    REDIRECTION_WILL_NOT_BE_INVOKED(2),
    UNKNOWN(-1);

    private final int value;

    private RedirectStatusIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static RedirectStatusIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NOT_USED;
            case 1:
                return ACKNOWLEDGMENT_OF_REDIRECTION;
            case 2:
                return REDIRECTION_WILL_NOT_BE_INVOKED;
            default:
                return UNKNOWN;
        }
    }
}
