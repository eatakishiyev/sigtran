/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors.params;

/**
 *
 * @author eatakishiyev
 */
public enum UnknownSubscriberDiagnostic {

    IMSI_UNKNOWN(0),
    GPRS_EPS_SUBCRIPTION_UNKNOWN(1),
    NPDB_MISMATCH(2),
    UNKNOWN(-1);
    private int value;

    private UnknownSubscriberDiagnostic(int value) {
        this.value = value;
    }

    public static UnknownSubscriberDiagnostic getInstance(int value) {
        switch (value) {
            case 0:
                return IMSI_UNKNOWN;
            case 1:
                return GPRS_EPS_SUBCRIPTION_UNKNOWN;
            case 2:
                return NPDB_MISMATCH;
            default:
                return UNKNOWN;
        }
    }

    public int value() {
        return this.value;
    }
}
