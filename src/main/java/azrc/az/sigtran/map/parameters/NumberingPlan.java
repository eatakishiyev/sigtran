/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.Serializable;

/**
 *
 * @author root
 */
public enum NumberingPlan implements Serializable {

    UNKNOWN(0),
    ISDN(1),
    SPARE_2(2),
    DATA(3),
    TELEX(4),
    SPARE_5(5),
    LAND_MOBILE(6),
    SPARE_7(7),
    NATIONAL(8),
    PRIVATE_PLAN(9),
    RESERVED(15);
    private int value;

    private NumberingPlan(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static NumberingPlan getNumberingPlan(int value) {
        switch (value) {
            case 0:
                return UNKNOWN;
            case 1:
                return ISDN;
            case 2:
                return SPARE_2;
            case 3:
                return DATA;
            case 4:
                return TELEX;
            case 5:
                return SPARE_5;
            case 6:
                return LAND_MOBILE;
            case 7:
                return SPARE_7;
            case 8:
                return NATIONAL;
            case 9:
                return PRIVATE_PLAN;
            case 15:
                return RESERVED;
            default:
                return null;
        }

    }
}
