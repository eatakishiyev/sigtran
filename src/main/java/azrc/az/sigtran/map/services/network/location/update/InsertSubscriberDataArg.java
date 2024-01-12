/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.network.location.update;

import java.io.IOException;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.parameters.AccessRestrictionData;
import azrc.az.sigtran.map.parameters.AgeIndicator;
import azrc.az.sigtran.map.parameters.BearerServiceList;
import azrc.az.sigtran.map.parameters.Category;
import azrc.az.sigtran.map.parameters.DiameterIdentity;
import azrc.az.sigtran.map.parameters.ExtSSInfoList;
import azrc.az.sigtran.map.parameters.IMSI;
import azrc.az.sigtran.map.parameters.ISDNAddressString;
import azrc.az.sigtran.map.parameters.NAEAPrefferedCI;
import azrc.az.sigtran.map.parameters.NetworkAccessMode;
import azrc.az.sigtran.map.parameters.ODBData;
import azrc.az.sigtran.map.parameters.SubscribedPeriodicLAUTimer;
import azrc.az.sigtran.map.parameters.SubscribedPeriodicRAUTAUTimer;
import azrc.az.sigtran.map.parameters.SubscriberStatus;
import azrc.az.sigtran.map.parameters.TeleServiceList;
import azrc.az.sigtran.map.parameters.VBSDataList;
import azrc.az.sigtran.map.parameters.VGCSDataList;
import azrc.az.sigtran.map.parameters.VlrCamelSubscriptionInfo;
import azrc.az.sigtran.map.parameters.ZoneCodeList;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import azrc.az.sigtran.map.services.common.MAPArgument;

/**
 *
 * @author eatakishiyev
 */
public class InsertSubscriberDataArg implements MAPArgument {

