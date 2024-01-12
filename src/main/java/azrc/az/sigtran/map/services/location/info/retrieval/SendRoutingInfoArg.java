/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.location.info.retrieval;

import java.io.IOException;

import azrc.az.isup.enums.CallDiversionTreatmentIndicator;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.parameters.AlertingPattern;
import azrc.az.sigtran.map.parameters.CUGCheckInfo;
import azrc.az.sigtran.map.parameters.CallReferenceNumber;
import azrc.az.sigtran.map.parameters.CamelInfo;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.ISDNAddressString;
import azrc.az.sigtran.map.parameters.EMLPPPriority;
import azrc.az.sigtran.map.parameters.ExtBasicServiceCode;
import azrc.az.sigtran.map.parameters.ExtExternalSignalInfo;
import azrc.az.sigtran.map.parameters.ExternalSignalInfo;
import azrc.az.sigtran.map.parameters.ForwardingReason;
import azrc.az.sigtran.map.parameters.ISTSupportIndicator;
import azrc.az.sigtran.map.parameters.InterrogationType;
import azrc.az.sigtran.map.parameters.NumberOfForwarding;
import azrc.az.sigtran.map.parameters.ORPhase;
import azrc.az.sigtran.map.parameters.SupportedCCBSPhase;
import azrc.az.sigtran.map.parameters.SuppressMTSS;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import azrc.az.sigtran.map.services.common.MAPArgument;

/**
 * SendRoutingInfoArg ::= SEQUENCE {
 * msisdn [0] ISDN-AddressString,
 * cug-CheckInfo [1] CUG-CheckInfo OPTIONAL,
 * numberOfForwarding [2] NumberOfForwarding OPTIONAL,
 * interrogationType [3] InterrogationType,
 * or-Interrogation [4] NULL OPTIONAL,
 * or-Capability [5] OR-Phase OPTIONAL,
 * gmsc-OrGsmSCF-Address [6] ISDN-AddressString,
 * callReferenceNumber [7] CallReferenceNumber OPTIONAL,
 * forwardingReason [8] ForwardingReason OPTIONAL,
 * basicServiceGroup [9] Ext-BasicServiceCode OPTIONAL,
 * networkSignalInfo [10] ExternalSignalInfo OPTIONAL,
 * camelInfo [11] CamelInfo OPTIONAL,
 * suppressionOfAnnouncement [12] SuppressionOfAnnouncement OPTIONAL,
 * extensionContainer [13] ExtensionContainer OPTIONAL,
 * ...,
 * alertingPattern [14] AlertingPattern OPTIONAL,
 * ccbs-Call [15] NULL OPTIONAL,
 * supportedCCBS-Phase [16] SupportedCCBS-Phase OPTIONAL,
 * additionalSignalInfo [17] Ext-ExternalSignalInfo OPTIONAL,
 * istSupportIndicator [18] IST-SupportIndicator OPTIONAL,
 * pre-pagingSupported [19] NULL OPTIONAL,
 * callDiversionTreatmentIndicator [20] CallDiversionTreatmentIndicator
 * OPTIONAL,
 * longFTN-Supported [21] NULL OPTIONAL,
 * suppress-VT-CSI [22] NULL OPTIONAL,
 * suppressIncomingCallBarring [23] NULL OPTIONAL,
 * gsmSCF-InitiatedCall [24] NULL OPTIONAL,
 * basicServiceGroup2 [25] Ext-BasicServiceCode OPTIONAL,
 * networkSignalInfo2 [26] ExternalSignalInfo OPTIONAL,
 * suppressMTSS [27] SuppressMTSS OPTIONAL,
 * mtRoamingRetrySupported [28] NULL OPTIONAL,
 * callPriority [29] EMLPP-Priority OPTIONAL
 * }
 *
 * @author eatakishiyev
 */
public class SendRoutingInfoArg implements MAPArgument {

