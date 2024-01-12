/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

/**
 *
 * @author root
 */
public enum MessageClass {

    MGMT(0),
    TRANSFER_MESSAGE(1),
    SSNM(2),
    ASPSM(3),
    ASPTM(4),
    RKM(9),
    UNKNOWN(-1);
    int value;

    MessageClass(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static MessageClass getInstance(int value) {
        switch (value) {
            case 0:
                return MGMT;
            case 1:
                return TRANSFER_MESSAGE;
            case 2:
                return SSNM;
            case 3:
                return ASPSM;
            case 4:
                return ASPTM;
            case 9:
                return RKM;
            default:
                return UNKNOWN;
        }
    }
}
