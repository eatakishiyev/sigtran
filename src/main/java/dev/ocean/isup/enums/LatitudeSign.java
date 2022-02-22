/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.enums;

public enum LatitudeSign {

    NORTH(0),
    SOUTH(1),
    UNKNOWN(-1);

    private int value;

    private LatitudeSign(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static LatitudeSign getInstance(int value) {
        switch (value) {
            case 0:
                return NORTH;
            case 1:
                return SOUTH;
            default:
                return UNKNOWN;
        }
    }
}
