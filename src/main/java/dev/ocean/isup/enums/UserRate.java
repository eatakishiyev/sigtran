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
public enum UserRate {
    UNSPECIFIED(0b00000000),
    RATE_0_6KBIT(0b00000001),
    RATE_1_2KBIT(0b00000010),
    RATE_2_4KBIT(0b00000011),
    RATE_3_6KBIT(0b00000100),
    RATE_4_8KBIT(0b00000101),
    RATE_7_2KBIT(0b00000110),
    RATE_8KBIT(0b00000111),
    RATE_9_6KBIT(0b00001000),
    RATE_14_4KBIT(0b00001001),
    RATE_16KBIT(0b00001010),
    RATE_19_2KBIT(0b00001011),
    RATE_32KBIT(0b00001100),
    RATE_38_4KBIT(0b00001101),
    RATE_48KBIT(0b00001110),
    RATE_56KBIT(0b00001111),
    RATE_57_6KBIT(0b00010010),
    RATE_28_8KBIT(0b00010011),
    RATE_24KBIT(0b00010100),
    RATE_0_1345KBIT(0b00010101),
    RATE_0_100KBIT(0b00010110),
    RATE_0_075__1_2KBIT(0b00010111),
    RATE_1_2__075KBIT(0b00011000),
    RATE_0_050KBIT(0b00011001),
    RATE_0_075KBIT(0b00011010),
    RATE_0_110KBIT(0b00011011),
    RATE_0_150KBIT(0b00011100),
    RATE_0_200KBIT(0b00011101),
    RATE_0_300KBIT(0b00011110),
    RATE_12KBIT(0b00011111),
    UNKNOWN(-1);

    public static UserRate getInstance(int value) {
        switch (value) {

            case (0b00000000):
                return UNSPECIFIED;
            case (0b00000001):
                return RATE_0_6KBIT;
            case (0b00000010):
                return RATE_1_2KBIT;
            case (0b00000011):
                return RATE_2_4KBIT;
            case (0b00000100):
                return RATE_3_6KBIT;
            case (0b00000101):
                return RATE_4_8KBIT;
            case (0b00000110):
                return RATE_7_2KBIT;
            case (0b00000111):
                return RATE_8KBIT;
            case (0b00001000):
                return RATE_9_6KBIT;
            case (0b00001001):
                return RATE_14_4KBIT;
            case (0b00001010):
                return RATE_16KBIT;
            case (0b00001011):
                return RATE_19_2KBIT;
            case (0b00001100):
                return RATE_32KBIT;
            case (0b00001101):
                return RATE_38_4KBIT;
            case (0b00001110):
                return RATE_48KBIT;
            case (0b00001111):
                return RATE_56KBIT;
            case (0b00010010):
                return RATE_57_6KBIT;
            case (0b00010011):
                return RATE_28_8KBIT;
            case (0b00010100):
                return RATE_24KBIT;
            case (0b00010101):
                return RATE_0_1345KBIT;
            case (0b00010110):
                return RATE_0_100KBIT;
            case (0b00010111):
                return RATE_0_075__1_2KBIT;
            case (0b00011000):
                return RATE_1_2__075KBIT;
            case (0b00011001):
                return RATE_0_050KBIT;
            case (0b00011010):
                return RATE_0_075KBIT;
            case (0b00011011):
                return RATE_0_110KBIT;
            case (0b00011100):
                return RATE_0_150KBIT;
            case (0b00011101):
                return RATE_0_200KBIT;
            case (0b00011110):
                return RATE_0_300KBIT;
            case (0b00011111):
                return RATE_12KBIT;
            default:
                return UNKNOWN;
        }
    }

    private final int value;

    private UserRate(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
