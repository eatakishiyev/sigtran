/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum SCMGFormatIdentifiers {
    /**
     * Subsystem allowed
     */
    SSA(1),
    /**
     * Subsystem prohibited
     */
    SSP(2),
    /**
     * Subsystem status test
     */
    SST(3),
    /**
     * Subsystem out of Service request
     */
    SOR(4),
    /**
     * Subsystem out of service grant
     */
    SOG(5),
    /**
     * Subsystem congested
     */
    SSC(6),
    UNKNOWN(-1);
    private int value;

    private SCMGFormatIdentifiers(int value) {
        this.value = value;
    }

    public static SCMGFormatIdentifiers getInstance(int value) {
        switch (value) {
            case 1:
                return SSA;
            case 2:
                return SSP;
            case 3:
                return SST;
            case 4:
                return SOR;
            case 5:
                return SOG;
            case 6:
                return SSC;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return value;
    }
}
