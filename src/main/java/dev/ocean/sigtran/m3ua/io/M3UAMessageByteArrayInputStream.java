/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.io;

import dev.ocean.sigtran.m3ua.MessageClass;
import dev.ocean.sigtran.m3ua.MessageType;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class M3UAMessageByteArrayInputStream extends ByteArrayInputStream {

    private int version = 0x01;
    private int reserved;
    private MessageClass messageClass;
    private MessageType messageType;
    private int messageLength = 0;
    private M3UAParameterByteArrayInputStream messageContent;

    public M3UAMessageByteArrayInputStream(byte[] buf) {
        super(buf);
    }

    public int readVersion() {
        this.version = super.read();
        return this.getVersion();
    }

    public int readReserved() {
        this.reserved = super.read();
        return this.getReserved();
    }

    public MessageClass readMessageClass() {
        this.messageClass = MessageClass.getInstance(super.read());
        return this.getMessageClass();
    }

    public MessageType readMessageType() {
        this.messageType = MessageType.getInstance(super.read(), getMessageClass());
        return this.messageType;
    }

    public int readMessageLength() {
        int length = super.read();
        length = length << 8;
        length = length | super.read();
        length = length << 8;
        length = length | super.read();
        length = length << 8;
        length = length | super.read();
        this.messageLength = length;
        return length;
    }

    public M3UAParameterByteArrayInputStream readMessageContet() throws IOException {
        int paramDataOctet = this.messageLength - 8;
        byte[] paramData = new byte[paramDataOctet];
        super.read(paramData);
        this.messageContent = new M3UAParameterByteArrayInputStream(paramData);
        return this.messageContent;
    }

    /**
     * @return the messageLength
     */
    public int getMessageLength() {
        return messageLength;
    }

    /**
     * @return the messageClass
     */
    public MessageClass getMessageClass() {
        return messageClass;
    }

    /**
     * @return the messageType
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @return the reserved
     */
    public int getReserved() {
        return reserved;
    }

    /**
     * @return the messageContent
     */
    public M3UAParameterByteArrayInputStream getMessageContent() {
        return messageContent;
    }
}
