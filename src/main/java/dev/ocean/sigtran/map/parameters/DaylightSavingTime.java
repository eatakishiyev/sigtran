/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum DaylightSavingTime {
    NO_ADJUSTMENT(0),
    PLUS_ONE_HOUR_ADJUSTMENT(1),
    PLUS_TWO_HOUR_ADJUSTMENT(2),
    UNKNOWN(-1);

    private final int value;

    private DaylightSavingTime(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static DaylightSavingTime getInstance(int v) {
        switch (v) {
            case 0:
                return NO_ADJUSTMENT;
            case 1:
                return PLUS_ONE_HOUR_ADJUSTMENT;
            case 2:
                return PLUS_TWO_HOUR_ADJUSTMENT;
            default:
                return UNKNOWN;
        }
    }
}
