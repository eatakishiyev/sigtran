/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters;

/**
 *
 * @author root
 */
public enum OperationCodeType {

    LOCAL(2),
    GLOBAL(6);
    private final int value;

    private OperationCodeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OperationCodeType getInstance(int value) {
        switch (value) {
            case 2:
                return LOCAL;
            case 6:
                return GLOBAL;
            default:
                return null;
        }
    }
}
