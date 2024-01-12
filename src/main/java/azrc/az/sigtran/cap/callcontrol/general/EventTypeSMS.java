/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.general;

/**
 *
 * @author eatakishiyev
 */
public enum EventTypeSMS {

    SMS_COLLECT_INFO(1),
    O_SMS_FAILURE(2),
    O_SMS_SUBMISSION(3),
    SMS_DELIVERY_REQUESTED(11),
    T_SMS_FAILURE(12),
    T_SMS_DELIVERY(13);

    private final int value;

    private EventTypeSMS(int value) {
        this.value = value;
    }

    public static EventTypeSMS getInstance(int value) {
        switch (value) {
            case 1:
                return SMS_COLLECT_INFO;
            case 2:
                return O_SMS_FAILURE;
            case 3:
                return O_SMS_SUBMISSION;
            case 11:
                return SMS_DELIVERY_REQUESTED;
            case 12:
                return T_SMS_FAILURE;
            case 13:
                return T_SMS_DELIVERY;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
