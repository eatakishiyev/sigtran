/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum DefaultSMSHandling {

    CONTINUE_TRANSACTION(0),
    RELEASE_TRANSACTION(1);
    private final int value;

    private DefaultSMSHandling(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static DefaultSMSHandling getInstance(int value) {
        switch (value) {
            case 0:
                return CONTINUE_TRANSACTION;
            case 1:
                return RELEASE_TRANSACTION;
            default:
                return null;
        }
    }
}
