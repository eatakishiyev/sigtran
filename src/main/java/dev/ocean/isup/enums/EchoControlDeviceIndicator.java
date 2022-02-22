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
public enum EchoControlDeviceIndicator {
    OUTGOING_ECD_NOT_INCLUDED(0),
    OUTGOING_ECD_INCLUDED(1);

    private final int value;

    private EchoControlDeviceIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static EchoControlDeviceIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return OUTGOING_ECD_NOT_INCLUDED;
            case 1:
                return OUTGOING_ECD_INCLUDED;
            default:
                return OUTGOING_ECD_NOT_INCLUDED
                        ;
        }
    }
}
