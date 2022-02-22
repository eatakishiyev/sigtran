/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import dev.ocean.sigtran.utils.ByteUtils;


/**
 *
 * @author eatakishiyev
 */
public class SctpPayload {

    private int protocolId;
    private byte[] data;
    private int streamNumber;

    public SctpPayload(int protocolId, byte[] data, int streamNumber) {
        this.data = data;
        this.streamNumber = streamNumber;
        this.protocolId = protocolId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SctpPayload [");
        sb.append("ProtocolId = ").append(protocolId)
                .append(" StreamNumber = ").append(streamNumber)
                .append(" Data = ").append(ByteUtils.bytes2Hex(data));
        sb.append("]");
        return sb.toString();
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @return the streamNumber
     */
    public int getStreamNumber() {
        return streamNumber;
    }

    /**
     * @param streamNumber the streamNumber to set
     */
    public void setStreamNumber(int streamNumber) {
        this.streamNumber = streamNumber;
    }

    /**
     * @return the protocolId
     */
    public int getProtocolId() {
        return protocolId;
    }

    /**
     * @param protocolId the protocolId to set
     */
    public void setProtocolId(int protocolId) {
        this.protocolId = protocolId;
    }

}
