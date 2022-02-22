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
 * CUG-Interlock ::= OCTET STRING (SIZE (4))
 * @author eatakishiyev
 */
public class CugInterLock  implements MAPParameter{

    private byte[] cugInterLock;

    public CugInterLock() {
    }

    public CugInterLock(byte[] cugInterLock) {
        this.cugInterLock = cugInterLock;
    }

    public final void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeOctetString(tagClass, tag, cugInterLock);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public final void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
    }

    public final void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            this.cugInterLock = ais.readOctetString();
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public byte[] getCUGInterLock() {
        return this.cugInterLock;
    }

    public void setCUGInterLock(byte[] cugInterLock) {
        this.cugInterLock = cugInterLock;
    }
}
