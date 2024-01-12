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
 *
 * @author eatakishiyev
 */
public abstract class OctetString {

    private byte[] value;

    public OctetString() {
    }

    public OctetString(byte[] value) {
        this.value = value;
    }

    public final void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            if (value.length < getMinLength() || value.length > getMaxLength()) {
                throw new UnexpectedDataException(String.format("OctetString length is out of range. Length must be in range [%d, %d]", getMinLength(), getMaxLength()));
            }

            aos.writeOctetString(tagClass, tag, value);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public final void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
    }

    public final void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            this.value = ais.readOctetString();
            if (value.length < getMinLength() || value.length > getMaxLength()) {
                throw new UnexpectedDataException(String.format("OctetString length is out of range. Length must be in range [%d, %d]", getMinLength(), getMaxLength()));
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public abstract int getMinLength();

    public abstract int getMaxLength();

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
