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
import dev.ocean.sigtran.m3ua.parameters.TrafficMode;
import dev.ocean.sigtran.m3ua.parameters.TrafficModeType;

/**
 *
 * @author root
 */
public class ASPActive extends Message {

    private final MessageClass messageClass = MessageClass.ASPTM;
    private final MessageType messageType = MessageType.ASPAC;
    private TrafficMode trafficMode = null;
    private RoutingContext routingContext = null;
    private InfoString infoString = null;

    public ASPActive() {
    }

    public ASPActive(RoutingContext rc) {
        this.routingContext = rc;
    }

    @Override
    public void encode(M3UAMessageByteArrayOutputStream baos) throws IOException {
        M3UAParameterByteArrayOutputStream tmpOutputStream = new M3UAParameterByteArrayOutputStream(272);
        if (trafficMode != null) {
            new TrafficModeType(trafficMode).encode(tmpOutputStream);
        }
        if (routingContext != null) {
            routingContext.encode(tmpOutputStream);
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
                case TRAFFIC_MODE_TYPE:
                    TrafficModeType trafficModeType = new TrafficModeType();
                    trafficModeType.decode(bais);
                    this.trafficMode = trafficModeType.getTrafficMode();
                    break;
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
     * @return the trafficModeType
     */
    public TrafficMode getTrafficMode() {
        return trafficMode;
    }

    /**
     * @param trafficMode
     */
    public void setTrafficMode(TrafficMode trafficMode) {
        this.trafficMode = trafficMode;
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
        return String.format("AspActive:[RoutingContext = %s; TrafficMode = %s]", this.routingContext, this.trafficMode);
    }
}
