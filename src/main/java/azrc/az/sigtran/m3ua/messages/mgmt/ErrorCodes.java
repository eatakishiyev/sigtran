/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.messages.mgmt;

/**
 *
 * @author eatakishiyev
 */
public enum ErrorCodes {

    INVALID_VERSION(0x01),
    UNSUPPORTED_MESSAGE_CLASS(0x03),
    UNSUPPORTED_MESSAGE_TYPE(0x04),
    UNSUPPORTED_TRAFFIC_MODE_TYPE(0x05),
    UNEXPECTED_MESSAGE(0x06),
    PROTOCOL_ERROR(0x07),
    INVALID_STREAM_IDENTIFIER(0x09),
    REFUSED_MANAGEMENT_BLOCKING(0x0d),
    ASP_IDENTIFIER_REQUIRED(0x0e),
    INVALID_ASP_IDENTIFIER(0x0f),
    INVALID_PARAMETER_VALUE(0x11),
    PARAMETER_FIELD_ERROR(0x12),
    UNEXPECTED_PARAMETER(0x13),
    DESTINATION_STATUS_UNKNOWN(0x14),
    INVALID_NETWORK_APPEARANCE(0x15),
    MISSING_PARAMETER(0x16),
    INVALID_ROUTING_CONTEXT(0x19),
    NO_CONFIGURED_AS_FOR_ASP(0x1a);
    private final int value;

    private ErrorCodes(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static ErrorCodes getInstance(int value) {
        switch (value) {
            case 0x01:
                return INVALID_VERSION;
            case 0x03:
                return UNSUPPORTED_MESSAGE_CLASS;
            case 0x04:
                return UNSUPPORTED_MESSAGE_TYPE;
            case 0x05:
                return UNSUPPORTED_TRAFFIC_MODE_TYPE;
            case 0x06:
                return UNEXPECTED_MESSAGE;
            case 0x07:
                return PROTOCOL_ERROR;
            case 0x09:
                return INVALID_STREAM_IDENTIFIER;
            case 0x0d:
                return REFUSED_MANAGEMENT_BLOCKING;
            case 0x0e:
                return ASP_IDENTIFIER_REQUIRED;
            case 0x0f:
                return INVALID_ASP_IDENTIFIER;
            case 0x11:
                return INVALID_PARAMETER_VALUE;
            case 0x12:
                return PARAMETER_FIELD_ERROR;
            case 0x13:
                return UNEXPECTED_PARAMETER;
            case 0x14:
                return DESTINATION_STATUS_UNKNOWN;
            case 0x15:
                return INVALID_NETWORK_APPEARANCE;
            case 0x16:
                return MISSING_PARAMETER;
            case 0x19:
                return INVALID_ROUTING_CONTEXT;
            case 0x1a:
                return NO_CONFIGURED_AS_FOR_ASP;
            default:
                return null;
        }
    }
}