    /**
     * InsertSubscriberDataArg ::= SEQUENCE {
     * imsi [0] IMSI OPTIONAL,
     * SubscriberData ::= SEQUENCE {
     * msisdn [1] ISDN-AddressString OPTIONAL,
     * category [2] Category OPTIONAL,
     * subscriberStatus [3] SubscriberStatus OPTIONAL,
     * bearerServiceList [4] BearerServiceList OPTIONAL,
     * -- The exception handling for reception of unsupported / not allocated
     * -- bearerServiceCodes is defined in section 8.8.1
     * teleserviceList [6] TeleserviceList OPTIONAL,
     * -- The exception handling for reception of unsupported / not allocated
     * -- teleserviceCodes is defined in section 8.8.1
     * provisionedSS [7] Ext-SS-InfoList OPTIONAL,
     * odb-Data [8] ODB-Data OPTIONAL,
     * roamingRestrictionDueToUnsupportedFeature [9] NULL OPTIONAL,
     * regionalSubscriptionData [10] ZoneCodeList OPTIONAL,
     * vbsSubscriptionData [11] VBSDataList OPTIONAL,
     * vgcsSubscriptionData [12] VGCSDataList OPTIONAL,
     * vlrCamelSubscriptionInfo [13] VlrCamelSubscriptionInfo OPTIONAL
     * }
     * extensionContainer [14] ExtensionContainer OPTIONAL,
     * ... ,
     * naea-PreferredCI [15] NAEA-PreferredCI OPTIONAL,
     * -- naea-PreferredCI is included at the discretion of the HLR operator.
     * gprsSubscriptionData [16] GPRSSubscriptionData OPTIONAL,
     * roamingRestrictedInSgsnDueToUnsupportedFeature [23] NULL
     * OPTIONAL, networkAccessMode [24] NetworkAccessMode OPTIONAL,
     * lsaInformation [25] LSAInformation OPTIONAL,
     * lmu-Indicator [21] NULL OPTIONAL,
     * lcsInformation [22] LCSInformation OPTIONAL,
     * istAlertTimer [26] IST-AlertTimerValue OPTIONAL,
     * superChargerSupportedInHLR [27] AgeIndicator OPTIONAL,
     * mc-SS-Info [28] MC-SS-Info OPTIONAL,
     * cs-AllocationRetentionPriority [29] CS-AllocationRetentionPriority
     * OPTIONAL,
     * sgsn-CAMEL-SubscriptionInfo [17] SGSN-CAMEL-SubscriptionInfo OPTIONAL,
     * chargingCharacteristics [18] ChargingCharacteristics OPTIONAL,
     * accessRestrictionData [19] AccessRestrictionData OPTIONAL,
     * ics-Indicator [20] BOOLEAN OPTIONAL,
     * eps-SubscriptionData [31] EPS-SubscriptionData OPTIONAL,
     * csg-SubscriptionDataList [32] CSG-SubscriptionDataList OPTIONAL,
     * ue-ReachabilityRequestIndicator [33] NULL OPTIONAL,
     * sgsn-Number [34] ISDN-AddressString OPTIONAL,
     * mme-Name [35] DiameterIdentity OPTIONAL,
     * subscribedPeriodicRAUTAUtimer [36] SubscribedPeriodicRAUTAUtimer
     * OPTIONAL,
     * vplmnLIPAAllowed [37] NULL OPTIONAL,
     * mdtUserConsent [38] BOOLEAN OPTIONAL,
     * subscribedPeriodicLAUtimer [39] SubscribedPeriodicLAUtimer OPTIONAL }
     * -- If the Network Access Mode parameter is sent, it shall be present only
     * in
     * -- the first sequence if seqmentation is used
     */
    private IMSI imsi;
    //Components of SubscriberData
    private ISDNAddressString msisdn;
    private Category category;
    private SubscriberStatus subscriberStatus;
    private BearerServiceList bearerServiceList;
    private TeleServiceList teleServiceList;
    private ExtSSInfoList provisionedSs;
    private ODBData odbData;
    private Boolean roamingRestrictionDueToUnsupportedFeature;
    private ZoneCodeList regionalSubscriptionData;
    private VBSDataList vbsSubscriptionData;
    private VGCSDataList vgcsSubscriptionData;
    private VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo;
    //End of components of SubscriberData
    private NAEAPrefferedCI naeaPrefferecCI;
    private byte[] gprsSubscriptionData;
    private Boolean roamingRestrictedInSgsnDueToUnsupportedFeature;
    private NetworkAccessMode networkAccessMode;
    private byte[] lsaInformation;
    private Boolean lmuIndicator;
    private byte[] lcsInformation;
    private byte[] istAlertTimer;
    private AgeIndicator superChargerSupportedInHLR;
    private byte[] mcSSInfo;
    private byte[] csAllocationRetentionPriority;
    private byte[] sgsnCamelSubscriptionInfo;
    private byte[] chargingCharacteristics;
    private AccessRestrictionData accessRestrictionData;
    private Boolean icsIndicator;
    private byte[] epsSubscriptionData;
    private byte[] csgSubscriptionData;
    private Boolean ueReachabilityRequestIndicator;
    private ISDNAddressString sgsnNumber;
    private DiameterIdentity mmeName;
    private SubscribedPeriodicRAUTAUTimer subscribedPeriodicRAUTAUTimer;
    private Boolean vplmnLIPAAllowed;
    private Boolean mdtUserConsent;
    private SubscribedPeriodicLAUTimer subscribedPeriodicLAUTimer;
    private byte[] requestData;
    protected boolean requestCorrupted = false;

    public InsertSubscriberDataArg() {
    }

    public InsertSubscriberDataArg(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.decode(data);
    }

