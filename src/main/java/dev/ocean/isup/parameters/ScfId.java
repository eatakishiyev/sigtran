/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class ScfId implements IsupParameter {

    private byte[] value;

    public ScfId() {
    }

    public ScfId(byte[] value) {
        this.value = value;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException {
        aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, tag);
        int lenPos = aos.StartContentDefiniteLength();
        aos.write(value);
        aos.FinalizeContent(lenPos);
    }

    @Override
    public byte[] encode() throws Exception {
        return value;
    }

    public void decode(AsnInputStream ais) throws IOException {
        this.value = new byte[ais.readLength()];
        ais.read(value);
    }

    @Override
    public void decode(byte[] data) {
        this.value = data;
    }

    public void encode(ByteArrayOutputStream baos) throws IOException {
        baos.write(value);
    }

    /**
     * @return the value
     */
    public byte[] getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(byte[] value) {
        this.value = value;
    }

    @Override
    public int getParameterCode() {
        return SCF_ID;
    }

}
