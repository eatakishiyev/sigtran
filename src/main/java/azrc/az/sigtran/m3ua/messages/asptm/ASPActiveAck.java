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
import azrc.az.sigtran.m3ua.parameters.TrafficModeType;
import azrc.az.sigtran.m3ua.parameters.InfoString;
import azrc.az.sigtran.m3ua.parameters.ParameterTag;
import azrc.az.sigtran.m3ua.parameters.RoutingContext;

/**
 *
 * @author root
 */
public class ASPActiveAck extends Message {

    private final MessageClass messageClass = MessageClass.ASPTM;
    private final MessageType messageType = MessageType.ASPAC_ACK;
    private TrafficModeType trafficModeType;
    private RoutingContext routingContext;
    private InfoString infoString;

    public ASPActiveAck() {
    }

    @Override
    public void encode(M3UAMessageByteArrayOutputStream baos) throws IOException {
        M3UAParameterByteArrayOutputStream tmpBaos = new M3UAParameterByteArrayOutputStream(272);
        if (trafficModeType != null) {
            trafficModeType.encode(tmpBaos);
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
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        while (bais.available() > 0) {
            ParameterTag tag = ParameterTag.getInstance(bais.readParameterTag());
            switch (tag) {
                case TRAFFIC_MODE_TYPE:
                    trafficModeType = new TrafficModeType();
                    trafficModeType.decode(bais);
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
    public TrafficModeType getTrafficModeType() {
        return trafficModeType;
    }

    /**
     * @param trafficModeType the trafficModeType to set
     */
    public void setTrafficModeType(TrafficModeType trafficModeType) {
        this.trafficModeType = trafficModeType;
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
        return String.format("AspActiveAck [TrafficModeType = %s; RoutingContext = %s; InfoString = %s]", trafficModeType, routingContext, infoString);
    }

}
