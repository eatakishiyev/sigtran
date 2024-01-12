/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.messages.ssnm;

import java.io.IOException;

import azrc.az.sigtran.m3ua.Message;
import azrc.az.sigtran.m3ua.MessageClass;
import azrc.az.sigtran.m3ua.MessageType;
import azrc.az.sigtran.m3ua.io.M3UAMessageByteArrayOutputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import azrc.az.sigtran.m3ua.parameters.AffectedPointCode;
import azrc.az.sigtran.m3ua.parameters.InfoString;
import azrc.az.sigtran.m3ua.parameters.NetworkAppearance;
import azrc.az.sigtran.m3ua.parameters.ParameterTag;
import azrc.az.sigtran.m3ua.parameters.RoutingContext;

/**
 *
 * @author root
 */
public class DestinationUnavailable extends Message {

    /**
     * @description SSNM Message Class DUNA Message Type
     */
    private final MessageClass messageClass = MessageClass.SSNM;
    private final MessageType messageType = MessageType.DUNA;
    private NetworkAppearance networkAppearance;
    private RoutingContext routingContext;
    private AffectedPointCode affectedPointCode;
    private InfoString infoString;

    @Override
    public void encode(M3UAMessageByteArrayOutputStream mbaos) throws IOException {
        M3UAParameterByteArrayOutputStream tmpBaos = new M3UAParameterByteArrayOutputStream(272);
        if (networkAppearance != null) {
            networkAppearance.encode(tmpBaos);
        }
        if (routingContext != null) {
            routingContext.encode(tmpBaos);
        }
        if (affectedPointCode != null) {
            affectedPointCode.encode(tmpBaos);
        }
        if (infoString != null) {
            infoString.encode(tmpBaos);
        }
        byte[] data = tmpBaos.toByteArray();
        mbaos.write(data);
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream mbais) throws IOException {
        while (mbais.available() > 0) {
            ParameterTag tag = ParameterTag.getInstance(mbais.readParameterTag());
            switch (tag) {
                case NETWORK_APPEARANCE:
                    networkAppearance = new NetworkAppearance();
                    networkAppearance.decode(mbais);
                    break;
                case ROUTING_CONTEXT:
                    routingContext = new RoutingContext();
                    routingContext.decode(mbais);
                    break;
                case AFFECTED_POINT_CODE:
                    affectedPointCode = new AffectedPointCode();
                    affectedPointCode.decode(mbais);
                    break;
                case INFO_STRING:
                    infoString = new InfoString();
                    infoString.decode(mbais);
                    break;
            }
        }
    }

    /**
     * @return the networkAppearance
     */
    public NetworkAppearance getNetworkAppearance() {
        return networkAppearance;
    }

    /**
     * @param networkAppearance the networkAppearance to set
     */
    public void setNetworkAppearance(NetworkAppearance networkAppearance) {
        this.networkAppearance = networkAppearance;
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
     * @return the affectedPointCode
     */
    public AffectedPointCode getAffectedPointCode() {
        return affectedPointCode;
    }

    /**
     * @param affectedPointCode the affectedPointCode to set
     */
    public void setAffectedPointCode(AffectedPointCode affectedPointCode) {
        this.affectedPointCode = affectedPointCode;
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
        return String.format("DestinationUnavailable [RoutingContext = %s; NetworkAppearance = %s; "
                + "AffectedPointCode = %s; InfoString = %s]", routingContext,
                networkAppearance, affectedPointCode, infoString);
    }
}
