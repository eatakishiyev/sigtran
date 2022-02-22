/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import java.io.IOException;
import dev.ocean.sigtran.m3ua.io.M3UAMessageByteArrayOutputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;

/**
 *
 * @author root
 */
public abstract class Message  {

//    private MessageClass messageClass;
//    private MessageType messageType;
    protected int messageVersion = 0x01;
//    private Integer messageLength;

    public abstract void encode(M3UAMessageByteArrayOutputStream mbaos) throws IOException;

    public abstract void decode(M3UAParameterByteArrayInputStream mbais) throws IOException;

    /**
     * @return the messageClass
     */
    public abstract MessageClass getMessageClass();

    /**
     * @return the messageType
     */
    public abstract MessageType getMessageType();
}
