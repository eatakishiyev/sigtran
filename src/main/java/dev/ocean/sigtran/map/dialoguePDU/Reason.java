/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.dialoguePDU;

/**
 *
 * @author eatakishiyev
 */
public enum Reason {

    NO_REASON_GIVEN(0),
    INVALID_DESTIONATION_REFERENCE(1),
    INVALID_ORIGINATING_REFERENCE(2),
    UNKNOWN(-1);
    private int value;

    private Reason(int value) {
        this.value = value;
    }

    public static Reason getInstance(int value) {
        switch (value) {
            case 0:
                return NO_REASON_GIVEN;
            case 1:
                return INVALID_DESTIONATION_REFERENCE;
            case 2:
                return INVALID_ORIGINATING_REFERENCE;
            default:
                return null;
        }
    }

    /**
     * @return the value
     */
    public int value() {
        return value;
    }
}
