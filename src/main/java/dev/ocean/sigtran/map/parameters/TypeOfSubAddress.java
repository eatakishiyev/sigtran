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
public enum TypeOfSubAddress {

    NSAP(0),
    USER_SPECIFIC(2),
    UNKNOWN(-1);

    private final int value;

    private TypeOfSubAddress(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static final TypeOfSubAddress getInstance(int value) {
        switch (value) {
            case 0:
                return NSAP;
            case 2:
                return USER_SPECIFIC;
            default:
                return UNKNOWN;
        }
    }
}
