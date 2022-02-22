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
public enum OutgoingEchoControlDeviceRequestIndicator {
    NO_INFORMATION(0),
    OECD_ACTIVATION_REQUEST(1),
    OECD_DEACTIVATION_REQUEST(2),
    UNKNOWN(-1);

    private final int value;

    private OutgoingEchoControlDeviceRequestIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static OutgoingEchoControlDeviceRequestIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_INFORMATION;
            case 1:
                return OECD_ACTIVATION_REQUEST;
            case 2:
                return OECD_DEACTIVATION_REQUEST;
            default:
                return UNKNOWN;
        }
    }
}
