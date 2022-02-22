/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.general;

/**
 *
 * @author root
 */
public enum NumberingPlan {

    /*
     * UNKNOWN = 0
     */
    UNKNOWN((byte) 0x00),
    /*
     * ISDN_TELEPHONY = 1
     */
    ISDN_TELEPHONY((byte) 0x01),
    /*
     * GENERIC = 2
     */
    GENERIC((byte) 0x02),
    /*
     * DATA = 3
     */
    DATA((byte) 0x03),
    /*
     * TELEX = 4
     */
    TELEX((byte) 0x04),
    /*
     * MARITIME_MOBILE = 5
     */
    MARITIME_MOBILE((byte) 0x05),
    /*
     * LAND_MOBILE = 6
     */
    LAND_MOBILE((byte) 0x06),
    /*
     * ISDN_MOBILE = 8
     */
    ISDN_MOBILE((byte) 0x08),
    /*
     * PRIVATE_NETWORK = 14
     */
    PRIVATE_NETWORK((byte) 0xE),
    /*
     * RESERVED = 15
     */
    RESERVED((byte) 0xF),
    /*
     *ANY = 1. Used for internal purposes
     */
    ANY((byte) -1);
    private final byte NumberingPlan;

    private NumberingPlan(byte NumberingPlan) {
        this.NumberingPlan = NumberingPlan;
    }

    public static NumberingPlan getInstance(byte value) {
        switch (value) {
            case 0:
                return UNKNOWN;
            case 1:
                return ISDN_TELEPHONY;
            case 2:
                return GENERIC;
            case 3:
                return DATA;
            case 4:
                return TELEX;
            case 5:
                return MARITIME_MOBILE;
            case 6:
                return LAND_MOBILE;
            case 8:
                return ISDN_MOBILE;
            case 14:
                return PRIVATE_NETWORK;
            case 15:
                return RESERVED;
            default:
                return RESERVED;

        }
    }

    public byte value() {
        return this.NumberingPlan;
    }
}
