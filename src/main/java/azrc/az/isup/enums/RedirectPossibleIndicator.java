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
public enum RedirectPossibleIndicator {
    NOT_USED(0),
    REDIRECT_POSSIBLE_BEFORE_ACM(1),
    REDIRECT_POSSIBLE_BEFORE_ANM(2),
    REDIRECT_POSSIBLE_AT_ANY_TIME_DURING_CALL(3),
    UNKNOWN(-1);

    private final int value;

    private RedirectPossibleIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static RedirectPossibleIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NOT_USED;
            case 1:
                return REDIRECT_POSSIBLE_BEFORE_ACM;
            case 2:
                return REDIRECT_POSSIBLE_BEFORE_ANM;
            case 3:
                return REDIRECT_POSSIBLE_AT_ANY_TIME_DURING_CALL;
            default:
                return UNKNOWN;
        }
    }
}
