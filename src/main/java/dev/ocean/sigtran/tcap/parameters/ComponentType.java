/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters;

/**
 *
 * @author root
 */
public enum ComponentType {

    INVOKE(1),
    RETURN_RESULT_LAST(2),
    RETURN_ERROR(3),
    REJECT(4),
    RETUR_RESULT_NOT_LAST(7);
    private int value;

    private ComponentType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static ComponentType getInstance(int value) {
        switch (value) {
            case 1:
                return INVOKE;
            case 2:
                return RETURN_RESULT_LAST;
            case 3:
                return RETURN_ERROR;
            case 4:
                return REJECT;
            case 7:
                return RETUR_RESULT_NOT_LAST;
            default:
                return null;
        }
    }
}
