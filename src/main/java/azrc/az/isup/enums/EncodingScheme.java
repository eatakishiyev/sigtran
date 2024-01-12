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
public enum EncodingScheme {

    BCD_EVEN,
    BCD_ODD,
    IA5_CHARACTER,
    BINARY_CODE;

    public static EncodingScheme valueOf(int value) {
        switch (value) {
            case 0:
                return BCD_EVEN;
            case 1:
                return BCD_ODD;
            case 2:
                return IA5_CHARACTER;
            case 3:
                return BINARY_CODE;
            default:
                return IA5_CHARACTER;
        }
    }
}
