/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum SatelliteIndicator {
    NO_SATELLITE_CIRCUIT(0),
    ONE_SATELLITE_CIRCUIT(1),
    TWO_SATELLITE_CIRCUIT(2),
    SPARE(3);

    private final int value;

    private SatelliteIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static SatelliteIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_SATELLITE_CIRCUIT;
            case 1:
                return ONE_SATELLITE_CIRCUIT;
            case 2:
                return TWO_SATELLITE_CIRCUIT;
            default:
                return SPARE;
        }
    }
}
