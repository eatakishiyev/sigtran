/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum ParameterTag {

    INFO_STRING(0x0004),
    ROUTING_CONTEXT(0x0006),
    DIAGNOSTIC_INFORMATION(0x0007),
    HEARTBEAD_DATA(0x0009),
    TRAFFIC_MODE_TYPE(0x000b),
    ERROR_CODE(0x000c),
    STATUS(0x000d),
    ASP_IDENTIFIER(0x0011),
    AFFECTED_POINT_CODE(0x0012),
    CORRELATION_ID(0x0013),
    NETWORK_APPEARANCE(0x0200),
    USER_CAUSE(0x0204),
    CONGESTION_INDICATIONS(0x0205),
    CONCERNED_DESTINATIONS(0x0206),
    ROUTING_KEY(0x0207),
    REGISTRATION_RESULT(0x0208),
    DEREGISTRATION_RESULT(0x0209),
    LOCAL_ROUTING_KEY_IDENTIFIER(0x020a),
    DESTINATION_POINT_CODE(0x020b),
    SERVICE_INDICATORS(0x020c),
    ORIGINATING_POINT_CODE_LIST(0x020e),
    PROTOCOL_DATA(0x0210),
    REGISTRATION_STATUS(0x0212),
    DEREGISTRATION_STATUS(0x0213),
    RESERVED(0xffff);
    private int value;

    private ParameterTag(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static ParameterTag getInstance(int value) {
        switch (value) {
            case 0x0004:
                return INFO_STRING;
            case 0x0006:
                return ROUTING_CONTEXT;
            case 0x0007:
                return DIAGNOSTIC_INFORMATION;
            case 0x0009:
                return HEARTBEAD_DATA;
            case 0x000b:
                return TRAFFIC_MODE_TYPE;
            case 0x000c:
                return ERROR_CODE;
            case 0x000d:
                return STATUS;
            case 0x0011:
                return ASP_IDENTIFIER;
            case 0x0012:
                return AFFECTED_POINT_CODE;
            case 0x0013:
                return CORRELATION_ID;
            case 0x0200:
                return NETWORK_APPEARANCE;
            case 0x0204:
                return USER_CAUSE;
            case 0x0205:
                return CONGESTION_INDICATIONS;
            case 0x0206:
                return CONCERNED_DESTINATIONS;
            case 0x0207:
                return ROUTING_KEY;
            case 0x0208:
                return REGISTRATION_RESULT;
            case 0x0209:
                return DEREGISTRATION_RESULT;
            case 0x020a:
                return LOCAL_ROUTING_KEY_IDENTIFIER;
            case 0x020b:
                return DESTINATION_POINT_CODE;
            case 0x020c:
                return SERVICE_INDICATORS;
            case 0x020e:
                return ORIGINATING_POINT_CODE_LIST;
            case 0x0210:
                return PROTOCOL_DATA;
            case 0x0212:
                return REGISTRATION_STATUS;
            case 0x0213:
                return DEREGISTRATION_STATUS;
            default:
                return RESERVED;
        }
    }
}
