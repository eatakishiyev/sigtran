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
public class DestinationPointCode implements Parameter {

    private ParameterTag tag = ParameterTag.DESTINATION_POINT_CODE;
    private int destinationPointCode;

    public DestinationPointCode() {
    }

    public DestinationPointCode(int destinationPointCode) {
        this.destinationPointCode = destinationPointCode;
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        byte[] data = new byte[length];
        bais.readData(data);

        this.destinationPointCode = data[1] & 0xFF;
        this.destinationPointCode = this.destinationPointCode << 8;
        this.destinationPointCode = this.destinationPointCode | (data[2] & 0xFF);
        this.destinationPointCode = this.destinationPointCode << 8;
        this.destinationPointCode = this.destinationPointCode | (data[3] & 0xFF);
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        byte[] data = new byte[4];
        data[0] = 0x00;
        data[1] = (byte) ((this.destinationPointCode >> 16) & 0xFF);
        data[2] = (byte) ((this.destinationPointCode >> 8) & 0xFF);
        data[3] = (byte) (this.destinationPointCode & 0xFF);
        baos.encode(tag, data);
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    /**
     * @param destinationPointCode the destinationPointCode to set
     */
    public void setDestinationPointCode(int destinationPointCode) {
        this.destinationPointCode = destinationPointCode;
    }

    public int getDestinationPointCode() {
        return this.destinationPointCode;
    }
}
