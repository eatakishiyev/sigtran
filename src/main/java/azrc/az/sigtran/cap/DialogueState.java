/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap;

/**
 *
 * @author eatakishiyev
 */
public enum DialogueState {

    IDLE(0),
    WAIT_FOR_USER_REQUESTS(1),
    DIALOGUE_INITIATED(2),
    DIALOGUE_ESTABLISHED(3),
    DIALOGUE_PENDING(4),
    DIALOGUE_ACCEPTED(5);

    private final int value;

    private DialogueState(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static DialogueState getInstance(int value) {
        switch (value) {
            case 0:
                return IDLE;
            case 1:
                return WAIT_FOR_USER_REQUESTS;
            case 2:
                return DIALOGUE_INITIATED;
            case 3:
                return DIALOGUE_ESTABLISHED;
            case 4:
                return DIALOGUE_PENDING;
            case 5:
                return DIALOGUE_ACCEPTED;
            default:
                return IDLE;
        }
    }

}
