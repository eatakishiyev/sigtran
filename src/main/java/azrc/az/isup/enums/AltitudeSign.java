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
public enum AltitudeSign {

    ABOVE_THE_ELLIPSOID(0),
    BELOW_THE_ELLIPSOID(1);

    private int value;

    private AltitudeSign(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static AltitudeSign getInstance(int value) {
        switch (value) {
            case 0:
                return ABOVE_THE_ELLIPSOID;
            default:
                return BELOW_THE_ELLIPSOID;
        }
    }
}
