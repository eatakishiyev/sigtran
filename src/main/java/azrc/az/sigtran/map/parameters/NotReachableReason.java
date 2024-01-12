/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum NotReachableReason {

    MS_PURGED(0),
    IMSI_DETACHED(1),
    RESTRICTED_AREA(2),
    NOT_REGISTERED(3),
    UNKNOWN(-1);

    private int value;

    private NotReachableReason(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static NotReachableReason getInstance(int value) {
        switch (value) {
            case 0:
                return MS_PURGED;
            case 1:
                return IMSI_DETACHED;
            case 2:
                return RESTRICTED_AREA;
            case 3:
                return NOT_REGISTERED;
            default:
                return UNKNOWN;
        }
    }
}
