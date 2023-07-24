/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import java.util.Arrays;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

import javax.xml.bind.DatatypeConverter;

/**
 * ExtensionContainer::= SEQUENCE {
 * privateExtensionList [0]PrivateExtensionList OPTIONAL,
 * pcs-Extensions [1]PCS-Extensions OPTIONAL,
 * ...}
 * @author eatakishiyev
 */
public class ExtensionContainer implements MAPParameter {

    private byte[] data;

    public ExtensionContainer() {
    }

    public ExtensionContainer(AsnInputStream ais) throws IncorrectSyntaxException {
        this.decode(ais);
    }

    @Override
    public final void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int position = aos.StartContentDefiniteLength();
            aos.write(data);
            aos.FinalizeContent(position);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public final void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    @Override
    public final void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            this.data = ais.readSequence();
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ExtensionContainer{" +
                "data=" + DatatypeConverter.printHexBinary(data) +
                '}';
    }
}
