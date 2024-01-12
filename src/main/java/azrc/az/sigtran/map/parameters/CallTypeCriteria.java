/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 * CallTypeCriteria::= ENUMERATED {
 * FORWARDED (0),
 * NOT_FORWARDED (1)}
 *
 * @author eatakishiyev
 */
public enum CallTypeCriteria {

    FORWARDED(0),
    NOT_FORWARDED(1);
    private final int value;

    private CallTypeCriteria(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static CallTypeCriteria getInstance(int value) {
        switch (value) {
            case 0:
                return FORWARDED;
            case 1:
                return NOT_FORWARDED;
            default:
                return null;
        }
    }
}
