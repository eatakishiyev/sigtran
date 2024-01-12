/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors.params;

/**
 *
 * @author eatakishiyev
 */
public enum AdditionalNetworkResource {

    SGSN(0),
    GGSN(1),
    GMLC(2),
    GSMSCF(3),
    NPLR(4),
    AUC(5),
    UE(6),
    MME(7),
    UNKNOWN(-1);
    private int value;

    private AdditionalNetworkResource(int value) {
        this.value = value;
    }

    public static AdditionalNetworkResource getInstance(int value) {
        switch (value) {
            case 0:
                return SGSN;
            case 1:
                return GGSN;
            case 2:
                return GMLC;
            case 3:
                return GSMSCF;
            case 4:
                return NPLR;
            case 5:
                return AUC;
            case 6:
                return UE;
            case 7:
                return MME;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
