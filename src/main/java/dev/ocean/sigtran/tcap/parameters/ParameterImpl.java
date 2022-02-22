/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters;

import dev.ocean.sigtran.utils.ByteUtils;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author root
 */
public class ParameterImpl implements Parameter {

    private byte[] data;

    public ParameterImpl() {
    }

    public ParameterImpl(byte[] data) {
        this.data = data;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IOException, AsnException {
        aos.write(data);
    }

    @Override
    public void decode(AsnInputStream ais) throws IOException, AsnException {
        data = new byte[ais.available()];
        ais.read(data);
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public byte[] getData() {
        return this.data;
    }

    public String toString() {
        return "ParameterImpl: " + ByteUtils.bytes2Hex(data);
    }
}