    private void _decode(AsnInputStream ais) throws IOException, UnexpectedDataException, IncorrectSyntaxException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new IncorrectSyntaxException(String.format("Unexpected tag class received.Expecting Class[CONTEXT], found Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }
            switch (tag) {
                case 0:
                    this.imsi = new IMSI(ais);
                    break;
                case 1:
                    this.msisdn = new ISDNAddressString(ais);
                    break;
                case 2:
                    this.category = new Category();
                    this.category.decode(ais);
                    break;
                case 3:
                    this.subscriberStatus = new SubscriberStatus();
                    subscriberStatus.decode(ais);
                    break;
                case 4:
                    this.bearerServiceList = new BearerServiceList();
                    this.bearerServiceList.decode(ais.readSequenceStream());
                    break;
                case 6:
                    this.teleServiceList = new TeleServiceList();
                    this.teleServiceList.decode(ais.readSequenceStream());
                    break;
                default:
                    int length = ais.readLength();
                    byte[] data = new byte[length];
                    ais.read(data);
                    break;
            }
        }

    }

    @Override
    public final void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (this.imsi != null) {
                this.imsi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }

            if (this.msisdn != null) {
                this.msisdn.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }

            if (this.category != null) {
                this.category.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }

            if (subscriberStatus != null) {
                this.subscriberStatus.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }
            if (bearerServiceList != null) {
                this.bearerServiceList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }
            if (teleServiceList != null) {
                this.teleServiceList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }

            if (provisionedSs != null) {
                this.provisionedSs.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }

            if (accessRestrictionData != null) {
                this.accessRestrictionData.encode(Tag.CLASS_CONTEXT_SPECIFIC, 19, aos);
            }

