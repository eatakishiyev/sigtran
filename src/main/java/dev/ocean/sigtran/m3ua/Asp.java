/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import dev.ocean.sctp.SctpAssociation;
import java.io.IOException;
import java.util.Collection;
import dev.ocean.sigtran.m3ua.messages.mgmt.ErrorCodes;
import dev.ocean.sigtran.m3ua.messages.transfer.PayloadData;
import dev.ocean.sigtran.m3ua.parameters.AffectedPointCode;
import dev.ocean.sigtran.m3ua.parameters.DiagnosticInformation;
import dev.ocean.sigtran.m3ua.parameters.RoutingContext;
import dev.ocean.sigtran.m3ua.parameters.TrafficMode;
import java.io.Serializable;

/**
 *
 * @author eatakishiyev
 */
public abstract class Asp implements Serializable {

    private TrafficMode trafficMode;

    private Integer aspId;

    public abstract void stop();

    public abstract void shutdown();

    public abstract void start() throws Exception;

    public abstract String getName();

    public abstract void setName(String name) throws Exception;

    public abstract LinkType getLinkType();

    public abstract void setLinkType(LinkType connectionMode);

    public abstract boolean isNoDelay();

    public abstract void setNoDelay(boolean nodelay);

    public abstract int getSoSndBuf();

    public abstract void setSoSndBuf(int soSndBuf);

    public abstract int getSoRcvBuf();

    public abstract void setSoRcvBuf(int soRcvBuf);

    public abstract long getAspUpTimeOut();

    public abstract long getAspDownTimeOut();

    public abstract long getAspInactiveTimeOut();

    public abstract long getAspActiveTimeOut();

    public abstract void sendAspUp();

    public abstract void sendAspUpAck() throws IOException;

    public abstract void sendDAVA(AffectedPointCode affectedPointCode) throws IOException;

    public abstract void sendAspActive(As as) throws IOException;

    public abstract void sendAspActiveAck(As as) throws IOException;

    public abstract void sendAspInactive() throws Exception;

    public abstract void sendAspInactive(Integer rc) throws Exception;

    public abstract void sendAspInactiveAck(As as) throws Exception;

    public abstract void sendHeartBeat(byte[] data);

    public abstract void sendHeartBeatAck(byte[] data);

    public abstract void sendError(ErrorCodes errorCode, DiagnosticInformation diagnosticInformation, RoutingContext rc);

    public abstract void sendPayload(PayloadData payload);

    public abstract StateMaintenanceMessageHandler getStateMaintenanceMessageHandler();

    public abstract TrafficManagementMessageHandler getTrafficManagementMessageHandler();

    public abstract ManagementMessageHandler getManagementMessageHandler();

    public abstract SignallingNetworkManagementMessageHandler getSignallingNetworkManagementMessageHandler();

    public abstract Collection<AspStateMachine> aspStateMachines();

    public abstract AspStateMachine getAspStateMachine(int rc);

    public abstract NodeType getNodeType();

//    public void onCommunicationLost();
//
//    public void onCommunicationRestart();
//
//    public void onCommunicationUp();
    public abstract int getRcCount();

//    public abstract List<AspStateListener> getAspStateListeners();
//    public abstract void addAspStateListener(AspStateListener aspStateListener);
//    public abstract void removeAspStateListener(AspStateListener aspStateListener);
    public abstract SctpAssociation getSctpAssociation();

    public abstract int getWorkersCount();

    public abstract void setWorkersCount(int workersCount);

    public abstract boolean isConnected();

    public void setTrafficMode(TrafficMode trafficMode) {
        this.trafficMode = trafficMode;
    }

    public TrafficMode getTrafficMode() {
        return this.trafficMode;
    }

    public Integer getAspId() {
        return aspId;
    }

    public void setAspId(Integer aspId) {
        this.aspId = aspId;
    }
}
