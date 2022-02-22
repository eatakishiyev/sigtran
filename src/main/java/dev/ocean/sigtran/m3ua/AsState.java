/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

/**
 *
 * @author eatakishiyev
 */
public enum AsState {

    AS_DOWN("AS_DOWN"),
    AS_INACTIVE("AS_INACTIVE"),
    AS_ACTIVE("AS_ACTIVE"),
    AS_PENDIGN("AS_PENDING"),
    UNKNOWN("UNKNOWN");
    private String value;

    private AsState(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static AsState getInstance(String value) {   
        switch (value.toUpperCase()) {
            case "AS_DOWN":
                return AS_DOWN;
            case "AS_INACTIVE":
                return AS_INACTIVE;
            case "AS_ACTIVE":
                return AS_ACTIVE;
            case "AS_PENDING":
                return AS_PENDIGN;
            default:
                return UNKNOWN;
        }
    }
}
