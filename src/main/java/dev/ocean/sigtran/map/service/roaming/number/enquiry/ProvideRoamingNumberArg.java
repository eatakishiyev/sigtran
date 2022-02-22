/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.service.roaming.number.enquiry;

import java.io.IOException;
import dev.ocean.sigtran.cap.parameters.SuppressionOfAnnouncement;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.services.common.MAPArgument;
import dev.ocean.sigtran.map.parameters.AlertingPattern;
import dev.ocean.sigtran.map.parameters.CallReferenceNumber;
import dev.ocean.sigtran.map.parameters.EMLPPPriority;
import dev.ocean.sigtran.map.parameters.ExtExternalSignalInfo;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.ExternalSignalInfo;
import dev.ocean.sigtran.map.parameters.IMSI;
import dev.ocean.sigtran.map.parameters.ISDNAddressString;
import dev.ocean.sigtran.map.parameters.LMSI;
import dev.ocean.sigtran.map.parameters.OfferedCamel4CSIs;
import dev.ocean.sigtran.map.parameters.PagingArea;
import dev.ocean.sigtran.map.parameters.SupportedCamelPhases;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * ProvideRoamingNumberArg ::= SEQUENCE {
 * imsi [0] IMSI,
 * msc-Number [1] ISDN-AddressString,
 * msisdn [2] ISDN-AddressString OPTIONAL,
 * lmsi [4] LMSI OPTIONAL,
 * gsm-BearerCapability [5] ExternalSignalInfo OPTIONAL,
 * networkSignalInfo [6] ExternalSignalInfo OPTIONAL,
 * suppressionOfAnnouncement [7] SuppressionOfAnnouncement OPTIONAL,
 * gmsc-Address [8] ISDN-AddressString OPTIONAL,
 * callReferenceNumber [9] CallReferenceNumber OPTIONAL,
 * or-Interrogation [10] NULL OPTIONAL,
 * extensionContainer [11] ExtensionContainer OPTIONAL,
 * ... ,
 * alertingPattern [12] AlertingPattern OPTIONAL,
 * ccbs-Call [13] NULL OPTIONAL,
 * supportedCamelPhasesInInterrogatingNode [15] SupportedCamelPhases OPTIONAL,
 * additionalSignalInfo [14] Ext-ExternalSignalInfo OPTIONAL,
 * orNotSupportedInGMSC [16] NULL OPTIONAL,
 * pre-pagingSupported [17] NULL OPTIONAL,
 * longFTN-Supported [18] NULL OPTIONAL,
 * suppress-VT-CSI [19] NULL OPTIONAL,
 * offeredCamel4CSIsInInterrogatingNode [20] OfferedCamel4CSIs OPTIONAL,
 * mtRoamingRetrySupported [21] NULL OPTIONAL,
 * pagingArea [22] PagingArea OPTIONAL,
 * callPriority [23] EMLPP-Priority OPTIONAL,
 * mtrf-Indicator [24] NULL OPTIONAL,
 * oldMSC-Number [25] ISDN-AddressString OPTIONAL }
 *
 * @author eatakishiyev
 */
public class ProvideRoamingNumberArg implements MAPArgument {

    private IMSI imsi;
    private ISDNAddressString mscNumber;
    private ISDNAddressString msisdn;
    private LMSI lmsi;
    private ExternalSignalInfo gsmBearerCapability;
    private ExternalSignalInfo networkSignalInfo;
    private SuppressionOfAnnouncement suppressionOfAnnouncement;
    private ISDNAddressString gmscAddress;
    private CallReferenceNumber callReferenceNumber;
    private boolean orInterrogation;
    private ExtensionContainer extensionContainer;
    private AlertingPattern alertingPattern;
    private boolean ccbsCall;
    private SupportedCamelPhases supportedCamelPhasesInInterrogatingNode;
    private ExtExternalSignalInfo additionalSignalInfo;
    private boolean orNotSupportedInGMSC;
    private boolean prepagingSupported;
    private boolean longFTNSupported;
    private boolean suppressVTCSI;
    private OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode;
    private boolean mtRoamingRetrySupported;
    private PagingArea pagingArea;
    private EMLPPPriority callPriority;
    private boolean mtrfPriority;
    private ISDNAddressString oldMSCNumber;
    private byte[] argData;
    protected boolean corruptedArgument = false;

    public ProvideRoamingNumberArg() {
    }

