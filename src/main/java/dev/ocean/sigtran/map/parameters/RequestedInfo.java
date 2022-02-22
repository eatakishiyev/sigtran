/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * RequestedInfo ::= SEQUENCE {
 * locationInformation [0] NULL OPTIONAL,
 * subscriberState [1] NULL OPTIONAL,
 * extensionContainer [2] ExtensionContainer
 * OPTIONAL,
 * ...,
 * currentLocation [3] NULL OPTIONAL,
 * requestedDomain [4] DomainType OPTIONAL,
 * imei [6] NULL OPTIONAL,
 * ms-classmark [5] NULL OPTIONAL,
 * mnpRequestedInfo [7] NULL OPTIONAL,
 * locationInformationEPS-Supported [11] NULL OPTIONAL,
 * t-adsData [8] NULL OPTIONAL,
 * requestedNodes [9] RequestedNodes OPTIONAL,
 * servingNodeIndication [10] NULL OPTIONAL,
 * localTimeZoneRequest [12] NULL OPTIONAL 
 * }
 *
 * @author eatakishiyev
 */
public class RequestedInfo {

    private boolean locationInformation;
    private boolean subscriberState;
    private ExtensionContainer extensionContainer;
    private boolean currentLocation;
    private DomainType requestedDomain;
    private boolean imei;
    private boolean msClassMark;
    private boolean mnpRequestedInfo;
    private boolean locationInformationEPSSupported;
    private boolean tAdsData;
    private RequestedNodes requestedNodes;
    private boolean servingNodeIndication;
    private boolean localTimeZoneRequest;

    public RequestedInfo() {
    }

    public RequestedInfo(AsnInputStream ais) throws IncorrectSyntaxException{
        this.decode(ais);
    }
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int pos = aos.StartContentDefiniteLength();
            if (locationInformation) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0);
            }
            if (subscriberState) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 1);
            }
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            if (currentLocation) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 3);
            }

            if (requestedDomain != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 4, requestedDomain.value());
            }

            if (imei) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 6);
            }
            if (msClassMark) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 5);
            }
            if (mnpRequestedInfo) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 7);
            }
            if (locationInformationEPSSupported) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 11);
            }
            if (tAdsData) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 8);
            }
            if (requestedNodes != null) {
                requestedNodes.encode(Tag.CLASS_CONTEXT_SPECIFIC, 9, aos);
            }
            if (servingNodeIndication) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 10);
            }
            if (localTimeZoneRequest) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 12);
            }

            aos.FinalizeContent(pos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public final void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            this._decode(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IOException, IncorrectSyntaxException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new IncorrectSyntaxException(String.format("Expecting TagClass[CONTEXT], found Tag[%d] Class[%d]", tag, ais.getTagClass()));
            }
            switch (tag) {
                case 0:
                    this.locationInformation = true;
                    ais.readNull();
                    break;
                case 1:
                    this.subscriberState = true;
                    ais.readNull();
                    break;
                case 2:
                    this.extensionContainer = new ExtensionContainer(ais);
                    break;
                case 3:
                    this.currentLocation = true;
                    ais.readNull();
                    break;
                case 4:
                    requestedDomain = DomainType.getInstance((int) ais.readInteger());
                    break;
                case 6:
                    this.imei = true;
                    ais.readNull();
                    break;
                case 5:
                    this.msClassMark = true;
                    ais.readNull();
                    break;
                case 7:
                    this.mnpRequestedInfo = true;
                    ais.readNull();
                    break;
                case 11:
                    this.locationInformationEPSSupported = true;
                    ais.readNull();
                    break;
                case 8:
                    this.tAdsData = true;
                    ais.readNull();
                    break;
                case 9:
                    this.requestedNodes = new RequestedNodes();
                    this.requestedNodes.decode(ais);
                    break;
                case 10:
                    this.servingNodeIndication = true;
                    ais.readNull();
                    break;
                case 12:
                    this.localTimeZoneRequest = true;
                    ais.readNull();
                    break;
                default:
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%d] Class[%d] received.", tag, ais.getTagClass()));
            }
        }
    }

    public void setCurrentLocation(boolean currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isCurrentLocation() {
        return currentLocation;
    }

    public void setRequestedDomain(DomainType requestedDomain) {
        this.requestedDomain = requestedDomain;
    }

    public DomainType getRequestedDomain() {
        return requestedDomain;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setImei(boolean imei) {
        this.imei = imei;
    }

    public boolean isImei() {
        return imei;
    }

    public void setLocalTimeZoneRequest(boolean localTimeZoneRequest) {
        this.localTimeZoneRequest = localTimeZoneRequest;
    }

    public boolean isLocalTimeZoneRequest() {
        return localTimeZoneRequest;
    }

    public void setLocationInformation(boolean locationInformation) {
        this.locationInformation = locationInformation;
    }

    public boolean isLocationInformation() {
        return locationInformation;
    }

    public void setLocationInformationEPSSupported(boolean locationInformationEPSSupported) {
        this.locationInformationEPSSupported = locationInformationEPSSupported;
    }

    public boolean isLocationInformationEPSSupported() {
        return locationInformationEPSSupported;
    }

    public void setMnpRequestedInfo(boolean mnpRequestedInfo) {
        this.mnpRequestedInfo = mnpRequestedInfo;
    }

    public boolean isMnpRequestedInfo() {
        return mnpRequestedInfo;
    }

    public void setMsClassMark(boolean msClassMark) {
        this.msClassMark = msClassMark;
    }

    public boolean isMsClassMark() {
        return msClassMark;
    }

    public void setRequestedNodes(RequestedNodes requestedNodes) {
        this.requestedNodes = requestedNodes;
    }

    public RequestedNodes getRequestedNodes() {
        return requestedNodes;
    }

    public void setServingNodeIndication(boolean servingNodeIndication) {
        this.servingNodeIndication = servingNodeIndication;
    }

    public boolean isServingNodeIndication() {
        return servingNodeIndication;
    }

    public void setSubscriberState(boolean subscriberState) {
        this.subscriberState = subscriberState;
    }

    public boolean isSubscriberState() {
        return subscriberState;
    }

    public void settAdsData(boolean tAdsData) {
        this.tAdsData = tAdsData;
    }

    public boolean istAdsData() {
        return tAdsData;
    }

}
