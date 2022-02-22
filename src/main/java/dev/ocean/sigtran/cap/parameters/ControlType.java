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
public enum ControlType {

    SCP_OVERLOADED(0),
    MANUALLY_INITIATED(1);

    private int value;

    private ControlType(int value) {
        this.value = value;
    }

    public static ControlType getInstance(int value) {
        switch (value) {
            case 0:
                return SCP_OVERLOADED;
            case 1:
                return MANUALLY_INITIATED;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }

}
