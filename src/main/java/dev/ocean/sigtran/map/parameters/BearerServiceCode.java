/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * BearerServiceCode ::= OCTET STRING (SIZE (1))
 * -- This type is used to represent the code identifying a single
 * -- bearer service, a group of bearer services, or all bearer
 * -- services. The services are defined in TS 3GPP TS 22.002 [3].
 * -- The internal structure is defined as follows:
 * --
 * -- plmn-specific bearer services:
 * -- bits 87654321: defined by the HPLMN operator
 * -- rest of bearer services:
 * -- bit 8: 0 (unused)
 * -- bits 7654321: group (bits 7654), and rate, if applicable
 * -- (bits 321)
 *
 * @author eatakishiyev
 */
public class BearerServiceCode implements MAPParameter{

    private BearerServiceCodes bearerServiceCode;

    public BearerServiceCode(BearerServiceCodes bearerServiceCode) {
        this.bearerServiceCode = bearerServiceCode;
    }

    public BearerServiceCode() {
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
            byte value = ais.readOctetString()[0];
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
