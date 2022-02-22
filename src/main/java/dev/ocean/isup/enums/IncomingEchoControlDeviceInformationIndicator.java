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
public enum IncomingEchoControlDeviceInformationIndicator {
    NO_INFORMATION(0),
    IECD_NOT_INCLUDED_AND_NOT_AVAILABLE(1),
    IECD_INCLUDED(2),
    IECD_NOT_INCLUDED_BUT_AVAILABLE(3),
    UNKNOWN(-1);

    private final int value;

    private IncomingEchoControlDeviceInformationIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static IncomingEchoControlDeviceInformationIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_INFORMATION;
            case 1:
                return IECD_NOT_INCLUDED_AND_NOT_AVAILABLE;
            case 2:
                return IECD_INCLUDED;
            case 3:
                return IECD_NOT_INCLUDED_BUT_AVAILABLE;
            default:
                return UNKNOWN;
        }
    }
}
