/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum Cause {

    UNKNOWN(0),
    UNEQUIPPED_REMOTE_USER(1),
    INACCESSIBLE_REMOTE_USER(2);
    private int value;

    private Cause(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int value() {
        return value;
    }

    public static Cause getInstance(int value) {
        switch (value) {
            case 0:
                return UNKNOWN;
            case 1:
                return UNEQUIPPED_REMOTE_USER;
            case 2:
                return INACCESSIBLE_REMOTE_USER;
            default:
                return null;
        }
    }
}
