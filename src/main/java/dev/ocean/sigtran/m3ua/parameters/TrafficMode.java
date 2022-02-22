/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.parameters;

/**
 *
 * @author root
 */
public enum TrafficMode {

    OVERRIDE(1),
    LOADSHARE(2),
    BROADCAST(3),
    UNKNOWN(-1);
    private int value = 1;

    private TrafficMode(int value) {
        this.value = value;
    }

    public static TrafficMode getInstance(int value) {
        switch (value) {
            case 1:
                return OVERRIDE;
            case 2:
                return LOADSHARE;
            case 3:
                return BROADCAST;
            default:
                return UNKNOWN;
        }
    }

    public static TrafficMode getInstance(String value) {
        if (value == null) {
            return null;
        }
        
        switch (value.toLowerCase()) {
            case "override":
                return OVERRIDE;
            case "loadshare":
                return LOADSHARE;
            case "broadcast":
                return BROADCAST;
            default:
                return null;
        }
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("TrafficMode [%s]", value == 1 ? "OVERRIDE" : value == 2 ? "LOADSHARE" : value == 3 ? "BRODCAST" : "UNKNOWN");
    }

}
