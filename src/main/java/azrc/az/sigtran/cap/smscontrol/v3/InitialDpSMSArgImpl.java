/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.smscontrol.v3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import azrc.az.sigtran.cap.api.InitialDpSMSArg;
import azrc.az.sigtran.cap.callcontrol.general.EventTypeSMS;
import azrc.az.sigtran.cap.parameters.CalledPartyBCDNumber;
import azrc.az.sigtran.cap.parameters.SMSAddressString;
import azrc.az.sigtran.cap.parameters.ServiceKey;
import azrc.az.sigtran.cap.parameters.TimeAndTimeZone;
import azrc.az.sigtran.map.parameters.IMSI;
import azrc.az.sigtran.map.parameters.ISDNAddressString;
import azrc.az.sigtran.map.parameters.location.information.LocationInformation;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;

import java.text.ParseException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class InitialDpSMSArgImpl implements InitialDpSMSArg {

    private ServiceKey serviceKey;
    private CalledPartyBCDNumber destinationSubscriberNumber;
    private SMSAddressString callingPartyNumber;
    private EventTypeSMS eventTypeSMS;
    private IMSI imsi;
    private LocationInformation locationInformationMSC;
    private byte[] locationInformationGPRS;
    private ISDNAddressString smscAddress;
    private TimeAndTimeZone timeAndTimeZone;
    private Byte tPShortMessageSpecificInfo;
    private Byte tPProtocolIdentifier;
    private Byte tPDataCodingScheme;
    private byte[] tPValidityPeriod;
    private byte[] extensions;
    private byte[] smsReferenceNumber;
    private ISDNAddressString mscAddress;
    private ISDNAddressString sgsnNumber;

    public InitialDpSMSArgImpl() {
    }

    public InitialDpSMSArgImpl(ServiceKey serviceKey) {
        this.serviceKey = serviceKey;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, IllegalNumberFormatException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            this.serviceKey.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

            if (destinationSubscriberNumber != null) {
                this.destinationSubscriberNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }

            if (callingPartyNumber != null) {
                this.callingPartyNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }

            if (eventTypeSMS != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 3, eventTypeSMS.value());
            }

            if (imsi != null) {
                imsi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }

            if (locationInformationMSC != null) {
                this.locationInformationMSC.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            }

            if (locationInformationGPRS != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 6, locationInformationGPRS);
            }

            if (smscAddress != null) {
                this.smscAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }

            if (timeAndTimeZone != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                timeAndTimeZone.encode(baos);

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 8);
                aos.writeLength(baos.toByteArray().length);
                aos.write(baos.toByteArray());

