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
public enum InformationTransferRate {
    ALL_RATES(0b00000000),
    RATE_64KBIT(0b00010000),
    RATE_2x64KBIT(0b00010001),
    RATE_384KBIT(0b000100011),
    RATE_1536KBIT(0b00010101),
    RATE_1920KBIT(0b00010111),
    MULTIRATE_64KBIT_BASE(0b00011000),
    UNKNOWN(-1);

    private final int value;

    private InformationTransferRate(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static InformationTransferRate getInstance(int value) {
        switch (value) {
            case 0b00000000:
                return ALL_RATES;
            case 0b00010000:
                return RATE_64KBIT;
            case 0b00010001:
                return RATE_2x64KBIT;
            case 0b000100011:
                return RATE_384KBIT;
            case 0b00010101:
                return RATE_1536KBIT;
            case 0b00010111:
                return RATE_1920KBIT;
            case 0b00011000:
                return MULTIRATE_64KBIT_BASE;
            default:
                return UNKNOWN;
        }
    }
}
