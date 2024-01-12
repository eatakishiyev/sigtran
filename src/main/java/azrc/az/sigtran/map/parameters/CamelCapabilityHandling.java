/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum CamelCapabilityHandling {

    CAMEL_PHASE1(1),
    CAMEL_PHASE2(2),
    CAMEL_PHASE3(3),
    CAMEL_PHASE4(4);
    private final int value;

    private CamelCapabilityHandling(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static CamelCapabilityHandling getInstance(int value) {
        switch (value) {
            case 1:
                return CAMEL_PHASE1;
            case 2:
                return CAMEL_PHASE2;
            case 3:
                return CAMEL_PHASE3;
            case 4:
                return CAMEL_PHASE4;
            default:
                return null;
        }
    }
}
