/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;

/**
 *
 * @author root
 */
public class OriginationPointCodeList implements Parameter {

    private ParameterTag tag = ParameterTag.ORIGINATING_POINT_CODE_LIST;
    private OriginationPointCode[] originationPointCodes;

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        byte[] data = new byte[length];
        bais.readData(data);

        ByteArrayInputStream tmpBais = new ByteArrayInputStream(data);
        ArrayList<OriginationPointCode> tmpOriginationPointCodes = new ArrayList<>();
        while (tmpBais.available() > 0) {
            int mask = tmpBais.read();
            int pointCode = tmpBais.read() & 0xFF;
            pointCode = pointCode << 8;
            pointCode = pointCode | tmpBais.read() & 0xFF;
            pointCode = pointCode << 8;
            pointCode = pointCode | tmpBais.read() & 0xFF;
            OriginationPointCode originationPointCode = new OriginationPointCode(mask, pointCode);
            tmpOriginationPointCodes.add(originationPointCode);
        }
        this.originationPointCodes = new OriginationPointCode[tmpOriginationPointCodes.size()];
        tmpOriginationPointCodes.toArray(originationPointCodes);
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream(272);
        for (OriginationPointCode originationPointCode : originationPointCodes) {
            tmpBaos.write(originationPointCode.getMask());
            tmpBaos.write((originationPointCode.getPointCode() >> 16) & 0xFF);
            tmpBaos.write((originationPointCode.getPointCode() >> 8) & 0xFF);
            tmpBaos.write(originationPointCode.getPointCode() & 0xFF);
        }
        baos.encode(this.tag, tmpBaos.toByteArray());
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    /**
     * @return the originationPointCodes
     */
    public OriginationPointCode[] getOriginationPointCodes() {
        return originationPointCodes;
    }

    /**
     * @param originationPointCodes the originationPointCodes to set
     */
    public void setOriginationPointCodes(OriginationPointCode[] originationPointCodes) {
        this.originationPointCodes = originationPointCodes;
    }
}