    private ISDNAddressString msisdn;
    private CUGCheckInfo cugCheckInfo;
    private NumberOfForwarding numberOfForwarding;
    private InterrogationType interrogationType;
    private boolean orIterrogation;
    private ORPhase orCapability;
    private ISDNAddressString gmscOrGsmSCFAddress;
    private CallReferenceNumber callReferenceNumber;
    private ForwardingReason forwardingReason;
    private ExtBasicServiceCode basicServiceGroup;
    private ExternalSignalInfo networkSignalInfo;
    private CamelInfo camelInfo;
    private boolean suppressionOnAnnouncement;
    private ExtensionContainer extensionContainer;
    private AlertingPattern alertingPattern;
    private boolean ccbsCall;
    private SupportedCCBSPhase supportedCCBSPhase;
    private ExtExternalSignalInfo additionalSignalInfo;
    private ISTSupportIndicator istSupportIndicator;
    private boolean prePagingSupported;
    private CallDiversionTreatmentIndicator callDiversionTreatmentIndicator;
    private boolean longFtnSupported;
    private boolean suppressVtCSI;
    private boolean suppressIncomingCallBarring;
    private boolean gsmSCFInitiatedCall;
    private ExtBasicServiceCode basicServiceGroup2;
    private ExternalSignalInfo networkSignalInfo2;
    private SuppressMTSS suppressMTSS;
    private boolean mtRoamingRetrySupported;
    private EMLPPPriority callPriority;
    private byte[] requestData;
    protected boolean requestCorrupted = false;

    public SendRoutingInfoArg() {
    }

    public SendRoutingInfoArg(ISDNAddressString msisdn) {
        this.msisdn = msisdn;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            msisdn.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            if (cugCheckInfo != null) {
                cugCheckInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
            if (numberOfForwarding != null) {
                numberOfForwarding.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            if (interrogationType != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 3, interrogationType.value());
            }
            if (orIterrogation) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 4);
            }
            if (orCapability != null) {
                orCapability.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            }

