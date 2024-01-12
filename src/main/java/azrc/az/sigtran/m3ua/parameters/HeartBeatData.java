/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.parameters;

import java.io.IOException;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import azrc.az.sigtran.utils.ByteUtils;


/**
 *
 * @author root
 */
public class HeartBeatData implements Parameter {

    /**
     * The contents/format of the Heartbeat Data parameter is
     * implementation-depend and only of local interest to the original sender.
     */
    private final ParameterTag tag = ParameterTag.HEARTBEAD_DATA;
    private byte[] heartbeatData;

    public HeartBeatData() {
    }

    public HeartBeatData(byte[] heartbeatData) {
        this.heartbeatData = heartbeatData;
    }

    public byte[] getHeartBeatData() {
        return this.heartbeatData;
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        this.heartbeatData = new byte[length];
        bais.readData(heartbeatData);
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        baos.encode(tag, heartbeatData);
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    /**
     * @param heartbeatData the heartbeatData to set
     */
    public void setHeartbeatData(byte[] heartbeatData) {
        this.heartbeatData = heartbeatData;
    }

    @Override
    public String toString() {
        return String.format("HeartBeatData [%s]", ByteUtils.bytes2Hex(heartbeatData));
    }
}
