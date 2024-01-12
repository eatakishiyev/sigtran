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
public enum PivotPossibleIndicator {
    NO_INDICATION(0),
    PIVOT_ROUTING_POSSIBLE_BEFORE_ACM(1),
    PIVOT_ROUTING_POSSIBLE_BEFORE_ANM(2),
    PIVOT_ROUTING_POSSIBLE_ANY_TIME_DURING_CALL(3),
    UNKNOWN(-1);

    private final int value;

    private PivotPossibleIndicator(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static PivotPossibleIndicator getInstance(int value) {
        switch (value) {
            case 0:
                return NO_INDICATION;
            case 1:
                return PIVOT_ROUTING_POSSIBLE_BEFORE_ACM;
            case 2:
                return PIVOT_ROUTING_POSSIBLE_BEFORE_ANM;
            case 3:
                return PIVOT_ROUTING_POSSIBLE_ANY_TIME_DURING_CALL;
            default:
                return UNKNOWN;
        }
    }
}
