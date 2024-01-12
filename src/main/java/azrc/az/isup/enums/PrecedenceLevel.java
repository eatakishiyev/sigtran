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
public enum PrecedenceLevel {
    FLASH_OVERRIDE(0),
    FLASH(1),
    IMMEDIATE(2),
    PRIORITY(3),
    ROUTINE(4),
    UNKNOWN(-1);

    private final int value;

    private PrecedenceLevel(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static PrecedenceLevel getInstance(int value) {
        switch (value) {
            case 0:
                return FLASH_OVERRIDE;
            case 1:
                return FLASH;
            case 2:
                return IMMEDIATE;
            case 3:
                return PRIORITY;
            case 4:
                return ROUTINE;
            default:
                return UNKNOWN;
        }
    }
}
