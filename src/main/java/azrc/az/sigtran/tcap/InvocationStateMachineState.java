/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap;

public enum InvocationStateMachineState {

    IDLE(0),
    CLASS1(1),
    CLASS2(2),
    CLASS3(3),
    CLASS4(4),
    WAIT_FOR_REJECT(5);
    private int value;

    private InvocationStateMachineState(int value) {
        this.value = value;
    }

    public static InvocationStateMachineState getInstance(int value) {
        switch (value) {
            case 0:
                return IDLE;
            case 1:
                return CLASS1;
            case 2:
                return CLASS2;
            case 3:
                return CLASS3;
            case 4:
                return CLASS4;
            case 5:
                return WAIT_FOR_REJECT;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
