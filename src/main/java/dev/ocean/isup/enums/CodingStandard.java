/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum CodingStandard {

    CCITT(0),
    ISO_EIC(1),
    NATIONAL_STANDARD(3),
    NETWORK_DEFINED(4),
    UNKNOWN(-1);
    private final int value;

    private CodingStandard(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static CodingStandard getInstance(int value) {
        switch (value) {
            case 0:
                return CCITT;
            case 1:
                return ISO_EIC;
            case 3:
                return NATIONAL_STANDARD;
            case 4:
                return NETWORK_DEFINED;
            default:
                return UNKNOWN;
        }
    }
}
