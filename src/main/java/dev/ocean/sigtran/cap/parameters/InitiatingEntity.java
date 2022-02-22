/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum InitiatingEntity {

    MOBILE_STATION(0),
    SGSN(1),
    HLR(2),
    GGSN(3);

    private int value;

    private InitiatingEntity(int value) {
        this.value = value;
    }

    public static InitiatingEntity getInstance(int value) {
        switch (value) {
            case 0:
                return MOBILE_STATION;
            case 1:
                return SGSN;
            case 2:
                return HLR;
            case 3:
                return GGSN;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
