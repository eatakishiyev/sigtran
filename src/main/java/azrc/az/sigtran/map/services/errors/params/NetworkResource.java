/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors.params;

/**
 *
 * @author eatakishiyev
 */
public enum NetworkResource {

    /**
     * NetworkResource ::= ENUMERATED { plmn (0), hlr (1), vlr (2), pvlr (3),
     * controllingMSC (4), vmsc (5), eir (6), rss (7)}
     */
    PLMN(0),
    HLR(1),
    VLR(2),
    PVLR(3),
    CONTROLLING_MSC(4),
    VMSC(5),
    EIR(6),
    RSS(7),
    UNKNOWN(-1);
    private int value;

    private NetworkResource(int value) {
        this.value = value;
    }

    public static NetworkResource getInstance(int value) {
        switch (value) {
            case 0:
                return PLMN;
            case 1:
                return HLR;
            case 2:
                return VLR;
            case 3:
                return PVLR;
            case 4:
                return CONTROLLING_MSC;
            case 5:
                return VMSC;
            case 6:
                return EIR;
            case 7:
                return RSS;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
