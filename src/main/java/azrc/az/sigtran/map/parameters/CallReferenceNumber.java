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
 * CallReferenceNumber ::= OCTET STRING (SIZE (1..8))
 * @author eatakishiyev
 */
public class CallReferenceNumber implements MAPParameter{

    private int value;

    public CallReferenceNumber() {
    }

    public CallReferenceNumber(int value) {
        this.value = value;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            byte[] data = new byte[4];
            data[0] = (byte) ((value >> 24) & 0xFF);
            data[1] = (byte) ((value >> 16) & 0xFF);
            data[2] = (byte) ((value >> 8) & 0xFF);
            data[3] = (byte) (value & 0xFF);
            aos.writeOctetString(tagClass, tag, data);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            byte[] data  = ais.readOctetString();
            value = data[0] & 0xFF;
            value = value << 8;
            value = value | (data[1] & 0xFF);
            value = value << 8;
            value = value | (data[2] & 0xFF);
            value = value << 8;
            value = value | (data[3] & 0xFF);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

}
