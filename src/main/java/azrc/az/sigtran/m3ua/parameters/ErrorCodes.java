/*
 *Tochangethistemplate,chooseTools|Templates
 *andopenthetemplateintheeditor.
 */
package azrc.az.sigtran.m3ua.parameters;

/**
 *
 * @authoreatakishiyev
 */
public enum ErrorCodes {

    InvalidVersion(0x01),
    UnsupportedMessageClass(0x03),
    UnsupportedMessageType(0x04),
    UnsupportedTrafficModeType(0x05),
    UnexpectedMessage(0x06),
    ProtocolError(0x07),
    InvalidStreamIdentifier(0x09),
    RefusedManagementBlocking(0x0d),
    ASPIdentifierRequired(0x0e),
    InvalidASPIdentifier(0x0f),
    InvalidParameterValue(0x11),
    ParameterFieldError(0x12),
    UnexpectedParameter(0x13),
    DestinationStatusUnknown(0x14),
    InvalidNetworkAppearance(0x15),
    MissingParameter(0x16),
    InvalidRoutingContext(0x19),
    NoConfiguredASforASP(0x1a),
    Reserved(0x00);
    private int value;

    private ErrorCodes(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static ErrorCodes getInstance(int value) {
        switch (value) {
            case 0x01:
                return InvalidVersion;
            case 0x03:
                return UnsupportedMessageClass;
            case 0x04:
                return UnsupportedMessageType;
            case 0x05:
                return UnsupportedTrafficModeType;
            case 0x06:
                return UnexpectedMessage;
            case 0x07:
                return ProtocolError;
            case 0x09:
                return InvalidStreamIdentifier;
            case 0x0d:
                return RefusedManagementBlocking;
            case 0x0e:
                return ASPIdentifierRequired;
            case 0x0f:
                return InvalidASPIdentifier;
            case 0x11:
                return InvalidParameterValue;
            case 0x12:
                return ParameterFieldError;
            case 0x13:
                return UnexpectedParameter;
            case 0x14:
                return DestinationStatusUnknown;
            case 0x15:
                return InvalidNetworkAppearance;
            case 0x16:
                return MissingParameter;
            case 0x19:
                return InvalidRoutingContext;
            case 0x1a:
                return NoConfiguredASforASP;
            default:
                return Reserved;
        }
    }
}
