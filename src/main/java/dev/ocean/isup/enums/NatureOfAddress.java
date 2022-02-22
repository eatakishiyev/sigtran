/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum NatureOfAddress {

    SPARE(0),
    SUBSCRIBER_NUMBER(1),
    UNKNOWN(2),
    NATIONAL_SIGNIFICANT_NUMBER(3),
    INTERNATIONAL_NUMBER(4);
    private final int value;

    private NatureOfAddress(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static NatureOfAddress getInstance(int value) {
        switch (value) {
            case 0:
                return SPARE;
            case 1:
                return SUBSCRIBER_NUMBER;
            case 2:
                return UNKNOWN;
            case 3:
                return NATIONAL_SIGNIFICANT_NUMBER;
            case 4:
                return INTERNATIONAL_NUMBER;
            default:
                return SPARE;
        }
    }
}
