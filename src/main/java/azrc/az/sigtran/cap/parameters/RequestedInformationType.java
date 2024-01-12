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
public enum RequestedInformationType {

    CALL_ATTEMPT_ELAPSED_TIME(0),
    CALL_STOP_TIME(1),
    CALL_CONNECTED_ELAPSED_TIME(2),
    RELEASE_CAUSE(30);

    private int value;

    private RequestedInformationType(int value) {
        this.value = value;
    }

    public static RequestedInformationType getInstance(int value) {
        switch (value) {
            case 0:
                return CALL_ATTEMPT_ELAPSED_TIME;
            case 1:
                return CALL_STOP_TIME;
            case 30:
                return RELEASE_CAUSE;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
