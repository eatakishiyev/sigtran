/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors.params;

/**
 *
 * @author eatakishiyev
 */
public enum AbsentSubscriberReason {

    IMSI_DETACH(0),
    RESTRICTED_AREA(1),
    NO_PAGE_RESPONSE(2),
    PURGED_MS(3),
    MT_ROAMING_RETRY(4),
    BUSY_SUBSCRIBER(5),
    UNKNOWN(-1);
    private final int value;

    private AbsentSubscriberReason(int value) {
        this.value = value;
    }

    public static AbsentSubscriberReason getInstance(int value) {
        switch (value) {
            case 0:
                return IMSI_DETACH;
            case 1:
                return RESTRICTED_AREA;
            case 2:
                return NO_PAGE_RESPONSE;
            case 3:
                return PURGED_MS;
            case 4:
                return MT_ROAMING_RETRY;
            case 5:
                return BUSY_SUBSCRIBER;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return value;
    }
}
