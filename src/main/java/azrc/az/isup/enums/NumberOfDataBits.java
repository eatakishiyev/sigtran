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
public enum NumberOfDataBits {
    NOT_USED(0),
    _5BITS(1),
    _7BITS(2),
    _8BITS(3);

    private final int value;

    private NumberOfDataBits(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static NumberOfDataBits getInstance(int value) {
        switch (value) {
            case 0:
                return NOT_USED;
            case 1:
                return _5BITS;
            case 2:
                return _7BITS;
            case 3:
                return _8BITS;
            default:
                return NOT_USED;
        }
    }
}