            aos.FinalizeContent(lenPos);

        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }

    }

    @Override
    public final void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.requestData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this._decode(ais.readSequenceStream());
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException();
        }
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
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the subscriberStatus
     */
    public SubscriberStatus getSubscriberStatus() {
        return subscriberStatus;
    }

    /**
     * @param subscriberStatus the subscriberStatus to set
     */
    public void setSubscriberStatus(SubscriberStatus subscriberStatus) {
        this.subscriberStatus = subscriberStatus;
    }

    /**
     * @return the bearerServiceList
     */
    public BearerServiceList getBearerServiceList() {
        return bearerServiceList;
    }

    /**
     * @param bearerServiceList the bearerServiceList to set
     */
    public void setBearerServiceList(BearerServiceList bearerServiceList) {
        this.bearerServiceList = bearerServiceList;
    }

    /**
     * @return the teleServiceList
     */
    public TeleServiceList getTeleServiceList() {
        return teleServiceList;
    }

    /**
     * @param teleServiceList the teleServiceList to set
     */
    public void setTeleServiceList(TeleServiceList teleServiceList) {
        this.teleServiceList = teleServiceList;
    }

    /**
     * @return the provisionedSs
     */
    public ExtSSInfoList getProvisionedSs() {
        return provisionedSs;
    }

    /**
     * @param provisionedSs the provisionedSs to set
     */
    public void setProvisionedSs(ExtSSInfoList provisionedSs) {
        this.provisionedSs = provisionedSs;
    }

    /**
     * @return the odbData
     */
    public ODBData getOdbData() {
        return odbData;
    }

    /**
     * @param odbData the odbData to set
     */
    public void setOdbData(ODBData odbData) {
        this.odbData = odbData;
    }

    /**
     * @return the roamingRestrictionDueToUnsupportedFeature
     */
    public Boolean getRoamingRestrictionDueToUnsupportedFeature() {
        return roamingRestrictionDueToUnsupportedFeature;
    }

    /**
     * @param roamingRestrictionDueToUnsupportedFeature the
     * roamingRestrictionDueToUnsupportedFeature to set
     */
    public void setRoamingRestrictionDueToUnsupportedFeature(Boolean roamingRestrictionDueToUnsupportedFeature) {
        this.roamingRestrictionDueToUnsupportedFeature = roamingRestrictionDueToUnsupportedFeature;
    }

    /**
     * @return the regionalSubscriptionData
     */
    public ZoneCodeList getRegionalSubscriptionData() {
        return regionalSubscriptionData;
    }

    /**
     * @param regionalSubscriptionData the regionalSubscriptionData to set
     */
    public void setRegionalSubscriptionData(ZoneCodeList regionalSubscriptionData) {
        this.regionalSubscriptionData = regionalSubscriptionData;
    }

    /**
     * @return the vbsSubscriptionData
     */
    public VBSDataList getVbsSubscriptionData() {
        return vbsSubscriptionData;
    }

    /**
     * @param vbsSubscriptionData the vbsSubscriptionData to set
     */
    public void setVbsSubscriptionData(VBSDataList vbsSubscriptionData) {
        this.vbsSubscriptionData = vbsSubscriptionData;
    }

    /**
     * @return the vgcsSubscriptionData
     */
    public VGCSDataList getVgcsSubscriptionData() {
        return vgcsSubscriptionData;
    }

    /**
     * @param vgcsSubscriptionData the vgcsSubscriptionData to set
     */
    public void setVgcsSubscriptionData(VGCSDataList vgcsSubscriptionData) {
        this.vgcsSubscriptionData = vgcsSubscriptionData;
    }

    /**
     * @return the vlrCamelSubscriptionInfo
     */
    public VlrCamelSubscriptionInfo getVlrCamelSubscriptionInfo() {
        return vlrCamelSubscriptionInfo;
    }

    /**
     * @param vlrCamelSubscriptionInfo the vlrCamelSubscriptionInfo to set
     */
    public void setVlrCamelSubscriptionInfo(VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo) {
        this.vlrCamelSubscriptionInfo = vlrCamelSubscriptionInfo;
    }

    /**
     * @return the naeaPrefferecCI
     */
    public NAEAPrefferedCI getNaeaPrefferecCI() {
        return naeaPrefferecCI;
    }

    /**
     * @param naeaPrefferecCI the naeaPrefferecCI to set
     */
    public void setNaeaPrefferecCI(NAEAPrefferedCI naeaPrefferecCI) {
        this.naeaPrefferecCI = naeaPrefferecCI;
    }

    /**
     * @return the gprsSubscriptionData
     */
    public byte[] getGprsSubscriptionData() {
        return gprsSubscriptionData;
    }

    /**
     * @param gprsSubscriptionData the gprsSubscriptionData to set
     */
    public void setGprsSubscriptionData(byte[] gprsSubscriptionData) {
        this.gprsSubscriptionData = gprsSubscriptionData;
    }

    /**
     * @return the roamingRestrictedInSgsnDueToUnsupportedFeature
     */
    public Boolean getRoamingRestrictedInSgsnDueToUnsupportedFeature() {
        return roamingRestrictedInSgsnDueToUnsupportedFeature;
    }

    /**
     * @param roamingRestrictedInSgsnDueToUnsupportedFeature the
     * roamingRestrictedInSgsnDueToUnsupportedFeature to set
     */
    public void setRoamingRestrictedInSgsnDueToUnsupportedFeature(Boolean roamingRestrictedInSgsnDueToUnsupportedFeature) {
        this.roamingRestrictedInSgsnDueToUnsupportedFeature = roamingRestrictedInSgsnDueToUnsupportedFeature;
    }

    /**
     * @return the networkAccessMode
     */
    public NetworkAccessMode getNetworkAccessMode() {
        return networkAccessMode;
    }

    /**
     * @param networkAccessMode the networkAccessMode to set
     */
    public void setNetworkAccessMode(NetworkAccessMode networkAccessMode) {
        this.networkAccessMode = networkAccessMode;
    }

    /**
     * @return the lsaInformation
     */
    public byte[] getLsaInformation() {
        return lsaInformation;
    }

    /**
     * @param lsaInformation the lsaInformation to set
     */
    public void setLsaInformation(byte[] lsaInformation) {
        this.lsaInformation = lsaInformation;
    }

    /**
     * @return the lmuIndicator
     */
    public Boolean getLmuIndicator() {
        return lmuIndicator;
    }

    /**
     * @param lmuIndicator the lmuIndicator to set
     */
    public void setLmuIndicator(Boolean lmuIndicator) {
        this.lmuIndicator = lmuIndicator;
    }

    /**
     * @return the lcsInformation
     */
    public byte[] getLcsInformation() {
        return lcsInformation;
    }

    /**
     * @param lcsInformation the lcsInformation to set
     */
    public void setLcsInformation(byte[] lcsInformation) {
        this.lcsInformation = lcsInformation;
    }

    /**
     * @return the istAlertTimer
     */
    public byte[] getIstAlertTimer() {
        return istAlertTimer;
    }

    /**
     * @param istAlertTimer the istAlertTimer to set
     */
    public void setIstAlertTimer(byte[] istAlertTimer) {
        this.istAlertTimer = istAlertTimer;
    }

    /**
     * @return the superChargerSupportedInHLR
     */
    public AgeIndicator getSuperChargerSupportedInHLR() {
        return superChargerSupportedInHLR;
    }

    /**
     * @param superChargerSupportedInHLR the superChargerSupportedInHLR to set
     */
    public void setSuperChargerSupportedInHLR(AgeIndicator superChargerSupportedInHLR) {
        this.superChargerSupportedInHLR = superChargerSupportedInHLR;
    }

    /**
     * @return the mcSSInfo
     */
    public byte[] getMcSSInfo() {
        return mcSSInfo;
    }

    /**
     * @param mcSSInfo the mcSSInfo to set
     */
    public void setMcSSInfo(byte[] mcSSInfo) {
        this.mcSSInfo = mcSSInfo;
    }

    /**
     * @return the csAllocationRetentionPriority
     */
    public byte[] getCsAllocationRetentionPriority() {
        return csAllocationRetentionPriority;
    }

    /**
     * @param csAllocationRetentionPriority the csAllocationRetentionPriority to
     * set
     */
    public void setCsAllocationRetentionPriority(byte[] csAllocationRetentionPriority) {
        this.csAllocationRetentionPriority = csAllocationRetentionPriority;
    }

    /**
     * @return the sgsnCamelSubscriptionInfo
     */
    public byte[] getSgsnCamelSubscriptionInfo() {
        return sgsnCamelSubscriptionInfo;
    }

    /**
     * @param sgsnCamelSubscriptionInfo the sgsnCamelSubscriptionInfo to set
     */
    public void setSgsnCamelSubscriptionInfo(byte[] sgsnCamelSubscriptionInfo) {
        this.sgsnCamelSubscriptionInfo = sgsnCamelSubscriptionInfo;
    }

    /**
     * @return the chargingCharacteristics
     */
    public byte[] getChargingCharacteristics() {
        return chargingCharacteristics;
    }

    /**
     * @param chargingCharacteristics the chargingCharacteristics to set
     */
    public void setChargingCharacteristics(byte[] chargingCharacteristics) {
        this.chargingCharacteristics = chargingCharacteristics;
    }

    /**
     * @return the accessRestrictionData
     */
    public AccessRestrictionData getAccessRestrictionData() {
        return accessRestrictionData;
    }

    /**
     * @param accessRestrictionData the accessRestrictionData to set
     */
    public void setAccessRestrictionData(AccessRestrictionData accessRestrictionData) {
        this.accessRestrictionData = accessRestrictionData;
    }

    /**
     * @return the icsIndicator
     */
    public Boolean getIcsIndicator() {
        return icsIndicator;
    }

    /**
     * @param icsIndicator the icsIndicator to set
     */
    public void setIcsIndicator(Boolean icsIndicator) {
        this.icsIndicator = icsIndicator;
    }

    /**
     * @return the epsSubscriptionData
     */
    public byte[] getEpsSubscriptionData() {
        return epsSubscriptionData;
    }

    /**
     * @param epsSubscriptionData the epsSubscriptionData to set
     */
    public void setEpsSubscriptionData(byte[] epsSubscriptionData) {
        this.epsSubscriptionData = epsSubscriptionData;
    }

    /**
     * @return the csgSubscriptionData
     */
    public byte[] getCsgSubscriptionData() {
        return csgSubscriptionData;
    }

    /**
     * @param csgSubscriptionData the csgSubscriptionData to set
     */
    public void setCsgSubscriptionData(byte[] csgSubscriptionData) {
        this.csgSubscriptionData = csgSubscriptionData;
    }

    /**
     * @return the ueReachabilityRequestIndicator
     */
    public Boolean getUeReachabilityRequestIndicator() {
        return ueReachabilityRequestIndicator;
    }

    /**
     * @param ueReachabilityRequestIndicator the ueReachabilityRequestIndicator
     * to set
     */
    public void setUeReachabilityRequestIndicator(Boolean ueReachabilityRequestIndicator) {
        this.ueReachabilityRequestIndicator = ueReachabilityRequestIndicator;
    }

    /**
     * @return the sgsnNumber
     */
    public ISDNAddressString getSgsnNumber() {
        return sgsnNumber;
    }

    /**
     * @param sgsnNumber the sgsnNumber to set
     */
    public void setSgsnNumber(ISDNAddressString sgsnNumber) {
        this.sgsnNumber = sgsnNumber;
    }

    /**
     * @return the mmeName
     */
    public DiameterIdentity getMmeName() {
        return mmeName;
    }

    /**
     * @param mmeName the mmeName to set
     */
    public void setMmeName(DiameterIdentity mmeName) {
        this.mmeName = mmeName;
    }

    /**
     * @return the subscribedPeriodicRAUTAUTimer
     */
    public SubscribedPeriodicRAUTAUTimer getSubscribedPeriodicRAUTAUTimer() {
        return subscribedPeriodicRAUTAUTimer;
    }

    /**
     * @param subscribedPeriodicRAUTAUTimer the subscribedPeriodicRAUTAUTimer to
     * set
     */
    public void setSubscribedPeriodicRAUTAUTimer(SubscribedPeriodicRAUTAUTimer subscribedPeriodicRAUTAUTimer) {
        this.subscribedPeriodicRAUTAUTimer = subscribedPeriodicRAUTAUTimer;
    }

    /**
     * @return the vplmnLIPAAllowed
     */
    public Boolean getVplmnLIPAAllowed() {
        return vplmnLIPAAllowed;
    }

    /**
     * @param vplmnLIPAAllowed the vplmnLIPAAllowed to set
     */
    public void setVplmnLIPAAllowed(Boolean vplmnLIPAAllowed) {
        this.vplmnLIPAAllowed = vplmnLIPAAllowed;
    }

    /**
     * @return the mdtUserConsent
     */
    public Boolean getMdtUserConsent() {
        return mdtUserConsent;
    }

    /**
     * @param mdtUserConsent the mdtUserConsent to set
     */
    public void setMdtUserConsent(Boolean mdtUserConsent) {
        this.mdtUserConsent = mdtUserConsent;
    }

    /**
     * @return the subscribedPeriodicLAUTimer
     */
    public SubscribedPeriodicLAUTimer getSubscribedPeriodicLAUTimer() {
        return subscribedPeriodicLAUTimer;
    }

    /**
     * @param subscribedPeriodicLAUTimer the subscribedPeriodicLAUTimer to set
     */
    public void setSubscribedPeriodicLAUTimer(SubscribedPeriodicLAUTimer subscribedPeriodicLAUTimer) {
        this.subscribedPeriodicLAUTimer = subscribedPeriodicLAUTimer;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public boolean isRequestCorrupted() {
        return requestCorrupted;
    }

}
