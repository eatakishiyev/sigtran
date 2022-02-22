/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v4;

import java.io.IOException;
import dev.ocean.isup.enums.CallingPartysCategory;
import dev.ocean.isup.parameters.OriginalCalledNumber;
import dev.ocean.isup.parameters.RedirectingNumber;
import dev.ocean.sigtran.cap.parameters.ChargeNumber;
import dev.ocean.sigtran.cap.parameters.DestinationRoutingAddress;
import dev.ocean.sigtran.cap.parameters.GenericNumbers;
import dev.ocean.sigtran.cap.parameters.LegId;
import dev.ocean.isup.parameters.RedirectionInformation;
import dev.ocean.sigtran.cap.parameters.ServiceInteractionIndicatorsTwo;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.AlertingPattern;
import dev.ocean.sigtran.map.parameters.CugInterLock;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import dev.ocean.sigtran.cap.api.ConnectArg;

/**
 *
 * @author eatakishiyev
 */
public class ConnectArgImpl implements ConnectArg {

    private DestinationRoutingAddress destinationRoutingAddress;
    private AlertingPattern alertingPattern;
    private OriginalCalledNumber originalCalledPartyID;
    private byte[] extensions;
    private byte[] carrier;
    private CallingPartysCategory callingPartysCategory;
    private RedirectingNumber redirectingPartyID;
    private RedirectionInformation redirectionInformation;
    private GenericNumbers genericNumbers;
    private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
    private ChargeNumber chargeNumber;
    private LegId legToBeConnected;
    private CugInterLock cugInterlock;
    private boolean cugOutgoingAccess = false;
    //SuppressionOfAnnouncement::= NULL
    private boolean suppressionOfAnnouncement;
    //OCSIApplicable ::= NULL
    private boolean oCSIApplicable;
    private byte[] naInfo;
    private boolean borInterrogationReqeusted = false;
    private boolean suppressNCSI = false;

    public ConnectArgImpl() {
    }

    public ConnectArgImpl(DestinationRoutingAddress destinationRoutingAddress) {
        this.destinationRoutingAddress = destinationRoutingAddress;
    }

    @Override
    public void encode(AsnOutputStream aos) throws ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            destinationRoutingAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

