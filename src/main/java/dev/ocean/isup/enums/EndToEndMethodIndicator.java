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
public enum EndToEndMethodIndicator {
    NOT_END_TO_END_METHOD_AVAILABLE(0),
    PASS_ALONG_METHOD_AVAILABLE(1),
    SCCP_METHOD_AVAILABLE(2),
    PASS_ALONG_AND_SCCP_METHODS_AVAILABLE(3),
    UNKNOWN(-1);

    private final int value;

    private EndToEndMethodIndicator(int value) {
        this.value = value;
    }

    public static EndToEndMethodIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NOT_END_TO_END_METHOD_AVAILABLE;
            case 1:
                return PASS_ALONG_METHOD_AVAILABLE;
            case 2:
                return SCCP_METHOD_AVAILABLE;
            case 3:
                return PASS_ALONG_AND_SCCP_METHODS_AVAILABLE;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
