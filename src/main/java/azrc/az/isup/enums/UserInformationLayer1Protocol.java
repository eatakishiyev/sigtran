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
public enum UserInformationLayer1Protocol {
    ITU_T_RATE_V110_I460_X30(0b00000001),
    G_711_MU_LAW(0b00000010),
    G_711_A_LAW(0b00000011),
    G_721_32KBIT(0b00000100),
    H_221_AND_H_242(0b00000101),
    H_223_AND_H_245(0b00000110),
    NON_ITU_T_RATE(0b00000111),
    ITU_T_RATE_V_120(0b00001000),
    ITU_T_RATE_X_31(0b00001001),
    UNKNOWN(-1);

    private final int value;

    private UserInformationLayer1Protocol(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static UserInformationLayer1Protocol getInstance(int value) {
        switch (value) {
            case 0b00000001:
                return ITU_T_RATE_V110_I460_X30;
            case 0b00000010:
                return G_711_MU_LAW;
            case 0b00000011:
                return G_711_A_LAW;
            case 0b00000100:
                return G_721_32KBIT;
            case 0b00000101:
                return H_221_AND_H_242;
            case 0b00000110:
                return H_223_AND_H_245;
            case 0b00000111:
                return NON_ITU_T_RATE;
            case 0b00001000:
                return ITU_T_RATE_V_120;
            case 0b00001001:
                return ITU_T_RATE_X_31;
            default:
                return UNKNOWN;
        }
    }
}
