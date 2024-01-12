/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.messages.asptm;

import java.io.IOException;

import azrc.az.sigtran.m3ua.Message;
import azrc.az.sigtran.m3ua.MessageClass;
import azrc.az.sigtran.m3ua.MessageType;
import azrc.az.sigtran.m3ua.io.M3UAMessageByteArrayOutputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import azrc.az.sigtran.m3ua.parameters.InfoString;
import azrc.az.sigtran.m3ua.parameters.ParameterTag;
import azrc.az.sigtran.m3ua.parameters.RoutingContext;

/**
 *
 * @author root
 */
public class ASPInactiveAck extends Message {

    public static final MessageClass MESSAGE_CLASS = MessageClass.ASPTM;
    public static final MessageType MESSAGE_TYPE = MessageType.ASPAC_ACK;
    private RoutingContext routingContext;
    private InfoString infoString;

    public ASPInactiveAck() {
    }

    @Override
    public void encode(M3UAMessageByteArrayOutputStream baos) throws IOException {
        M3UAParameterByteArrayOutputStream tmpBaos = new M3UAParameterByteArrayOutputStream(272);
        if (routingContext != null) {
            routingContext.encode(tmpBaos);
        }
        if (infoString != null) {
            infoString.encode(tmpBaos);
        }
        byte[] data = tmpBaos.toByteArray();
        baos.write(data);
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        while (bais.available() > 0) {
            ParameterTag tag = ParameterTag.getInstance(bais.readParameterTag());
            switch (tag) {
                case ROUTING_CONTEXT:
                    routingContext = new RoutingContext();
                    routingContext.decode(bais);
                    break;
                case INFO_STRING:
                    infoString = new InfoString();
                    infoString.decode(bais);
                    break;
            }
        }
    }

    /**
     * @return the routingContext
     */
    public RoutingContext getRoutingContext() {
        return routingContext;
    }

    /**
     * @param routingContext the routingContext to set
     */
    public void setRoutingContext(RoutingContext routingContext) {
        this.routingContext = routingContext;
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
    public MessageClass getMessageClass() {
        return MessageClass.ASPTM;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.ASPIA_ACK;
    }

    @Override
    public String toString() {
        return String.format("AspInactiveAck [RoutingContext = %s; InfoString = %s]", this.routingContext, infoString);
    }
}
