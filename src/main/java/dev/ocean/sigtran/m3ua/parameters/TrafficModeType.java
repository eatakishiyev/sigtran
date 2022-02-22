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
public class TrafficModeType implements Parameter {

    private final ParameterTag tag = ParameterTag.TRAFFIC_MODE_TYPE;
    private TrafficMode trafficMode;

    public TrafficModeType() {
    }

    public TrafficModeType(TrafficMode trafficMode) {
        this.trafficMode = trafficMode;
    }

    /**
     * @return the trafficModeType
     */
    public TrafficMode getTrafficMode() {
        return trafficMode;
    }

    /**
     * @param trafficModeType the trafficModeType to set
     */
    public void setTrafficMode(TrafficMode trafficModeType) {
        this.trafficMode = trafficModeType;
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        byte[] data = new byte[length];
        bais.readData(data);

        int tm = data[0];
        tm = tm << 8;
        tm = tm | data[1];
        tm = tm << 8;
        tm = tm | data[2];
        tm = tm << 8;
        tm = tm | data[3];
        this.trafficMode = TrafficMode.getInstance(tm);
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        byte[] data = new byte[4];
        data[0] = (byte) ((this.trafficMode.value() >> 24) & 0xFF);
        data[1] = (byte) ((this.trafficMode.value() >> 16) & 0xFF);
        data[2] = (byte) ((this.trafficMode.value() >> 8) & 0xFF);
        data[3] = (byte) (this.trafficMode.value() & 0xFF);
        baos.encode(tag, data);
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    @Override
    public String toString() {
        return String.format("TrafficModeType[%s]", trafficMode);
    }
}