//                this.timeAndTimeZone.encode(Tag.CLASS_CONTEXT_SPECIFIC, 8, aos);
            }

            if (tPShortMessageSpecificInfo != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 9, new byte[]{tPShortMessageSpecificInfo});
            }

            if (tPProtocolIdentifier != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 10, new byte[]{tPProtocolIdentifier});
            }

            if (tPDataCodingScheme != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 11, new byte[]{tPDataCodingScheme});
            }

            if (tPValidityPeriod != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 12, tPValidityPeriod);
            }

            if (extensions != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 13, extensions);
            }

            if (smsReferenceNumber != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 14, smsReferenceNumber);
            }

            if (mscAddress != null) {
                this.mscAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 15, aos);
            }

            if (sgsnNumber != null) {
                this.sgsnNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 16, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Unexpected tag received, expecting TagClass[0] Tag[16], found TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
            this.decode_(ais.readSequenceStream());
        } catch (AsnException | IOException |ParseException ex) {
            throw new IncorrectSyntaxException(ex);
        } catch (IllegalNumberFormatException ex) {
            throw new UnexpectedDataException(ex);
        }
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, UnexpectedDataException, IncorrectSyntaxException, IllegalNumberFormatException, ParseException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received, expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.serviceKey = new ServiceKey();
                    this.serviceKey.decode(ais);
                    break;
                case 1:
                    this.destinationSubscriberNumber = new CalledPartyBCDNumber();
                    this.destinationSubscriberNumber.decode(ais);
                    break;
                case 2:
                    this.callingPartyNumber = new SMSAddressString();
                    this.callingPartyNumber.decode(ais);
                    break;
                case 3:
                    this.eventTypeSMS = EventTypeSMS.getInstance((int) ais.readInteger());
                    break;
                case 4:
                    this.imsi = new IMSI();
                    this.imsi.decode(ais);
                    break;
                case 5:
                    this.locationInformationMSC = new LocationInformation();
                    this.locationInformationMSC.decode(ais);
                    break;
                case 6:
                    this.locationInformationGPRS = new byte[ais.readLength()];
                    ais.read(locationInformationGPRS);
                    break;
                case 7:
                    this.smscAddress = new ISDNAddressString();
                    this.smscAddress.decode(ais);
                    break;
                case 8:
                    this.timeAndTimeZone = new TimeAndTimeZone(ais);
                    break;
                case 9:
                    this.tPShortMessageSpecificInfo = ais.readOctetString()[0];
                    break;
                case 10:
                    this.tPProtocolIdentifier = ais.readOctetString()[0];
                    break;
                case 11:
                    this.tPDataCodingScheme = ais.readOctetString()[0];
                    break;
                case 12:
                    this.tPValidityPeriod = ais.readOctetString();
                    break;
                case 13:
                    this.extensions = ais.readSequence();
                    break;
                case 14:
                    this.smsReferenceNumber = ais.readOctetString();
                    break;
                case 15:
                    this.mscAddress = new ISDNAddressString();
                    this.mscAddress.decode(ais);
                    break;
                case 16:
                    this.sgsnNumber = new ISDNAddressString();
                    this.sgsnNumber.decode(ais);
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received, TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
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
     * @return the destinationSubscriberNumber
     */
    @Override
    public CalledPartyBCDNumber getDestinationSubscriberNumber() {
        return destinationSubscriberNumber;
    }

    /**
     * @param destinationSubscriberNumber the destinationSubscriberNumber to set
     */
    @Override
    public void setDestinationSubscriberNumber(CalledPartyBCDNumber destinationSubscriberNumber) {
        this.destinationSubscriberNumber = destinationSubscriberNumber;
    }

    /**
     * @return the callingPartyNumber
     */
    @Override
    public SMSAddressString getCallingPartyNumber() {
        return callingPartyNumber;
    }

    /**
     * @param callingPartyNumber the callingPartyNumber to set
     */
    @Override
    public void setCallingPartyNumber(SMSAddressString callingPartyNumber) {
        this.callingPartyNumber = callingPartyNumber;
    }

    /**
     * @return the eventTypeSMS
     */
    public EventTypeSMS getEventTypeSMS() {
        return eventTypeSMS;
    }

    /**
     * @param eventTypeSMS the eventTypeSMS to set
     */
    public void setEventTypeSMS(EventTypeSMS eventTypeSMS) {
        this.eventTypeSMS = eventTypeSMS;
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
     * @return the locationInformationMSC
     */
    @Override
    public LocationInformation getLocationInformationMSC() {
        return locationInformationMSC;
    }

    /**
     * @param locationInformationMSC the locationInformationMSC to set
     */
    @Override
    public void setLocationInformationMSC(LocationInformation locationInformationMSC) {
        this.locationInformationMSC = locationInformationMSC;
    }

    /**
     * @return the locationInformationGPRS
     */
    @Override
    public byte[] getLocationInformationGPRS() {
        return locationInformationGPRS;
    }

    /**
     * @param locationInformationGPRS the locationInformationGPRS to set
     */
    @Override
    public void setLocationInformationGPRS(byte[] locationInformationGPRS) {
        this.locationInformationGPRS = locationInformationGPRS;
    }

    /**
     * @return the smscAddress
     */
    @Override
    public ISDNAddressString getSmscAddress() {
        return smscAddress;
    }

    /**
     * @param smscAddress the smscAddress to set
     */
    @Override
    public void setSmscAddress(ISDNAddressString smscAddress) {
        this.smscAddress = smscAddress;
    }

    /**
     * @return the timeAndTimeZone
     */
    @Override
    public TimeAndTimeZone getTimeAndTimeZone() {
        return timeAndTimeZone;
    }

    /**
     * @param timeAndTimeZone the timeAndTimeZone to set
     */
    @Override
    public void setTimeAndTimeZone(TimeAndTimeZone timeAndTimeZone) {
        this.timeAndTimeZone = timeAndTimeZone;
    }

    /**
     * @return the tPShortMessageSpecificInfo
     */
    @Override
    public byte gettPShortMessageSpecificInfo() {
        return tPShortMessageSpecificInfo;
    }

    /**
     * @param tPShortMessageSpecificInfo the tPShortMessageSpecificInfo to set
     */
    @Override
    public void settPShortMessageSpecificInfo(byte tPShortMessageSpecificInfo) {
        this.tPShortMessageSpecificInfo = tPShortMessageSpecificInfo;
    }

    /**
     * @return the tPProtocolIdentifier
     */
    @Override
    public byte gettPProtocolIdentifier() {
        return tPProtocolIdentifier;
    }

    /**
     * @param tPProtocolIdentifier the tPProtocolIdentifier to set
     */
    @Override
    public void settPProtocolIdentifier(byte tPProtocolIdentifier) {
        this.tPProtocolIdentifier = tPProtocolIdentifier;
    }

    /**
     * @return the tPDataCodingScheme
     */
    @Override
    public byte gettPDataCodingScheme() {
        return tPDataCodingScheme;
    }

    /**
     * @param tPDataCodingScheme the tPDataCodingScheme to set
     */
    @Override
    public void settPDataCodingScheme(byte tPDataCodingScheme) {
        this.tPDataCodingScheme = tPDataCodingScheme;
    }

    /**
     * @return the tPValidityPeriod
     */
    @Override
    public byte[] gettPValidityPeriod() {
        return tPValidityPeriod;
    }

    /**
     * @param tPValidityPeriod the tPValidityPeriod to set
     */
    @Override
    public void settPValidityPeriod(byte[] tPValidityPeriod) {
        this.tPValidityPeriod = tPValidityPeriod;
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
     * @return the smsReferenceNumber
     */
    @Override
    public byte[] getSmsReferenceNumber() {
        return smsReferenceNumber;
    }

    /**
     * @param smsReferenceNumber the smsReferenceNumber to set
     */
    @Override
    public void setSmsReferenceNumber(byte[] smsReferenceNumber) {
        this.smsReferenceNumber = smsReferenceNumber;
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
     * @return the sgsnNumber
     */
    @Override
    public ISDNAddressString getSgsnNumber() {
        return sgsnNumber;
    }

    /**
     * @param sgsnNumber the sgsnNumber to set
     */
    @Override
    public void setSgsnNumber(ISDNAddressString sgsnNumber) {
        this.sgsnNumber = sgsnNumber;
    }
}
