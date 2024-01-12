/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum AccessMode {

    CLOSED_MODE(0),
    HYBRID_MODE(1),
    RESERVED_2(2),
    RESERVED_3(3),
    UNKNOWN(-1);

    private final int value;

    private AccessMode(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static AccessMode getInstance(int value) {
        switch (value) {
            case 0:
                return CLOSED_MODE;
            case 1:
                return HYBRID_MODE;
            case 2:
                return RESERVED_2;
            case 3:
                return RESERVED_3;
            default:
                return UNKNOWN;
        }
    }
}
