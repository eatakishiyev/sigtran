/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.sigtran.cap.api.InitialDpArg;
import dev.ocean.sigtran.cap.callcontrol.general.EventTypeBCSM;
import dev.ocean.sigtran.cap.parameters.AdditionalCallingPartyNumber;
import dev.ocean.sigtran.cap.parameters.CGEncountered;
import dev.ocean.sigtran.cap.parameters.CalledPartyBCDNumber;
import dev.ocean.isup.enums.CallingPartysCategory;
import dev.ocean.sigtran.cap.parameters.IPSSPCapabilities;
import dev.ocean.isup.parameters.CalledPartyNumber;
import dev.ocean.isup.parameters.CallingPartyNumber;
import dev.ocean.isup.parameters.LocationNumber;
import dev.ocean.isup.parameters.OriginalCalledNumber;
import dev.ocean.isup.parameters.RedirectingNumber;
import dev.ocean.sigtran.cap.parameters.ITU.Q_850.Cause;
import dev.ocean.isup.parameters.RedirectionInformation;
import dev.ocean.sigtran.cap.parameters.ServiceInteractionIndicatorsTwo;
import dev.ocean.sigtran.cap.parameters.ServiceKey;
import dev.ocean.sigtran.cap.parameters.TimeAndTimeZone;
import dev.ocean.isup.parameters.HighLayerCompability;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.CallReferenceNumber;
import dev.ocean.sigtran.map.parameters.ExtBasicServiceCode;
import dev.ocean.sigtran.map.parameters.IMSI;
import dev.ocean.sigtran.map.parameters.ISDNAddressString;
import dev.ocean.sigtran.map.parameters.SubscriberState;
import dev.ocean.sigtran.map.parameters.location.information.LocationInformation;
import java.text.ParseException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class InitialDpArgImpl implements InitialDpArg {

    private ServiceKey serviceKey;
    private CalledPartyNumber calledPartyNumber;
    private CallingPartyNumber callingPartyNumber;
    private CallingPartysCategory callingPartysCategory;
    private CGEncountered cGEncountered;
    private IPSSPCapabilities iPSSPCapabilities;
    private LocationNumber locationNumber;
    private OriginalCalledNumber originalCalledPartyID;
    private byte[] extensions;
    private HighLayerCompability highLayerCompability;

//    AdditionalCallingPartyNumber = >Generic number "additional calling party number"
    private AdditionalCallingPartyNumber additionalCallingPartyNumber;
    private byte[] bearerCapability;
    private EventTypeBCSM eventTypeBCSM;
    private RedirectingNumber redirectingPartyID;
    private RedirectionInformation redirectionInformation;
    private Cause cause;
    private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorTwo;
    private byte[] carrier;
    private Integer cugIndex;
    private byte[] cugInterlock;
    private Boolean cugOutgoingAccess = false;
    private IMSI imsi;
    private SubscriberState subscriberState;
    private LocationInformation locationInformation;
    private ExtBasicServiceCode extBasicServiceCode;
    private CallReferenceNumber callReferenceNumber;
    private ISDNAddressString mscAddress;
    private CalledPartyBCDNumber calledPartyBCDNumber;
    private TimeAndTimeZone timeAndTimezone;
    private Boolean callForwardingSSPending = false;
    private InitialDpArgExtension initialDPArgExtension;

    @Override
    public void encode(AsnOutputStream aos) throws ParameterOutOfRangeException, IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            this.serviceKey.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

            if (this.calledPartyNumber != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                baos.write(this.calledPartyNumber.encode());

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 2);
                aos.writeLength(baos.toByteArray().length);
                aos.write(baos.toByteArray());
            }

            if (this.callingPartyNumber != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                baos.write(this.callingPartyNumber.encode());

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 3);
                aos.writeLength(baos.toByteArray().length);
                aos.write(baos.toByteArray());
            }

            if (callingPartysCategory != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 5, callingPartysCategory.value());
            }

            if (cGEncountered != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 7, cGEncountered.value());
            }

            if (iPSSPCapabilities != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                iPSSPCapabilities.encode(baos);

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 8);
                aos.writeLength(baos.toByteArray().length);
                aos.write(baos.toByteArray());
            }

            if (locationNumber != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                locationNumber.encode(baos);

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 10);
                aos.writeLength(baos.toByteArray().length);
                aos.write(baos.toByteArray());
            }

            if (originalCalledPartyID != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                originalCalledPartyID.encode(baos);

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 12);
                aos.writeLength(baos.toByteArray().length);
                aos.write(baos.toByteArray());
            }

            if (extensions != null) {
                aos.writeSequence(Tag.CLASS_CONTEXT_SPECIFIC, 15, this.extensions);
            }

            if (highLayerCompability != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                highLayerCompability.encode(baos);

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 23);
                aos.writeLength(baos.toByteArray().length);
                aos.write(baos.toByteArray());
            }

            if (additionalCallingPartyNumber != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                additionalCallingPartyNumber.encode(baos);

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 25);
                aos.writeLength(baos.toByteArray().length);
                aos.write(baos.toByteArray());
            }

            if (bearerCapability != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 27);
                int lenPos_ = aos.StartContentDefiniteLength();
                aos.write(bearerCapability);
                aos.FinalizeContent(lenPos_);
            }

            if (eventTypeBCSM != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 28, eventTypeBCSM.toInt());
            }

            if (redirectingPartyID != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                redirectingPartyID.encode(baos);

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 29);
                aos.writeLength(baos.toByteArray().length);
                aos.write(baos.toByteArray());
            }

            if (redirectionInformation != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                baos.write(redirectionInformation.encode());

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 30);
                aos.writeLength(baos.toByteArray().length);
                aos.write(baos.toByteArray());
            }

            if (cause != null) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                cause.encode(baos);

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 17);
                aos.writeLength(baos.toByteArray().length);
                aos.write(baos.toByteArray());
            }

            if (serviceInteractionIndicatorTwo != null) {
                serviceInteractionIndicatorTwo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 32, aos);
            }

            if (carrier != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 37);
                aos.writeLength(carrier.length);
                aos.write(carrier);
            }

            if (cugIndex != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 45, cugIndex);
            }

            if (cugInterlock != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 46);
                aos.writeLength(cugInterlock.length);
                aos.write(cugInterlock);
            }

            if (cugOutgoingAccess) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 47);
            }

            if (imsi != null) {
                imsi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 50, aos);
            }

            if (subscriberState != null) {
                subscriberState.encode(Tag.CLASS_CONTEXT_SPECIFIC, 51, aos);
            }

            if (locationInformation != null) {
                locationInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 52, aos);
            }

            if (extBasicServiceCode != null) {
                extBasicServiceCode.encode(Tag.CLASS_CONTEXT_SPECIFIC, 53, aos);
            }

            if (callReferenceNumber != null) {
                callReferenceNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 54, aos);
            }

            if (mscAddress != null) {
                mscAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 55, aos);
            }

            if (calledPartyBCDNumber != null) {
                calledPartyBCDNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 56, aos);
            }

            if (timeAndTimezone != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                timeAndTimezone.encode(baos);

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 57);
                aos.writeLength(baos.toByteArray().length);
                aos.write(baos.toByteArray());
            }

            if (callForwardingSSPending) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 58);
            }

            if (initialDPArgExtension != null) {
                initialDPArgExtension.encode(Tag.CLASS_CONTEXT_SPECIFIC, 59, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException | IllegalNumberFormatException ex) {
            throw new UnexpectedDataException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, ParameterOutOfRangeException, UnexpectedDataException, IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL], found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
            this.decode_(ais.readSequenceStream());
        } catch (IOException | AsnException | IllegalNumberFormatException | ParseException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }

    }

    /**
     * This parameter indicates to the gsmSCF the requested IN service. It is
     * used to address the required application/SLP within the gsmSCF; this
     * parameter is not for SCP addressing
     *
     * @return the serviceKey
     */
    @Override
    public ServiceKey getServiceKey() {
        return serviceKey;
    }

    /**
     * @param serviceKey This parameter indicates to the gsmSCF the requested IN
     * service. It is used to address the required application/SLP within the
     * gsmSCF; this parameter is not for SCP addressing
     */
    @Override
    public void setServiceKey(ServiceKey serviceKey) {
        this.serviceKey = serviceKey;
    }

    /**
     * This parameter contains the number used to identify the called party in
     * the forward direction, i.e. see ETSI EN 300 356-1 [23]. This parameter
     * shall be sent only in the Mobile Terminating, Mobile Forwarding, mobile
     * originating on unsuccessful TDP and trunk originating cases. For the
     * trunk originating case the end of pulsing signal (ST) is included in the
     * calledPartyNumber address signals if it has been received or the MSC has
     * determined that the called number information is complete.
     *
     * @return the calledPartyNumber
     */
    @Override
    public CalledPartyNumber getCalledPartyNumber() {
        return calledPartyNumber;
    }

    /**
     * @param calledPartyNumber This parameter contains the number used to
     * identify the called party in the forward direction, i.e. see ETSI EN 300
     * 356-1 [23]. This parameter shall be sent only in the Mobile Terminating,
     * Mobile Forwarding, mobile originating on unsuccessful TDP and trunk
     * originating cases. For the trunk originating case the end of pulsing
     * signal (ST) is included in the calledPartyNumber address signals if it
     * has been received or the MSC has determined that the called number
     * information is complete.
     */
    @Override
    public void setCalledPartyNumber(CalledPartyNumber calledPartyNumber) {
        this.calledPartyNumber = calledPartyNumber;
    }

    /**
     * This parameter carries the calling party number to identify the calling
     * party or the origin of the call. See ETSI EN 300 356-1 [23] Calling Party
     * Number signalling information.
     *
     * @return the callingPartyNumber
     */
    @Override
    public CallingPartyNumber getCallingPartyNumber() {
        return callingPartyNumber;
    }

    /**
     * @param callingPartyNumber This parameter carries the calling party number
     * to identify the calling party or the origin of the call. See ETSI EN 300
     * 356-1 [23] Calling Party Number signalling information.
     */
    @Override
    public void setCallingPartyNumber(CallingPartyNumber callingPartyNumber) {
        this.callingPartyNumber = callingPartyNumber;
    }

    /**
     * Indicates the type of calling party (e.g. operator, pay phone, ordinary
     * subscriber). See ETSI EN 300 356-1 [23] Calling Party Category signalling
     * information.
     *
     * @return the callingPartysCategory
     */
    @Override
    public CallingPartysCategory getCallingPartysCategory() {
        return callingPartysCategory;
    }

    /**
     * Indicates the type of calling party (e.g. operator, pay phone, ordinary
     * subscriber). See ETSI EN 300 356-1 [23] Calling Party Category signalling
     * information.
     *
     * @param callingPartysCategory the callingPartysCategory to set
     */
    @Override
    public void setCallingPartysCategory(CallingPartysCategory callingPartysCategory) {
        this.callingPartysCategory = callingPartysCategory;
    }

    /**
     * @return the cGEncountered
     */
    public CGEncountered getcGEncountered() {
        return cGEncountered;
    }

    /**
     * @param cGEncountered the cGEncountered to set
     */
    public void setcGEncountered(CGEncountered cGEncountered) {
        this.cGEncountered = cGEncountered;
    }

    /**
     * This parameter indicates which gsmSRF resources supported within the VMSC
     * or GMSC the gsmSSF resides in are attached and available
     *
     * @return the iPSSPCapabilities
     */
    public IPSSPCapabilities getiPSSPCapabilities() {
        return iPSSPCapabilities;
    }

    /**
     * @param iPSSPCapabilities the iPSSPCapabilities to set This parameter
     * indicates which gsmSRF resources supported within the VMSC or GMSC the
     * gsmSSF resides in are attached and available
     */
    public void setiPSSPCapabilities(IPSSPCapabilities iPSSPCapabilities) {
        this.iPSSPCapabilities = iPSSPCapabilities;
    }

    /**
     * This parameter is used to convey the geographical area address for
     * mobility services, see ITU-T Recommendation Q.762 [44]. It is used when
     * "callingPartyNumber" does not contain any information about the
     * geographical location of the calling party (e.g., origin dependent
     * routeing when the calling party is a mobile subscriber).
     *
     * @return the locationNumber
     */
    @Override
    public LocationNumber getLocationNumber() {
        return locationNumber;
    }

    /**
     * @param locationNumber This parameter is used to convey the geographical
     * area address for mobility services, see ITU-T Recommendation Q.762 [44].
     * It is used when "callingPartyNumber" does not contain any information
     * about the geographical location of the calling party (e.g., origin
     * dependent routeing when the calling party is a mobile subscriber).
     */
    @Override
    public void setLocationNumber(LocationNumber locationNumber) {
        this.locationNumber = locationNumber;
    }

    /**
     * If the call has met call forwarding on the route to the gsmSSF, then this
     * parameter carries the dialled digits. Refer to EN 300 356-1[23] Original
     * Called Number signalling information.
     *
     * @return the originalCalledPartyID
     */
    @Override
    public OriginalCalledNumber getOriginalCalledPartyID() {
        return originalCalledPartyID;
    }

    /**
     * If the call has met call forwarding on the route to the gsmSSF, then this
     * parameter carries the dialled digits. Refer to EN 300 356-1[23] Original
     * Called Number signalling information.
     *
     * @param originalCalledPartyID the originalCalledPartyID to set
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
     * This parameter indicates the type of the high layer compatibility, which
     * will be used to determine the ISDN -teleservice of a connected ISDN
     * terminal. The highlayerCompatibility can also be transported by ISUP
     * (e.g. within the ATP (see ITU-T Recommendation Q.763 [45]) parameter).
     *
     * @return the highLayerCompability
     */
    @Override
    public HighLayerCompability getHighLayerCompability() {
        return highLayerCompability;
    }

    /**
     * This parameter indicates the type of the high layer compatibility, which
     * will be used to determine the ISDN -teleservice of a connected ISDN
     * terminal. The highlayerCompatibility can also be transported by ISUP
     * (e.g. within the ATP (see ITU-T Recommendation Q.763 [45]) parameter).
     *
     * @param highLayerCompability the highLayerCompability to set
     */
    @Override
    public void setHighLayerCompability(HighLayerCompability highLayerCompability) {
        this.highLayerCompability = highLayerCompability;
    }

    /**
     * The calling party number provided by the access signalling system of the
     * calling user, e.g. provided by a PBX.
     *
     * @return the additionalCallingPartyNumber
     */
    @Override
    public AdditionalCallingPartyNumber getAdditionalCallingPartyNumber() {
        return additionalCallingPartyNumber;
    }

    /**
     * @param additionalCallingPartyNumber the additionalCallingPartyNumber to
     * set
     *
     * The calling party number provided by the access signalling system of the
     * calling user, e.g. provided by a PBX.
     */
    @Override
    public void setAdditionalCallingPartyNumber(AdditionalCallingPartyNumber additionalCallingPartyNumber) {
        this.additionalCallingPartyNumber = additionalCallingPartyNumber;
    }

    /**
     * This parameter indicates the type of the bearer capability connection or
     * the transmission medium requirements to the user. It is a network option
     * to select which of the two parameters to be used: - bearerCap: This
     * parameter contains the value of the ISUP User Service Information
     * parameter. The parameter "bearerCapability" shall be included in the
     * "InitialDP" operation only in the case the ISUP User Service Information
     * parameter is available at the gsmSSF. If User Service Information and
     * User Service Information Prime are available at the gsmSSF, then the
     * "bearerCap" shall contain the value of the User Service Information Prime
     * parameter.
     *
     * @return the bearerCapability
     */
    @Override
    public byte[] getBearerCapability() {
        return bearerCapability;
    }

    /**
     *
     * @param bearerCapability the bearerCapability to set This parameter
     * indicates the type of the bearer capability connection or the
     * transmission medium requirements to the user. It is a network option to
     * select which of the two parameters to be used: - bearerCap: This
     * parameter contains the value of the ISUP User Service Information
     * parameter. The parameter "bearerCapability" shall be included in the
     * "InitialDP" operation only in the case the ISUP User Service Information
     * parameter is available at the gsmSSF. If User Service Information and
     * User Service Information Prime are available at the gsmSSF, then the
     * "bearerCap" shall contain the value of the User Service Information Prime
     * parameter.
     */
    @Override
    public void setBearerCapability(byte[] bearerCapability) {
        this.bearerCapability = bearerCapability;
    }

    /**
     * This parameter indicates the armed BCSM DP event, resulting in the
     * "InitialDP" operation.
     *
     * @return the eventTypeBCSM
     */
    public EventTypeBCSM getEventTypeBCSM() {
        return eventTypeBCSM;
    }

    /**
     * @param eventTypeBCSM the eventTypeBCSM to set This parameter indicates
     * the armed BCSM DP event, resulting in the "InitialDP" operation.
     */
    public void setEventTypeBCSM(EventTypeBCSM eventTypeBCSM) {
        this.eventTypeBCSM = eventTypeBCSM;
    }

    /**
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
     * @param redirectingPartyID the redirectingPartyID to set This parameter
     * indicates the last directory number the call was redirected from.
     */
    @Override
    public void setRedirectingPartyID(RedirectingNumber redirectingPartyID) {
        this.redirectingPartyID = redirectingPartyID;
    }

    /**
     * This parameter contains forwarding related information, such as
     * redirecting counter. See ITU-T Recommendation Q.763 [45] Redirection
     * Information signalling information.
     *
     * @return the redirectionInformation
     */
    @Override
    public RedirectionInformation getRedirectionInformation() {
        return redirectionInformation;
    }

    /**
     * @param redirectionInformation the redirectionInformation to set This
     * parameter contains forwarding related information, such as redirecting
     * counter. See ITU-T Recommendation Q.763 [45] Redirection Information
     * signalling information.
     */
    @Override
    public void setRedirectionInformation(RedirectionInformation redirectionInformation) {
        this.redirectionInformation = redirectionInformation;
    }

    /**
     * This parameter indicates the release cause which triggered the event: For
     * Route_Select_Failure" it shall contain the "FailureCause", if available.
     * For T_Busy it may contain the following parameters, if available. - If
     * the busy event is triggered by an ISUP release message, then the
     * BusyCause shall a copy of the ISUP release cause, for example: Subscriber
     * absent, 20 or User busy, 17. - If the busy event is triggered by a MAP
     * error, for example: Absent subscriber, received from the HLR, then the
     * MAP cause is mapped to the corresponding ISUP release cause. - If the
     * busy event is triggered by call forwarding invocation in the GMSC or
     * VMSC, then the BusyCause shall refer to the type of the call forwarding
     * service in accordance with the mapping table in 3GPP TS 23.078 [7].
     *
     * @return the cause
     */
    public Cause getCause() {
        return cause;
    }

    /**
     * @param cause the cause to set This parameter indicates the release cause
     * which triggered the event: For Route_Select_Failure" it shall contain the
     * "FailureCause", if available. For T_Busy it may contain the following
     * parameters, if available. - If the busy event is triggered by an ISUP
     * release message, then the BusyCause shall a copy of the ISUP release
     * cause, for example: Subscriber absent, 20 or User busy, 17. - If the busy
     * event is triggered by a MAP error, for example: Absent subscriber,
     * received from the HLR, then the MAP cause is mapped to the corresponding
     * ISUP release cause. - If the busy event is triggered by call forwarding
     * invocation in the GMSC or VMSC, then the BusyCause shall refer to the
     * type of the call forwarding service in accordance with the mapping table
     * in 3GPP TS 23.078 [7].
     */
    public void setCause(Cause cause) {
        this.cause = cause;
    }

    /**
     * This parameter contains indicators that are used to resolve interactions
     * between CAMEL based services and network based services.
     *
     * @return the interactionIndicatorTwo
     */
    public ServiceInteractionIndicatorsTwo getInteractionIndicatorTwo() {
        return serviceInteractionIndicatorTwo;
    }

    /**
     * This parameter contains indicators that are used to resolve interactions
     * between CAMEL based services and network based services.
     *
     * @param serviceInteractionIndicatorTwo
     */
    public void setInteractionIndicatorTwo(ServiceInteractionIndicatorsTwo serviceInteractionIndicatorTwo) {
        this.serviceInteractionIndicatorTwo = serviceInteractionIndicatorTwo;
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
     * @return the cugIndex
     */
    public Integer getCugIndex() {
        return cugIndex;
    }

    /**
     * @param cugIndex the cugIndex to set
     */
    public void setCugIndex(Integer cugIndex) {
        this.cugIndex = cugIndex;
    }

    /**
     * @return the cugInterlock
     */
    public byte[] getCugInterlock() {
        return cugInterlock;
    }

    /**
     * @param cugInterlock the cugInterlock to set
     */
    public void setCugInterlock(byte[] cugInterlock) {
        this.cugInterlock = cugInterlock;
    }

    /**
     * @return the cugOutgoingAccess
     */
    public Boolean getCugOutgoingAccess() {
        return cugOutgoingAccess;
    }

    /**
     * @param cugOutgoingAccess the cugOutgoingAccess to set
     */
    public void setCugOutgoingAccess(Boolean cugOutgoingAccess) {
        this.cugOutgoingAccess = cugOutgoingAccess;
    }

    /**
     * This parameter contains the IMSI of the mobile subscriber for which the
     * service is invoked
     *
     * @return the imsi
     */
    @Override
    public IMSI getImsi() {
        return imsi;
    }

    /**
     * @param imsi the imsi to set This parameter contains the IMSI of the
     * mobile subscriber for which the service is invoked
     */
    @Override
    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    /**
     * This parameter indicates the state of the mobile subscriber for which the
     * service is invoked. The possible states are "busy", "idle" and "not
     * reachable".
     *
     * @return the subscriberState
     */
    @Override
    public SubscriberState getSubscriberState() {
        return subscriberState;
    }

    /**
     * @param subscriberState the subscriberState to set This parameter
     * indicates the state of the mobile subscriber for which the service is
     * invoked. The possible states are "busy", "idle" and "not reachable".
     */
    @Override
    public void setSubscriberState(SubscriberState subscriberState) {
        this.subscriberState = subscriberState;
    }

    /**
     * This parameter indicates the location of the MS and the age of the
     * information defining the location.
     *
     * @return the locationInformation
     */
    @Override
    public LocationInformation getLocationInformation() {
        return locationInformation;
    }

    /**
     * @param locationInformation the locationInformation to set This parameter
     * indicates the location of the MS and the age of the information defining
     * the location.
     */
    @Override
    public void setLocationInformation(LocationInformation locationInformation) {
        this.locationInformation = locationInformation;
    }

    /**
     * This parameter indicates the Basic Service Code.
     *
     * @return the extBasicServiceCode
     */
    @Override
    public ExtBasicServiceCode getExtBasicServiceCode() {
        return extBasicServiceCode;
    }

    /**
     * @param extBasicServiceCode the extBasicServiceCode to set This parameter
     * indicates the Basic Service Code.
     */
    @Override
    public void setExtBasicServiceCode(ExtBasicServiceCode extBasicServiceCode) {
        this.extBasicServiceCode = extBasicServiceCode;
    }

    /**
     * This parameter contains the call reference number assigned to the call by
     * the CCF.
     *
     * @return the callReferenceNumber
     */
    @Override
    public CallReferenceNumber getCallReferenceNumber() {
        return callReferenceNumber;
    }

    /**
     * @param callReferenceNumber the callReferenceNumber to set This parameter
     * contains the call reference number assigned to the call by the CCF.
     */
    @Override
    public void setCallReferenceNumber(CallReferenceNumber callReferenceNumber) {
        this.callReferenceNumber = callReferenceNumber;
    }

    /**
     * This parameter contains the mscId assigned to the MSC
     *
     * @return the mscAddress
     */
    @Override
    public ISDNAddressString getMscAddress() {
        return mscAddress;
    }

    /**
     * @param mscAddress the mscAddress to set This parameter contains the mscId
     * assigned to the MSC
     */
    @Override
    public void setMscAddress(ISDNAddressString mscAddress) {
        this.mscAddress = mscAddress;
    }

    /**
     * This parameter contains the number used to identify the called party in
     * the forward direction. It may also include service selection information,
     * including * and # characters.
     *
     * @return the calledPartyBCDNumber
     */
    @Override
    public CalledPartyBCDNumber getCalledPartyBCDNumber() {
        return calledPartyBCDNumber;
    }

    /**
     * @param calledPartyBCDNumber the calledPartyBCDNumber to set This
     * parameter contains the number used to identify the called party in the
     * forward direction. It may also include service selection information,
     * including * and # characters.
     */
    @Override
    public void setCalledPartyBCDNumber(CalledPartyBCDNumber calledPartyBCDNumber) {
        this.calledPartyBCDNumber = calledPartyBCDNumber;
    }

    /**
     * This parameter contains the time that the gsmSSF was triggered, and the
     * time zone that the invoking gsmSSF resides in.
     *
     * @return the timeAndTimezone
     */
    @Override
    public TimeAndTimeZone getEventTime() {
        return timeAndTimezone;
    }

    /**
     * @param timeAndTimezone the timeAndTimezone to set This parameter contains
     * the time that the gsmSSF was triggered, and the time zone that the
     * invoking gsmSSF resides in.
     */
    @Override
    public void setEventTime(TimeAndTimeZone timeAndTimezone) {
        this.timeAndTimezone = timeAndTimezone;
    }

    /**
     * This parameter indicates that a forwarded-to-number was received and that
     * the call will be forwarded due to the Call Forwarding supplementary
     * service in the GMSC or in the VMSC, unless otherwise instructed by the
     * gsmSCF.
     *
     * @return the callForwardingSSPending
     */
    public Boolean getCallForwardingSSPending() {
        return callForwardingSSPending;
    }

    /**
     * @param callForwardingSSPending the callForwardingSSPending to set This
     * parameter indicates that a forwarded-to-number was received and that the
     * call will be forwarded due to the Call Forwarding supplementary service
     * in the GMSC or in the VMSC, unless otherwise instructed by the gsmSCF.
     */
    public void setCallForwardingSSPending(Boolean callForwardingSSPending) {
        this.callForwardingSSPending = callForwardingSSPending;
    }

    /**
     * @return the initialDPArgExtension
     */
    public InitialDpArgExtension getInitialDPArgExtension() {
        return initialDPArgExtension;
    }

    /**
     * @param initialDPArgExtension the initialDPArgExtension to set
     */
    public void setInitialDPArgExtension(InitialDpArgExtension initialDPArgExtension) {
        this.initialDPArgExtension = initialDPArgExtension;
    }

    private void decode_(AsnInputStream ais) throws ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDataException, dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException, IllegalNumberFormatException, ParseException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new IncorrectSyntaxException(String.format("Unexpected TagClass[%s] received. Expecting TagClass[CONTEXT]", ais.getTagClass()));
                }

                switch (tag) {
                    case 0:
                        this.serviceKey = new ServiceKey();
                        this.serviceKey.decode(ais);
                        break;
                    case 2:
                        int length = ais.readLength();
                        byte[] data = new byte[length];
                        ais.read(data);

                        this.calledPartyNumber = new CalledPartyNumber();
                        this.calledPartyNumber.decode((data));
                        break;
                    case 3:
                        length = ais.readLength();
                        data = new byte[length];
                        ais.read(data);
                        this.callingPartyNumber = new CallingPartyNumber();
                        this.callingPartyNumber.decode((data));
                        break;
                    case 5:
                        this.callingPartysCategory = CallingPartysCategory.getInstance((int) ais.readInteger());
                        break;
                    case 7:
                        this.cGEncountered = CGEncountered.getInstance((int) ais.readInteger());
                        break;
                    case 8:
                        length = ais.readLength();
                        data = new byte[length];
                        ais.read(data);

                        this.iPSSPCapabilities = new IPSSPCapabilities();
                        this.iPSSPCapabilities.decode(new ByteArrayInputStream(data));
                        break;
                    case 10:
                        length = ais.readLength();
                        data = new byte[length];
                        ais.read(data);

                        this.locationNumber = new LocationNumber();
                        this.locationNumber.decode(new ByteArrayInputStream(data));
                        break;
                    case 12:
                        length = ais.readLength();
                        data = new byte[length];
                        ais.read(data);

                        this.originalCalledPartyID = new OriginalCalledNumber();
                        this.originalCalledPartyID.decode((data));
                        break;
                    case 15:
                        this.extensions = new byte[ais.readLength()];
                        ais.read(this.extensions);
                        break;
                    case 23:
                        data = new byte[ais.readLength()];
                        ais.read(data);

                        this.highLayerCompability = new HighLayerCompability();
                        this.highLayerCompability.decode(new ByteArrayInputStream(data));
                        break;
                    case 25:
                        data = new byte[ais.readLength()];
                        ais.read(data);

                        this.additionalCallingPartyNumber = new AdditionalCallingPartyNumber();
                        this.additionalCallingPartyNumber.decode((data));
                        break;
                    case 27:
                        this.bearerCapability = new byte[ais.readLength()];
                        ais.read(this.bearerCapability);
                        break;
                    case 28:
                        this.eventTypeBCSM = EventTypeBCSM.fromInt((int) ais.readInteger());
                        break;
                    case 29:
                        data = new byte[ais.readLength()];
                        ais.read(data);

                        this.redirectingPartyID = new RedirectingNumber();
                        this.redirectingPartyID.decode((data));
                        break;
                    case 30:
                        data = new byte[ais.readLength()];
                        ais.read(data);

                        this.redirectionInformation = new RedirectionInformation();
                        this.redirectionInformation.decode((data));
                        break;
                    case 17:
                        data = new byte[ais.readLength()];
                        ais.read(data);

                        this.cause = new Cause();
                        this.cause.decode(new ByteArrayInputStream(data));
                        break;
                    case 32:
                        this.serviceInteractionIndicatorTwo = new ServiceInteractionIndicatorsTwo();
                        this.serviceInteractionIndicatorTwo.decode(ais.readSequenceStream());
                        break;
                    case 37:
                        this.carrier = new byte[ais.readLength()];
                        ais.read(carrier);
                        break;
                    case 45:
                        this.cugIndex = (int) ais.readInteger();
                        break;
                    case 46:
                        this.cugInterlock = new byte[ais.readLength()];
                        ais.read(this.cugInterlock);
                        break;
                    case 47:
                        this.cugOutgoingAccess = true;
                        ais.readNull();
                        break;
                    case 50:
                        this.imsi = new IMSI();
                        this.imsi.decode(ais);
                        break;
                    case 51:
                        this.subscriberState = SubscriberState.create();
                        this.subscriberState.decode(ais);
                        break;
                    case 52:
                        this.locationInformation = new LocationInformation();
                        this.locationInformation.decode(ais);
                        break;
                    case 53:
                        this.extBasicServiceCode = new ExtBasicServiceCode();
                        this.extBasicServiceCode.decode(ais.readSequenceStream());
                        break;
                    case 54:
                        this.callReferenceNumber = new CallReferenceNumber();
                        this.callReferenceNumber.decode(ais);
                        break;
                    case 55:
                        this.mscAddress = new ISDNAddressString();
                        this.mscAddress.decode(ais);
                        break;
                    case 56:
                        this.calledPartyBCDNumber = new CalledPartyBCDNumber();
                        this.calledPartyBCDNumber.decode(ais);
                        break;
                    case 57:
                        data = new byte[ais.readLength()];
                        ais.read(data);
                        this.timeAndTimezone = new TimeAndTimeZone(data);
                        break;
                    case 58:
                        this.callForwardingSSPending = true;
                        ais.readNull();
                        break;
                    case 59:
                        this.initialDPArgExtension = new InitialDpArgExtension();
                        this.initialDPArgExtension.decode(ais.readSequenceStream());
                        break;
                }
            }
        } catch (AsnException | IOException | IllegalNumberFormatException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

}
