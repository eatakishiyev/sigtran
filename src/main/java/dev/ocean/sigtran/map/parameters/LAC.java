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
import org.mobicents.protocols.asn.Tag;

/**
 * LAC ::= OCTET STRING (SIZE (2))
 * -- Refers to Location Area Code of the Location Area Identification defined
 * in
 * -- 3GPP TS 23.003 [17].
 * -- Location Area Code according to 3GPP TS 24.008 [35]
 * @author eatakishiyev
 */
public class LAC  implements MAPParameter {

    private int lac;

    public LAC() {
    }

    public LAC(int lac) {
        this.lac = lac;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            byte[] lacData = new byte[2];
            lacData[0] = (byte) (lac >> 8);
            lacData[1] = (byte) (lac & 0xFF);
            aos.writeOctetString(tagClass, tag, lacData);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_CONTEXT_SPECIFIC, Tag.STRING_OCTET, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            byte[] lacData = ais.readOctetString();
            this.lac = lacData[3] & 0xff;
            this.lac = (this.lac << 8) | (lacData[4] & 0xFF);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

}
