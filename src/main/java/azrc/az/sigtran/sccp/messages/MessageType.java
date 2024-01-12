/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.messages;

/**
 *
 * @author eatakishiyev
 */
public enum MessageType {

    CR(1),
    CC(2),
    CREF(3),
    RLSD(4),
    RLC(5),
    DT1(6),
    DT2(7),
    AK(8),
    UDT(9),
    UDTS(10),
    ED(11),
    EA(12),
    RSR(13),
    RSC(14),
    ERR(15),
    IT(16),
    XUDT(17),
    XUDTS(18),
    LUDT(19),
    LUDTS(20),
    UNKNOWN(-1);
    private int value;

    private MessageType(int value) {
        this.value = value;
    }

    public static MessageType getInstance(int value) {
        switch (value) {
            case 1:
                return CR;
            case 2:
                return CC;
            case 3:
                return CREF;
            case 4:
                return RLSD;
            case 5:
                return RLC;
            case 6:
                return DT1;
            case 7:
                return DT2;
            case 8:
                return AK;
            case 9:
                return UDT;
            case 10:
                return UDTS;
            case 11:
                return ED;
            case 12:
                return EA;
            case 13:
                return RSR;
            case 14:
                return RSC;
            case 15:
                return ERR;
            case 16:
                return IT;
            case 17:
                return XUDT;
            case 18:
                return XUDTS;
            case 19:
                return LUDT;
            case 20:
                return LUDTS;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
