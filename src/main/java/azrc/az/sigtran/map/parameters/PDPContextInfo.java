/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * PDP-ContextInfo ::= SEQUENCE { pdp-ContextIdentifier [0] ContextId,
 * pdp-ContextActive [1] NULL OPTIONAL, pdp-Type [2] PDP-Type, pdp-Address [3]
 * PDP-Address OPTIONAL, apn-Subscribed [4] APN OPTIONAL, apn-InUse [5] APN
 * OPTIONAL, nsapi [6] NSAPI OPTIONAL, transactionId [7] TransactionId OPTIONAL,
 * teid-ForGnAndGp [8] TEID OPTIONAL, teid-ForIu [9] TEID OPTIONAL, ggsn-Address
 * [10] GSN-Address OPTIONAL, qos-Subscribed [11] Ext-QoS-Subscribed OPTIONAL,
 * qos-Requested [12] Ext-QoS-Subscribed OPTIONAL, qos-Negotiated [13]
 * Ext-QoS-Subscribed OPTIONAL, chargingId [14] GPRSChargingID OPTIONAL,
 * chargingCharacteristics [15] ChargingCharacteristics OPTIONAL, rnc-Address
 * [16] GSN-Address OPTIONAL, extensionContainer [17] ExtensionContainer
 * OPTIONAL, ..., qos2-Subscribed [18] Ext2-QoS-Subscribed OPTIONAL, --
 * qos2-Subscribed may be present only if qos-Subscribed is present.
 * qos2-Requested [19] Ext2-QoS-Subscribed OPTIONAL, -- qos2-Requested may be
 * present only if qos-Requested is present. qos2-Negotiated [20]
 * Ext2-QoS-Subscribed OPTIONAL, -- qos2-Negotiated may be present only if
 * qos-Negotiated is present. qos3-Subscribed [21] Ext3-QoS-Subscribed OPTIONAL,
 * -- qos3-Subscribed may be present only if qos2-Subscribed is present.
 * qos3-Requested [22] Ext3-QoS-Subscribed OPTIONAL, -- qos3-Requested may be
 * present only if qos2-Requested is present. qos3-Negotiated [23]
 * Ext3-QoS-Subscribed OPTIONAL, -- qos3-Negotiated may be present only if
 * qos2-Negotiated is present. qos4-Subscribed [25] Ext4-QoS-Subscribed
 * OPTIONAL, -- qos4-Subscribed may be present only if qos3-Subscribed is
 * present. qos4-Requested [26] Ext4-QoS-Subscribed OPTIONAL, -- qos4-Requested
 * may be present only if qos3-Requested is present. qos4-Negotiated [27]
 * Ext4-QoS-Subscribed OPTIONAL, -- qos4-Negotiated may be present only if
 * qos3-Negotiated is present. ext-pdp-Type [28] Ext-PDP-Type OPTIONAL, --
 * contains the value IPv4v6 defined in 3GPP TS 29.060 [105], if the PDP can be
 * -- accessed by dual-stack UEs. ext-pdp-Address [29] PDP-Address OPTIONAL --
 * contains an additional IP address in case of dual-stack static IP address
 * assignment -- for the UE. -- it may contain an IPv4 or an IPv6
 * address/prefix, and it may be present -- only if pdp-Address is present; if
 * both are present, each parameter shall -- contain a different type of address
 * (IPv4 or IPv6). }
 *
 * @author eatakishiyev
 */
public class PDPContextInfo {

    private ContextId pdpContextIdentifier;
    private boolean pdpContextActive;
    private PDPType pdpType;
    private PDPAddress pdpAddress;
    private APN apnSubscribed;
    private APN apnInUse;
    private NSAPI nsapi;
    private TransactionId transactionId;
    private TEID teidForGNandGP;
    private TEID teidForIu;
    private GSNAddress ggsnAddress;
    private ExtQoSSubscribed qosSubscribed;
    private ExtQoSSubscribed qosRequested;
    private ExtQoSSubscribed qosNegotiated;
    private GPRSChargingID chargingId;
    private ChargingCharacteristics chargingCharacteristics;
    private GSNAddress rncAddress;
    private ExtensionContainer extensionContainer;
//    
    private Ext2QoSSubscribed qos2Subscribed;
    private Ext2QoSSubscribed qos2Requested;
    private Ext2QoSSubscribed qos2Negotiated;
//    
    private Ext3QoSSubscribed qos3Subscribed;
    private Ext3QoSSubscribed qos3Requested;
    private Ext3QoSSubscribed qos3Negotiated;
//    
    private Ext4QoSSubscribed qos4Subscribed;
    private Ext4QoSSubscribed qos4Requested;
    private Ext4QoSSubscribed qos4Negotiated;

