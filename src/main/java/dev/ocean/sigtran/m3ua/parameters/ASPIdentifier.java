/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.parameters;

import java.io.IOException;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;

/**
 *
 * @author root
 */
public class ASPIdentifier implements Parameter {

    private final ParameterTag tag = ParameterTag.ASP_IDENTIFIER;
    private Integer aspIdentifier;

    public ASPIdentifier() {
    }

    public ASPIdentifier(Integer aspIdentifier) {
        this.aspIdentifier = aspIdentifier;
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        byte[] data = new byte[4];
        data[0] = (byte) ((this.aspIdentifier >> 24) & 0xFF);
        data[1] = (byte) ((this.aspIdentifier >> 16) & 0xFF);
        data[2] = (byte) ((this.aspIdentifier >> 8) & 0xFF);
        data[3] = (byte) ((this.aspIdentifier) & 0xFF);
        baos.encode(tag, data);
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        byte[] data = new byte[length];
        bais.readData(data);

        this.aspIdentifier = data[0] & 0xFF;
        this.aspIdentifier = this.aspIdentifier << 8;
        this.aspIdentifier = this.aspIdentifier | (data[1] & 0xFF);
        this.aspIdentifier = this.aspIdentifier << 8;
        this.aspIdentifier = this.aspIdentifier | (data[2] & 0xFF);
        this.aspIdentifier = this.aspIdentifier << 8;
        this.aspIdentifier = this.aspIdentifier | (data[3] & 0xFF);

    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    /**
     * @return the ASPIdentifier
     */
    public Integer value() {
        return aspIdentifier;
    }

    /**
     * @param aspIdentifier the ASPIdentifier to set
     */
    public void setValue(Integer aspIdentifier) {
        this.aspIdentifier = aspIdentifier;
    }

    @Override
    public String toString() {
        return String.format("AspIdentifier [%s]", aspIdentifier);
    }
}
