/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum NumberOfStopBits {
    STOP_BITS_NOT_USED(0),
    _1_STOP_BIT(1),
    _1_5_STOP_BIT(2),
    _2_STOP_BIT(3);

    private final int value;

    private NumberOfStopBits(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static NumberOfStopBits getInstance(int value) {
        switch (value) {
            case 0:
                return STOP_BITS_NOT_USED;
            case 1:
                return _1_STOP_BIT;
            case 2:
                return _1_5_STOP_BIT;
            case 3:
                return _2_STOP_BIT;
            default:
                return STOP_BITS_NOT_USED;
        }
    }
}
