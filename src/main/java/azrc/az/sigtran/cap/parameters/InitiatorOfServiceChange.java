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
public enum InitiatorOfServiceChange {

    A_SIDE(0),
    B_SIDE(1);

    private int value;

    private InitiatorOfServiceChange(int value) {
        this.value = value;
    }

    public static InitiatorOfServiceChange getInstance(int value) {
        switch (value) {
            case 0:
                return A_SIDE;
            case 1:
                return B_SIDE;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
