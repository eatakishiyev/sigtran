/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * ISDN-SubaddressString ::=
 * OCTET STRING (SIZE (1..maxISDN-SubaddressLength))
 * -- This type is used to represent ISDN subaddresses.
 * -- It is composed of
 * -- a) one octet for type of subaddress and odd/even indicator.
 * -- b) 20 octets for subaddress information.
 * -- a) The first octet includes a one bit extension indicator, a
 * -- 3 bits type of subaddress and a one bit odd/even indicator,
 * -- encoded as follows:
 * -- bit 8: 1 (no extension)
 * -- bits 765: type of subaddress
 * -- 000 NSAP (X.213/ISO 8348 AD2)
 * -- 010 User Specified
 * -- All other values are reserved
 * -- bit 4: odd/even indicator
 * -- 0 even number of address signals
 * -- 1 odd number of address signals
 * -- The odd/even indicator is used when the type of subaddress
 * -- is "user specified" and the coding is BCD.
 * -- bits 321: 000 (unused)
 * -- b) Subaddress information.
 * -- The NSAP X.213/ISO8348AD2 address shall be formatted as specified
 * -- by octet 4 which contains the Authority and Format Identifier
 * -- (AFI). The encoding is made according to the "preferred binary
 * -- encoding" as defined in X.213/ISO834AD2. For the definition
 * -- of this type of subaddress, see ITU-T Rec I.334.
 * -- For User-specific subaddress, this field is encoded according
 * -- to the user specification, subject to a maximum length of 20
 * -- octets. When interworking with X.25 networks BCD coding should
 * -- be applied.
 * @author eatakishiyev
 */
public class ISDNSubAddressString implements MAPParameter {
    
    private byte[] value;
    
    public ISDNSubAddressString() {
    }
    
    public ISDNSubAddressString(byte[] value) {
        this.value = value;
    }
    
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeOctetString(tagClass, tag, value);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }
    
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
    }
    
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            this.value = ais.readOctetString();
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the value
     */
    public byte[] getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(byte[] value) {
        this.value = value;
    }
    
}