    public ProvideRoamingNumberArg(IMSI imsi, ISDNAddressString mscNumber) {
        this.imsi = imsi;
        this.mscNumber = mscNumber;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            imsi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            mscNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            if (msisdn != null) {
                msisdn.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            if (lmsi != null) {
                lmsi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }
            if (gsmBearerCapability != null) {
                gsmBearerCapability.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            }
            if (networkSignalInfo != null) {
                networkSignalInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }
            if (suppressionOfAnnouncement != null) {
                suppressionOfAnnouncement.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }
            if (gmscAddress != null) {
                gmscAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 8, aos);
            }
            if (callReferenceNumber != null) {
                callReferenceNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 9, aos);
            }
            if (orInterrogation) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 10);
            }
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 11, aos);
            }
            if (alertingPattern != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 12, alertingPattern.value());
            }
            if (ccbsCall) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 13);
            }
            if (supportedCamelPhasesInInterrogatingNode != null) {
                supportedCamelPhasesInInterrogatingNode.encode(Tag.CLASS_CONTEXT_SPECIFIC, 15, aos);
            }
            if (additionalSignalInfo != null) {
                additionalSignalInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 14, aos);
            }
            if (orNotSupportedInGMSC) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 16);
            }
            if (prepagingSupported) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 17);
            }
            if (longFTNSupported) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 18);
            }
            if (suppressVTCSI) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 19);
            }
            if (offeredCamel4CSIsInInterrogatingNode != null) {
                offeredCamel4CSIsInInterrogatingNode.encode(Tag.CLASS_CONTEXT_SPECIFIC, 20, aos);
            }
            if (mtRoamingRetrySupported) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 21);
            }
            if (pagingArea != null) {
                pagingArea.encode(Tag.CLASS_CONTEXT_SPECIFIC, 22, aos);
            }
            if (callPriority != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 23, callPriority.getValue());
            }
            if (mtrfPriority) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 24);
            }
            if (oldMSCNumber != null) {
                oldMSCNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 25, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.argData = data;
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

    private void _decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag == 0 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.imsi = new IMSI(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[0] Class[CONTEXT]. "
                        + "Receveid Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == 1 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.mscNumber = new ISDNAddressString(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[1] Class[CONTEXT]. "
                        + "Receveid Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            while (ais.available() > 0) {
                tag = ais.readTag();
                if (tag == 2 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.msisdn = new ISDNAddressString(ais);
                } else if (tag == 4 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.lmsi = new LMSI(ais);
                } else if (tag == 5 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.gsmBearerCapability = new ExternalSignalInfo();
                    gsmBearerCapability.decode(ais.readSequenceStream());
                } else if (tag == 6 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.networkSignalInfo = new ExternalSignalInfo();
                    networkSignalInfo.decode(ais.readSequenceStream());
                } else if (tag == 7 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.suppressionOfAnnouncement = new SuppressionOfAnnouncement();
                    suppressionOfAnnouncement.decode(ais);
                } else if (tag == 8 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.gmscAddress = new ISDNAddressString(ais);
                } else if (tag == 9 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.callReferenceNumber = new CallReferenceNumber();
                    callReferenceNumber.decode(ais);
                } else if (tag == 10 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.orInterrogation = true;
                    ais.readNull();
                } else if (tag == 11 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.extensionContainer = new ExtensionContainer(ais);
                } else if (tag == 12 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.alertingPattern = AlertingPattern.getInstance((int) ais.readInteger());
                } else if (tag == 13 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.ccbsCall = true;
                    ais.readNull();
                } else if (tag == 15 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.supportedCamelPhasesInInterrogatingNode = new SupportedCamelPhases();
                    supportedCamelPhasesInInterrogatingNode.decode(ais);
                } else if (tag == 14 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.additionalSignalInfo = new ExtExternalSignalInfo();
                    additionalSignalInfo.decode(ais.readSequenceStream());
                } else if (tag == 16 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.orNotSupportedInGMSC = true;
                    ais.readNull();
                } else if (tag == 17 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.prepagingSupported = true;
                    ais.readNull();
                } else if (tag == 18 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.longFTNSupported = true;
                    ais.readNull();
                } else if (tag == 19 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.suppressVTCSI = true;
                    ais.readNull();
                } else if (tag == 20 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.offeredCamel4CSIsInInterrogatingNode = new OfferedCamel4CSIs();
                    offeredCamel4CSIsInInterrogatingNode.decode(ais);
                } else if (tag == 21 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.mtRoamingRetrySupported = true;
                    ais.readNull();
                } else if (tag == 22 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.pagingArea = new PagingArea();
                    pagingArea.decode(ais);
                } else if (tag == 23 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.callPriority = EMLPPPriority.getInstance((int) ais.readInteger());
                } else if (tag == 24 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.mtrfPriority = true;
                    ais.readNull();
                } else if (tag == 25 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.oldMSCNumber = new ISDNAddressString(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
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
     * @return the mscNumber
     */
    public ISDNAddressString getMscNumber() {
        return mscNumber;
    }

    /**
     * @param mscNumber the mscNumber to set
     */
    public void setMscNumber(ISDNAddressString mscNumber) {
        this.mscNumber = mscNumber;
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
     * @return the lmsi
     */
    public LMSI getLmsi() {
        return lmsi;
    }

    /**
     * @param lmsi the lmsi to set
     */
    public void setLmsi(LMSI lmsi) {
        this.lmsi = lmsi;
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
     * @return the suppressionOfAnnouncement
     */
    public SuppressionOfAnnouncement getSuppressionOfAnnouncement() {
        return suppressionOfAnnouncement;
    }

    /**
     * @param suppressionOfAnnouncement the suppressionOfAnnouncement to set
     */
    public void setSuppressionOfAnnouncement(SuppressionOfAnnouncement suppressionOfAnnouncement) {
        this.suppressionOfAnnouncement = suppressionOfAnnouncement;
    }

    /**
     * @return the gmscAddress
     */
    public ISDNAddressString getGmscAddress() {
        return gmscAddress;
    }

    /**
     * @param gmscAddress the gmscAddress to set
     */
    public void setGmscAddress(ISDNAddressString gmscAddress) {
        this.gmscAddress = gmscAddress;
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
     * @return the orInterrogation
     */
    public boolean isOrInterrogation() {
        return orInterrogation;
    }

    /**
     * @param orInterrogation the orInterrogation to set
     */
    public void setOrInterrogation(boolean orInterrogation) {
        this.orInterrogation = orInterrogation;
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
     * @return the orNotSupportedInGMSC
     */
    public boolean isOrNotSupportedInGMSC() {
        return orNotSupportedInGMSC;
    }

    /**
     * @param orNotSupportedInGMSC the orNotSupportedInGMSC to set
     */
    public void setOrNotSupportedInGMSC(boolean orNotSupportedInGMSC) {
        this.orNotSupportedInGMSC = orNotSupportedInGMSC;
    }

    /**
     * @return the prepagingSupported
     */
    public boolean isPrepagingSupported() {
        return prepagingSupported;
    }

    /**
     * @param prepagingSupported the prepagingSupported to set
     */
    public void setPrepagingSupported(boolean prepagingSupported) {
        this.prepagingSupported = prepagingSupported;
    }

    /**
     * @return the longFTNSupported
     */
    public boolean isLongFTNSupported() {
        return longFTNSupported;
    }

    /**
     * @param longFTNSupported the longFTNSupported to set
     */
    public void setLongFTNSupported(boolean longFTNSupported) {
        this.longFTNSupported = longFTNSupported;
    }

    /**
     * @return the suppressVTCSI
     */
    public boolean isSuppressVTCSI() {
        return suppressVTCSI;
    }

    /**
     * @param suppressVTCSI the suppressVTCSI to set
     */
    public void setSuppressVTCSI(boolean suppressVTCSI) {
        this.suppressVTCSI = suppressVTCSI;
    }

    /**
     * @return the offeredCamel4CSIsInInterrogatingNode
     */
    public OfferedCamel4CSIs getOfferedCamel4CSIsInInterrogatingNode() {
        return offeredCamel4CSIsInInterrogatingNode;
    }

    /**
     * @param offeredCamel4CSIsInInterrogatingNode the
     * offeredCamel4CSIsInInterrogatingNode to set
     */
    public void setOfferedCamel4CSIsInInterrogatingNode(OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode) {
        this.offeredCamel4CSIsInInterrogatingNode = offeredCamel4CSIsInInterrogatingNode;
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
     * @return the pagingArea
     */
    public PagingArea getPagingArea() {
        return pagingArea;
    }

    /**
     * @param pagingArea the pagingArea to set
     */
    public void setPagingArea(PagingArea pagingArea) {
        this.pagingArea = pagingArea;
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
     * @return the mtrfPriority
     */
    public boolean isMtrfPriority() {
        return mtrfPriority;
    }

    /**
     * @param mtrfPriority the mtrfPriority to set
     */
    public void setMtrfPriority(boolean mtrfPriority) {
        this.mtrfPriority = mtrfPriority;
    }

    /**
     * @return the oldMSCNumber
     */
    public ISDNAddressString getOldMSCNumber() {
        return oldMSCNumber;
    }

    /**
     * @param oldMSCNumber the oldMSCNumber to set
     */
    public void setOldMSCNumber(ISDNAddressString oldMSCNumber) {
        this.oldMSCNumber = oldMSCNumber;
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
     * @return the supportedCamelPhasesInInterrogatingNode
     */
    public SupportedCamelPhases getSupportedCamelPhasesInInterrogatingNode() {
        return supportedCamelPhasesInInterrogatingNode;
    }

    /**
     * @param supportedCamelPhasesInInterrogatingNode the
     * supportedCamelPhasesInInterrogatingNode to set
     */
    public void setSupportedCamelPhasesInInterrogatingNode(SupportedCamelPhases supportedCamelPhasesInInterrogatingNode) {
        this.supportedCamelPhasesInInterrogatingNode = supportedCamelPhasesInInterrogatingNode;
    }

    @Override
    public byte[] getRequestData() {
        return argData;
    }

    @Override
    public boolean isRequestCorrupted() {
        return this.corruptedArgument;
    }

}
