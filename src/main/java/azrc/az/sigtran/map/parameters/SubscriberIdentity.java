/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * SubscriberIdentity ::= CHOICE { imsi [0] IMSI, msisdn [1] ISDN-AddressString
 * }
 *
 * @author eatakishiyev
 */
public class SubscriberIdentity {

    private IMSI imsi;
    private ISDNAddressString msisdn;

    public SubscriberIdentity() {
    }

    public SubscriberIdentity(IMSI imsi) {
        this.imsi = imsi;
    }

    public SubscriberIdentity(ISDNAddressString msisdn) {
        this.msisdn = msisdn;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int pos = aos.StartContentDefiniteLength();

            if (imsi != null) {
                imsi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            } else if (msisdn != null) {
                msisdn.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            } else {
                throw new UnexpectedDataException("SubscriberIdentity is CHOICE type. There is IMSI or MSISDN parameter must be present");
            }
            aos.FinalizeContent(pos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            switch (tag) {
                case 0:
                    this.imsi = new IMSI(ais);
                    break;
                case 1:
                    this.msisdn = new ISDNAddressString(ais);
                    break;
                default:
                    throw new IncorrectSyntaxException("SubscriberIdentity is CHOICE type. IMSI or MSISDN must be present");
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public IMSI getImsi() {
        return imsi;
    }

    public ISDNAddressString getMsisdn() {
        return msisdn;
    }
}
