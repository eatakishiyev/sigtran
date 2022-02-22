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
public enum TypeOfShape {

    ELLIPSOID_POINT(0),
    ELLIPSOID_POINT_WITH_UNCERTAINTY(1),
    POINT_WITH_ALTITUDE_AND_UNCERTAINTY(2),
    ELLIPSE_ON_THE_ELLOPSOID(3),
    ELLIPSOID_CIRCLE_SECTOR(4),
    POLYGON(5),
    UNKNOWN(-1);

    private final int value;

    private TypeOfShape(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static TypeOfShape getInstance(int value) {
        switch (value) {
            case 0:
                return ELLIPSOID_POINT;
            case 1:
                return ELLIPSOID_POINT_WITH_UNCERTAINTY;
            case 2:
                return POINT_WITH_ALTITUDE_AND_UNCERTAINTY;
            case 3:
                return ELLIPSE_ON_THE_ELLOPSOID;
            case 4:
                return ELLIPSOID_CIRCLE_SECTOR;
            case 5:
                return POLYGON;
            default:
                return UNKNOWN;
        }
    }
}
