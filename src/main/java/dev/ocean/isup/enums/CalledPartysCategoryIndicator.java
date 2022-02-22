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
public enum CalledPartysCategoryIndicator {
    NO_INDICATION(0),
    ORDINARY_SUBSCRIBER(1),
    PAYPHONE(2),
    UNKNOWN(-1);

    private final int value;

    private CalledPartysCategoryIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static CalledPartysCategoryIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_INDICATION;
            case 1:
                return ORDINARY_SUBSCRIBER;
            case 2:
                return PAYPHONE;
            default:
                return UNKNOWN;
        }
    }
}
