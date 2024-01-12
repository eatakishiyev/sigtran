/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors.params;

/**
 *
 * @author eatakishiyev
 */
public enum SMEnumeratedDeliveryFailureCause {

    MEMORY_CAPACITY_EXCEEDED(0),
    EQUIPMENT_PROTOCOL_ERROR(1),
    EQUIPMENT_NOT_SM_EQUIPPED(2),
    UNKNOWN_SERVICE_CENTRE(3),
    SC_CONGESTION(4),
    INVALID_SME_ADDRESS(5),
    SUBSCRIBER_NOT_SC_SUBSCRIBER(6),
    UNKNOWN(-1);
    private int value;

    private SMEnumeratedDeliveryFailureCause(int value) {
        this.value = value;
    }

    public static SMEnumeratedDeliveryFailureCause getInstance(int value) {
        switch (value) {
            case 0:
                return MEMORY_CAPACITY_EXCEEDED;
            case 1:
                return EQUIPMENT_PROTOCOL_ERROR;
            case 2:
                return EQUIPMENT_NOT_SM_EQUIPPED;
            case 3:
                return UNKNOWN_SERVICE_CENTRE;
            case 4:
                return SC_CONGESTION;
            case 5:
                return INVALID_SME_ADDRESS;
            case 6:
                return SUBSCRIBER_NOT_SC_SUBSCRIBER;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