            if (alertingPattern != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, alertingPattern.value());
            }

            if (originalCalledPartyID != null) {
                this.originalCalledPartyID.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }

            if (extensions != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 10);
                aos.writeLength(extensions.length);
                aos.write(extensions);
            }

            if (carrier != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 11);
                aos.writeLength(carrier.length);
                aos.write(carrier);
            }

            if (callingPartysCategory != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 28, callingPartysCategory.value());
            }

            if (redirectingPartyID != null) {
                redirectingPartyID.encode(Tag.CLASS_CONTEXT_SPECIFIC, 29, aos);
            }

            if (redirectionInformation != null) {
                redirectionInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 30, aos);
            }

            if (genericNumbers != null) {
                genericNumbers.encode(Tag.CLASS_CONTEXT_SPECIFIC, 14, aos);

            }

            if (serviceInteractionIndicatorsTwo != null) {
                serviceInteractionIndicatorsTwo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 15, aos);
            }

            if (chargeNumber != null) {
                chargeNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 19, aos);
            }

            if (legToBeConnected != null) {
                legToBeConnected.encode(Tag.CLASS_CONTEXT_SPECIFIC, 21, aos);
            }

            if (cugInterlock != null) {
                cugInterlock.encode(Tag.CLASS_CONTEXT_SPECIFIC, 31, aos);
            }

            if (cugOutgoingAccess) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 32);
            }

            if (suppressionOfAnnouncement) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 55);
            }

            if (oCSIApplicable) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 56);
            }

            if (naInfo != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 57);
                int lenPos_ = aos.StartContentDefiniteLength();
                aos.write(naInfo);
                aos.FinalizeContent(lenPos_);
            }

            if (borInterrogationReqeusted) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 58);
            }

            if (suppressNCSI) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 59);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        } catch (IllegalNumberFormatException ex) {
            throw new UnexpectedDataException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new AsnException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL], found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        } catch (IllegalNumberFormatException ex) {
            throw new UnexpectedDataException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException, IllegalNumberFormatException, IncorrectSyntaxException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2] , found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.destinationRoutingAddress = new DestinationRoutingAddress();
                    this.destinationRoutingAddress.decode(ais);
                    break;
                case 1:
                    this.alertingPattern = AlertingPattern.getInstance((int) ais.readInteger());
                    break;
                case 6:
                    this.originalCalledPartyID = new OriginalCalledNumber();
                    this.originalCalledPartyID.decode(ais);
                    break;
                case 10:
                    this.extensions = new byte[ais.readLength()];
                    ais.read(extensions);
                    break;
                case 11:
                    this.carrier = new byte[ais.readLength()];
                    ais.read(carrier);
                    break;
                case 28:
                    this.callingPartysCategory = CallingPartysCategory.getInstance((int) ais.readInteger());
                    break;
                case 29:
                    this.redirectingPartyID = new RedirectingNumber();
                    this.redirectingPartyID.decode(ais);
                    break;
                case 30:
                    this.redirectionInformation = new RedirectionInformation();
                    this.redirectionInformation.decode(ais);
                    break;
                case 14:
                    this.genericNumbers = new GenericNumbers();
                    this.genericNumbers.decode(ais);
                    break;
                case 15:
                    this.serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwo();
                    this.serviceInteractionIndicatorsTwo.decode(ais);
                    break;
                case 19:
                    this.chargeNumber = new ChargeNumber();
                    this.chargeNumber.decode(ais);
                    break;
                case 21:
                    this.legToBeConnected = LegId.createLegId(ais.readSequenceStream());
                    break;
                case 31:
                    this.cugInterlock = new CugInterLock();
                    cugInterlock.decode(ais);
                    break;
                case 32:
                    this.cugOutgoingAccess = true;
                    ais.readNull();
                    break;
                case 55:
                    this.suppressionOfAnnouncement = true;
                    ais.readNull();
                    break;
                case 56:
                    this.oCSIApplicable = true;
                    ais.readNull();
                    break;
                case 57:
                    this.naInfo = new byte[ais.readLength()];
                    ais.read(naInfo);
                    break;
                case 58:
                    this.borInterrogationReqeusted = true;
                    ais.readNull();
                    break;
                case 59:
                    this.suppressNCSI = true;
                    ais.readNull();
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
        }
    }

    /**
     * destinationRoutingAddress:
     * This parameter contains the called party numbers towards which the call
     * shall be routed.
     *
     * @return the destinationRoutingAddress
     */
    @Override
    public DestinationRoutingAddress getDestinationRoutingAddress() {
        return destinationRoutingAddress;
    }

    /**
     *
     * destinationRoutingAddress:
     * This parameter contains the called party numbers towards which the call
     * shall be routed.
     *
     * @param destinationRoutingAddress
     */
    @Override
    public void setDestinationRoutingAddress(DestinationRoutingAddress destinationRoutingAddress) {
        this.destinationRoutingAddress = destinationRoutingAddress;
    }

    /**
     * alertingPattern:
     * This parameter indicates the type of alerting to be applied. It is
     * defined in 3GPP TS 29.002 [11].
     *
     * @return the alertingPattern
     */
    public AlertingPattern getAlertingPattern() {
        return alertingPattern;
    }

    /**
     * alertingPattern:
     * This parameter indicates the type of alerting to be applied. It is
     * defined in 3GPP TS 29.002 [11].
     *
     * @param alertingPattern
     */
    public void setAlertingPattern(AlertingPattern alertingPattern) {
        this.alertingPattern = alertingPattern;
    }

    /**
     * originalCalledPartyID:
     * If the call is forwarded by the gsmSCF, then this parameter carries the
     * dialled digits.
     *
     * @return the originalCalledPartyID
     */
    @Override
    public OriginalCalledNumber getOriginalCalledPartyID() {
        return originalCalledPartyID;
    }

    /**
     * originalCalledPartyID:
     * If the call is forwarded by the gsmSCF, then this parameter carries the
     * dialled digits.
     *
     * @param originalCalledPartyID
     */
    @Override
    public void setOriginalCalledPartyID(OriginalCalledNumber originalCalledPartyID) {
        this.originalCalledPartyID = originalCalledPartyID;
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
     * Carrier:
     * This parameter indicates carrier information. It consists of the carrier
     * selection field followed by the Carrier ID
     * information to be used by gsmSSF for routeing a call to a carrier.
     * It contains the following embedded parameters:
     * - carrierSelectionField:
     * This parameter indicates how the selected carrier is provided (e.g.
     * pre-subscribed).
     * - carrierID:
     * This parameter indicates the carrier to use for the call. It contains the
     * digits of the carrier identification code.
     *
     * @return the carrier
     */
    public byte[] getCarrier() {
        return carrier;
    }

    /**
     * Carrier:
     * This parameter indicates carrier information. It consists of the carrier
     * selection field followed by the Carrier ID
     * information to be used by gsmSSF for routeing a call to a carrier.
     * It contains the following embedded parameters:
     * - carrierSelectionField:
     * This parameter indicates how the selected carrier is provided (e.g.
     * pre-subscribed).
     * - carrierID:
     * This parameter indicates the carrier to use for the call. It contains the
     * digits of the carrier identification code.
     *
     * @param carrier the carrier to set
     */
    public void setCarrier(byte[] carrier) {
        this.carrier = carrier;
    }

    /**
     * callingPartysCategory:
     * This parameter indicates the type of calling party (e.g., operator, pay
     * phone, ordinary subscriber).
     *
     * @return the callingPartysCategory
     */
    @Override
    public CallingPartysCategory getCallingPartysCategory() {
        return callingPartysCategory;
    }

    /**
     * callingPartysCategory:
     * This parameter indicates the type of calling party (e.g., operator, pay
     * phone, ordinary subscriber).
     *
     * @param callingPartysCategory
     */
    @Override
    public void setCallingPartysCategory(CallingPartysCategory callingPartysCategory) {
        this.callingPartysCategory = callingPartysCategory;
    }

    /**
     * redirectingPartyID:
     * This parameter indicates the last directory number the call was
     * redirected from.
     *
     * @return the redirectingPartyID
     */
    @Override
    public RedirectingNumber getRedirectingPartyID() {
        return redirectingPartyID;
    }

    /**
     * redirectingPartyID:
     * This parameter indicates the last directory number the call was
     * redirected from.
     *
     * @param redirectingPartyID the redirectingPartyID to set
     */
    @Override
    public void setRedirectingPartyID(RedirectingNumber redirectingPartyID) {
        this.redirectingPartyID = redirectingPartyID;
    }

    /**
     * redirectionInformation:
     * This parameter contains forwarding related information, such as
     * redirecting counter.
     *
     * @return the redirectionInformation
     */
    @Override
    public RedirectionInformation getRedirectionInformation() {
        return redirectionInformation;
    }

    /**
     * redirectionInformation:
     * This parameter contains forwarding related information, such as
     * redirecting counter.
     *
     * @param redirectionInformation the redirectionInformation to set
     */
    @Override
    public void setRedirectionInformation(RedirectionInformation redirectionInformation) {
        this.redirectionInformation = redirectionInformation;
    }

    /**
     * genericNumbers:
     * This parameter allows the gsmSCF to set the Generic Number parameter used
     * in the network. It is used for transfer
     * of Additional Calling Party Number
     *
     * @return the genericNumber
     */
    @Override
    public GenericNumbers getGenericNumbers() {
        return genericNumbers;
    }

    /**
     * genericNumbers:
     * This parameter allows the gsmSCF to set the Generic Number parameter used
     * in the network. It is used for transfer
     * of Additional Calling Party Number
     *
     * @param genericNumbers the genericNumber to set
     */
    @Override
    public void setGenericNumbers(GenericNumbers genericNumbers) {
        this.genericNumbers = genericNumbers;
    }

    /**
     * serviceInteractionIndicatorsTwo:
     * This parameter contains indicators to resolve interactions between CAMEL
     * based services and network based
     * services.
     *
     * @return the serviceInteractionIndicatorsTwo
     */
    public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
        return serviceInteractionIndicatorsTwo;
    }

    /**
     * serviceInteractionIndicatorsTwo:
     * This parameter contains indicators to resolve interactions between CAMEL
     * based services and network based
     * services.
     *
     * @param serviceInteractionIndicatorsTwo
     */
    public void setServiceInteractionIndicatorsTwo(ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo) {
        this.serviceInteractionIndicatorsTwo = serviceInteractionIndicatorsTwo;
    }

    /**
     * ChargeNumber:
     * This parameter contains the number that identifies the entity to be
     * charged for the call. It identifies the chargeable
     * number for the usage of a carrier (applicable on a call sent into a North
     * American long distance carrier). For a
     * definition of this parameter refer to ANSI T1.113-1995 [92].
     *
     * @return the chargeNumber
     */
    public ChargeNumber getChargeNumber() {
        return chargeNumber;
    }

    /**
     * ChargeNumber:
     * This parameter contains the number that identifies the entity to be
     * charged for the call. It identifies the chargeable
     * number for the usage of a carrier (applicable on a call sent into a North
     * American long distance carrier). For a
     * definition of this parameter refer to ANSI T1.113-1995 [92].
     *
     * @param chargeNumber the chargeNumber to set
     */
    public void setChargeNumber(ChargeNumber chargeNumber) {
        this.chargeNumber = chargeNumber;
    }

    /**
     * legToBeConnected:
     * This parameter indicates the leg to be connected.
     *
     * @return the legToBeConnected
     */
    public LegId getLegToBeConnected() {
        return legToBeConnected;
    }

    /**
     * legToBeConnected:
     * This parameter indicates the leg to be connected.
     *
     * @param legToBeConnected the legToBeConnected to set
     */
    public void setLegToBeConnected(LegId legToBeConnected) {
        this.legToBeConnected = legToBeConnected;
    }

    /**
     * cug-Interlock:
     * This parameter uniquely identifies a CUG within a network.
     *
     * @return the cugInterlock
     */
    public CugInterLock getCugInterlock() {
        return cugInterlock;
    }

    /**
     * cug-Interlock:
     * This parameter uniquely identifies a CUG within a network.
     *
     * @param cugInterlock the cugInterlock to set
     */
    public void setCugInterlock(CugInterLock cugInterlock) {
        this.cugInterlock = cugInterlock;
    }

    /**
     * cug-OutgoingAccess:
     * This parameter indicates if the calling user has subscribed to the
     * outgoing access inter-CUG accessibility
     * subscription option.
     *
     * @return the cugOutgoingAccess
     */
    public boolean isCugOutgoingAccess() {
        return cugOutgoingAccess;
    }

    /**
     * cug-OutgoingAccess:
     * This parameter indicates if the calling user has subscribed to the
     * outgoing access inter-CUG accessibility
     * subscription option.
     *
     * @param cugOutgoingAccess the cugOutgoingAccess to set
     */
    public void setCugOutgoingAccess(boolean cugOutgoingAccess) {
        this.cugOutgoingAccess = cugOutgoingAccess;
    }

    /**
     * suppressionOfAnnouncement:
     * This parameter indicates that announcements and tones which are played in
     * the exchange at non-successful call
     * set-up attempts shall be suppressed.
     *
     * @return the suppressionOfAnnouncement
     */
    @Override
    public boolean isSuppressionOfAnnouncement() {
        return suppressionOfAnnouncement;
    }

    /**
     * suppressionOfAnnouncement:
     * This parameter indicates that announcements and tones which are played in
     * the exchange at non-successful call
     * set-up attempts shall be suppressed.
     *
     * @param suppressionOfAnnouncement the suppressionOfAnnouncement to set
     */
    @Override
    public void setSuppressionOfAnnouncement(boolean suppressionOfAnnouncement) {
        this.suppressionOfAnnouncement = suppressionOfAnnouncement;
    }

    /**
     * oCSIApplicable:
     * This parameter indicates to the GMSC/gsmSSF or VMSC/gsmSSF that the
     * Originating CAMEL Subscription
     * Information, if present, shall be applied on the outgoing call leg
     * created with the Connect operation. For the use of
     * this parameter see 3GPP TS 23.078 [7]
     *
     * @return the oCSIApplicable
     */
    @Override
    public boolean isOCSIApplicable() {
        return oCSIApplicable;
    }

    /**
     * oCSIApplicable:
     * This parameter indicates to the GMSC/gsmSSF or VMSC/gsmSSF that the
     * Originating CAMEL Subscription
     * Information, if present, shall be applied on the outgoing call leg
     * created with the Connect operation. For the use of
     * this parameter see 3GPP TS 23.078 [7]
     *
     * @param oCSIApplicable the oCSIApplicable to set
     */
    @Override
    public void setOCSIApplicable(boolean oCSIApplicable) {
        this.oCSIApplicable = oCSIApplicable;
    }

    /**
     * bor-InterrogationRequested:
     * This parameter indicates that Basic Optimal Routeing is requested for the
     * call.
     *
     * @return the borInterrogationReqeusted
     */
    public boolean isBorInterrogationReqeusted() {
        return borInterrogationReqeusted;
    }

    /**
     * bor-InterrogationRequested:
     * This parameter indicates that Basic Optimal Routeing is requested for the
     * call.
     *
     * @param borInterrogationReqeusted the borInterrogationReqeusted to set
     */
    public void setBorInterrogationReqeusted(boolean borInterrogationReqeusted) {
        this.borInterrogationReqeusted = borInterrogationReqeusted;
    }

    /**
     * suppress-N-CSI:
     * This parameter indicates that N-CSI shall be suppressed
     *
     * @return the suppressNCSI
     */
    public boolean isSuppressNCSI() {
        return suppressNCSI;
    }

    /**
     * suppress-N-CSI:
     * This parameter indicates that N-CSI shall be suppressed
     *
     * @param suppressNCSI the suppressNCSI to set
     */
    public void setSuppressNCSI(boolean suppressNCSI) {
        this.suppressNCSI = suppressNCSI;
    }

    /**
     * naOliInfo:
     * This parameter contains originating line information which identifies the
     * charged party number type to the carrier.
     *
     * @return the naoLiInfo
     */
    public byte[] getNaInfo() {
        return naInfo;
    }

    /**
     * naOliInfo:
     * This parameter contains originating line information which identifies the
     * charged party number type to the carrier.
     *
     * @param naInfo
     */
    public void setNaInfo(byte[] naInfo) {
        this.naInfo = naInfo;
    }
}
