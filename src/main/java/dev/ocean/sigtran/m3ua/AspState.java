/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

/**
 *
 * @author eatakishiyev
 */
public enum AspState {

    ASP_DOWN("ASP_DOWN"),
    ASP_INACTIVE("ASP_INACTIVE"),
    ASP_ACTIVE("ASP_ACTIVE"),
    UP_SENT("UP_SENT"),
    DOWN_SENT("DOWN_SENT"),
    ACTIVE_SENT("ACTIVE_SENT"),
    INACTIVE_SENT("INACTIVE_SENT");
    
    private final String value;

    private AspState(String value) {
        this.value = value;
    }

    public static AspState getInstance(String value) {
        switch (value) {
            case "ASP_DOWN":
                return ASP_DOWN;
            case "ASP_INACTIVE":
                return ASP_INACTIVE;
            case "ASP_ACTIVE":
                return ASP_ACTIVE;
            case "UP_SENT":
                return UP_SENT;
            case "DOWN_SENT":
                return DOWN_SENT;
            case "ACTIVE_SENT":
                return ACTIVE_SENT;
            case "INACTIVE_SENT":
                return INACTIVE_SENT;
            default:
                return null;
        }
    }

    public String value() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
