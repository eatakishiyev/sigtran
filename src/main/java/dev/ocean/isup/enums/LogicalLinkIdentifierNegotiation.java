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
public enum LogicalLinkIdentifierNegotiation {
    DEFAULT(0),
    FULL_PROTOCOL_NEGOTIATION(1),
    UNKNOWN(-1);

    private final int value;

    private LogicalLinkIdentifierNegotiation(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static LogicalLinkIdentifierNegotiation getInstance(int value) {
        switch (value) {
            case 0:
                return DEFAULT;
            case 1:
                return FULL_PROTOCOL_NEGOTIATION;
            default:
                return UNKNOWN;
        }
    }
}
