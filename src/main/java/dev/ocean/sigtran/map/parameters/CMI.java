/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum CMI {

    CSG_MEMBERSHIP(0),
    NON_CSG_MEMBERSHIP(1);
    private final int value;

    private CMI(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static CMI getInstance(int value) {
        switch (value) {
            case 0:
                return CSG_MEMBERSHIP;
            default:
                return NON_CSG_MEMBERSHIP;
        }
    }
}
