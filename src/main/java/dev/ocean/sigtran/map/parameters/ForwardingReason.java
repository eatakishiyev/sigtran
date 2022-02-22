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
public enum ForwardingReason {

    MS_NOT_REACHABLE(0),
    MS_BUSY(1),
    NO_REPLY(2),
    UNCONDITIONAL(3);
    private final int value;

    private ForwardingReason(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static ForwardingReason getInstance(int value) {
        switch (value) {
            case 0:
                return MS_NOT_REACHABLE;
            case 1:
                return MS_BUSY;
            case 2:
                return NO_REPLY;
            case 3:
                return UNCONDITIONAL;
            default:
                return null;
        }
    }
}
