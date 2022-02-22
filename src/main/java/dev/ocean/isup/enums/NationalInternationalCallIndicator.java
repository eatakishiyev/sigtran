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
public enum NationalInternationalCallIndicator {
    NATIONAL_CALL(0),
    INTERNATIONAL_CALL(1),
    UNKNOWN(-1);

    private final int value;

    private NationalInternationalCallIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static NationalInternationalCallIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NATIONAL_CALL;
            case 1:
                return INTERNATIONAL_CALL;
            default:
                return UNKNOWN;
        }
    }

}
