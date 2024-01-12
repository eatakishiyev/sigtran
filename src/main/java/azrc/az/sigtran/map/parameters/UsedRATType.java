/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 * Used-RAT-Type::= ENUMERATED {
 * utran (0),
 * geran (1),
 * gan (2),
 * i-hspa-evolution (3),
 * e-utran (4),
 * ...}
 * @author eatakishiyev
 */
public enum UsedRATType {

    UTRAN(0),
    GERAN(1),
    GAN(2),
    I_HSPA_EVOLUTION(3),
    E_UTRAN(4),
    UNKNOWN(-1);

    private final int value;

    private UsedRATType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static final UsedRATType getInstance(int value) {
        switch (value) {
            case 0:
                return UTRAN;
            case 1:
                return GERAN;
            case 2:
                return GAN;
            case 3:
                return I_HSPA_EVOLUTION;
            case 4:
                return E_UTRAN;
            default:
                return UNKNOWN;
        }
    }
}
