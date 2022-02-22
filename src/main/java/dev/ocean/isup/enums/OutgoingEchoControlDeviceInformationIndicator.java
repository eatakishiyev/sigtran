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
public enum OutgoingEchoControlDeviceInformationIndicator {
    NO_INFORMATION(0),
    OECD_NOT_INCLUDED_AND_NOT_AVAILABLE(1),
    OECD_INCLUDED(2),
    OECD_NOT_INCLUDED_BUT_AVAILABLE(3),
    UNKNOWN(-1);

    private final int value;

    private OutgoingEchoControlDeviceInformationIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static final OutgoingEchoControlDeviceInformationIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_INFORMATION;
            case 1:
                return OECD_NOT_INCLUDED_AND_NOT_AVAILABLE;
            case 2:
                return OECD_INCLUDED;
            case 3:
                return OECD_NOT_INCLUDED_BUT_AVAILABLE;
            default:
                return UNKNOWN;
        }
    }
}
