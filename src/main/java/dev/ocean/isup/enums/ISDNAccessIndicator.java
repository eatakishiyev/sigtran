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
public enum ISDNAccessIndicator {
    ORIGINATING_ADDRESS_NON_ISDN(0),
    ORIGINATING_ADDRESS_ISDN(1),
    UNKNOWN(-1);

    private final int value;

    private ISDNAccessIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static ISDNAccessIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return ORIGINATING_ADDRESS_NON_ISDN;
            case 1:
                return ORIGINATING_ADDRESS_ISDN;
            default:
                return UNKNOWN;
        }
    }
}
