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
public class ConcernedDPC implements Parameter {

    private final ParameterTag tag = ParameterTag.CONCERNED_DESTINATIONS;
    private int pointCode;

    public ConcernedDPC() {
    }

    public ConcernedDPC(int pointCode) {
        this.pointCode = pointCode;
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        byte[] data = new byte[4];
        data[0] = 0x00;//reserved
        data[1] = (byte) ((pointCode >> 16) & 0xFF);
        data[2] = (byte) ((pointCode >> 8) & 0xFF);
        data[3] = (byte) (pointCode & 0xFF);
        baos.encode(tag, data);
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        byte[] data = new byte[length];
        bais.readData(data);

        this.pointCode = data[1] & 0xff;
        this.pointCode = this.pointCode << 8;
        this.pointCode = this.pointCode | (data[2] & 0xff);
        this.pointCode = this.pointCode << 8;
        this.pointCode = this.pointCode | (data[3] & 0xff);
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    /**
     * @param pointCode the pointCode to set
     */
    public void setPointCode(int pointCode) {
        this.pointCode = pointCode;
    }

    public int getPointCode() {
        return pointCode;
    }

    @Override
    public String toString() {
        return String.format("ConcernedDPC [%s]", this.pointCode);
    }
}
