/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum CancellationType {

    UPDATE_PROCEDURE(0),
    SUBSCRIBTION_WITHDRAW(1),
    INITIAL_ATTACHMENT_PROCEDURE(2);
    private final int value;

    private CancellationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static CancellationType getInstance(int value) {
        switch (value) {
            case 0:
                return UPDATE_PROCEDURE;
            case 1:
                return SUBSCRIBTION_WITHDRAW;
            case 2:
                return INITIAL_ATTACHMENT_PROCEDURE;
            default:
                return null;
        }
    }

}
