/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum SubscriberStatusEnum {

    serviceGranted(0),
    operationDeterminedBarring(1);
    private int value;

    private SubscriberStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SubscriberStatusEnum getInstance(int value) {
        switch (value) {
            case 0:
                return serviceGranted;
            case 1:
                return operationDeterminedBarring;
            default:
                return null;
        }
    }
}