    private ExtPDPType extPdpType;
    private PDPAddress extPdpAddress;

    public PDPContextInfo() {
    }

    public PDPContextInfo(ContextId pdpContextIdentifier, PDPType pdpType) {
        this.pdpContextIdentifier = pdpContextIdentifier;
        this.pdpType = pdpType;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int pos = aos.StartContentDefiniteLength();
            this.pdpContextIdentifier.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

            if (pdpContextActive) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 1);
            }

            pdpType.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);

            if (pdpAddress != null) {
                pdpAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }

            if (apnSubscribed != null) {
                apnSubscribed.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }

            if (apnInUse != null) {
                apnInUse.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            }

            if (nsapi != null) {
                nsapi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }

            if (transactionId != null) {
                transactionId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }

            if (teidForGNandGP != null) {
                teidForGNandGP.encode(Tag.CLASS_CONTEXT_SPECIFIC, 8, aos);
            }

            if (teidForIu != null) {
                teidForIu.encode(Tag.CLASS_CONTEXT_SPECIFIC, 9, aos);
            }

            if (ggsnAddress != null) {
                ggsnAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 10, aos);
            }

            if (qosSubscribed != null) {
                qosSubscribed.encode(Tag.CLASS_CONTEXT_SPECIFIC, 11, aos);
            }

            if (qosRequested != null) {
                qosRequested.encode(Tag.CLASS_CONTEXT_SPECIFIC, 12, aos);
            }

            if (qosNegotiated != null) {
                qosNegotiated.encode(Tag.CLASS_CONTEXT_SPECIFIC, 13, aos);
            }

            if (chargingId != null) {
                chargingId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 14, aos);
            }

            if (chargingCharacteristics != null) {
                chargingCharacteristics.encode(Tag.CLASS_CONTEXT_SPECIFIC, 15, aos);
            }

            if (rncAddress != null) {
                rncAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 16, aos);
            }

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 17, aos);
            }

            if (qos2Subscribed != null) {
                qos2Subscribed.encode(Tag.CLASS_CONTEXT_SPECIFIC, 18, aos);
            }

            if (qos2Requested != null) {
                qos2Requested.encode(Tag.CLASS_CONTEXT_SPECIFIC, 19, aos);
            }

            if (qos2Negotiated != null) {
                qos2Negotiated.encode(Tag.CLASS_CONTEXT_SPECIFIC, 20, aos);
            }

            if (qos3Subscribed != null) {
                qos3Subscribed.encode(Tag.CLASS_CONTEXT_SPECIFIC, 21, aos);
            }

            if (qos3Requested != null) {
                qos3Requested.encode(Tag.CLASS_CONTEXT_SPECIFIC, 22, aos);
            }

            if (qos3Negotiated != null) {
                qos3Negotiated.encode(Tag.CLASS_CONTEXT_SPECIFIC, 23, aos);
            }

            if (qos4Subscribed != null) {
                qos4Subscribed.encode(Tag.CLASS_CONTEXT_SPECIFIC, 25, aos);
            }

            if (qos4Requested != null) {
                qos4Requested.encode(Tag.CLASS_CONTEXT_SPECIFIC, 26, aos);
            }

            if (qos4Negotiated != null) {
                qos4Negotiated.encode(Tag.CLASS_CONTEXT_SPECIFIC, 27, aos);
            }

            if (extPdpType != null) {
                extPdpType.encode(Tag.CLASS_CONTEXT_SPECIFIC, 28, aos);
            }

            if (extPdpAddress != null) {
                extPdpAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 29, aos);
            }

            aos.FinalizeContent(pos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            this._decode(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IOException, UnexpectedDataException, AsnException, IncorrectSyntaxException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new UnexpectedDataException(String.format("Expecting Class[CONTEXT], found Tag[%d] Class[%d]", tag, ais.getTagClass()));
            }
            switch (tag) {
                case 0:
                    pdpContextIdentifier = new ContextId();
                    pdpContextIdentifier.decode(ais);
                    break;
                case 1:
                    pdpContextActive = true;
                    ais.readNull();
                    break;
                case 2:
                    pdpType = new PDPType();
                    pdpType.decode(ais);
                    break;
                case 3:
                    pdpAddress = new PDPAddress();
                    pdpAddress.decode(ais);
                    break;
                case 4:
                    apnSubscribed = new APN();
                    apnSubscribed.decode(ais);
                    break;
                case 5:
                    apnInUse = new APN();
                    apnInUse.decode(ais);
                    break;
                case 6:
                    nsapi = new NSAPI();
                    nsapi.decode(ais);
                    break;
                case 7:
                    transactionId = new TransactionId();
                    transactionId.decode(ais);
                    break;
                case 8:
                    teidForGNandGP = new TEID();
                    teidForGNandGP.decode(ais);
                    break;
                case 9:
                    teidForIu = new TEID();
                    teidForIu.decode(ais);
                    break;
                case 10:
                    ggsnAddress = new GSNAddress();
                    ggsnAddress.decode(ais);
                    break;
                case 11:
                    qosSubscribed = new ExtQoSSubscribed();
                    qosSubscribed.decode(ais);
                    break;
                case 12:
                    qosRequested = new ExtQoSSubscribed();
                    qosRequested.decode(ais);
                    break;
                case 13:
                    qosNegotiated = new ExtQoSSubscribed();
                    qosNegotiated.decode(ais);
                    break;
                case 14:
                    chargingId = new GPRSChargingID();
                    chargingId.decode(ais);
                    break;
                case 15:
                    chargingCharacteristics = new ChargingCharacteristics();
                    chargingCharacteristics.decode(ais);
                    break;
                case 16:
                    rncAddress = new GSNAddress();
                    rncAddress.decode(ais);
                    break;
                case 17:
                    extensionContainer = new ExtensionContainer();
                    extensionContainer.decode(ais);
                    break;
                case 18:
                    qos2Subscribed = new Ext2QoSSubscribed();
                    qos2Subscribed.decode(ais);
                    break;
                case 19:
                    qos2Requested = new Ext2QoSSubscribed();
                    qos2Requested.decode(ais);
                    break;
                case 20:
                    qos2Negotiated = new Ext2QoSSubscribed();
                    qos2Negotiated.decode(ais);
                    break;
                case 21:
                    qos3Subscribed = new Ext3QoSSubscribed();
                    qos3Subscribed.decode(ais);
                    break;
                case 22:
                    qos3Requested = new Ext3QoSSubscribed();
                    qos3Requested.decode(ais);
                    break;
                case 23:
                    qos3Negotiated = new Ext3QoSSubscribed();
                    qos3Negotiated.decode(ais);
                    break;
                case 25:
                    qos4Subscribed = new Ext4QoSSubscribed();
                    qos4Subscribed.decode(ais);
                    break;
                case 26:
                    qos4Requested = new Ext4QoSSubscribed();
                    qos4Requested.decode(ais);
                    break;
                case 27:
                    qos4Negotiated = new Ext4QoSSubscribed();
                    qos4Negotiated.decode(ais);
                    break;
                case 28:
                    extPdpType = new ExtPDPType();
                    extPdpType.decode(ais);
                    break;
                case 29:
                    extPdpAddress = new PDPAddress();
                    extPdpAddress.decode(ais);
                    break;
                default:
                    throw new UnexpectedDataException(String.format("Expecting Class[CONTEXT], found Tag[%d] Class[%d]", tag, ais.getTagClass()));
            }
            if (pdpContextIdentifier == null) {
                throw new UnexpectedDataException("PDPContextIdentifier parameter is null.");
            }
            if (pdpType == null) {
                throw new UnexpectedDataException("PDPType parameter is null.");
            }
        }
    }

    public APN getApnInUse() {
        return apnInUse;
    }

    public void setApnInUse(APN apnInUse) {
        this.apnInUse = apnInUse;
    }

    public APN getApnSubscribed() {
        return apnSubscribed;
    }

    public void setApnSubscribed(APN apnSubscribed) {
        this.apnSubscribed = apnSubscribed;
    }

    public ChargingCharacteristics getChargingCharacteristics() {
        return chargingCharacteristics;
    }

    public void setChargingCharacteristics(ChargingCharacteristics chargingCharacteristics) {
        this.chargingCharacteristics = chargingCharacteristics;
    }

    public GPRSChargingID getChargingId() {
        return chargingId;
    }

    public void setChargingId(GPRSChargingID chargingId) {
        this.chargingId = chargingId;
    }

    public PDPAddress getExtPdpAddress() {
        return extPdpAddress;
    }

    public void setExtPdpAddress(PDPAddress extPdpAddress) {
        this.extPdpAddress = extPdpAddress;
    }

    public ExtPDPType getExtPdpType() {
        return extPdpType;
    }

    public void setExtPdpType(ExtPDPType extPdpType) {
        this.extPdpType = extPdpType;
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public GSNAddress getGgsnAddress() {
        return ggsnAddress;
    }

    public void setGgsnAddress(GSNAddress ggsnAddress) {
        this.ggsnAddress = ggsnAddress;
    }

    public NSAPI getNsapi() {
        return nsapi;
    }

    public void setNsapi(NSAPI nsapi) {
        this.nsapi = nsapi;
    }

    public PDPAddress getPdpAddress() {
        return pdpAddress;
    }

    public void setPdpAddress(PDPAddress pdpAddress) {
        this.pdpAddress = pdpAddress;
    }

    public ContextId getPdpContextIdentifier() {
        return pdpContextIdentifier;
    }

    public void setPdpContextActive(boolean pdpContextActive) {
        this.pdpContextActive = pdpContextActive;
    }

    public PDPType getPdpType() {
        return pdpType;
    }

    public void setPdpContextIdentifier(ContextId pdpContextIdentifier) {
        this.pdpContextIdentifier = pdpContextIdentifier;
    }

    public Ext2QoSSubscribed getQos2Negotiated() {
        return qos2Negotiated;
    }

    public void setPdpType(PDPType pdpType) {
        this.pdpType = pdpType;
    }

    public Ext2QoSSubscribed getQos2Requested() {
        return qos2Requested;
    }

    public void setQos2Negotiated(Ext2QoSSubscribed qos2Negotiated) {
        this.qos2Negotiated = qos2Negotiated;
    }

    public Ext2QoSSubscribed getQos2Subscribed() {
        return qos2Subscribed;
    }

    public void setQos2Requested(Ext2QoSSubscribed qos2Requested) {
        this.qos2Requested = qos2Requested;
    }

    public Ext3QoSSubscribed getQos3Negotiated() {
        return qos3Negotiated;
    }

    public void setQos2Subscribed(Ext2QoSSubscribed qos2Subscribed) {
        this.qos2Subscribed = qos2Subscribed;
    }

    public Ext3QoSSubscribed getQos3Requested() {
        return qos3Requested;
    }

    public void setQos3Negotiated(Ext3QoSSubscribed qos3Negotiated) {
        this.qos3Negotiated = qos3Negotiated;
    }

    public Ext3QoSSubscribed getQos3Subscribed() {
        return qos3Subscribed;
    }

    public void setQos3Requested(Ext3QoSSubscribed qos3Requested) {
        this.qos3Requested = qos3Requested;
    }

    public Ext4QoSSubscribed getQos4Negotiated() {
        return qos4Negotiated;
    }

    public void setQos3Subscribed(Ext3QoSSubscribed qos3Subscribed) {
        this.qos3Subscribed = qos3Subscribed;
    }

    public Ext4QoSSubscribed getQos4Requested() {
        return qos4Requested;
    }

    public void setQos4Negotiated(Ext4QoSSubscribed qos4Negotiated) {
        this.qos4Negotiated = qos4Negotiated;
    }

    public Ext4QoSSubscribed getQos4Subscribed() {
        return qos4Subscribed;
    }

    public void setQos4Requested(Ext4QoSSubscribed qos4Requested) {
        this.qos4Requested = qos4Requested;
    }

    public ExtQoSSubscribed getQosNegotiated() {
        return qosNegotiated;
    }

    public void setQos4Subscribed(Ext4QoSSubscribed qos4Subscribed) {
        this.qos4Subscribed = qos4Subscribed;
    }

    public ExtQoSSubscribed getQosRequested() {
        return qosRequested;
    }

    public void setQosNegotiated(ExtQoSSubscribed qosNegotiated) {
        this.qosNegotiated = qosNegotiated;
    }

    public ExtQoSSubscribed getQosSubscribed() {
        return qosSubscribed;
    }

    public void setQosRequested(ExtQoSSubscribed qosRequested) {
        this.qosRequested = qosRequested;
    }

    public GSNAddress getRncAddress() {
        return rncAddress;
    }

    public void setQosSubscribed(ExtQoSSubscribed qosSubscribed) {
        this.qosSubscribed = qosSubscribed;
    }

    public TEID getTeidForGNandGP() {
        return teidForGNandGP;
    }

    public void setRncAddress(GSNAddress rncAddress) {
        this.rncAddress = rncAddress;
    }

    public TEID getTeidForIu() {
        return teidForIu;
    }

    public void setTeidForGNandGP(TEID teidForGNandGP) {
        this.teidForGNandGP = teidForGNandGP;
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }

    public void setTeidForIu(TEID teidForIu) {
        this.teidForIu = teidForIu;
    }

    public void setTransactionId(TransactionId transactionId) {
        this.transactionId = transactionId;
    }

    public boolean isPdpContextActive() {
        return pdpContextActive;
    }

}
