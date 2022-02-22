/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 * MmDetectionPoint ::= INTEGER (0..255)
 * -- 0: Location registration
 * -- 1: IMSI detach
 * -- Other values are not used.
 */
public enum MmDetectionPoint {

    LOCATION_REGISTRATION(0),
    IMSI_DETACH(1);

    private final int value;

    private MmDetectionPoint(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static MmDetectionPoint getInstance(int value) {
        if (value == 0) {
            return LOCATION_REGISTRATION;
        } else if (value == 1) {
            return IMSI_DETACH;
        }
        return null;
    }
}
