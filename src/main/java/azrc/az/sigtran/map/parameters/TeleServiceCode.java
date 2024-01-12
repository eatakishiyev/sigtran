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

/**
 * TeleserviceCode ::= OCTET STRING (SIZE (1))
 * -- This type is used to represent the code identifying a single
 * -- teleservice, a group of teleservices, or all teleservices. The
 * -- services are defined in TS GSM 22.003 [4].
 * -- The internal structure is defined as follows:
 * -- bits 87654321: group (bits 8765) and specific service
 * -- (bits 4321)
 * @author eatakishiyev
 */
public class TeleServiceCode {

    private TeleServiceCodes teleServiceCode;

    public TeleServiceCode() {
    }

    public TeleServiceCode(TeleServiceCodes teleServiceCode) {
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
            byte value = ais.readOctetString()[0];
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
