/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.common;

import azrc.az.sigtran.map.ProviderError;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.ISDNAddressString;
import azrc.az.sigtran.map.parameters.MAPApplicationContextImpl;
import azrc.az.sigtran.sccp.address.SCCPAddress;

/**
 *
 * @author root
 */
public class MapOpen {

    /**
     * Destination-reference: *
     *
     * This parameter is a reference that refines the identification of the
     * called process. It may be identical to Destination address but its value
     * is to be carried at MAP level. Table 7.3/2 describes the MAP services
     * using this parameter.
     *
     */
    /**
     * Originating-reference: *
     *
     * This parameter is a reference that refines the identification of the
     * calling process. It may be identical to the Originating address but its
     * value is to be carried at MAP level. Table 7.3/3 describes the MAP
     * services using the parameter. Only these services are allowed to use it.
     * Processing of the Originating-reference shall be performed according to
     * the supplementary service descriptions and other service descriptions,
     * e.g. operator determined barring. Furthermore the receiving entity may be
     * able to use the value of the Originating-reference to screen the service
     * indication.
     */
    private MAPApplicationContextImpl mapApplicationContext;
    private SCCPAddress destinationAddress;
    private ISDNAddressString destinationReference;
    private SCCPAddress originatingAddress;
    private ISDNAddressString originatingReference;
    private ExtensionContainer specificInformation;
    private SCCPAddress respondingAddress;
    private Result result;
    private RefuseReason refuseReason;
    private ProviderError providerError;

    public MapOpen() {
    }

    /**
     * @return the respondingAddress
     */
    public SCCPAddress getRespondingAddress() {
        return respondingAddress;
    }

    /**
     * @param respondingAddress the respondingAddress to set
     */
    public void setRespondingAddress(SCCPAddress respondingAddress) {
        this.respondingAddress = respondingAddress;
    }

    /**
     * @return the result
     */
    public Result getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * @return the refuseReason
     */
    public RefuseReason getRefuseReason() {
        return refuseReason;
    }

    /**
     * @param refuseReason the refuseReason to set
     */
    public void setRefuseReason(RefuseReason refuseReason) {
        this.refuseReason = refuseReason;
    }

    /**
     * @return the mapApplicationContext
     */
    public MAPApplicationContextImpl getMapApplicationContext() {
        return mapApplicationContext;
    }

    /**
     * @param mapApplicationContext the mapApplicationContext to set
     */
    public void setMapApplicationContext(MAPApplicationContextImpl mapApplicationContext) {
        this.mapApplicationContext = mapApplicationContext;
    }

    /**
     * @return the destinationAddress
     */
    public SCCPAddress getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * @param destinationAddress the destinationAddress to set
     */
    public void setDestinationAddress(SCCPAddress destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    /**
     * @return the originatingAddress
     */
    public SCCPAddress getOriginatingAddress() {
        return originatingAddress;
    }

    /**
     * @param originatingAddress the originatingAddress to set
     */
    public void setOriginatingAddress(SCCPAddress originatingAddress) {
        this.originatingAddress = originatingAddress;
    }

    /**
     * @return the destinationReference
     */
    public ISDNAddressString getDestinationReference() {
        return destinationReference;
    }

    /**
     * @param destinationReference the destinationReference to set
     */
    public void setDestinationReference(ISDNAddressString destinationReference) {
        this.destinationReference = destinationReference;
    }

    /**
     * @return the originatingReference
     */
    public ISDNAddressString getOriginatingReference() {
        return originatingReference;
    }

    /**
     * @param originatingReference the originatingReference to set
     */
    public void setOriginatingReference(ISDNAddressString originatingReference) {
        this.originatingReference = originatingReference;
    }

    /**
     * @return the specificInformation
     */
    public ExtensionContainer getSpecificInformation() {
        return specificInformation;
    }

    /**
     * @param specificInformation the specificInformation to set
     */
    public void setSpecificInformation(ExtensionContainer specificInformation) {
        this.specificInformation = specificInformation;
    }

    /**
     * @return the providerError
     */
    public ProviderError getProviderError() {
        return providerError;
    }

    /**
     * @param providerError the providerError to set
     */
    public void setProviderError(ProviderError providerError) {
        this.providerError = providerError;
    }
}
