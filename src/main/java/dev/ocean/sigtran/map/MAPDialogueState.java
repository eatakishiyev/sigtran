/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map;

/**
 *
 * @author eatakishiyev
 */
public enum MAPDialogueState {

    IDLE(0),
    WAIT_FOR_USER_REQUESTS(1),
    DIALOGUE_INITIATED(2),
    WAIT_FOR_LOAD_CHECK(3),
    WAIT_FOR_INIT_DATA(4),
    DIALOGUE_ACCEPTED(5),
    DIALOGUE_ESTABLISHED(6),
    WAIT_FOR_RESULT(7),
    DIALOGUE_PENDING(8),
    CLOSING(9);

    private final int value;

    private MAPDialogueState(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static MAPDialogueState getInstance(int value) {
        switch (value) {
            case 0:
                return IDLE;
            case 1:
                return WAIT_FOR_USER_REQUESTS;
            case 2:
                return DIALOGUE_INITIATED;
            case 3:
                return WAIT_FOR_LOAD_CHECK;
            case 4:
                return WAIT_FOR_INIT_DATA;
            case 5:
                return DIALOGUE_ACCEPTED;
            case 6:
                return DIALOGUE_ESTABLISHED;
            case 7:
                return WAIT_FOR_RESULT;
            case 8:
                return DIALOGUE_PENDING;
            case 9:
                return CLOSING;
            default:
                return IDLE;
        }
    }

}
