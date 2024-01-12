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
public enum MOSMSCause {

    SYSTEM_FAILURE(0),
    UNEXPECTED_DATA_VALUE(1),
    FACILITY_NOT_SUPPORTED(2),
    SM_DELIVERY_FAILURE(3),
    RELEASE_FROM_RADIO_INTERFACE(4);

    private int value;

    private MOSMSCause(int value) {
        this.value = value;
    }

    public static MOSMSCause getInstance(int value) {
        switch (value) {
            case 0:
                return SYSTEM_FAILURE;
            case 1:
                return UNEXPECTED_DATA_VALUE;
            case 2:
                return FACILITY_NOT_SUPPORTED;
            case 3:
                return SM_DELIVERY_FAILURE;
            case 4:
                return RELEASE_FROM_RADIO_INTERFACE;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
