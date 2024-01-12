/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.Serializable;

/**
 *
 * @author eatakishiyev
 */
public enum SM_DeliveryNotIntended implements Serializable {

    ONLY_IMSI_REQUESTED(0),
    ONLY_MCC_MNC_REQUESTED(1);
    private int value;

    private SM_DeliveryNotIntended(int value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }

    public static SM_DeliveryNotIntended getInstance(int value) {
        switch (value) {
            case 0:
                return ONLY_IMSI_REQUESTED;
            case 1:
                return ONLY_MCC_MNC_REQUESTED;
            default:
                return null;
        }
    }
}
