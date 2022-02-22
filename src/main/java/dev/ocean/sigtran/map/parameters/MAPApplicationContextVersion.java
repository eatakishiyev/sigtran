/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum MAPApplicationContextVersion {

    VERSION_1(1),
    VERSION_2(2),
    VERSION_3(3),
    VERSION_4(4),
    UNKNOWN(-1);
    private final int value;

    private MAPApplicationContextVersion(int value) {
        this.value = value;
    }

    public static MAPApplicationContextVersion getInstance(int value) {
        switch (value) {
            case 1:
                return VERSION_1;
            case 2:
                return VERSION_2;
            case 3:
                return VERSION_3;
            case 4:
                return VERSION_4;
            default:
                return UNKNOWN;
        }
    }

    /**
     * @return the value
     */
    public int value() {
        return value;
    }
}
