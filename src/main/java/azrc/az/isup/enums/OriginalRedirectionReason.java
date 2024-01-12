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
public enum OriginalRedirectionReason {

    UNKNOWN(0),
    USER_BUSY(1),
    NO_REPLY(2),
    UNCONDITIONAL(3);

    private final int value;

    private OriginalRedirectionReason(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static OriginalRedirectionReason getInstance(int value) {
        switch (value) {
            case 0:
                return UNKNOWN;
            case 1:
                return USER_BUSY;
            case 2:
                return NO_REPLY;
            case 3:
                return UNCONDITIONAL;
            default:
                return UNKNOWN;
        }
    }
}
