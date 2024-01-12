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
 * RoutingInfo ::= CHOICE {
 * roamingNumber ISDN-AddressString,
 * forwardingData ForwardingData}
 *
 * @author eatakishiyev
 */
public class RoutingInfo {

    private ISDNAddressString roamingNumber;
    private ForwardingData forwardingData;

    public RoutingInfo() {
    }

    public RoutingInfo(ISDNAddressString roamingNumber) {
        this.roamingNumber = roamingNumber;
    }

    public RoutingInfo(ForwardingData forwardingData) {
        this.forwardingData = forwardingData;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (roamingNumber != null) {
            roamingNumber.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
        } else {
            forwardingData.encode(aos);
        }
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            this.encode(aos);
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            this.decode(ais, tag);
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais, int tag) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.decodeRoamingNumber(ais);
            } else if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.decodeForwardingData(ais.readSequenceStream());
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, ais.getTagClass()));
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decodeRoamingNumber(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        this.roamingNumber = new ISDNAddressString(ais);
    }

    public void decodeForwardingData(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        this.forwardingData = new ForwardingData();
        forwardingData.decode(ais);
    }

    public ISDNAddressString getRoamingNumber() {
        return roamingNumber;
    }

    public ForwardingData getForwardingData() {
        return forwardingData;
    }

}
