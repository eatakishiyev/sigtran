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
public enum Lfb {
    LFB_ALLOWED(0),
    PATH_RESERVED(1),
    LFB_NOT_ALLOWED(2),
    UNKNOWN(-1);

    private final int value;

    private Lfb(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static Lfb getInstance(int value) {
        switch (value) {
            case 0:
                return LFB_ALLOWED;
            case 1:
                return PATH_RESERVED;
            case 2:
                return LFB_NOT_ALLOWED;
            default:
                return UNKNOWN;
        }
    }
}
