/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.parameters;

import java.io.IOException;
import dev.ocean.sigtran.m3ua.NetworkIndicator;
import dev.ocean.sigtran.m3ua.ServiceIdentificator;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.messages.mgmt.ErrorCodes;

/**
 *
 * @author root
 */
public class ParameterFactory {

    public ASPIdentifier createASPIdentifier(Integer aspId) {
        ASPIdentifier aSPIdentifier = new ASPIdentifier();
        aSPIdentifier.setValue(aspId);
        return aSPIdentifier;
    }

    public AffectedPointCode createAffectedPointCode(PointCode[] affectedPointCodes) {
        AffectedPointCode affectedPointCode = new AffectedPointCode();
        affectedPointCode.setoAffectedPointCodes(affectedPointCodes);
        return affectedPointCode;
    }

    public ConcernedDPC createConcernedDPC(int pointCode) {
        ConcernedDPC concernedDPC = new ConcernedDPC();
        concernedDPC.setPointCode(pointCode);
        return concernedDPC;
    }

    public CongestionIndication createCongestionLevel(CongestionLevel level) {
        CongestionIndication congestionLevel = new CongestionIndication();
        congestionLevel.setCongestionLevel(level);
        return congestionLevel;
    }

    public CorrelationID createCorrelationID(Integer id) {
        CorrelationID correlationID = new CorrelationID();
        correlationID.setCorrelationID(id);
        return correlationID;
    }

    public DiagnosticInformation createDiagnosticInformation(byte[] information) {
        DiagnosticInformation diagnosticInformation = new DiagnosticInformation();
        diagnosticInformation.setDiagnosticInformation(information);
        return diagnosticInformation;
    }

    public ErrorCode createErrorCode(ErrorCodes code) {
        ErrorCode errorCode = new ErrorCode();
        errorCode.setErrorCode(code);
        return errorCode;
    }

    public HeartBeatData createBeatData(byte[] data) {
        HeartBeatData heartBeatData = new HeartBeatData();
        heartBeatData.setHeartbeatData(data);
        return heartBeatData;
    }

    public InfoString createInfoString(String infoString) {
        return new InfoString(infoString);
    }

    public NetworkAppearance createNetworkApprAppearance(Integer appearance) {
        NetworkAppearance networkAppearance = new NetworkAppearance();
        networkAppearance.setNetworkAppearance(appearance);
        return networkAppearance;
    }

    public OriginationPointCodeList createOriginationPointCode(OriginationPointCode[] originationPointCodes) {
        OriginationPointCodeList originationPointCode = new OriginationPointCodeList();
        originationPointCode.setOriginationPointCodes(originationPointCodes);
        return originationPointCode;
    }

    public ProtocolData createProtocolData(Integer opc, Integer dpc, ServiceIdentificator si, NetworkIndicator ni, Integer mp, Integer sls, byte[] data) {
        return new ProtocolData(opc, dpc, si, ni, mp, sls, data);
    }

    public RoutingContext createRoutingContext(Integer[] routingContexts) {
        RoutingContext routingContext = new RoutingContext();
        routingContext.setRoutingContext(routingContexts);
        return routingContext;
    }

    public Status createStatus(StatusType statusType, StatusInformation statusInformation) {
        Status status = new Status();
        status.setStatusType(statusType);
        status.setStatusInformation(statusInformation);
        return status;
    }

    public TrafficModeType createTrafficMode(TrafficMode mode) {
        TrafficModeType trafficModeType = new TrafficModeType();
        trafficModeType.setTrafficMode(mode);
        return trafficModeType;
    }

    public UserPartUnavailableCause createUserCause(Cause cause, ServiceIdentificator user) {
        UserPartUnavailableCause userCause = new UserPartUnavailableCause();
        userCause.setCause(cause);
        userCause.setUser(user);
        return userCause;
    }

