/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.router;

/**
 *
 * @author eatakishiyev
 */
public enum RouteMode {

    LOADSHARE,
    BACKUP,
    BROADCAST,
    UNKNOWN;

    private RouteMode() {
    }

    public static RouteMode getInstance(String value) {
        switch (value.toUpperCase().trim()) {
            case "LOADSHARE":
                return LOADSHARE;
            case "BACKUP":
                return BACKUP;
            case "BROADCAST":
                return BROADCAST;
            default:
                return UNKNOWN;
        }
    }
}
