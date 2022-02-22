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
public enum InformationTransferCapability {
    SPEECH(0b00000000),
    UNRESTRICTED_DIGITAL_INFO(0b00001000),
    RESTRICTED_DIGITAL_INFO(0b00001001),
    AUDIO_3_1KHZ(0b00010000),
    UNRESTRICTED_DIGITAL_INFO_WITH_TONES(0b00010001),
    VIDEO(0b00011000),
    UNKNOWN(-1);

    private final int value;

    private InformationTransferCapability(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static InformationTransferCapability getInstance(int value) {
        switch (value) {
            case 0b00000000:
                return SPEECH;
            case 0b00001000:
                return UNRESTRICTED_DIGITAL_INFO;
            case 0b00001001:
                return RESTRICTED_DIGITAL_INFO;
            case 0b00010000:
                return AUDIO_3_1KHZ;
            case 0b00010001:
                return UNRESTRICTED_DIGITAL_INFO_WITH_TONES;
            case 0b00011000:
                return VIDEO;
            default:
                return UNKNOWN;
        }
    }
}
