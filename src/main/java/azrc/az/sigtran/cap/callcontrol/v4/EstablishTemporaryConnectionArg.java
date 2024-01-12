/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v4;

import java.io.IOException;

import azrc.az.isup.parameters.CallingPartyNumber;
import azrc.az.isup.parameters.GenericDigits;
import azrc.az.isup.parameters.OriginalCalledNumber;
import azrc.az.isup.parameters.ScfId;
import azrc.az.sigtran.cap.api.IEstablishTemporaryConnectionArg;
import azrc.az.sigtran.cap.parameters.AssistingSSPIPRoutingAddress;
import azrc.az.sigtran.cap.parameters.CallSegmentID;
import azrc.az.sigtran.cap.parameters.ChargeNumber;
import azrc.az.sigtran.cap.parameters.ServiceInteractionIndicatorsTwo;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * EstablishTemporaryConnectionArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
 * assistingSSPIPRoutingAddress [0] AssistingSSPIPRoutingAddress {bound},
 * correlationID [1] CorrelationID {bound} OPTIONAL, scfID [3] ScfID {bound}
 * OPTIONAL, extensions [4] Extensions {bound} OPTIONAL, carrier [5] Carrier
 * {bound} OPTIONAL, serviceInteractionIndicatorsTwo [6]
 * ServiceInteractionIndicatorsTwo OPTIONAL, callSegmentID [7] CallSegmentID
 * {bound} OPTIONAL, naOliInfo [50] NAOliInfo OPTIONAL, chargeNumber [51]
 * ChargeNumber {bound} OPTIONAL, ..., originalCalledPartyID [52]
 * OriginalCalledPartyID {bound} OPTIONAL, callingPartyNumber [53]
 * CallingPartyNumber {bound} OPTIONAL }
 *
 * @author eatakishiyev
 */
public class EstablishTemporaryConnectionArg implements IEstablishTemporaryConnectionArg {

    private AssistingSSPIPRoutingAddress assistingSSPIPRoutingAddress;
    private GenericDigits correlationId;
    private ScfId scfId;
    private byte[] extensions;
    private byte[] carrier;
    private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
    private CallSegmentID callSegmentID;
    private byte[] naOliInfo;
    private ChargeNumber chargeNumber;
    private OriginalCalledNumber originalCalledPartyID;
    private CallingPartyNumber callingPartyNumber;

    public EstablishTemporaryConnectionArg() {
    }

    public EstablishTemporaryConnectionArg(AssistingSSPIPRoutingAddress assistingSSPIPRoutingAddress) {
        this.assistingSSPIPRoutingAddress = assistingSSPIPRoutingAddress;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IllegalNumberFormatException, IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            this.assistingSSPIPRoutingAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

            if (correlationId != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 1, correlationId.encode());
            }

            if (scfId != null) {
                this.scfId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }

            if (extensions != null) {
                aos.writeSequence(Tag.CLASS_CONTEXT_SPECIFIC, 4, extensions);
            }

