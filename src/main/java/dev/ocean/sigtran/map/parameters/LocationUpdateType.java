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
public enum LocationUpdateType {

    NORMAL_LOCATION_UPDATING(0),
    PERIODIC_LOCATION_UPDATING(1),
    IMSI_ATTACH(2);

    private final int value;

    private LocationUpdateType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static LocationUpdateType getInstance(int value) {
        switch (value) {
            case 0:
                return NORMAL_LOCATION_UPDATING;
            case 1:
                return PERIODIC_LOCATION_UPDATING;
            case 2:
                return IMSI_ATTACH;
            default:
                return null;
        }
    }

}
