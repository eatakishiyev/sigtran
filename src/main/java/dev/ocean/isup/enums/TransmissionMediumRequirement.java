/*
 * To change this license header; choose License Headers in Project Properties.
 * To change this template file; choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum TransmissionMediumRequirement {

    TRANSMISSION_SPEECH(0b00000000),
    TRANSMISSION_64_KBIT_UNRESTRICTED(0b00000010),
    TRANSMISSION_3_1_KHZ_AUDIO(0b00000011),
    TRANSMISSION_SERVICE_1(0b00000100),
    TRANSMISSION_SERVICE_2(0b00000101),
    TRANSMISSION_64_KBIT_PREFERRED(0b00000110),
    TRANSMISSION_2x64_KBIT_UNRESTRICTED(0b00000111),
    TRANSMISSION_384_KBIT_UNRESTRICTED(0b00001000),
    TRANSMISSION_1536_KBIT_UNRESTRICTED(0b00001001),
    TRANSMISSION_1920_KBIT_UNRESTRICTED(0b00001010),
    TRANSMISSION_3x64_KBIT_UNRESTRICTED(0b00010000),
    TRANSMISSION_4x64_KBIT_UNRESTRICTED(0b00010001),
    TRANSMISSION_5x64_KBIT_UNRESTRICTED(0b00010010),
    TRANSMISSION_7x64_KBIT_UNRESTRICTED(0b00010100),
    TRANSMISSION_8x64_KBIT_UNRESTRICTED(0b00010101),
    TRANSMISSION_9x64_KBIT_UNRESTRICTED(0b00010110),
    TRANSMISSION_10b64_KBIT_UNRESTRICTED(0b00010111),
    TRANSMISSION_11x64_KBIT_UNRESTRICTED(0b00011000),
    TRANSMISSION_12x64_KBIT_UNRESTRICTED(0b00011001),
    TRANSMISSION_13x64_KBIT_UNRESTRICTED(0b00011010),
    TRANSMISSION_14x64_KBIT_UNRESTRICTED(0b00011011),
    TRANSMISSION_15x64_KBIT_UNRESTRICTED(0b00011100),
    TRANSMISSION_16x64_KBIT_UNRESTRICTED(0b00011101),
    TRANSMISSION_17x64_KBIT_UNRESTRICTED(0b00011110),
    TRANSMISSION_18x64_KBIT_UNRESTRICTED(0b00011111),
    TRANSMISSION_19x64_KBIT_UNRESTRICTED(0b00100000),
    TRANSMISSION_20b64_KBIT_UNRESTRICTED(0b00100001),
    TRANSMISSION_21x64_KBIT_UNRESTRICTED(0b00100010),
    TRANSMISSION_22x64_KBIT_UNRESTRICTED(0b00100011),
    TRANSMISSION_23x64_KBIT_UNRESTRICTED(0b00100100),
    TRANSMISSION_25x64_KBIT_UNRESTRICTED(0b00100110),
    TRANSMISSION_26x64_KBIT_UNRESTRICTED(0b00100111),
    TRANSMISSION_27x64_KBIT_UNRESTRICTED(0b00101000),
    TRANSMISSION_28x64_KBIT_UNRESTRICTED(0b00101001),
    TRANSMISSION_29x64_KBIT_UNRESTRICTED(0b00101010),
    UNKNOWN(-1);

    private final int value;

    private TransmissionMediumRequirement(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static TransmissionMediumRequirement getInstance(int value) {
        switch (value) {
            case (0b00000000):
                return TRANSMISSION_SPEECH;
            case (0b00000010):
                return TRANSMISSION_64_KBIT_UNRESTRICTED;
            case (0b00000011):
                return TRANSMISSION_3_1_KHZ_AUDIO;
            case (0b00000100):
                return TRANSMISSION_SERVICE_1;
            case (0b00000101):
                return TRANSMISSION_SERVICE_2;
            case (0b00000110):
                return TRANSMISSION_64_KBIT_PREFERRED;
            case (0b00000111):
                return TRANSMISSION_2x64_KBIT_UNRESTRICTED;
            case (0b00001000):
                return TRANSMISSION_384_KBIT_UNRESTRICTED;
            case (0b00001001):
                return TRANSMISSION_1536_KBIT_UNRESTRICTED;
            case (0b00001010):
                return TRANSMISSION_1920_KBIT_UNRESTRICTED;
            case (0b00010000):
                return TRANSMISSION_3x64_KBIT_UNRESTRICTED;
            case (0b00010001):
                return TRANSMISSION_4x64_KBIT_UNRESTRICTED;
            case (0b00010010):
                return TRANSMISSION_5x64_KBIT_UNRESTRICTED;
            case (0b00010100):
                return TRANSMISSION_7x64_KBIT_UNRESTRICTED;
            case (0b00010101):
                return TRANSMISSION_8x64_KBIT_UNRESTRICTED;
            case (0b00010110):
                return TRANSMISSION_9x64_KBIT_UNRESTRICTED;
            case (0b00010111):
                return TRANSMISSION_10b64_KBIT_UNRESTRICTED;
            case (0b00011000):
                return TRANSMISSION_11x64_KBIT_UNRESTRICTED;
            case (0b00011001):
                return TRANSMISSION_12x64_KBIT_UNRESTRICTED;
            case (0b00011010):
                return TRANSMISSION_13x64_KBIT_UNRESTRICTED;
            case (0b00011011):
                return TRANSMISSION_14x64_KBIT_UNRESTRICTED;
            case (0b00011100):
                return TRANSMISSION_15x64_KBIT_UNRESTRICTED;
            case (0b00011101):
                return TRANSMISSION_16x64_KBIT_UNRESTRICTED;
            case (0b00011110):
                return TRANSMISSION_17x64_KBIT_UNRESTRICTED;
            case (0b00011111):
                return TRANSMISSION_18x64_KBIT_UNRESTRICTED;
            case (0b00100000):
                return TRANSMISSION_19x64_KBIT_UNRESTRICTED;
            case (0b00100001):
                return TRANSMISSION_20b64_KBIT_UNRESTRICTED;
            case (0b00100010):
                return TRANSMISSION_21x64_KBIT_UNRESTRICTED;
            case (0b00100011):
                return TRANSMISSION_22x64_KBIT_UNRESTRICTED;
            case (0b00100100):
                return TRANSMISSION_23x64_KBIT_UNRESTRICTED;
            case (0b00100110):
                return TRANSMISSION_25x64_KBIT_UNRESTRICTED;
            case (0b00100111):
                return TRANSMISSION_26x64_KBIT_UNRESTRICTED;
            case (0b00101000):
                return TRANSMISSION_27x64_KBIT_UNRESTRICTED;
            case (0b00101001):
                return TRANSMISSION_28x64_KBIT_UNRESTRICTED;
            case (0b00101010):
                return TRANSMISSION_29x64_KBIT_UNRESTRICTED;
            default:
                return UNKNOWN;

        }
    }
}
