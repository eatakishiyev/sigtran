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
public enum IntermediateRate {

    NOT_USED(0),
    RATE_8KBIT(1),
    RATE_16KBIT(2),
    RATE_32KBIT(3),
    UNKNOWN(-1);

    private final int value;

    private IntermediateRate(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static IntermediateRate getInstance(int value) {
        switch (value) {
            case 0:
                return NOT_USED;
            case 1:
                return RATE_8KBIT;
            case 2:
                return RATE_16KBIT;
            case 3:
                return RATE_32KBIT;
            default:
                return UNKNOWN;
        }
    }

}
