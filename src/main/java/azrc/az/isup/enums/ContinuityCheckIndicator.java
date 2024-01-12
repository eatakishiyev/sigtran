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
public enum ContinuityCheckIndicator {
    NOT_REQUIRED(0),
    REQUIRED(1),
    PERFORMED_ON_PREVIOUS_CIRCUIT(2),
    SPARE(3);

    private final int value;

    private ContinuityCheckIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static ContinuityCheckIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NOT_REQUIRED;
            case 1:
                return REQUIRED;
            case 2:
                return PERFORMED_ON_PREVIOUS_CIRCUIT;
            default:
                return SPARE;
        }
    }
}
