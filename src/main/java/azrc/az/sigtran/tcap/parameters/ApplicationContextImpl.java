/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.parameters;

import java.io.IOException;

import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.tcap.parameters.interfaces.ApplicationContext;

import java.util.Arrays;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class ApplicationContextImpl implements ApplicationContext {

    private long[] oid;

    public ApplicationContextImpl() {
    }

    public ApplicationContextImpl(long[] oid) {
        this.oid = oid;
    }

    public ApplicationContextImpl(byte[] data) throws Exception {
        if (data == null) {
            throw new NullPointerException("Parameter can not be null");
        }

        if (data.length == 0) {
            throw new Exception("Parameter length can not be zero");
        }

        AsnInputStream ais = new AsnInputStream(data);
        ais.readTag();
        this.decode(ais);
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0x01);
            int position = aos.StartContentDefiniteLength();

            aos.writeObjectIdentifier(this.oid);

            aos.FinalizeContent(position);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", ais.getTagClass(), ais.isTagPrimitive(), ais.getTag()));
        }

        try {
            AsnInputStream tmpAis = ais.readSequenceStream();
            int tag = tmpAis.readTag();

            if (tmpAis.getTagClass() != Tag.CLASS_UNIVERSAL || !tmpAis.isTagPrimitive() || tag != Tag.OBJECT_IDENTIFIER) {
                throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
            }

            this.oid = tmpAis.readObjectIdentifier();
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public long[] getOid() {
        return this.oid;
    }

    @Override
    public void setOid(long[] oid) {
        this.oid = oid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ApplicationContext[");
        sb.append("OID = ").append(Arrays.toString(oid))
                .append("]");
        return sb.toString();
    }

}
