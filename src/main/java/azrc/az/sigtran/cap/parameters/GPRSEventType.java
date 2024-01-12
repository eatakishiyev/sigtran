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
public enum GPRSEventType {

    ATTACH(1),
    ATTACH_CHANGE_OF_POSITION(2),
    DETACH(3),
    PDP_CONTEXT_ESTABLISHMENT(11),
    PDP_CONTEXT_ESTABLISHMENT_ACKNOWLEDGEMENT(12),
    DISCONNECT(13),
    PDP_CONTEXT_CHANGE_OF_POSITION(14);

    private int value;

    private GPRSEventType(int value) {
        this.value = value;
    }

    public static GPRSEventType getInstance(int value) {
        switch (value) {
            case 1:
                return ATTACH;
            case 2:
                return ATTACH_CHANGE_OF_POSITION;
            case 3:
                return DETACH;
            case 11:
                return PDP_CONTEXT_ESTABLISHMENT;
            case 12:
                return PDP_CONTEXT_ESTABLISHMENT_ACKNOWLEDGEMENT;
            case 13:
                return DISCONNECT;
            case 14:
                return PDP_CONTEXT_CHANGE_OF_POSITION;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
