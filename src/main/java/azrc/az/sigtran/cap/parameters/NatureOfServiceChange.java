/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum NatureOfServiceChange {

    USER_INITIATED(0),
    NETWORK_INITIATED(1);

    private int value;

    private NatureOfServiceChange(int value) {
        this.value = value;
    }

    public static NatureOfServiceChange getInstance(int value) {
        switch (value) {
            case 0:
                return USER_INITIATED;
            case 1:
                return NETWORK_INITIATED;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
