/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.messages.aspsm;

import java.io.IOException;
import dev.ocean.sigtran.m3ua.Message;
import dev.ocean.sigtran.m3ua.MessageClass;
import dev.ocean.sigtran.m3ua.MessageType;
import dev.ocean.sigtran.m3ua.io.M3UAMessageByteArrayOutputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import dev.ocean.sigtran.m3ua.parameters.HeartBeatData;
import dev.ocean.sigtran.m3ua.parameters.ParameterTag;

/**
 *
 * @author root
 */
public class HeartBeat extends Message {

    private final MessageClass messageClass = MessageClass.ASPSM;
    private final MessageType messageType = MessageType.BEAT;
    private HeartBeatData heartBeatData = null;

    public HeartBeat() {
    }

    public HeartBeat(HeartBeatData heartBeatData) {
        this.heartBeatData = heartBeatData;
    }

    public HeartBeat(byte[] heartBeatData) {
        this.heartBeatData = new HeartBeatData(heartBeatData);
    }

    /**
     * @return the heartBeatData
     */
    public HeartBeatData getHeartBeatData() {
        return heartBeatData;
    }

    /**
     * @param heartBeatData the heartBeatData to set
     */
    public void setHeartBeatData(HeartBeatData heartBeatData) {
        this.heartBeatData = heartBeatData;
    }

    @Override
    public void encode(M3UAMessageByteArrayOutputStream baos) throws IOException {
        M3UAParameterByteArrayOutputStream tmpOutputStream = new M3UAParameterByteArrayOutputStream(272);
        if (heartBeatData != null) {
            heartBeatData.encode(tmpOutputStream);
        }
        byte[] data = tmpOutputStream.toByteArray();
        baos.write(data);
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        ParameterTag tags = ParameterTag.getInstance(bais.readParameterTag());
        switch (tags) {
            case HEARTBEAD_DATA:
                heartBeatData = new HeartBeatData();
                heartBeatData.decode(bais);
                break;

        }
    }

    @Override
    public MessageClass getMessageClass() {
        return this.messageClass;
    }

    @Override
    public MessageType getMessageType() {
        return this.messageType;
    }

    @Override
    public String toString() {
        return String.format("HeartBeat [HeartBeatData = %s]", heartBeatData);
    }

}
