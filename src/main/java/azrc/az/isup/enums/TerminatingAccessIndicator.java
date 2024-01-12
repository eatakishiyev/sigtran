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
public enum TerminatingAccessIndicator {
    NO_INFORMATION(0),
    DEDICATED_TERMINATING_ACCESS(1),
    SWITCHED_TERMINATING_ACCESS(2),
    UNKNOWN(-1);

    private final int value;

    private TerminatingAccessIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static TerminatingAccessIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_INFORMATION;
            case 1:
                return DEDICATED_TERMINATING_ACCESS;
            case 2:
                return SWITCHED_TERMINATING_ACCESS;
            default:
                return UNKNOWN;
        }
    }
}
