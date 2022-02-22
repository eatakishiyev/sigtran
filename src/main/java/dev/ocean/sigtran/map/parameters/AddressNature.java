/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.Serializable;

/**
 *
 * @author root
 */
public enum AddressNature implements Serializable {

    UNKNOWN(0),
    INTERNATIONAL_NUMBER(1),
    NATIONAL_SIGNIFICANT_NUMBER(2),
    NETWORK_SPECIFIC_NUMBER(3),
    SUBSCRIBER_NUMBER(4),
    RESERVED(5),
    ABBREVIATED_NUMBER(6),
    RESERVED_FOR_EXTENSION(7);
    private final int value;

    private AddressNature(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static AddressNature getAddressNature(int value) {
        switch (value) {
            case 0:
                return UNKNOWN;
            case 1:
                return INTERNATIONAL_NUMBER;
            case 2:
                return NATIONAL_SIGNIFICANT_NUMBER;
            case 3:
                return NETWORK_SPECIFIC_NUMBER;
            case 4:
                return SUBSCRIBER_NUMBER;
            case 5:
                return RESERVED;
            case 6:
                return ABBREVIATED_NUMBER;
            case 7:
                return RESERVED_FOR_EXTENSION;
            default:
                return null;
        }
    }
}
