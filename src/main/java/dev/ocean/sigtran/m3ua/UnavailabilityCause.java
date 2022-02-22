/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

/**
 *
 * @author root
 */
public enum UnavailabilityCause {

    Unknown(0),
    UnequippedRemoteUser(1),
    InaccesibleRemoteUser(2);
    private final int cause;

    private UnavailabilityCause(int cause) {
        this.cause = cause;
    }

    public int getCause() {
        return cause;
    }

    public static UnavailabilityCause getInstance(int value) {
        switch (value) {
            case 0:
                return Unknown;
            case 1:
                return UnequippedRemoteUser;
            case 2:
                return InaccesibleRemoteUser;
            default:
                return Unknown;
        }
    }
}
