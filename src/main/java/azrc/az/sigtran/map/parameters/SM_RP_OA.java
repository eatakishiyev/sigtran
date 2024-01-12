/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * SM-RP-OA ::= CHOICE {
 * msisdn [2] ISDN-AddressString,
 * serviceCentreAddressOA [4] AddressString,
 * noSM-RP-OA [5] NULL}
 * @author eatakishiyev
 */
public class SM_RP_OA {

    private AddressStringImpl msisdn;
    private ISDNAddressString serviceCenterAddressAO;
    private boolean noSmRpOA;

    public SM_RP_OA() {
    }

    public SM_RP_OA(AddressStringImpl msisdn) {
        this.msisdn = msisdn;
    }

    public SM_RP_OA(ISDNAddressString serviceCenterAddressAO) {
        this.serviceCenterAddressAO = serviceCenterAddressAO;
    }

    /**
     * @return the serviceCenterAddressDA
     */
    public ISDNAddressString getServiceCenterAddressAO() {
        return serviceCenterAddressAO;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            if (this.msisdn != null) {
                this.msisdn.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            } else if (serviceCenterAddressAO != null) {
                serviceCenterAddressAO.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            } else {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 5);
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            ais.readTag();

            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
                throw new IncorrectSyntaxException();
            }

            switch (ais.getTag()) {
                case 0x02:
                    this.setMsisdn(new AddressStringImpl());
                    this.getMsisdn().decode(ais);
                    break;
                case 0x04:
                    this.serviceCenterAddressAO = new ISDNAddressString();
                    this.serviceCenterAddressAO.decode(ais);
                    break;
                case 0x05:
                    ais.readNull();
                    this.noSmRpOA = true;
                    break;
                default:
                    throw new IncorrectSyntaxException("Unknown address type");
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the msisdn
     */
    public AddressStringImpl getMsisdn() {
        return msisdn;
    }

    /**
     * @param msisdn the msisdn to set
     */
    public void setMsisdn(AddressStringImpl msisdn) {
        this.msisdn = msisdn;
    }

    public boolean isNoSmRpOA() {
        return noSmRpOA;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SM-RP-OA[");
        if (msisdn != null) {
            sb.append("Msisdn=").append(msisdn.toString());
        } else if (serviceCenterAddressAO != null) {
            sb.append("ServiceCentreAddressOA=").append(serviceCenterAddressAO.toString());
        } else {
            sb.append("noSM-RP-OA");
        }
        sb.append("]");
        return sb.toString();
    }

}
