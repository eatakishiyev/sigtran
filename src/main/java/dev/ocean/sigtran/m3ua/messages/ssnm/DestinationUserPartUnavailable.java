/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.messages.ssnm;

import java.io.IOException;
import dev.ocean.sigtran.m3ua.Message;
import dev.ocean.sigtran.m3ua.MessageClass;
import dev.ocean.sigtran.m3ua.MessageType;
import dev.ocean.sigtran.m3ua.io.M3UAMessageByteArrayOutputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import dev.ocean.sigtran.m3ua.parameters.AffectedPointCode;
import dev.ocean.sigtran.m3ua.parameters.InfoString;
import dev.ocean.sigtran.m3ua.parameters.NetworkAppearance;
import dev.ocean.sigtran.m3ua.parameters.ParameterTag;
import dev.ocean.sigtran.m3ua.parameters.RoutingContext;
import dev.ocean.sigtran.m3ua.parameters.UserPartUnavailableCause;

/**
 *
 * @author root
 */
public class DestinationUserPartUnavailable extends Message {

    private final MessageClass messageClass = MessageClass.SSNM;
    private final MessageType messageType = MessageType.DUPU;
//    
    private NetworkAppearance networkAppearance;
    private RoutingContext routingContext;
    private AffectedPointCode affectedPointCode;
    private UserPartUnavailableCause userPartUnavailableCause;
    private InfoString infoString;

    @Override
    public String toString() {
        return String.format("DestinationUserPartUnavailable [NetworkAppearance = %s;"
                + "RoutingContext = %s; AffectedPointCode = %s; UserPartUnavailableCause = %s; "
                + "InfoString = %s]", networkAppearance, routingContext, affectedPointCode,
                userPartUnavailableCause, infoString);
    }

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
            routingContext.encode(tmpBaos);
        }
        if (userPartUnavailableCause != null) {
            userPartUnavailableCause.encode(tmpBaos);
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
                case USER_CAUSE:
                    userPartUnavailableCause = new UserPartUnavailableCause();
                    userPartUnavailableCause.decode(mbais);
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
     * @return the userCause
     */
    public UserPartUnavailableCause getUserPartUnavailableCause() {
        return userPartUnavailableCause;
    }

    /**
     * @param userCause the userCause to set
     */
    public void setUserPartUnavailableCause(UserPartUnavailableCause userCause) {
        this.userPartUnavailableCause = userCause;
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
}
