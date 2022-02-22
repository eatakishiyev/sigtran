/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum CGEncountered {

    NO_CG_ENCOUNTERED(0),
    MANUAL_CG_ENCOUNTERED(1),
    SCP_OVERLOAD(2);

    private int value;

    private CGEncountered(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static CGEncountered getInstance(int value) {
        switch (value) {
            case 0:
                return NO_CG_ENCOUNTERED;
            case 1:
                return MANUAL_CG_ENCOUNTERED;
            case 2:
                return SCP_OVERLOAD;
            default:
                return null;
        }
    }
}
