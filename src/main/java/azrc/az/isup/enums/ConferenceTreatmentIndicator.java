/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum ConferenceTreatmentIndicator {

    ACCEPT_CONFERENCE_REQUEST(0b00000001),
    REJECT_CONFERENCE_REQUEST(0b00000010),
    UNKNOWN(-1);

    private final int value;

    private ConferenceTreatmentIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static ConferenceTreatmentIndicator getInstance(int value) {
        switch (value) {
            case 0b00000001:
                return ACCEPT_CONFERENCE_REQUEST;
            case 0b00000010:
                return REJECT_CONFERENCE_REQUEST;
            default:
                return UNKNOWN;
        }
    }
}
