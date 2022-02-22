/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum CongestionLevel {

    NO_CONGESTION(0),
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3);
    private final int value;

    private CongestionLevel(int value) {
        this.value = value;
    }

    public static CongestionLevel getInstance(int value) {
        switch (value) {
            case 0:
                return NO_CONGESTION;
            case 1:
                return LEVEL_1;
            case 2:
                return LEVEL_2;
            case 3:
                return LEVEL_3;
            default:
                return NO_CONGESTION;
        }
    }

    public int value() {
        return this.value;
    }
}
