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
public enum ModeOfOperation {
    BIT_TRANSPARENT_MODE(0),
    PROTOCOL_SENSITIVE_MODE(1),
    UNKNOWN(-1);

    private final int value;

    private ModeOfOperation(int value) {
        this.value = value;
    }

    public static ModeOfOperation getInstance(int value) {
        switch (value) {
            case 0:
                return BIT_TRANSPARENT_MODE;
            case 1:
                return PROTOCOL_SENSITIVE_MODE;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
