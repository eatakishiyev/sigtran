/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.location.info.retrieval;

import java.io.IOException;

import azrc.az.sigtran.m3ua.UnavailabilityCause;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.parameters.AllowedServices;
import azrc.az.sigtran.map.parameters.CCBSIndicators;
import azrc.az.sigtran.map.parameters.CUGCheckInfo;
import azrc.az.sigtran.map.parameters.ExtBasicServiceCode;
import azrc.az.sigtran.map.parameters.ExtendedRoutingInfo;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.ExternalSignalInfo;
import azrc.az.sigtran.map.parameters.IMSI;
import azrc.az.sigtran.map.parameters.ISDNAddressString;
import azrc.az.sigtran.map.parameters.ISTAlertTimerValue;
import azrc.az.sigtran.map.parameters.NAEAPrefferedCI;
import azrc.az.sigtran.map.parameters.NumberPortabilityStatus;
import azrc.az.sigtran.map.parameters.OfferedCamel4CSIs;
import azrc.az.sigtran.map.parameters.RoutingInfo;
import azrc.az.sigtran.map.parameters.SSList;
import azrc.az.sigtran.map.parameters.SupportedCamelPhases;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import azrc.az.sigtran.map.services.common.MAPResponse;

/**
 * SendRoutingInfoRes ::= [3] SEQUENCE {
 * imsi [9] IMSI OPTIONAL,
 * -- IMSI must be present if SendRoutingInfoRes is not segmented.
 * -- If the TC-Result-NL segmentation option is taken the IMSI must be
 * -- present in one segmented transmission of SendRoutingInfoRes.
 * extendedRoutingInfo ExtendedRoutingInfo OPTIONAL,
 * cug-CheckInfo [3] CUG-CheckInfo OPTIONAL,
 * cugSubscriptionFlag [6] NULL OPTIONAL,
 * subscriberInfo [7] SubscriberInfo OPTIONAL,
 * ss-List [1] SS-List OPTIONAL,
 * basicService [5] Ext-BasicServiceCode OPTIONAL,
 * forwardingInterrogationRequired [4] NULL OPTIONAL,
 * vmsc-Address [2] ISDN-AddressString OPTIONAL,
 * extensionContainer [0] ExtensionContainer OPTIONAL,
 * ... ,
 * naea-PreferredCI [10] NAEA-PreferredCI OPTIONAL,
 * -- naea-PreferredCI is included at the discretion of the HLR operator.
 * ccbs-Indicators [11] CCBS-Indicators OPTIONAL,
 * msisdn [12] ISDN-AddressString OPTIONAL,
 * numberPortabilityStatus [13] NumberPortabilityStatus OPTIONAL,
 * istAlertTimer [14] IST-AlertTimerValue OPTIONAL,
 * supportedCamelPhasesInVMSC [15] SupportedCamelPhases OPTIONAL,
 * offeredCamel4CSIsInVMSC [16] OfferedCamel4CSIs OPTIONAL,
 * routingInfo2 [17] RoutingInfo OPTIONAL,
 * ss-List2 [18] SS-List OPTIONAL,
 * basicService2 [19] Ext-BasicServiceCode OPTIONAL,
 * allowedServices [20] AllowedServices OPTIONAL,
 * unavailabilityCause [21] UnavailabilityCause OPTIONAL,
 * releaseResourcesSupported [22] NULL OPTIONAL,
 * gsm-BearerCapability [23] ExternalSignalInfo OPTIONAL
 * }
 *
 * @author eatakishiyev
 */
public class SendRoutingInfoRes implements MAPResponse {

    private IMSI imsi;
    private ExtendedRoutingInfo extendedRoutingInfo;
    private CUGCheckInfo cugCheckInfo;
    private boolean cugSubcriptionFlag;
    private byte[] subscriberInfo;
    private SSList ssList;
    private ExtBasicServiceCode basicService;
    private boolean forwardingInterrogationRequired;
    private ISDNAddressString vmscAddress;
    private ExtensionContainer extensionContainer;
    private NAEAPrefferedCI naeaPreferredCI;
    private CCBSIndicators ccbsIndicators;
    private ISDNAddressString msisdn;
    private NumberPortabilityStatus numberPortabilityStatus;
    private ISTAlertTimerValue istAlertTimer;
    private SupportedCamelPhases supportedCamelPhases;
    private OfferedCamel4CSIs offeredCamel4CSIsInVMSC;
    private RoutingInfo routingInfo2;
    private SSList ssList2;
    private ExtBasicServiceCode basicService2;
    private AllowedServices allowedServices;
    private UnavailabilityCause unavailabilityCause;
    private boolean releaseResourceSupported;
    private ExternalSignalInfo gsmBearerCapability;
    private byte[] responseData;
    protected boolean responseCorrupted = false;

