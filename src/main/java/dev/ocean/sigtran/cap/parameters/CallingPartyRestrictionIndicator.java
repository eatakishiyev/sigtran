/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum CallingPartyRestrictionIndicator {

    NO_IN_IMPACT(0b00000001),
    PRESENTATION_RESTRICTED(0b00000010),
    UNKNOWN(-1);

    private final int value;

    private CallingPartyRestrictionIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static CallingPartyRestrictionIndicator getInstance(int value) {
        switch (value) {
            case 0b00000001:
                return NO_IN_IMPACT;
            case 0b00000010:
                return PRESENTATION_RESTRICTED;
            default:
                return UNKNOWN;
        }
    }
}
