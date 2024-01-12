/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public enum MTSMSTPDUType {

    smsDELIVER(0),
    smsSUBMITREPORT(1),
    smsSTATUSREPORT(2);
    private int value;

    private MTSMSTPDUType(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public static MTSMSTPDUType getInstance(int value) {
        switch (value) {
            case 0:
                return smsDELIVER;
            case 1:
                return smsSUBMITREPORT;
            case 2:
                return smsSTATUSREPORT;
            default:
                return null;

        }
    }
}
