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
public class CongestionIndication implements Parameter {

    private final ParameterTag tag = ParameterTag.CONGESTION_INDICATIONS;
    private CongestionLevel congestionLevel;

    public CongestionIndication() {
    }

    public CongestionIndication(CongestionLevel congestionLevel) {
        this.congestionLevel = congestionLevel;
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        byte[] data = new byte[length];
        bais.readData(data);//Reserved
        this.setCongestionLevel(CongestionLevel.getInstance(data[3]));
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {

        byte[] data = new byte[]{0x00, 0x00, 0x00, 0x00};
        data[3] = (byte) this.congestionLevel.value();
        baos.encode(tag, data);
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    /**
     * @return the congestionLevel
     */
    public CongestionLevel getCongestionLevel() {
        return congestionLevel;
    }

    /**
     * @param congestionLevel the congestionLevel to set
     */
    public void setCongestionLevel(CongestionLevel congestionLevel) {
        this.congestionLevel = congestionLevel;
    }

    @Override
    public String toString() {
        return String.format("CongestionIndication [CongestionLevel [%s]]", this.congestionLevel);
    }
}
