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
public enum EventIndicator {
    ALERTING(0b00000001),
    PROGRESS(0b00000010),
    IN_BAND_INFORMATION_OR_ANY_APPROPRIATE_PATTERN_IS_NOW_AVAILABLE(0b00000011),
    CALL_FORWARDED_ON_BUSY(0b00000100),
    CALL_FORWARDED_ON_NO_REPLY(0b00000101),
    CALL_FORWARDED_UNCONDITIONAL(0b00000110),
    UNKNOWN(-1);

    private final int value;

    private EventIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static EventIndicator getInstance(int value) {
        switch (value) {
            case 0b00000001:
                return ALERTING;
            case 0b00000010:
                return PROGRESS;
            case 0b00000011:
                return IN_BAND_INFORMATION_OR_ANY_APPROPRIATE_PATTERN_IS_NOW_AVAILABLE;
            case 0b00000100:
                return CALL_FORWARDED_ON_BUSY;
            case 0b00000101:
                return CALL_FORWARDED_ON_NO_REPLY;
            case 0b00000110:
                return CALL_FORWARDED_UNCONDITIONAL;
            default:
                return UNKNOWN;
        }
    }
}