            if (gmscOrGsmSCFAddress != null) {
                gmscOrGsmSCFAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }
            if (callReferenceNumber != null) {
                callReferenceNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }
            if (forwardingReason != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 8, forwardingReason.getValue());
            }
            if (basicServiceGroup != null) {
                basicServiceGroup.encode(Tag.CLASS_CONTEXT_SPECIFIC, 9, aos);
            }
            if (networkSignalInfo != null) {
                networkSignalInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 10, aos);
            }
            if (camelInfo != null) {
                camelInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 11, aos);
            }
            if (suppressionOnAnnouncement) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 12);
            }
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 13, aos);
            }
            if (alertingPattern != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 14, alertingPattern.value());
            }
            if (ccbsCall) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 15);
            }
            if (supportedCCBSPhase != null) {
                supportedCCBSPhase.encode(Tag.CLASS_CONTEXT_SPECIFIC, 16, aos);
            }
            if (additionalSignalInfo != null) {
                additionalSignalInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 17, aos);
            }
            if (istSupportIndicator != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 18, istSupportIndicator.getValue());
            }
            if (prePagingSupported) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 19);
            }
            if (callDiversionTreatmentIndicator != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 20, callDiversionTreatmentIndicator.value());
            }
            if (longFtnSupported) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 21);
            }
            if (suppressVtCSI) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 22);
            }
            if (suppressIncomingCallBarring) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 23);
            }
            if (gsmSCFInitiatedCall) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 24);
            }
            if (basicServiceGroup2 != null) {
                basicServiceGroup2.encode(Tag.CLASS_CONTEXT_SPECIFIC, 25, aos);
            }
            if (networkSignalInfo2 != null) {
                networkSignalInfo2.encode(Tag.CLASS_CONTEXT_SPECIFIC, 26, aos);
            }
            if (suppressMTSS != null) {
                suppressMTSS.encode(Tag.CLASS_CONTEXT_SPECIFIC, 27, aos);
            }
            if (mtRoamingRetrySupported) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 28);
            }
            if (callPriority != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 29, callPriority.getValue());
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws UnexpectedDataException, IncorrectSyntaxException {
        this.requestData = data;
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
        int tag = ais.readTag();

        if (tag == 0 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            this.msisdn = new ISDNAddressString(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received."
                    + "Expecting Tag[0] Class[UNIVERSAL].", tag, ais.getTagClass()));
        }

        while (ais.available() > 0) {
            tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new IncorrectSyntaxException(String.format("Unexpected tag Class[%s] received."
                        + "Expecting tag Class[CONTEXT].", ais.getTagClass()));
            }

            switch (tag) {
                case 1:
                    this.cugCheckInfo = new CUGCheckInfo();
                    cugCheckInfo.decode(ais.readSequenceStream());
                    break;
                case 2:
                    this.numberOfForwarding = new NumberOfForwarding();
                    numberOfForwarding.decode(ais);
                    break;
                case 3:
                    this.interrogationType = InterrogationType.getInstance((int) ais.readInteger());
                    break;
                case 4:
                    this.orIterrogation = true;
                    ais.readNull();
                    break;
                case 5:
                    this.orCapability = new ORPhase();
                    orCapability.decode(ais);
                    break;
                case 6:
                    this.gmscOrGsmSCFAddress = new ISDNAddressString(ais);
                    break;
                case 7:
                    this.callReferenceNumber = new CallReferenceNumber();
                    callReferenceNumber.decode(ais);
                    break;
                case 8:
                    this.forwardingReason = ForwardingReason.getInstance((int) ais.readInteger());
                    break;
                case 9:
                    this.basicServiceGroup = new ExtBasicServiceCode();
                    basicServiceGroup.decode(ais);
                    break;
                case 10:
                    this.networkSignalInfo = new ExternalSignalInfo();
                    networkSignalInfo.decode(ais.readSequenceStream());
                    break;
                case 11:
                    this.camelInfo = new CamelInfo();
                    camelInfo.decode(ais.readSequenceStream());
                    break;
                case 12:
                    this.suppressionOnAnnouncement = true;
                    ais.readNull();
                    break;
                case 13:
                    this.extensionContainer = new ExtensionContainer(ais);
                    break;
                case 14:
                    this.alertingPattern = AlertingPattern.getInstance((int) ais.readInteger());
                    break;
                case 15:
                    this.ccbsCall = true;
                    ais.readNull();
                    break;
                case 16:
                    this.supportedCCBSPhase = new SupportedCCBSPhase();
                    supportedCCBSPhase.decode(ais);
                    break;
                case 17:
                    this.additionalSignalInfo = new ExtExternalSignalInfo();
                    additionalSignalInfo.decode(ais.readSequenceStream());
                    break;
                case 18:
                    this.istSupportIndicator = ISTSupportIndicator.getInstance((int) ais.readInteger());
                    break;
                case 19:
                    this.prePagingSupported = true;
                    ais.readNull();
                    break;
                case 20:
                    this.callDiversionTreatmentIndicator = CallDiversionTreatmentIndicator.getInstance((int) ais.readInteger());
                    break;
                case 21:
                    this.longFtnSupported = true;
                    ais.readNull();
                    break;
                case 22:
                    this.suppressVtCSI = true;
                    ais.readNull();
                    break;
                case 23:
                    this.suppressIncomingCallBarring = true;
                    ais.readNull();
                    break;
                case 24:
                    this.gsmSCFInitiatedCall = true;
                    ais.readNull();
                    break;
                case 25:
                    this.basicServiceGroup2 = new ExtBasicServiceCode();
                    basicServiceGroup2.decode(ais);
                    break;
                case 26:
                    this.networkSignalInfo2 = new ExternalSignalInfo();
                    networkSignalInfo2.decode(ais.readSequenceStream());
                    break;
                case 27:
                    this.suppressMTSS = new SuppressMTSS();
                    suppressMTSS.decode(ais);
                    break;
                case 28:
                    this.mtRoamingRetrySupported = true;
                    ais.readNull();
                    break;
                case 29:
                    this.callPriority = EMLPPPriority.getInstance((int) ais.readInteger());
                    break;
                default:
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] received.", tag));
            }
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
     * @return the numberOfForwarding
     */
    public NumberOfForwarding getNumberOfForwarding() {
        return numberOfForwarding;
    }

    /**
     * @param numberOfForwarding the numberOfForwarding to set
     */
    public void setNumberOfForwarding(NumberOfForwarding numberOfForwarding) {
        this.numberOfForwarding = numberOfForwarding;
    }

    /**
     * @return the interrogationType
     */
    public InterrogationType getInterrogationType() {
        return interrogationType;
    }

    /**
     * @param interrogationType the interrogationType to set
     */
    public void setInterrogationType(InterrogationType interrogationType) {
        this.interrogationType = interrogationType;
    }

    /**
     * @return the orIterrogation
     */
    public boolean isOrIterrogation() {
        return orIterrogation;
    }

    /**
     * @param orIterrogation the orIterrogation to set
     */
    public void setOrIterrogation(boolean orIterrogation) {
        this.orIterrogation = orIterrogation;
    }

    /**
     * @return the orCapability
     */
    public ORPhase getOrCapability() {
        return orCapability;
    }

    /**
     * @param orCapability the orCapability to set
     */
    public void setOrCapability(ORPhase orCapability) {
        this.orCapability = orCapability;
    }

    /**
     * @return the gmscOrGsmSCFAddress
     */
    public ISDNAddressString getGmscOrGsmSCFAddress() {
        return gmscOrGsmSCFAddress;
    }

    /**
     * @param gmscOrGsmSCFAddress the gmscOrGsmSCFAddress to set
     */
    public void setGmscOrGsmSCFAddress(ISDNAddressString gmscOrGsmSCFAddress) {
        this.gmscOrGsmSCFAddress = gmscOrGsmSCFAddress;
    }

    /**
     * @return the callReferenceNumber
     */
    public CallReferenceNumber getCallReferenceNumber() {
        return callReferenceNumber;
    }

    /**
     * @param callReferenceNumber the callReferenceNumber to set
     */
    public void setCallReferenceNumber(CallReferenceNumber callReferenceNumber) {
        this.callReferenceNumber = callReferenceNumber;
    }

    /**
     * @return the forwardingReason
     */
    public ForwardingReason getForwardingReason() {
        return forwardingReason;
    }

    /**
     * @param forwardingReason the forwardingReason to set
     */
    public void setForwardingReason(ForwardingReason forwardingReason) {
        this.forwardingReason = forwardingReason;
    }

    /**
     * @return the basicServiceGroup
     */
    public ExtBasicServiceCode getBasicServiceGroup() {
        return basicServiceGroup;
    }

    /**
     * @param basicServiceGroup the basicServiceGroup to set
     */
    public void setBasicServiceGroup(ExtBasicServiceCode basicServiceGroup) {
        this.basicServiceGroup = basicServiceGroup;
    }

    /**
     * @return the networkSignalInfo
     */
    public ExternalSignalInfo getNetworkSignalInfo() {
        return networkSignalInfo;
    }

    /**
     * @param networkSignalInfo the networkSignalInfo to set
     */
    public void setNetworkSignalInfo(ExternalSignalInfo networkSignalInfo) {
        this.networkSignalInfo = networkSignalInfo;
    }

    /**
     * @return the camelInfo
     */
    public CamelInfo getCamelInfo() {
        return camelInfo;
    }

    /**
     * @param camelInfo the camelInfo to set
     */
    public void setCamelInfo(CamelInfo camelInfo) {
        this.camelInfo = camelInfo;
    }

    /**
     * @return the supressionOnAnnouncement
     */
    public boolean isSuppressionOnAnnouncement() {
        return suppressionOnAnnouncement;
    }

    /**
     * @param suppressionOnAnnouncement the suppressionOnAnnouncement to set
     */
    public void setSuppressionOnAnnouncement(boolean suppressionOnAnnouncement) {
        this.suppressionOnAnnouncement = suppressionOnAnnouncement;
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
     * @return the alertingPattern
     */
    public AlertingPattern getAlertingPattern() {
        return alertingPattern;
    }

    /**
     * @param alertingPattern the alertingPattern to set
     */
    public void setAlertingPattern(AlertingPattern alertingPattern) {
        this.alertingPattern = alertingPattern;
    }

    /**
     * @return the ccbsCall
     */
    public boolean isCcbsCall() {
        return ccbsCall;
    }

    /**
     * @param ccbsCall the ccbsCall to set
     */
    public void setCcbsCall(boolean ccbsCall) {
        this.ccbsCall = ccbsCall;
    }

    /**
     * @return the supportedCCBSPhase
     */
    public SupportedCCBSPhase getSupportedCCBSPhase() {
        return supportedCCBSPhase;
    }

    /**
     * @param supportedCCBSPhase the supportedCCBSPhase to set
     */
    public void setSupportedCCBSPhase(SupportedCCBSPhase supportedCCBSPhase) {
        this.supportedCCBSPhase = supportedCCBSPhase;
    }

    /**
     * @return the additionalSignalInfo
     */
    public ExtExternalSignalInfo getAdditionalSignalInfo() {
        return additionalSignalInfo;
    }

    /**
     * @param additionalSignalInfo the additionalSignalInfo to set
     */
    public void setAdditionalSignalInfo(ExtExternalSignalInfo additionalSignalInfo) {
        this.additionalSignalInfo = additionalSignalInfo;
    }

    /**
     * @return the istSupportIndicator
     */
    public ISTSupportIndicator getIstSupportIndicator() {
        return istSupportIndicator;
    }

    /**
     * @param istSupportIndicator the istSupportIndicator to set
     */
    public void setIstSupportIndicator(ISTSupportIndicator istSupportIndicator) {
        this.istSupportIndicator = istSupportIndicator;
    }

    /**
     * @return the prePagingSupported
     */
    public boolean isPrePagingSupported() {
        return prePagingSupported;
    }

    /**
     * @param prePagingSupported the prePagingSupported to set
     */
    public void setPrePagingSupported(boolean prePagingSupported) {
        this.prePagingSupported = prePagingSupported;
    }

    /**
     * @return the callDiversionTreatmentIndicator
     */
    public CallDiversionTreatmentIndicator getCallDiversionTreatmentIndicator() {
        return callDiversionTreatmentIndicator;
    }

    /**
     * @param callDiversionTreatmentIndicator the
     * callDiversionTreatmentIndicator to set
     */
    public void setCallDiversionTreatmentIndicator(CallDiversionTreatmentIndicator callDiversionTreatmentIndicator) {
        this.callDiversionTreatmentIndicator = callDiversionTreatmentIndicator;
    }

    /**
     * @return the longFtnSupported
     */
    public boolean isLongFtnSupported() {
        return longFtnSupported;
    }

    /**
     * @param longFtnSupported the longFtnSupported to set
     */
    public void setLongFtnSupported(boolean longFtnSupported) {
        this.longFtnSupported = longFtnSupported;
    }

    /**
     * @return the suppressVtCSI
     */
    public boolean isSuppressVtCSI() {
        return suppressVtCSI;
    }

    /**
     * @param suppressVtCSI the suppressVtCSI to set
     */
    public void setSuppressVtCSI(boolean suppressVtCSI) {
        this.suppressVtCSI = suppressVtCSI;
    }

    /**
     * @return the suppressIncomingCallBarring
     */
    public boolean isSuppressIncomingCallBarring() {
        return suppressIncomingCallBarring;
    }

    /**
     * @param suppressIncomingCallBarring the suppressIncomingCallBarring to set
     */
    public void setSuppressIncomingCallBarring(boolean suppressIncomingCallBarring) {
        this.suppressIncomingCallBarring = suppressIncomingCallBarring;
    }

    /**
     * @return the gsmSCFInitiatedCall
     */
    public boolean isGsmSCFInitiatedCall() {
        return gsmSCFInitiatedCall;
    }

    /**
     * @param gsmSCFInitiatedCall the gsmSCFInitiatedCall to set
     */
    public void setGsmSCFInitiatedCall(boolean gsmSCFInitiatedCall) {
        this.gsmSCFInitiatedCall = gsmSCFInitiatedCall;
    }

    /**
     * @return the basicServiceGroup2
     */
    public ExtBasicServiceCode getBasicServiceGroup2() {
        return basicServiceGroup2;
    }

    /**
     * @param basicServiceGroup2 the basicServiceGroup2 to set
     */
    public void setBasicServiceGroup2(ExtBasicServiceCode basicServiceGroup2) {
        this.basicServiceGroup2 = basicServiceGroup2;
    }

    /**
     * @return the networkSignalInfo2
     */
    public ExternalSignalInfo getNetworkSignalInfo2() {
        return networkSignalInfo2;
    }

    /**
     * @param networkSignalInfo2 the networkSignalInfo2 to set
     */
    public void setNetworkSignalInfo2(ExternalSignalInfo networkSignalInfo2) {
        this.networkSignalInfo2 = networkSignalInfo2;
    }

    /**
     * @return the mtRoamingRetrySupported
     */
    public boolean isMtRoamingRetrySupported() {
        return mtRoamingRetrySupported;
    }

    /**
     * @param mtRoamingRetrySupported the mtRoamingRetrySupported to set
     */
    public void setMtRoamingRetrySupported(boolean mtRoamingRetrySupported) {
        this.mtRoamingRetrySupported = mtRoamingRetrySupported;
    }

    /**
     * @return the callPriority
     */
    public EMLPPPriority getCallPriority() {
        return callPriority;
    }

    /**
     * @param callPriority the callPriority to set
     */
    public void setCallPriority(EMLPPPriority callPriority) {
        this.callPriority = callPriority;
    }

    /**
     * @return the suppressMTSS
     */
    public SuppressMTSS getSuppressMTSS() {
        return suppressMTSS;
    }

    /**
     * @param suppressMTSS the suppressMTSS to set
     */
    public void setSuppressMTSS(SuppressMTSS suppressMTSS) {
        this.suppressMTSS = suppressMTSS;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public boolean isRequestCorrupted() {
        return requestCorrupted;
    }

}
