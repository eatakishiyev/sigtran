/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap;

import java.io.IOException;
import azrc.az.sigtran.sccp.messages.MessageHandling;

/**
 *
 * @author eatakishiyev
 */
public class QoS {

    private MessageHandling messageHandling;
    private Integer sequenceNumber = null;
    private boolean sequenceControl = false;

    @Override
    public String toString() {
        return String.format("QoS[MessageHandling = %s; SequenceControl = %s; SequenceNumber = %d]", messageHandling, sequenceControl, sequenceNumber);
    }

    public QoS() {
    }

    public QoS(byte[] data) throws Exception {
        if (data == null) {
            throw new NullPointerException("Parameter can not be null");
        }
        if (data.length == 0) {
            throw new Exception("Parameter length can not be zero");
        }
        
        this.decode(data);
    }

    public QoS(MessageHandling messageHandling, boolean sequenceControl, Integer sequenceNumber) {
        this.messageHandling = messageHandling;
        this.sequenceControl = sequenceControl;
        this.sequenceNumber = sequenceNumber;
    }

    public QoS(MessageHandling messageHandling, boolean sequenceControl) {
        this.messageHandling = messageHandling;
        this.sequenceControl = sequenceControl;
        this.sequenceNumber = null;
    }

    public QoS(MessageHandling messageHandling) {
        this.messageHandling = messageHandling;
        this.sequenceNumber = null;
    }

    /**
     * @return the messageHandling
     */
    public MessageHandling getMessageHandling() {
        return this.messageHandling;
    }

    /**
     * @param messageHandling the messageHandling to set
     */
    public void setMessageHandling(MessageHandling messageHandling) {
        this.messageHandling = messageHandling;
    }

    /**
     * @return the seqControl
     */
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @param sequenceNumber
     */
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * @return the sequenceControl
     */
    public boolean isSequenceControl() {
        return sequenceControl;
    }

    /**
     * @param sequenceControl the sequenceControl to set
     */
    public final void setSequenceControl(boolean sequenceControl) {
        this.sequenceControl = sequenceControl;
    }

    public byte[] encode() throws IOException {
        byte[] data;
        if (sequenceNumber == null) {
            data = new byte[2];
        } else {
            data = new byte[6];
        }
        int position = 0;

        data[position++] = (byte) (messageHandling.value());
        data[position++] = (byte) (sequenceControl ? 1 : 0);

        if (sequenceNumber != null) {
            data[position++] = (byte) ((sequenceNumber >> 24) & 0xFF);
            data[position++] = (byte) ((sequenceNumber >> 16) & 0xFF);
            data[position++] = (byte) ((sequenceNumber >> 8) & 0xFF);
            data[position++] = (byte) (sequenceNumber & 0xFF);
        }

        return data;
    }

    public final void decode(byte[] data) throws IOException {
        int position = 0;
        this.messageHandling = MessageHandling.getInstance(data[position++] & 0xFF);
        this.sequenceControl = data[position++] == 1;

        if (position < data.length) {
            this.sequenceNumber = data[position++] & 0xFF;
            this.sequenceNumber = (this.sequenceNumber << 8) | (data[position++] & 0xFF);
            this.sequenceNumber = (this.sequenceNumber << 8) | (data[position++] & 0xFF);
            this.sequenceNumber = (this.sequenceNumber << 8) | (data[position] & 0xFF);
        }

    }
}
