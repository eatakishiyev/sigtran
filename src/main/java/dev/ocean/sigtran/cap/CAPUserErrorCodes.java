/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap;

/**
 *
 * @author eatakishiyev
 */
public enum CAPUserErrorCodes {
 
    CANCELED(0),
    CANCEL_FAILED(1),
    ETC_FAILED(3),
    IMPROPER_CALLER_RESPONSE(4),
    MISSING_CUSTOMER_RECORD(6),
    MISSING_PARAMETER(7),
    PARAMETER_OUT_OF_RANGE(8),
    REQUESTED_INFO_ERROR(10),
    SYSTEM_FAILURE(11),
    TASK_REFUSED(12),
    UNAVAILABLE_RESOURCE(13),
    UNEXPECTED_COMPONENT_SEQUENCE(14),
    UNEXPECTED_DATA_VALUE(15),
    UNEXPECTED_PARAMETER(16),
    UNKNOWN_LEG_ID(17),
    UNKNOWN_PD_PID(50),
    UNKNOWN_CSID(51);
    private int value;

    private CAPUserErrorCodes(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static CAPUserErrorCodes getInstance(int value) {
        switch (value) {
            case 0:
                return CANCELED;
            case 1:
                return CANCEL_FAILED;
            case 3:
                return ETC_FAILED;
            case 4:
                return IMPROPER_CALLER_RESPONSE;
            case 6:
                return MISSING_CUSTOMER_RECORD;
            case 7:
                return MISSING_PARAMETER;
            case 8:
                return PARAMETER_OUT_OF_RANGE;
            case 10:
                return REQUESTED_INFO_ERROR;
            case 11:
                return SYSTEM_FAILURE;
            case 12:
                return TASK_REFUSED;
            case 13:
                return UNAVAILABLE_RESOURCE;
            case 14:
                return UNEXPECTED_COMPONENT_SEQUENCE;
            case 15:
                return UNEXPECTED_DATA_VALUE;
            case 16:
                return UNEXPECTED_PARAMETER;
            case 17:
                return UNKNOWN_LEG_ID;
            case 50:
                return UNKNOWN_PD_PID;
            case 51:
                return UNKNOWN_CSID;
            default:
                return null;
        }
    }
}
