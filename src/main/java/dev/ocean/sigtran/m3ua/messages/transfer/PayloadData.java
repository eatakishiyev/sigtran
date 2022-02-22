/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.messages.transfer;

import java.io.IOException;
import dev.ocean.sigtran.m3ua.Message;
import dev.ocean.sigtran.m3ua.MessageClass;
import dev.ocean.sigtran.m3ua.MessageType;
import dev.ocean.sigtran.m3ua.io.M3UAMessageByteArrayOutputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import dev.ocean.sigtran.m3ua.parameters.CorrelationID;
import dev.ocean.sigtran.m3ua.parameters.NetworkAppearance;
import dev.ocean.sigtran.m3ua.parameters.ParameterTag;
import dev.ocean.sigtran.m3ua.parameters.ProtocolData;
import dev.ocean.sigtran.m3ua.parameters.RoutingContext;

/**
 *
 * @author root
 */
public class PayloadData extends Message {

    private final MessageClass messageClass = MessageClass.TRANSFER_MESSAGE;
    private final MessageType messageType = MessageType.DATA;
    private NetworkAppearance networkAppearance;
    private RoutingContext routingContext;
    private ProtocolData protocolData;
    private CorrelationID correlationID;

    public PayloadData() {
    }

    @Override
    public void encode(M3UAMessageByteArrayOutputStream baos) throws IOException {
        if (protocolData == null) {
            throw new IOException("Mandatory ProtocolData parameter is absent.");
        }
        M3UAParameterByteArrayOutputStream tmpBaos = new M3UAParameterByteArrayOutputStream(272);
        if (networkAppearance != null) {
            networkAppearance.encode(tmpBaos);
        }

        if (routingContext != null) {
            routingContext.encode(tmpBaos);
        }

        protocolData.encode(tmpBaos);

        if (correlationID != null) {
            correlationID.encode(tmpBaos);
        }
        baos.write(tmpBaos.toByteArray());
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream mbais) throws IOException {
        while (mbais.available() > 0) {
            ParameterTag tag = ParameterTag.getInstance(mbais.readParameterTag());
            switch (tag) {
                case NETWORK_APPEARANCE:
                    networkAppearance = new NetworkAppearance();
                    networkAppearance.decode(mbais);
                    continue;
                case ROUTING_CONTEXT:
                    routingContext = new RoutingContext();
                    routingContext.decode(mbais);
                    continue;
                case PROTOCOL_DATA:
                    protocolData = new ProtocolData();
                    protocolData.decode(mbais);
                    continue;
                case CORRELATION_ID:
                    correlationID = new CorrelationID();
                    correlationID.decode(mbais);
                    continue;
            }
        }
    }

    public void setNetworkAppearance(NetworkAppearance networkAppearance) {
        this.networkAppearance = networkAppearance;
    }

    public void setNetworkAppearance(int networkAppearance) {
        this.networkAppearance = new NetworkAppearance(networkAppearance);
    }

    public NetworkAppearance getNetworkAppearance() {
        return this.networkAppearance;
    }

    public void setRoutingContext(RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    public RoutingContext getRoutingContext() {
        return this.routingContext;
    }

    public void setProtocolData(ProtocolData protocolData) {
        this.protocolData = protocolData;
    }

    public ProtocolData getProtocolData() {
        return this.protocolData;

    }

    public void setCorrelationID(CorrelationID correlationID) {
        this.correlationID = correlationID;
    }

    public CorrelationID getCorrelationID() {
        return this.correlationID;
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
        return String.format("PayloadData [NetworkAppearance = %s; RoutingContext = %s;"
                + "CorrelationId = %s; ProtocolData = %s]", this.networkAppearance,
                this.routingContext, this.correlationID, this.protocolData);
    }
}
