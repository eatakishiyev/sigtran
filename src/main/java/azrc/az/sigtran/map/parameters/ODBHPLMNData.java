/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum ODBHPLMNData {

    plmnSpecificBarringType1(0),
    plmnSpecificBarringType2(1),
    plmnSpecificBarringType3(2),
    plmnSpecificBarringType4(3);
    private int value;

    private ODBHPLMNData(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static ODBHPLMNData getInstance(int value) {
        switch (value) {
            case 0:
                return plmnSpecificBarringType1;
            case 1:
                return plmnSpecificBarringType2;
            case 2:
                return plmnSpecificBarringType3;
            case 3:
                return plmnSpecificBarringType4;
            default:
                return null;
        }
    }
}
