/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.address;

/**
 *
 * @author eatakishiyev
 */
public enum NatureOfAddress {

    /**
     * UNKNOWN = 0;
     */
    UNKNOWN((byte)0),
    /**
     * SUBSCRIBER_NUMBER = 1
     */
    SUBSCRIBER_NUMBER((byte)1),
    /**
     * RESERVED_FOR_NATIONAL_USE = 2
     */
    RESERVED_FOR_NATIONAL_USE((byte)2),
    /**
     * NATIONAL_SIGNIFICANT_NUMBER = 3
     */
    NATIONAL_SIGNIFICANT_NUMBER((byte)3),
    /**
     * INTERNATIONAL_NUMBER = 4
     */
    INTERNATIONAL_NUMBER((byte)4),
    /**
     * RESERVED = 5
     */
    RESERVED((byte)5),
    /**
     * ANY = -1. Used for internal purposes
     */
    ANY((byte)-1);
    /* 0000101 to 1101111 spare
     * 1110000 to 1111110 reserved for national use
     * 1111111 reserved
     */
    private final byte value;

    private NatureOfAddress(byte value) {
        this.value = value;
    }

    public static NatureOfAddress getInstance(byte value) {
        switch (value) {
            case 0x00:
                return UNKNOWN;
            case 0x01:
                return SUBSCRIBER_NUMBER;
            case 0x02:
                return RESERVED_FOR_NATIONAL_USE;
            case 0x03:
                return NATIONAL_SIGNIFICANT_NUMBER;
            case 0x04:
                return INTERNATIONAL_NUMBER;
            default:
                return RESERVED;
        }
    }

    public byte value() {
        return this.value;
    }
}
