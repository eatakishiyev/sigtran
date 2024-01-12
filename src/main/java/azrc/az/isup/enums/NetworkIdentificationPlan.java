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
public enum NetworkIdentificationPlan {

    UNKNOWN(0),
    DNIC(3),
    MNIC(6);

    private  final int value;

    private NetworkIdentificationPlan(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static NetworkIdentificationPlan getInstance(int value) {
        switch (value) {
            case 0:
                return UNKNOWN;
            case 3:
                return DNIC;
            case 6:
                return MNIC;
            default:
                return UNKNOWN;
        }
    }
}