    public SendRoutingInfoRes() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 3);
            int lenPos = aos.StartContentDefiniteLength();

            if (imsi != null) {
                imsi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 9, aos);
            }
            if (extendedRoutingInfo != null) {
                extendedRoutingInfo.encode(aos);
            }
            if (cugCheckInfo != null) {
                cugCheckInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }
            if (cugSubcriptionFlag) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 6);
            }
            if (subscriberInfo != null) {
                aos.writeSequence(Tag.CLASS_CONTEXT_SPECIFIC, 7, subscriberInfo);
            }
            if (ssList != null) {
                ssList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
            if (basicService != null) {
                basicService.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            }
            if (forwardingInterrogationRequired) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 4);
            }
            if (vmscAddress != null) {
                vmscAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }
            if (naeaPreferredCI != null) {
                naeaPreferredCI.encode(Tag.CLASS_CONTEXT_SPECIFIC, 10, aos);
            }
            if (ccbsIndicators != null) {
                ccbsIndicators.encode(Tag.CLASS_CONTEXT_SPECIFIC, 11, aos);
            }
            if (msisdn != null) {
                msisdn.encode(Tag.CLASS_CONTEXT_SPECIFIC, 12, aos);
            }
            if (numberPortabilityStatus != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 13, numberPortabilityStatus.value());
            }
            if (istAlertTimer != null) {
                istAlertTimer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 14, aos);
            }
            if (supportedCamelPhases != null) {
                supportedCamelPhases.encode(Tag.CLASS_CONTEXT_SPECIFIC, 15, aos);
            }
            if (offeredCamel4CSIsInVMSC != null) {
                offeredCamel4CSIsInVMSC.encode(Tag.CLASS_CONTEXT_SPECIFIC, 16, aos);
            }
            if (routingInfo2 != null) {
                routingInfo2.encode(Tag.CLASS_CONTEXT_SPECIFIC, 17, aos);
            }
            if (ssList2 != null) {
                ssList2.encode(Tag.CLASS_CONTEXT_SPECIFIC, 18, aos);
            }
            if (basicService2 != null) {
                basicService2.encode(Tag.CLASS_CONTEXT_SPECIFIC, 19, aos);
            }
            if (allowedServices != null) {
                allowedServices.encode(Tag.CLASS_CONTEXT_SPECIFIC, 20, aos);
            }
            if (unavailabilityCause != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 21, unavailabilityCause.getCause());
            }
            if (releaseResourceSupported) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 22);
            }
            if (gsmBearerCapability != null) {
                gsmBearerCapability.encode(Tag.CLASS_CONTEXT_SPECIFIC, 23, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.responseData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
            this._decode(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IOException, UnexpectedDataException, IncorrectSyntaxException, AsnException {

        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (tag == 9 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.imsi = new IMSI(ais);
            } else if (((tag == Tag.STRING_OCTET || tag == Tag.SEQUENCE)
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL)
                    || (tag == 8 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC)) {
                this.extendedRoutingInfo = new ExtendedRoutingInfo();
                extendedRoutingInfo.decode(ais);
            } else if (tag == 3 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.cugCheckInfo = new CUGCheckInfo();
                cugCheckInfo.decode(ais.readSequenceStream());
            } else if (tag == 6 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.cugSubcriptionFlag = true;
                ais.readNull();
            } else if (tag == 7 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.subscriberInfo = ais.readSequence();
            } else if (tag == 1 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.ssList = new SSList();
                ssList.decode(ais.readSequenceStream());
            } else if (tag == 5 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.basicService = new ExtBasicServiceCode();
                basicService.decode(ais);
            } else if (tag == 4 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.forwardingInterrogationRequired = true;
                ais.readNull();
            } else if (tag == 2 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.vmscAddress = new ISDNAddressString(ais);
            } else if (tag == 0 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.extensionContainer = new ExtensionContainer(ais);
            } else if (tag == 10 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.naeaPreferredCI = new NAEAPrefferedCI();
                naeaPreferredCI.decode(ais.readSequenceStream());
            } else if (tag == 11 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.ccbsIndicators = new CCBSIndicators();
                ccbsIndicators.decode(ais.readSequenceStream());
            } else if (tag == 12 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.msisdn = new ISDNAddressString(ais);
            } else if (tag == 13 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.numberPortabilityStatus = NumberPortabilityStatus.getInstance((int) ais.readInteger());
            } else if (tag == 14 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.istAlertTimer = new ISTAlertTimerValue();
                istAlertTimer.decode(ais);
            } else if (tag == 15 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.supportedCamelPhases = new SupportedCamelPhases();
                supportedCamelPhases.decode(ais);
            } else if (tag == 16 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.offeredCamel4CSIsInVMSC = new OfferedCamel4CSIs();
                offeredCamel4CSIsInVMSC.decode(ais);
            } else if (tag == 17 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.routingInfo2 = new RoutingInfo();
                routingInfo2.decode(ais);
            } else if (tag == 18 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.ssList2 = new SSList();
                ssList2.decode(ais.readSequenceStream());
            } else if (tag == 19 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.basicService2 = new ExtBasicServiceCode();
                basicService2.decode(ais);
            } else if (tag == 20 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.allowedServices = new AllowedServices();
                allowedServices.decode(ais);
            } else if (tag == 21 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.unavailabilityCause = UnavailabilityCause.getInstance((int) ais.readInteger());
            } else if (tag == 22 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                releaseResourceSupported = true;
                ais.readNull();
            } else if (tag == 23 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.gsmBearerCapability = new ExternalSignalInfo();
                gsmBearerCapability.decode(ais.readSequenceStream());
            }
        }
    }

    /**
     * @return the imsi
     */
    public IMSI getImsi() {
        return imsi;
    }

    /**
     * @param imsi the imsi to set
     */
    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    /**
     * @return the extendedRoutingInfo
     */
    public ExtendedRoutingInfo getExtendedRoutingInfo() {
        return extendedRoutingInfo;
    }

    /**
     * @param extendedRoutingInfo the extendedRoutingInfo to set
     */
    public void setExtendedRoutingInfo(ExtendedRoutingInfo extendedRoutingInfo) {
        this.extendedRoutingInfo = extendedRoutingInfo;
    }

    /**
     * @return the cugCheckInfo
     */
    public CUGCheckInfo getCugCheckInfo() {
        return cugCheckInfo;
    }

    /**
     * @param cugCheckInfo the cugCheckInfo to set
     */
    public void setCugCheckInfo(CUGCheckInfo cugCheckInfo) {
        this.cugCheckInfo = cugCheckInfo;
    }

    /**
     * @return the cugSubcriptionFlag
     */
    public boolean isCugSubcriptionFlag() {
        return cugSubcriptionFlag;
    }

    /**
     * @param cugSubcriptionFlag the cugSubcriptionFlag to set
     */
    public void setCugSubcriptionFlag(boolean cugSubcriptionFlag) {
        this.cugSubcriptionFlag = cugSubcriptionFlag;
    }

    /**
     * @return the subscriberInfo
     */
    public byte[] getSubscriberInfo() {
        return subscriberInfo;
    }

    /**
     * @param subscriberInfo the subscriberInfo to set
     */
    public void setSubscriberInfo(byte[] subscriberInfo) {
        this.subscriberInfo = subscriberInfo;
    }

    /**
     * @return the ssList
     */
    public SSList getSsList() {
        return ssList;
    }

    /**
     * @param ssList the ssList to set
     */
    public void setSsList(SSList ssList) {
        this.ssList = ssList;
    }

    /**
     * @return the basicService
     */
    public ExtBasicServiceCode getBasicService() {
        return basicService;
    }

    /**
     * @param basicService the basicService to set
     */
    public void setBasicService(ExtBasicServiceCode basicService) {
        this.basicService = basicService;
    }

    /**
     * @return the forwardingInterrogationRequired
     */
    public boolean isForwardingInterrogationRequired() {
        return forwardingInterrogationRequired;
    }

    /**
     * @param forwardingInterrogationRequired the
     * forwardingInterrogationRequired to set
     */
    public void setForwardingInterrogationRequired(boolean forwardingInterrogationRequired) {
        this.forwardingInterrogationRequired = forwardingInterrogationRequired;
    }

    /**
     * @return the vmscAddress
     */
    public ISDNAddressString getVmscAddress() {
        return vmscAddress;
    }

    /**
     * @param vmscAddress the vmscAddress to set
     */
    public void setVmscAddress(ISDNAddressString vmscAddress) {
        this.vmscAddress = vmscAddress;
    }

    /**
     * @return the extensionContainer
     */
    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    /**
     * @param extensionContainer the extensionContainer to set
     */
    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    /**
     * @return the naeaPreferredCI
     */
    public NAEAPrefferedCI getNaeaPreferredCI() {
        return naeaPreferredCI;
    }

    /**
     * @param naeaPreferredCI the naeaPreferredCI to set
     */
    public void setNaeaPreferredCI(NAEAPrefferedCI naeaPreferredCI) {
        this.naeaPreferredCI = naeaPreferredCI;
    }

    /**
     * @return the ccbsIndicators
     */
    public CCBSIndicators getCcbsIndicators() {
        return ccbsIndicators;
    }

    /**
     * @param ccbsIndicators the ccbsIndicators to set
     */
    public void setCcbsIndicators(CCBSIndicators ccbsIndicators) {
        this.ccbsIndicators = ccbsIndicators;
    }

    /**
     * @return the msisdn
     */
    public ISDNAddressString getMsisdn() {
        return msisdn;
    }

    /**
     * @param msisdn the msisdn to set
     */
    public void setMsisdn(ISDNAddressString msisdn) {
        this.msisdn = msisdn;
    }

    /**
     * @return the numberPortabilityStatus
     */
    public NumberPortabilityStatus getNumberPortabilityStatus() {
        return numberPortabilityStatus;
    }

    /**
     * @param numberPortabilityStatus the numberPortabilityStatus to set
     */
    public void setNumberPortabilityStatus(NumberPortabilityStatus numberPortabilityStatus) {
        this.numberPortabilityStatus = numberPortabilityStatus;
    }

    /**
     * @return the istAlertTimer
     */
    public ISTAlertTimerValue getIstAlertTimer() {
        return istAlertTimer;
    }

    /**
     * @param istAlertTimer the istAlertTimer to set
     */
    public void setIstAlertTimer(ISTAlertTimerValue istAlertTimer) {
        this.istAlertTimer = istAlertTimer;
    }

    /**
     * @return the supportedCamelPhases
     */
    public SupportedCamelPhases getSupportedCamelPhases() {
        return supportedCamelPhases;
    }

    /**
     * @param supportedCamelPhases the supportedCamelPhases to set
     */
    public void setSupportedCamelPhases(SupportedCamelPhases supportedCamelPhases) {
        this.supportedCamelPhases = supportedCamelPhases;
    }

    /**
     * @return the offeredCamel4CSIsInVMSC
     */
    public OfferedCamel4CSIs getOfferedCamel4CSIsInVMSC() {
        return offeredCamel4CSIsInVMSC;
    }

    /**
     * @param offeredCamel4CSIsInVMSC the offeredCamel4CSIsInVMSC to set
     */
    public void setOfferedCamel4CSIsInVMSC(OfferedCamel4CSIs offeredCamel4CSIsInVMSC) {
        this.offeredCamel4CSIsInVMSC = offeredCamel4CSIsInVMSC;
    }

    /**
     * @return the routingInfo2
     */
    public RoutingInfo getRoutingInfo2() {
        return routingInfo2;
    }

    /**
     * @param routingInfo2 the routingInfo2 to set
     */
    public void setRoutingInfo2(RoutingInfo routingInfo2) {
        this.routingInfo2 = routingInfo2;
    }

    /**
     * @return the ssList2
     */
    public SSList getSsList2() {
        return ssList2;
    }

    /**
     * @param ssList2 the ssList2 to set
     */
    public void setSsList2(SSList ssList2) {
        this.ssList2 = ssList2;
    }

    /**
     * @return the basicService2
     */
    public ExtBasicServiceCode getBasicService2() {
        return basicService2;
    }

    /**
     * @param basicService2 the basicService2 to set
     */
    public void setBasicService2(ExtBasicServiceCode basicService2) {
        this.basicService2 = basicService2;
    }

    /**
     * @return the allowedServices
     */
    public AllowedServices getAllowedServices() {
        return allowedServices;
    }

    /**
     * @param allowedServices the allowedServices to set
     */
    public void setAllowedServices(AllowedServices allowedServices) {
        this.allowedServices = allowedServices;
    }

    /**
     * @return the unavailabilityCause
     */
    public UnavailabilityCause getUnavailabilityCause() {
        return unavailabilityCause;
    }

    /**
     * @param unavailabilityCause the unavailabilityCause to set
     */
    public void setUnavailabilityCause(UnavailabilityCause unavailabilityCause) {
        this.unavailabilityCause = unavailabilityCause;
    }

    /**
     * @return the releaseResourceSupported
     */
    public boolean isReleaseResourceSupported() {
        return releaseResourceSupported;
    }

    /**
     * @param releaseResourceSupported the releaseResourceSupported to set
     */
    public void setReleaseResourceSupported(boolean releaseResourceSupported) {
        this.releaseResourceSupported = releaseResourceSupported;
    }

    /**
     * @return the gsmBearerCapability
     */
    public ExternalSignalInfo getGsmBearerCapability() {
        return gsmBearerCapability;
    }

    /**
     * @param gsmBearerCapability the gsmBearerCapability to set
     */
    public void setGsmBearerCapability(ExternalSignalInfo gsmBearerCapability) {
        this.gsmBearerCapability = gsmBearerCapability;
    }

    public byte[] getResponseData() {
        return responseData;
    }

    public boolean isResponseCorrupted() {
        return responseCorrupted;
    }

}
