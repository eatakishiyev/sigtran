/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum TransmissionMediumRequirementPrime {

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
    UNKNOWN(-1);

    private final int value;

    private TransmissionMediumRequirementPrime(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static TransmissionMediumRequirementPrime getInstance(int value) {
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
            default:
                return UNKNOWN;
        }
    }
}
