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
public enum ConnectedNumberTreatmentInd {

    NO_IN_IMPACT(0),
    PRESENTATION_RESTRICTED(1),
    PRESENT_CALLED_IN_NUMBER(2),
    PRESENT_CALL_IN_NUMBER_RESTRICTED(3);

    private int value;

    private ConnectedNumberTreatmentInd(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static ConnectedNumberTreatmentInd getInstance(int value) {
        switch (value) {
            case 0:
                return NO_IN_IMPACT;
            case 1:
                return PRESENTATION_RESTRICTED;
            case 2:
                return PRESENT_CALLED_IN_NUMBER;
            case 3:
                return PRESENT_CALL_IN_NUMBER_RESTRICTED;
            default:
                return null;
        }
    }

}
