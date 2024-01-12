/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.messages.mgmt;

import java.io.IOException;

import azrc.az.sigtran.m3ua.Message;
import azrc.az.sigtran.m3ua.MessageClass;
import azrc.az.sigtran.m3ua.MessageType;
import azrc.az.sigtran.m3ua.io.M3UAMessageByteArrayOutputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import azrc.az.sigtran.m3ua.parameters.ASPIdentifier;
import azrc.az.sigtran.m3ua.parameters.Status;
import azrc.az.sigtran.m3ua.parameters.InfoString;
import azrc.az.sigtran.m3ua.parameters.ParameterTag;
import azrc.az.sigtran.m3ua.parameters.RoutingContext;

/**
 *
 * @author root
 */
public class Notify extends Message {

    private final MessageClass messageClass = MessageClass.MGMT;
    private final MessageType messageType = MessageType.NTFY;
    private Status status;
    private ASPIdentifier aspIdentifier;
    private RoutingContext routingContext;
    private InfoString infoString;

    @Override
    public void encode(M3UAMessageByteArrayOutputStream baos) throws IOException {
        M3UAParameterByteArrayOutputStream tmpBaos = new M3UAParameterByteArrayOutputStream(272);
        if (status != null) {
            status.encode(tmpBaos);
        }
        if (aspIdentifier != null
                && aspIdentifier.value() != null) {
            aspIdentifier.encode(tmpBaos);
        }
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
    public void decode(M3UAParameterByteArrayInputStream mbais) throws IOException {
        while (mbais.available() > 0) {
            ParameterTag tag = ParameterTag.getInstance(mbais.readParameterTag());
            switch (tag) {
                case STATUS:
                    status = new Status();
                    status.decode(mbais);
                    break;
                case ASP_IDENTIFIER:
                    aspIdentifier = new ASPIdentifier();
                    aspIdentifier.decode(mbais);
                    break;
                case ROUTING_CONTEXT:
                    routingContext = new RoutingContext();
                    routingContext.decode(mbais);
                    break;
                case INFO_STRING:
                    infoString = new InfoString();
                    infoString.decode(mbais);
                    break;
            }
        }
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
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
        return this.messageClass;
    }

    @Override
    public MessageType getMessageType() {
        return this.messageType;
    }

    @Override
    public String toString() {
        return String.format("Notify [Status = %s; AspIdentifier = %s; RoutingContext = %s;"
                + "InfoString = %s]", status, aspIdentifier, routingContext, infoString);
    }

}
