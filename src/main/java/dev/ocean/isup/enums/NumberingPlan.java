/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum NumberingPlan {

    SPARE(0),
    ISDN(1),
    DATA(3),
    TELEX(4);
    private final int value;

    private NumberingPlan(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static NumberingPlan getInstance(int value) {
        switch (value) {
            case 1:
                return ISDN;
            case 3:
                return DATA;
            case 4:
                return TELEX;
            default:
                return SPARE;
        }
    }
}
