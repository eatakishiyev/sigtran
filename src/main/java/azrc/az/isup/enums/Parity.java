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
public enum Parity {
    ODD(0b00000000),
    EVEN(0b00000010),
    NONE(0b00000011),
    FORCED_TO_0(0b00000100),
    FORCED_TO_1(0b00000101);

    private final int value;

    private Parity(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static Parity getInstance(int value) {
        switch (value) {
            case 0b00000000:
                return ODD;
            case 0b00000010:
                return EVEN;
            case 0b00000011:
                return NONE;
            case 0b00000100:
                return FORCED_TO_0;
            case 0b00000101:
                return FORCED_TO_1;
            default:
                return NONE;
        }
    }
}
