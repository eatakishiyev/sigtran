/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.smscontrol.general;

import dev.ocean.sigtran.cap.api.CAPMessage;
import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * ReleaseSMSArg ::= RPCause
 *
 * @author eatakishiyev
 */
public class ReleaseSMSArg implements CAPMessage {

    private byte rpCause;

    public ReleaseSMSArg() {
    }

    public ReleaseSMSArg(byte rpCause) {
        this.rpCause = rpCause;
    }

    public void encode(AsnOutputStream aos) throws AsnException, IOException {
        aos.writeOctetString(new byte[]{rpCause});
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            ais.readTag();
            if (ais.getTag() != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Unexpected tag received, expecting TagClass[0] Tag[4], found TagClass[%s] Tag[%s]", ais.getTagClass(), ais.getTag()));
            }

            rpCause = ais.readOctetString()[0];
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }
}
