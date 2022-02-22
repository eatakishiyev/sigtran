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
public enum CalledPartysStatusIndicator {
    NO_INDICATION(0),
    SUBSCRIBER_FREE(1),
    CONNECT_WHEN_FREE(2),
    UNKNOWN(-1);

    private final int value;

    private CalledPartysStatusIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static CalledPartysStatusIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_INDICATION;
            case 1:
                return SUBSCRIBER_FREE;
            case 2:
                return CONNECT_WHEN_FREE;
            default:
                return UNKNOWN;
        }
    }
}
