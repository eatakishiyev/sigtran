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
 * Ext-TeleserviceCode ::= OCTET STRING (SIZE (1..5))
 * -- This type is used to represent the code identifying a single
 * -- teleservice, a group of teleservices, or all teleservices. The
 * -- services are defined in TS GSM 22.003 [4].
 * -- The internal structure is defined as follows:
 * -- OCTET 1:
 * -- bits 87654321: group (bits 8765) and specific service
 * -- (bits 4321)
 * -- OCTETS 2-5: reserved for future use. If received the
 * -- Ext-TeleserviceCode shall be
 * -- treated according to the exception handling defined for the
 * -- operation that uses this type.
 * -- Ext-TeleserviceCode includes all values defined for TeleserviceCode.
 *
 * @author eatakishiyev
 */
public class ExtTeleServiceCode implements MAPParameter {

    private TeleServiceCodes teleServiceCode;

    public ExtTeleServiceCode() {
    }

    public ExtTeleServiceCode(TeleServiceCodes teleServiceCode) {
        this.teleServiceCode = teleServiceCode;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeOctetString(tagClass, tag, new byte[]{(byte) teleServiceCode.getValue()});
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            byte[] b = ais.readOctetString();
            if (b.length < 1 || b.length > 5) {
                throw new IncorrectSyntaxException(String.format("Expecting OctetString length [1..5], received %s", b.length));
            }
            byte value = b[0];
            this.teleServiceCode = TeleServiceCodes.getInstance(value);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the teleServiceCode
     */
    public TeleServiceCodes getTeleServiceCode() {
        return teleServiceCode;
    }

    /**
     * @param teleServiceCode the teleServiceCode to set
     */
    public void setTeleServiceCode(TeleServiceCodes teleServiceCode) {
        this.teleServiceCode = teleServiceCode;
    }
}
