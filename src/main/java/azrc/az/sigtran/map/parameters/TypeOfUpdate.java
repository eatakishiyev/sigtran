/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum TypeOfUpdate {

    SGSN_CHANGE(0),
    MME_CHANGE(1);
    private final int value;

    private TypeOfUpdate(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static TypeOfUpdate getInstance(int value) {
        switch (value) {
            case 0:
                return SGSN_CHANGE;
            case 1:
                return MME_CHANGE;
            default:
                return null;
        }
    }
}
