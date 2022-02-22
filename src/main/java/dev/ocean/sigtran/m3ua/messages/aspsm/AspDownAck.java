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
import dev.ocean.sigtran.m3ua.parameters.InfoString;
import dev.ocean.sigtran.m3ua.parameters.ParameterTag;

/**
 *
 * @author root
 */
public class AspDownAck extends Message {

    private final MessageClass messageClass = MessageClass.ASPSM;
    private final MessageType messageType = MessageType.ASPDN_ACK;
    private InfoString infoString;

    public AspDownAck() {
    }

    /**
     * @return the infoString
     */
    public InfoString getInfoString() {
        return infoString;
    }

    /**
     * @param infoString the infoString to set
     */
    public void setInfoString(InfoString infoString) {
        this.infoString = infoString;
    }

    @Override
    public void encode(M3UAMessageByteArrayOutputStream baos) throws IOException {
        M3UAParameterByteArrayOutputStream tmpmbaos = new M3UAParameterByteArrayOutputStream(272);
        if (infoString != null) {
            infoString.encode(tmpmbaos);
        }
        byte[] data = tmpmbaos.toByteArray();
        baos.write(data);
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream mbais) throws IOException {
        ParameterTag tags = ParameterTag.getInstance(mbais.readParameterTag());
        switch (tags) {
            case INFO_STRING:
                infoString = new InfoString();
                infoString.decode(mbais);
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
        return String.format("AspdownAck [InfoString = %s]", infoString);
    }
}
