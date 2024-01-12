/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

/**
 *
 * @author root
 */
public enum ServiceIdentificator {

    SCCP(3),
    TUP(4),
    ISUP(5),
    BROADBAND_ISUP(9),
    SATELLITE_ISUP(10),
    AAL_TYPE2_SIGNALLING(12),
    BICC(13),
    GATEWAY_CONTROL_PROTOCOL(14),
    RESERVED(1);
    private final int value;

    ServiceIdentificator(int value) {
        this.value = value;
    }

    public static ServiceIdentificator getInstance(int value) {
        switch (value) {
            case 3:
                return SCCP;
            case 4:
                return TUP;
            case 5:
                return ISUP;
            case 9:
                return BROADBAND_ISUP;
            case 10:
                return SATELLITE_ISUP;
            case 12:
                return AAL_TYPE2_SIGNALLING;
            case 13:
                return BICC;
            case 14:
                return GATEWAY_CONTROL_PROTOCOL;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
