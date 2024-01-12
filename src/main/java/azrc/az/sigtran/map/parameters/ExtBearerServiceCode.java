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
 * Ext-BearerServiceCode ::= OCTET STRING (SIZE (1..5))
 * -- This type is used to represent the code identifying a single
 * -- bearer service, a group of bearer services, or all bearer
 * -- services. The services are defined in TS 3GPP TS 22.002 [3].
 * -- The internal structure is defined as follows:
 * --
 * -- OCTET 1:
 * -- plmn-specific bearer services:
 * -- bits 87654321: defined by the HPLMN operator
 * --
 * -- rest of bearer services:
 * -- bit 8: 0 (unused)
 * -- bits 7654321: group (bits 7654), and rate, if applicable
 * -- (bits 321)
 * -- OCTETS 2-5: reserved for future use. If received the
 * -- Ext-TeleserviceCode shall be
 * -- treated according to the exception handling defined for the
 * -- operation that uses this type.
 * -- Ext-BearerServiceCode includes all values defined for BearerServiceCode.
 * @author eatakishiyev
 */
public class ExtBearerServiceCode  implements MAPParameter {

    private BearerServiceCodes bearerServiceCode;

    public ExtBearerServiceCode(BearerServiceCodes bearerServiceCode) {
        this.bearerServiceCode = bearerServiceCode;
    }

    public ExtBearerServiceCode() {
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeOctetString(tagClass, tag, new byte[]{(byte) bearerServiceCode.getValue()});
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            byte[] b = ais.readOctetString();
            if (b.length < 1 || b.length > 5) {
                throw new IncorrectSyntaxException(String.format("Expecting OctetString length [1..5], received %s", b.length));
            }
            
            byte value = b[0];
            this.bearerServiceCode = BearerServiceCodes.getInstance(value);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the bearerServiceCode
     */
    public BearerServiceCodes getBearerServiceCode() {
        return bearerServiceCode;
    }

    /**
     * @param bearerServiceCode the bearerServiceCode to set
     */
    public void setBearerServiceCode(BearerServiceCodes bearerServiceCode) {
        this.bearerServiceCode = bearerServiceCode;
    }
}
