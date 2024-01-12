/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v1;

import azrc.az.isup.enums.CallingPartysCategory;
import azrc.az.isup.parameters.*;
import azrc.az.sigtran.cap.api.InitialDpArg;
import azrc.az.sigtran.cap.callcontrol.general.EventTypeBCSM;
import azrc.az.sigtran.cap.parameters.AdditionalCallingPartyNumber;
import azrc.az.sigtran.cap.parameters.CalledPartyBCDNumber;
import azrc.az.sigtran.cap.parameters.ServiceKey;
import azrc.az.sigtran.cap.parameters.TimeAndTimeZone;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.parameters.*;
import azrc.az.sigtran.map.parameters.location.information.LocationInformation;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * InitialDPArg ::= SEQUENCE { serviceKey [0] ServiceKey, calledPartyNumber [2]
 * CalledPartyNumber OPTIONAL, callingPartyNumber [3] CallingPartyNumber
 * OPTIONAL, callingPartysCategory [5] CallingPartysCategory OPTIONAL,
 * locationNumber [10] LocationNumber OPTIONAL, originalCalledPartyID [12]
 * OriginalCalledPartyID OPTIONAL, extensions [15] SEQUENCE
 * SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL, highLayerCompatibility
 * [23] HighLayerCompatibility OPTIONAL, additionalCallingPartyNumber [25]
 * AdditionalCallingPartyNumber OPTIONAL, bearerCapability [27] BearerCapability
 * OPTIONAL, eventTypeBCSM [28] EventTypeBCSM OPTIONAL, redirectingPartyID [29]
 * RedirectingPartyID OPTIONAL, redirectionInformation [30]
 * RedirectionInformation OPTIONAL, iMSI [50] IMSI OPTIONAL, subscriberState
 * [51] SubscriberState OPTIONAL, locationInformation [52] LocationInformation
 * OPTIONAL, ext-basicServiceCode [53] Ext-BasicServiceCode OPTIONAL,
 * callReferenceNumber [54] CallReferenceNumber OPTIONAL, mscAddress [55]
 * ISDN-AddressString OPTIONAL, calledPartyBCDNumber [56] CalledPartyBCDNumber
 * OPTIONAL ... }
 *
 * @author eatakishiyev
 */
public class InitialDpArgImpl implements InitialDpArg {

    private final static Logger logger = LoggerFactory.getLogger(InitialDpArgImpl.class);

    private ServiceKey serviceKey;
    private CalledPartyNumber calledPartyNumber;
    private CallingPartyNumber callingPartyNumber;
    private CallingPartysCategory callingPartysCategory;
    private LocationNumber locationNumber;
    private OriginalCalledNumber originalCalledPartyID;
    private byte[] extensions;
    private HighLayerCompability highLayerCompability;
    private AdditionalCallingPartyNumber additionalCallingPartyNumber;
    private byte[] bearerCapability;
    private EventTypeBCSM eventTypeBCSM;
    private RedirectingNumber redirectingPartyID;
    private RedirectionInformation redirectionInformation;
    private IMSI imsi;
    private SubscriberState subscriberState;
    private LocationInformation locationInformation;
    private ExtBasicServiceCode extBasicServiceCode;
    private CallReferenceNumber callReferenceNumber;
    private ISDNAddressString mscAddress;
    private CalledPartyBCDNumber calledPartyBCDNumber;

    public InitialDpArgImpl() {
    }

    public InitialDpArgImpl(ServiceKey serviceKey) {
        this.serviceKey = serviceKey;
    }

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

