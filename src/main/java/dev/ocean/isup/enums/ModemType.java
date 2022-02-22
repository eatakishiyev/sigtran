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
public enum ModemType {
    RECOMMENDATION_V_21(0b00010001),
    RECOMMENDATION_V_22(0b00010010),
    RECOMMENDATION_V_22bis(0b00010011),
    RECOMMENDATION_V_23(0b00010100),
    RECOMMENDATION_V_26(0b00010101),
    RECOMMENDATION_V_26bis(0b00010110),
    RECOMMENDATION_V_26ter(0b00010111),
    RECOMMENDATION_V_27(0b00011000),
    RECOMMENDATION_V_27bis(0b00011001),
    RECOMMENDATION_V_27ter(0b00011010),
    RECOMMENDATION_V_29(0b00011011),
    RECOMMENDATION_V_32(0b00011101),
    RECOMMENDATION_V_34(0b00011110),
    UNKNOWN(-1);

    private final int value;

    private ModemType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static ModemType getInstance(int value) {
        switch (value) {
            case (0b00010001):
                return RECOMMENDATION_V_21;
            case (0b00010010):
                return RECOMMENDATION_V_22;
            case (0b00010011):
                return RECOMMENDATION_V_22bis;
            case (0b00010100):
                return RECOMMENDATION_V_23;
            case (0b00010101):
                return RECOMMENDATION_V_26;
            case (0b00010110):
                return RECOMMENDATION_V_26bis;
            case (0b00010111):
                return RECOMMENDATION_V_26ter;
            case (0b00011000):
                return RECOMMENDATION_V_27;
            case (0b00011001):
                return RECOMMENDATION_V_27bis;
            case (0b00011010):
                return RECOMMENDATION_V_27ter;
            case (0b00011011):
                return RECOMMENDATION_V_29;
            case (0b00011101):
                return RECOMMENDATION_V_32;
            case (0b00011110):
                return RECOMMENDATION_V_34;
            default:
                return UNKNOWN;

        }
    }

}
