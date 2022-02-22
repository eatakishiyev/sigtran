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
public enum RedirectingReason {
    UNKNOWN(0b00000000),
    USER_BUSY(0b00000001),
    NO_REPLY(0b00000010),
    UNCONDITIONAL(0b00000011),
    DEFLECTION_DURING_ALERTING(0b00000100),
    DEFLECTION_IMMEDIATE_RESPONSE(0b00000101),
    MOBILE_SUBSCRIBER_NOT_REACHABLE(0b00000110);

    private final int value;

    private RedirectingReason(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static RedirectingReason getInstance(int value) {
        switch (value) {
            case 0b00000000:
                return UNKNOWN;
            case 0b00000001:
                return USER_BUSY;
            case 0b00000010:
                return NO_REPLY;
            case 0b00000011:
                return UNCONDITIONAL;
            case 0b00000100:
                return DEFLECTION_DURING_ALERTING;
            case 0b00000101:
                return DEFLECTION_IMMEDIATE_RESPONSE;
            case 0b00000110:
                return MOBILE_SUBSCRIBER_NOT_REACHABLE;
            default:
                return UNKNOWN;
        }
    }
}