    public DestinationPointCode createDestinationPointCode(Integer pointCode) {
        DestinationPointCode destinationPointCode = new DestinationPointCode();
        destinationPointCode.setDestinationPointCode(pointCode);
        return destinationPointCode;
    }

    public Parameter createParameter(ParameterTag tag, byte[] data) throws IOException {
        switch (tag) {
            case ASP_IDENTIFIER:
                ASPIdentifier aSPIdentifier = new ASPIdentifier();
                aSPIdentifier.decode(new M3UAParameterByteArrayInputStream(data));
                return aSPIdentifier;
            case AFFECTED_POINT_CODE:
                AffectedPointCode affectedPointCode = new AffectedPointCode();
                affectedPointCode.decode(new M3UAParameterByteArrayInputStream(data));
                return affectedPointCode;
            case CONCERNED_DESTINATIONS:
                ConcernedDPC concernedDPC = new ConcernedDPC();
                concernedDPC.decode(new M3UAParameterByteArrayInputStream(data));
                return concernedDPC;
            case CONGESTION_INDICATIONS:
                CongestionIndication congestionLevel = new CongestionIndication();
                congestionLevel.decode(new M3UAParameterByteArrayInputStream(data));
                return congestionLevel;
            case CORRELATION_ID:
                CorrelationID correlationID = new CorrelationID();
                correlationID.decode(new M3UAParameterByteArrayInputStream(data));
                return correlationID;
            case DESTINATION_POINT_CODE:
                DestinationPointCode destinationPointCode = new DestinationPointCode();
                destinationPointCode.decode(new M3UAParameterByteArrayInputStream(data));
                return destinationPointCode;
            case DIAGNOSTIC_INFORMATION:
                DiagnosticInformation diagnosticInformation = new DiagnosticInformation();
                M3UAParameterByteArrayInputStream stream = new M3UAParameterByteArrayInputStream(data);
                diagnosticInformation.decode(stream);
                return diagnosticInformation;
            case ERROR_CODE:
                ErrorCode errorCode = new ErrorCode();
                errorCode.decode(new M3UAParameterByteArrayInputStream(data));
                return errorCode;
            case HEARTBEAD_DATA:
                HeartBeatData heartBeatData = new HeartBeatData();
                stream = new M3UAParameterByteArrayInputStream(data);
                heartBeatData.decode(stream);
                return heartBeatData;
            case INFO_STRING:
                InfoString infoString = new InfoString();
                stream = new M3UAParameterByteArrayInputStream(data);
                infoString.decode(stream);
                return infoString;
            case NETWORK_APPEARANCE:
                NetworkAppearance networkAppearance = new NetworkAppearance();
                networkAppearance.decode(new M3UAParameterByteArrayInputStream(data));
                return networkAppearance;
            case ORIGINATING_POINT_CODE_LIST:
                OriginationPointCodeList originationPointCode = new OriginationPointCodeList();
                originationPointCode.decode(new M3UAParameterByteArrayInputStream(data));
                return originationPointCode;
            case PROTOCOL_DATA:
                ProtocolData protocolData = new ProtocolData();
                stream = new M3UAParameterByteArrayInputStream(data);
                protocolData.decode(stream);
                return protocolData;
            case ROUTING_CONTEXT:
                RoutingContext routingContext = new RoutingContext();
                routingContext.decode(new M3UAParameterByteArrayInputStream(data));
                return routingContext;
            case STATUS:
                Status status = new Status();
                status.decode(new M3UAParameterByteArrayInputStream(data));
                return status;
            case TRAFFIC_MODE_TYPE:
                TrafficModeType trafficMode = new TrafficModeType();
                trafficMode.decode(new M3UAParameterByteArrayInputStream(data));
                return trafficMode;
            case USER_CAUSE:
                UserPartUnavailableCause userCause = new UserPartUnavailableCause();
                userCause.decode(new M3UAParameterByteArrayInputStream(data));
                return userCause;
            default:
                return null;
        }
    }
}
