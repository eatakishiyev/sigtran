/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.parameters;

import java.io.IOException;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;

/**
 *
 * @author root
 */
public class RoutingKey implements Parameter {

    private LocalRoutingKeyIdentifier localRKIdentifier;
    private RoutingContext routingContext;
    private TrafficMode trafficMode;
    private DestinationPointCode[] destinationPointCode;
    private NetworkAppearance networkAppearance;
    private ServiceIndicators[] serviceIndicators;
    private OriginationPointCodeList[] originationPointCodes;
    private ParameterTag tag = ParameterTag.ROUTING_KEY;

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    /**
     * @return the localRKIdentifier
     */
    public LocalRoutingKeyIdentifier getLocalRKIdentifier() {
        return localRKIdentifier;
    }

    /**
     * @param localRKIdentifier the localRKIdentifier to set
     */
    public void setLocalRKIdentifier(LocalRoutingKeyIdentifier localRKIdentifier) {
        this.localRKIdentifier = localRKIdentifier;
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
     * @return the trafficMode
     */
    public TrafficMode getTrafficMode() {
        return trafficMode;
    }

    /**
     * @param trafficMode the trafficMode to set
     */
    public void setTrafficMode(TrafficMode trafficMode) {
        this.trafficMode = trafficMode;
    }

    /**
     * @return the destinationPointCode
     */
    public DestinationPointCode[] getDestinationPointCode() {
        return destinationPointCode;
    }

    /**
     * @param destinationPointCode the destinationPointCode to set
     */
    public void setDestinationPointCode(DestinationPointCode[] destinationPointCode) {
        this.destinationPointCode = destinationPointCode;
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
     * @return the serviceIndicators
     */
    public ServiceIndicators[] getServiceIndicators() {
        return serviceIndicators;
    }

    /**
     * @param serviceIndicators the serviceIndicators to set
     */
    public void setServiceIndicators(ServiceIndicators[] serviceIndicators) {
        this.serviceIndicators = serviceIndicators;
    }

    /**
     * @return the originationPointCodes
     */
    public OriginationPointCodeList[] getOriginationPointCodes() {
        return originationPointCodes;
    }

    /**
     * @param originationPointCodes the originationPointCodes to set
     */
    public void setOriginationPointCodes(OriginationPointCodeList[] originationPointCodes) {
        this.originationPointCodes = originationPointCodes;
    }
}