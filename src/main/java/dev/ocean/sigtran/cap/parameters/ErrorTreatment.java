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
public enum ErrorTreatment {

    STD_ERROR_AND_INFO(0),
    HELP(1),
    REPEAT_PROMPT(2);

    private int value;

    private ErrorTreatment(int value) {
        this.value = value;
    }

    public static ErrorTreatment getInstance(int value) {
        switch (value) {
            case 0:
                return STD_ERROR_AND_INFO;
            case 1:
                return HELP;
            case 2:
                return REPEAT_PROMPT;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }

}
