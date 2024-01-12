/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import azrc.az.sigtran.m3ua.MessageClass;
import azrc.az.sigtran.m3ua.MessageType;

/**
 *
 * @author eatakishiyev
 */
public class M3UAMessageByteArrayOutputStream {

    private final int messageHeaderFixedBytes = 8; //Version(1) + Reserved(1) + Class(1) + Type(1) + Length(4)
    private int version = 0x01;
    private ByteArrayOutputStream baos;
    private MessageClass messageClass;
    private MessageType messageType;

    public M3UAMessageByteArrayOutputStream(MessageClass messageClass, MessageType messageType) {
        this.messageClass = messageClass;
        this.messageType = messageType;

        this.baos = new ByteArrayOutputStream(272);

        this.baos.write(this.version);//Version
        this.baos.write(0x00);//Reserved
        this.baos.write(this.messageClass.getValue());
        this.baos.write(this.messageType.getValue());
    }

    public void write(byte[] data) throws IOException {
        int length = messageHeaderFixedBytes + data.length;
        this.baos.write((length >> 24) & 0xFF);
        this.baos.write((length >> 16) & 0xFF);
        this.baos.write((length >> 8) & 0xFF);
        this.baos.write(length & 0xFF);
        this.baos.write(data);
    }

    public byte[] toByteArray() {
        return this.baos.toByteArray();
    }
}
