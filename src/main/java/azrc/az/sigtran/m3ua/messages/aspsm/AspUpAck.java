/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.messages.aspsm;

import java.io.IOException;

import azrc.az.sigtran.m3ua.Message;
import azrc.az.sigtran.m3ua.MessageClass;
import azrc.az.sigtran.m3ua.MessageType;
import azrc.az.sigtran.m3ua.io.M3UAMessageByteArrayOutputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import azrc.az.sigtran.m3ua.parameters.ASPIdentifier;
import azrc.az.sigtran.m3ua.parameters.InfoString;
import azrc.az.sigtran.m3ua.parameters.ParameterTag;

/**
 *
 * @author root
 */
public class AspUpAck extends Message {

    private final MessageClass messageClass = MessageClass.ASPSM;
    private final MessageType messageType = MessageType.ASPUP_ACK;
    private InfoString infoString = null;
    private ASPIdentifier aspIdentifier = null;

    public AspUpAck() {
    }

    @Override
    public void encode(M3UAMessageByteArrayOutputStream baos) throws IOException {
        M3UAParameterByteArrayOutputStream tmpOutputStream = new M3UAParameterByteArrayOutputStream(272);
        if (aspIdentifier != null && aspIdentifier.value() != null) {
            aspIdentifier.encode(tmpOutputStream);
        }
        if (infoString != null) {
            infoString.encode(tmpOutputStream);
        }
        byte[] data = tmpOutputStream.toByteArray();
        baos.write(data);
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        while (bais.available() > 0) {
            ParameterTag tags = ParameterTag.getInstance(bais.readParameterTag());
            switch (tags) {
                case ASP_IDENTIFIER:
                    this.aspIdentifier = new ASPIdentifier();
                    aspIdentifier.decode(bais);
                    break;
                case INFO_STRING:
                    infoString = new InfoString();
                    infoString.decode(bais);
                    break;
            }
        }
    }

    /**
     * @param infoString the infoString to set
     */
    public void setInfoString(InfoString infoString) {
        this.infoString = infoString;
    }

    /**
     * @return the infoString
     */
    public InfoString getInfoString() {
        return infoString;
    }

    /**
     * @return the aspIdentifier
     */
    public ASPIdentifier getAspIdentifier() {
        return aspIdentifier;
    }

    /**
     * @param aspIdentifier the aspIdentifier to set
     */
    public void setAspIdentifier(ASPIdentifier aspIdentifier) {
        this.aspIdentifier = aspIdentifier;
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
        return String.format("AspUpAck [InfoString = %s; AspIdentifier = %s]", infoString, aspIdentifier);
    }

}
