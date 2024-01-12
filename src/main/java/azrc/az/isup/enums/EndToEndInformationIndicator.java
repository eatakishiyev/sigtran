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
public enum EndToEndInformationIndicator {
    NO_END_TO_END_AVAILABLE(0),
    END_TO_END_AVAILABLE(1),
    UNKNOWN(-1);

    private EndToEndInformationIndicator(int value) {
        this.value = value;
    }

    private final int value;

    public static EndToEndInformationIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_END_TO_END_AVAILABLE;
            case 1:
                return END_TO_END_AVAILABLE;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
