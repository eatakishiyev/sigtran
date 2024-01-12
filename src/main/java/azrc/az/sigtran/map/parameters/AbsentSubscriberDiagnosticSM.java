/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class AbsentSubscriberDiagnosticSM implements MAPParameter {

    private AbsentSubscriberDiagnosticSMValues absentSubscriberDiagnosticSMValue;


    @Override
    public String toString() {
        return "AbsentSubscriberDiagnosticSM{" +
                "absentSubscriberDiagnosticSMValue=" + absentSubscriberDiagnosticSMValue +
                '}';
    }

    public AbsentSubscriberDiagnosticSM() {
    }

    public AbsentSubscriberDiagnosticSM(AbsentSubscriberDiagnosticSMValues absentSubscriberDiagnosticSM) {
        this.absentSubscriberDiagnosticSMValue = absentSubscriberDiagnosticSM;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeInteger(tagClass, tag, absentSubscriberDiagnosticSMValue.value);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            this.absentSubscriberDiagnosticSMValue = AbsentSubscriberDiagnosticSMValues.getInstance(((Long) ais.readInteger()).intValue());
            if (this.absentSubscriberDiagnosticSMValue == AbsentSubscriberDiagnosticSMValues.UNKNOWN) {
                throw new IncorrectSyntaxException("Unknown absent subscriber diagnostics sm value.");
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the absentSubscriberDiagnosticSM
     */
    public AbsentSubscriberDiagnosticSMValues getAbsentSubscriberDiagnosticSM() {
        return absentSubscriberDiagnosticSMValue;
    }

    /**
     * @param absentSubscriberDiagnosticSM the absentSubscriberDiagnosticSM to
     * set
     */
    public void setAbsentSubscriberDiagnosticSM(AbsentSubscriberDiagnosticSMValues absentSubscriberDiagnosticSM) {
        this.absentSubscriberDiagnosticSMValue = absentSubscriberDiagnosticSM;
    }

    public enum AbsentSubscriberDiagnosticSMValues {

        NO_PAGING_RESPONSE_VIA_MSC(0),
        IMSI_DETACH(1),
        ROAMING_RESTRICTION(2),
        DEREGISTERED_IN_THE_HLR_FOR_NON_GPRS(3),
        MS_PURGED_FOR_NON_GPRS(4),
        NO_PAGING_RESPONSE_VIA_SGSN(5),
        GPRS_DETACHED(6),
        DEREGISTERED_IN_THE_HLR_FOR_GPRS(7),
        MS_PURGED_FOR_GPRS(8),
        UNIDENTIFIED_SUBSCRIBER_VIA_THE_MSC(9),
        UNIDENTIFIED_SUBSCRIBER_VIA_THE_SGSN(10),
        DEREGISTERED_IN_THE_HSS_FOR_IMS(11),
        NO_RESPONSE_VIA_THE_IP_SM_GW(12),
        UNKNOWN(-1);
        private int value;

        private AbsentSubscriberDiagnosticSMValues(int value) {
            this.value = value;
        }

        public static AbsentSubscriberDiagnosticSMValues getInstance(int value) {
            switch (value) {
                case 0:
                    return NO_PAGING_RESPONSE_VIA_MSC;
                case 1:
                    return IMSI_DETACH;
                case 2:
                    return ROAMING_RESTRICTION;
                case 3:
                    return DEREGISTERED_IN_THE_HLR_FOR_NON_GPRS;
                case 4:
                    return MS_PURGED_FOR_NON_GPRS;
                case 5:
                    return NO_PAGING_RESPONSE_VIA_SGSN;
                case 6:
                    return GPRS_DETACHED;
                case 7:
                    return DEREGISTERED_IN_THE_HLR_FOR_GPRS;
                case 8:
                    return MS_PURGED_FOR_GPRS;
                case 9:
                    return UNIDENTIFIED_SUBSCRIBER_VIA_THE_MSC;
                case 10:
                    return UNIDENTIFIED_SUBSCRIBER_VIA_THE_SGSN;
                case 11:
                    return DEREGISTERED_IN_THE_HSS_FOR_IMS;
                case 12:
                    return NO_RESPONSE_VIA_THE_IP_SM_GW;
                default:
                    return UNKNOWN;
            }
        }
    }
}