            if (carrier != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 5, carrier);
            }

            if (serviceInteractionIndicatorsTwo != null) {
                this.serviceInteractionIndicatorsTwo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }

            if (callSegmentID != null) {
                this.callSegmentID.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }

            if (naOliInfo != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 50, naOliInfo);
            }

            if (chargeNumber != null) {
                this.chargeNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 51, aos);
            }

            if (originalCalledPartyID != null) {
                this.originalCalledPartyID.encode(Tag.CLASS_CONTEXT_SPECIFIC, 52, aos);
            }

            if (callingPartyNumber != null) {
                this.callingPartyNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 53, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IllegalNumberFormatException, IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new AsnException(String.format("Unexpected tag received. Expecting Tag[16] TagClass[0], found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void decode_(AsnInputStream ais) throws AsnException, IOException, IllegalNumberFormatException, IncorrectSyntaxException {
        while (ais.available() > 0) {
            int tag = ais.readTag();

            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.assistingSSPIPRoutingAddress = new AssistingSSPIPRoutingAddress();
                    this.assistingSSPIPRoutingAddress.decode(ais);
                    break;
                case 1:
                    this.correlationId = new GenericDigits();
                    correlationId.decode(ais.readOctetString());
                    break;
                case 3:
                    this.scfId = new ScfId();
                    this.scfId.decode(ais);
                    break;
                case 4:
                    this.extensions = new byte[ais.readLength()];
                    ais.read(this.extensions);
                    break;
                case 5:
                    this.carrier = new byte[ais.readLength()];
                    ais.read(this.carrier);
                    break;
                case 6:
                    this.serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwo();
                    this.serviceInteractionIndicatorsTwo.decode(ais);
                    break;
                case 7:
                    this.callSegmentID = new CallSegmentID();
                    this.callSegmentID.decode(ais);
                    break;
                case 50:
                    this.naOliInfo = new byte[ais.readLength()];
                    ais.read(naOliInfo);
                    break;
                case 51:
                    this.chargeNumber = new ChargeNumber();
                    this.chargeNumber.decode(ais);
                    break;
                case 52:
                    this.originalCalledPartyID = new OriginalCalledNumber();
                    this.originalCalledPartyID.decode(ais);
                    break;
                case 53:
                    this.callingPartyNumber = new CallingPartyNumber();
                    this.callingPartyNumber.decode(ais);
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
        }
    }

    /**
     * @return the assistingSSPIPRoutingAddress
     */
    @Override
    public AssistingSSPIPRoutingAddress getAssistingSSPIPRoutingAddress() {
        return assistingSSPIPRoutingAddress;
    }

    /**
     * @param assistingSSPIPRoutingAddress the assistingSSPIPRoutingAddress to
     * set
     */
    @Override
    public void setAssistingSSPIPRoutingAddress(AssistingSSPIPRoutingAddress assistingSSPIPRoutingAddress) {
        this.assistingSSPIPRoutingAddress = assistingSSPIPRoutingAddress;
    }

    /**
     * @return the correlationID
     */
    @Override
    public GenericDigits getCorrelationId() {
        return correlationId;
    }

    /**
     * @param correlationId the correlationID to set
     */
    @Override
    public void setCorrelationId(GenericDigits correlationId) {
        this.correlationId = correlationId;
    }

    /**
     * @return the scfID
     */
    @Override
    public ScfId getScfId() {
        return scfId;
    }

    /**
     * @param scfId the scfID to set
     */
    @Override
    public void setScfId(ScfId scfId) {
        this.scfId = scfId;
    }

    /**
     * @return the extensions
     */
    @Override
    public byte[] getExtensions() {
        return extensions;
    }

    /**
     * @param extensions the extensions to set
     */
    @Override
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

    /**
     * @return the carrier
     */
    public byte[] getCarrier() {
        return carrier;
    }

    /**
     * @param carrier the carrier to set
     */
    public void setCarrier(byte[] carrier) {
        this.carrier = carrier;
    }

    /**
     * @return the serviceInteractionIndicatorsTwo
     */
    @Override
    public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
        return serviceInteractionIndicatorsTwo;
    }

    /**
     * @param serviceInteractionIndicatorsTwo the
     * serviceInteractionIndicatorsTwo to set
     */
    @Override
    public void setServiceInteractionIndicatorsTwo(ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo) {
        this.serviceInteractionIndicatorsTwo = serviceInteractionIndicatorsTwo;
    }

    /**
     * @return the callSegmentID
     */
    public CallSegmentID getCallSegmentID() {
        return callSegmentID;
    }

    /**
     * @param callSegmentID the callSegmentID to set
     */
    public void setCallSegmentID(CallSegmentID callSegmentID) {
        this.callSegmentID = callSegmentID;
    }

    /**
     * @return the naOliInfo
     */
    @Override
    public byte[] getNaInfo() {
        return naOliInfo;
    }

    /**
     * @param naOliInfo the naOliInfo to set
     */
    @Override
    public void setNaInfo(byte[] naOliInfo) {
        this.naOliInfo = naOliInfo;
    }

    /**
     * @return the chargeNumber
     */
    public ChargeNumber getChargeNumber() {
        return chargeNumber;
    }

    /**
     * @param chargeNumber the chargeNumber to set
     */
    public void setChargeNumber(ChargeNumber chargeNumber) {
        this.chargeNumber = chargeNumber;
    }

    /**
     * @return the originalCalledPartyID
     */
    public OriginalCalledNumber getOriginalCalledPartyID() {
        return originalCalledPartyID;
    }

    /**
     * @param originalCalledPartyID the originalCalledPartyID to set
     */
    public void setOriginalCalledPartyID(OriginalCalledNumber originalCalledPartyID) {
        this.originalCalledPartyID = originalCalledPartyID;
    }

    /**
     * @return the callingPartyNumber
     */
    public CallingPartyNumber getCallingPartyNumber() {
        return callingPartyNumber;
    }

    /**
     * @param callingPartyNumber the callingPartyNumber to set
     */
    public void setCallingPartyNumber(CallingPartyNumber callingPartyNumber) {
        this.callingPartyNumber = callingPartyNumber;
    }
}
