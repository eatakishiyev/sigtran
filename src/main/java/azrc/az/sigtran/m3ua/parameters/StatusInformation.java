/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum StatusInformation {

    /**
     * AS_STATE_CHANGE type values is: AS_INACTIVE, AS_ACTIVE, AS_PENDING. 
     * OTHER type values is:
     * INSUFFICIENT_ASP_RESOURCES_ACTIVE_IN_AS,ALTERNATE_ASP_ACTIVE,ASP_FAILURE
     *
     */
    RESERVED(1),
    INSUFFICIENT_ASP_RESOURCES_ACTIVE_IN_AS(1),
    AS_INACTIVE(2),
    ALTERNATE_ASP_ACTIVE(2),
    AS_ACTIVE(3),
    ASP_FAILURE(3),
    AS_PENDING(4),
    UNKNOWN(0);
    private final int value;

    private StatusInformation(int value) {
        this.value = value;
    }

    public static StatusInformation getInstance(StatusType type, int value) {
        switch (type) {
            case APPLICATION_SERVER_STATE_CHANGE:
                switch (value) {
                    case 1:
                        return RESERVED;
                    case 2:
                        return AS_INACTIVE;
                    case 3:
                        return AS_ACTIVE;
                    case 4:
                        return AS_PENDING;
                }
                break;
            case OTHER:
                switch (value) {
                    case 1:
                        return INSUFFICIENT_ASP_RESOURCES_ACTIVE_IN_AS;
                    case 2:
                        return ALTERNATE_ASP_ACTIVE;
                    case 3:
                        return ASP_FAILURE;
                }
                break;
        }
        return UNKNOWN;
    }

    /**
     * @return the value
     */
    public int value() {
        return value;
    }
}
