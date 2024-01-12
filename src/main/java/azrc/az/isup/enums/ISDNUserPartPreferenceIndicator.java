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
public enum ISDNUserPartPreferenceIndicator {
    PREFERRED_ALL_THE_WAY(0),
    NOT_REQUIRED_ALL_THE_WAY(1),
    REQUIRED_ALL_THE_WAY(2),
    SPARE(3);

    private final int value;

    private ISDNUserPartPreferenceIndicator(int value) {
        this.value = value;
    }

    public static ISDNUserPartPreferenceIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return PREFERRED_ALL_THE_WAY;
            case 1:
                return NOT_REQUIRED_ALL_THE_WAY;
            case 2:
                return REQUIRED_ALL_THE_WAY;
            default:
                return SPARE;
        }
    }

    public int value() {
        return this.value;
    }
}
