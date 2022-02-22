/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.messages.asptm;

import java.io.IOException;
import dev.ocean.sigtran.m3ua.Message;
import dev.ocean.sigtran.m3ua.MessageClass;
import dev.ocean.sigtran.m3ua.MessageType;
import dev.ocean.sigtran.m3ua.io.M3UAMessageByteArrayOutputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import dev.ocean.sigtran.m3ua.parameters.InfoString;
import dev.ocean.sigtran.m3ua.parameters.ParameterTag;
import dev.ocean.sigtran.m3ua.parameters.RoutingContext;

/**
 *
 * @author root
 */
public class ASPInactive extends Message {

    public static final MessageClass MESSAGE_CLASS = MessageClass.ASPTM;
    public static final MessageType MESSAGE_TYPE = MessageType.ASPIA;
    private RoutingContext routingContext = null;
    private InfoString infoString = null;

    public ASPInactive() {
    }

    public ASPInactive(Integer[] rc) {
        this.routingContext = new RoutingContext(rc);
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

    public void setRoutingContext(Integer[] rc) {
        this.routingContext = new RoutingContext(rc);
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
        return MessageType.ASPIA;
    }

    @Override
    public String toString() {
        return String.format("AspInactive[RoutingContext = %s; InfoString = %s]", this.routingContext, infoString);
    }
}
