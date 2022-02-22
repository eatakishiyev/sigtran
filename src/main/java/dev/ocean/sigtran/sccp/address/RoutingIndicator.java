/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.address;

/**
 *
 * @author eatakishiyev
 */
public enum RoutingIndicator {

    ROUTE_ON_SSN((byte)1),
    ROUTE_ON_GT((byte)0),
    UNKNOWN((byte)-1);
    private final byte value;

    private RoutingIndicator(byte value) {
        this.value = value;
    }

    public static RoutingIndicator getInstance(byte value) {
        switch (value) {
            case 1:
                return ROUTE_ON_SSN;
            case 0:
                return ROUTE_ON_GT;
            default:
                return null;
        }
    }

    /**
     * @return the routingIndicator
     */
    public byte value() {
        return value;
    }
}