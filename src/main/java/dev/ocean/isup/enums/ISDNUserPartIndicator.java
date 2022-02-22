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
public enum ISDNUserPartIndicator {
    NOT_USED(0),
    USED(1),
    UNKNOWN(-1);

    private final int value;

    private ISDNUserPartIndicator(int value) {
        this.value = value;
    }

    public static ISDNUserPartIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NOT_USED;
            case 1:
                return USED;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
