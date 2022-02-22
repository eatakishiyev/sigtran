/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum AddressPresentationRestricted {

    PRESENTATION_ALLOWED(0),
    PRESENTATION_RESTRICTED(1),
    ADDRESS_NOT_AVAILABLE(2),
    RESERVED(3);
    private final int value;

    private AddressPresentationRestricted(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static AddressPresentationRestricted getInstance(int value) {
        switch (value) {
            case 0:
                return PRESENTATION_ALLOWED;
            case 1:
                return PRESENTATION_RESTRICTED;
            case 2:
                return ADDRESS_NOT_AVAILABLE;
            case 3:
                return RESERVED;
            default:
                return null;
        }
    }
}
