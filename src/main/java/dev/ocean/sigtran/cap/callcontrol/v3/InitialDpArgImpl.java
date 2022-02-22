/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v3;

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
                baos.write(calledPartyNumber.encode());

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

    @Override
    public AdditionalCallingPartyNumber getAdditionalCallingPartyNumber() {
        return additionalCallingPartyNumber;
    }

    @Override
    public byte[] getBearerCapability() {
        return bearerCapability;
    }

    @Override
    public CallReferenceNumber getCallReferenceNumber() {
        return callReferenceNumber;
    }

    @Override
    public CalledPartyBCDNumber getCalledPartyBCDNumber() {
        return calledPartyBCDNumber;
    }

    @Override
    public CalledPartyNumber getCalledPartyNumber() {
        return calledPartyNumber;
    }

    @Override
    public CallingPartyNumber getCallingPartyNumber() {
        return callingPartyNumber;
    }

    @Override
    public CallingPartysCategory getCallingPartysCategory() {
        return callingPartysCategory;
    }

    @Override
    public ExtBasicServiceCode getExtBasicServiceCode() {
        return extBasicServiceCode;
    }

    @Override
    public byte[] getExtensions() {
        return extensions;
    }

    @Override
    public HighLayerCompability getHighLayerCompability() {
        return highLayerCompability;
    }

    @Override
    public IMSI getImsi() {
        return imsi;
    }

    @Override
    public LocationInformation getLocationInformation() {
        return locationInformation;
    }

    @Override
    public LocationNumber getLocationNumber() {
        return locationNumber;
    }

    @Override
    public ISDNAddressString getMscAddress() {
        return mscAddress;
    }

    @Override
    public OriginalCalledNumber getOriginalCalledPartyID() {
        return originalCalledPartyID;
    }

    @Override
    public RedirectingNumber getRedirectingPartyID() {
        return redirectingPartyID;
    }

    @Override
    public RedirectionInformation getRedirectionInformation() {
        return redirectionInformation;
    }

    @Override
    public ServiceKey getServiceKey() {
        return serviceKey;
    }

    @Override
    public SubscriberState getSubscriberState() {
        return subscriberState;
    }

    @Override
    public void setAdditionalCallingPartyNumber(AdditionalCallingPartyNumber additionalCallingPartyNumber) {
        this.additionalCallingPartyNumber = additionalCallingPartyNumber;
    }

    @Override
    public void setBearerCapability(byte[] bearerCapability) {
        this.bearerCapability = bearerCapability;
    }

    @Override
    public void setCallReferenceNumber(CallReferenceNumber callReferenceNumber) {
        this.callReferenceNumber = callReferenceNumber;
    }

    @Override
    public void setCalledPartyBCDNumber(CalledPartyBCDNumber calledPartyBCDNumber) {
        this.calledPartyBCDNumber = calledPartyBCDNumber;
    }

    @Override
    public void setCalledPartyNumber(CalledPartyNumber calledPartyNumber) {
        this.calledPartyNumber = calledPartyNumber;
    }

    @Override
    public void setCallingPartyNumber(CallingPartyNumber callingPartyNumber) {
        this.callingPartyNumber = callingPartyNumber;
    }

    @Override
    public void setCallingPartysCategory(CallingPartysCategory callingPartysCategory) {
        this.callingPartysCategory = callingPartysCategory;
    }

    @Override
    public void setExtBasicServiceCode(ExtBasicServiceCode extBasicServiceCode) {
        this.extBasicServiceCode = extBasicServiceCode;
    }

    @Override
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

    @Override
    public void setHighLayerCompability(HighLayerCompability highLayerCompability) {
        this.highLayerCompability = highLayerCompability;
    }

    @Override
    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    @Override
    public void setLocationInformation(LocationInformation locationInformation) {
        this.locationInformation = locationInformation;
    }

    @Override
    public void setLocationNumber(LocationNumber locationNumber) {
        this.locationNumber = locationNumber;
    }

    @Override
    public void setMscAddress(ISDNAddressString mscAddress) {
        this.mscAddress = mscAddress;
    }

    @Override
    public void setOriginalCalledPartyID(OriginalCalledNumber originalCalledPartyID) {
        this.originalCalledPartyID = originalCalledPartyID;
    }

    @Override
    public void setRedirectingPartyID(RedirectingNumber redirectingPartyID) {
        this.redirectingPartyID = redirectingPartyID;
    }

    @Override
    public void setRedirectionInformation(RedirectionInformation redirectionInformation) {
        this.redirectionInformation = redirectionInformation;
    }

    @Override
    public void setServiceKey(ServiceKey serviceKey) {
        this.serviceKey = serviceKey;
    }

    @Override
    public void setSubscriberState(SubscriberState subscriberState) {
        this.subscriberState = subscriberState;
    }

    public InitialDpArgExtension getInitialDPArgExtension() {
        return initialDPArgExtension;
    }

    public void setInitialDPArgExtension(InitialDpArgExtension initialDPArgExtension) {
        this.initialDPArgExtension = initialDPArgExtension;
    }

    /**
     *
     * @return
     */
    @Override
    public EventTypeBCSM getEventTypeBCSM() {
        return eventTypeBCSM;
    }

    @Override
    public TimeAndTimeZone getEventTime() {
        return this.timeAndTimezone;
    }

    @Override
    public void setEventTime(TimeAndTimeZone eventTime) {
        this.timeAndTimezone = eventTime;
    }

}