            aos.FinalizeContent(lenPos);
        } catch (Exception ex) {
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
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }

    }

    private void decode_(AsnInputStream ais) throws ParameterOutOfRangeException, IncorrectSyntaxException, UnexpectedDataException, IncorrectSyntaxException, IllegalNumberFormatException {
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
                        this.eventTypeBCSM = (EventTypeBCSM) EventTypeBCSM.fromInt((int) ais.readInteger());
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
                    default:
                        logger.error("error: Unexpected tag received: " + tag);
                        throw new UnexpectedDataException();
                }
            }
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the serviceKey
     */
    @Override
    public ServiceKey getServiceKey() {
        return serviceKey;
    }

    /**
     * @param serviceKey the serviceKey to set
     */
    @Override
    public void setServiceKey(ServiceKey serviceKey) {
        this.serviceKey = serviceKey;
    }

    /**
     * @return the calledPartyNumber
     */
    @Override
    public CalledPartyNumber getCalledPartyNumber() {
        return calledPartyNumber;
    }

    /**
     * @param calledPartyNumber the calledPartyNumber to set
     */
    @Override
    public void setCalledPartyNumber(CalledPartyNumber calledPartyNumber) {
        this.calledPartyNumber = calledPartyNumber;
    }

    /**
     * @return the callingPartyNumber
     */
    @Override
    public CallingPartyNumber getCallingPartyNumber() {
        return callingPartyNumber;
    }

    /**
     * @param callingPartyNumber the callingPartyNumber to set
     */
    @Override
    public void setCallingPartyNumber(CallingPartyNumber callingPartyNumber) {
        this.callingPartyNumber = callingPartyNumber;
    }

    /**
     * @return the callingPartysCategory
     */
    @Override
    public CallingPartysCategory getCallingPartysCategory() {
        return callingPartysCategory;
    }

    /**
     * @param callingPartysCategory the callingPartysCategory to set
     */
    @Override
    public void setCallingPartysCategory(CallingPartysCategory callingPartysCategory) {
        this.callingPartysCategory = callingPartysCategory;
    }

    /**
     * @return the locationNumber
     */
    @Override
    public LocationNumber getLocationNumber() {
        return locationNumber;
    }

    /**
     * @param locationNumber the locationNumber to set
     */
    @Override
    public void setLocationNumber(LocationNumber locationNumber) {
        this.locationNumber = locationNumber;
    }

    /**
     * @return the originalCalledPartyID
     */
    @Override
    public OriginalCalledNumber getOriginalCalledPartyID() {
        return originalCalledPartyID;
    }

    /**
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
     * @return the highLayerCompability
     */
    @Override
    public HighLayerCompability getHighLayerCompability() {
        return highLayerCompability;
    }

    /**
     * @param highLayerCompability the highLayerCompability to set
     */
    @Override
    public void setHighLayerCompability(HighLayerCompability highLayerCompability) {
        this.highLayerCompability = highLayerCompability;
    }

    /**
     * @return the additionalCallingPartyNumber
     */
    @Override
    public AdditionalCallingPartyNumber getAdditionalCallingPartyNumber() {
        return additionalCallingPartyNumber;
    }

    /**
     * @param additionalCallingPartyNumber the additionalCallingPartyNumber to
     * set
     */
    @Override
    public void setAdditionalCallingPartyNumber(AdditionalCallingPartyNumber additionalCallingPartyNumber) {
        this.additionalCallingPartyNumber = additionalCallingPartyNumber;
    }

    /**
     * @return the bearerCapability
     */
    @Override
    public byte[] getBearerCapability() {
        return bearerCapability;
    }

    /**
     * @param bearerCapability the bearerCapability to set
     */
    @Override
    public void setBearerCapability(byte[] bearerCapability) {
        this.bearerCapability = bearerCapability;
    }

    /**
     * @return the eventTypeBCSM
     */
    @Override
    public EventTypeBCSM getEventTypeBCSM() {
        return eventTypeBCSM;
    }

    /**
     * @param eventTypeBCSM the eventTypeBCSM to set
     */
    public void setEventTypeBCSM(EventTypeBCSM eventTypeBCSM) {
        this.eventTypeBCSM = eventTypeBCSM;
    }

    /**
     * @return the redirectingPartyID
     */
    @Override
    public RedirectingNumber getRedirectingPartyID() {
        return redirectingPartyID;
    }

    /**
     * @param redirectingPartyID the redirectingPartyID to set
     */
    @Override
    public void setRedirectingPartyID(RedirectingNumber redirectingPartyID) {
        this.redirectingPartyID = redirectingPartyID;
    }

    /**
     * @return the redirectionInformation
     */
    @Override
    public RedirectionInformation getRedirectionInformation() {
        return redirectionInformation;
    }

    /**
     * @param redirectionInformation the redirectionInformation to set
     */
    @Override
    public void setRedirectionInformation(RedirectionInformation redirectionInformation) {
        this.redirectionInformation = redirectionInformation;
    }

    /**
     * @return the imsi
     */
    @Override
    public IMSI getImsi() {
        return imsi;
    }

    /**
     * @param imsi the imsi to set
     */
    @Override
    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    /**
     * @return the subscriberState
     */
    @Override
    public SubscriberState getSubscriberState() {
        return subscriberState;
    }

    /**
     * @param subscriberState the subscriberState to set
     */
    @Override
    public void setSubscriberState(SubscriberState subscriberState) {
        this.subscriberState = subscriberState;
    }

    /**
     * @return the locationInformation
     */
    @Override
    public LocationInformation getLocationInformation() {
        return locationInformation;
    }

    /**
     * @param locationInformation the locationInformation to set
     */
    @Override
    public void setLocationInformation(LocationInformation locationInformation) {
        this.locationInformation = locationInformation;
    }

    /**
     * @return the extBasicServiceCode
     */
    @Override
    public ExtBasicServiceCode getExtBasicServiceCode() {
        return extBasicServiceCode;
    }

    /**
     * @param extBasicServiceCode the extBasicServiceCode to set
     */
    @Override
    public void setExtBasicServiceCode(ExtBasicServiceCode extBasicServiceCode) {
        this.extBasicServiceCode = extBasicServiceCode;
    }

    /**
     * @return the callReferenceNumber
     */
    @Override
    public CallReferenceNumber getCallReferenceNumber() {
        return callReferenceNumber;
    }

    /**
     * @param callReferenceNumber the callReferenceNumber to set
     */
    @Override
    public void setCallReferenceNumber(CallReferenceNumber callReferenceNumber) {
        this.callReferenceNumber = callReferenceNumber;
    }

    /**
     * @return the mscAddress
     */
    @Override
    public ISDNAddressString getMscAddress() {
        return mscAddress;
    }

    /**
     * @param mscAddress the mscAddress to set
     */
    @Override
    public void setMscAddress(ISDNAddressString mscAddress) {
        this.mscAddress = mscAddress;
    }

    /**
     * @return the calledPartyBCDNumber
     */
    @Override
    public CalledPartyBCDNumber getCalledPartyBCDNumber() {
        return calledPartyBCDNumber;
    }

    /**
     * @param calledPartyBCDNumber the calledPartyBCDNumber to set
     */
    @Override
    public void setCalledPartyBCDNumber(CalledPartyBCDNumber calledPartyBCDNumber) {
        this.calledPartyBCDNumber = calledPartyBCDNumber;
    }

    @Override
    public TimeAndTimeZone getEventTime() {
        return null;
    }

    @Override
    public void setEventTime(TimeAndTimeZone eventTime) {
        //Dummy method
    }

}
