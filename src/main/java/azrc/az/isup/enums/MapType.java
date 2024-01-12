/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.enums;

/**
 *
 * @author eatakishiyev
 */
public enum MapType {
    MAP_FORMAT_1544_KBIT_S_DIGITAL_PATH(1),
    MAP_FORMAT_2048_KBIT_s_DIGITAL_PATH(2),
    UNKNOWN(-1);

    private final int value;

    private MapType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static MapType getInstance(int value) {
        switch (value) {
            case 1:
                return MAP_FORMAT_1544_KBIT_S_DIGITAL_PATH;
            case 2:
                return MAP_FORMAT_2048_KBIT_s_DIGITAL_PATH;
            default:
                return UNKNOWN;
        }
    }
}
