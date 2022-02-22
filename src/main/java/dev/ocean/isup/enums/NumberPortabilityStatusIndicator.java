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
public enum NumberPortabilityStatusIndicator {
    NO_INDICATION(0),
    NUMBER_PORTABILITY_QUERY_NOT_DONE_FOR_CALLED_NUMBER(1),
    NON_PORTED_CALLED_SUBSCRIBER(2),
    PORTED_CALLED_SUBSCRIBER(3),
    UNKNOWN(-1);

    private final int value;

    private NumberPortabilityStatusIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static NumberPortabilityStatusIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_INDICATION;
            case 1:
                return NUMBER_PORTABILITY_QUERY_NOT_DONE_FOR_CALLED_NUMBER;
            case 2:
                return NON_PORTED_CALLED_SUBSCRIBER;
            case 3:
                return PORTED_CALLED_SUBSCRIBER;
            default:
                return UNKNOWN;
        }
    }
}
