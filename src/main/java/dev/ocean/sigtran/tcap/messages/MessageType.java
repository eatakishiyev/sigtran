/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.messages;

/**
 *
 * @author root
 */
public enum MessageType {

    UNIDIRECTIONAL(0x01),
    BEGIN(0x02),
    END(0x04),
    CONTINUE(0x05),
    ABORT(0x07),
    UNKNOWN(-1);
    private int value;

    private MessageType(int value) {
        this.value = value;
    }

    public static MessageType getInstance(int value) {
        switch (value) {
            case 0x01:
                return UNIDIRECTIONAL;
            case 0x02:
                return BEGIN;
            case 0x04:
                return END;
            case 0x05:
                return CONTINUE;
            case 0x07:
                return ABORT;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
