/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 * InterrogationType ::= ENUMERATED {
 * basicCall (0),
 * forwarding (1)}
 * @author eatakishiyev
 */
public enum InterrogationType {

    BASIC_CALL(0),
    FORWARDING(1),
    UNKNOWN(-1);

    private final int value;

    private InterrogationType(int value) {
        this.value = value;
    }

    public static InterrogationType getInstance(int value) {
        switch (value) {
            case 0:
                return BASIC_CALL;
            case 1:
                return FORWARDING;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return value;
    }
}
