/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.ericsson.mobility.management.IN;

import dev.ocean.sigtran.cap.parameters.ServiceKey;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.services.common.MAPArgument;
import dev.ocean.sigtran.map.parameters.Category;
import dev.ocean.sigtran.map.parameters.IMSI;
import dev.ocean.sigtran.map.parameters.ISDNAddressString;
import dev.ocean.sigtran.map.parameters.LocationUpdateType;
import dev.ocean.sigtran.map.parameters.MmDetectionPoint;
import dev.ocean.sigtran.map.parameters.location.information.LocationInformation;
import java.io.IOException;
import org.apache.logging.log4j.*;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 *
 * PARAMETER SEQUENCE
 * serviceKey ServiceKey,
 * mmDetectionPoint MmDetectionPoint,
 * originatingEntityNumber ISDN-AddressString,
 * imsi IMSI,
 * mainMSISDN ISDN-AddressString,
 * classmarkInfo [0] ClassmarkInfo OPTIONAL,
 * locationInformation [1] LocationInformation OPTIONAL,
 * callingPartyCategory [2] Category OPTIONAL,
 * locationUpdateType [3] LocationUpdateType OPTIONAL,
 * subscriptionType [4] SubscriptionType OPTIONAL
 */
public class MobilityMngtINTriggeringArg implements MAPArgument {

    private static final Logger logger = LogManager.getLogger(MobilityMngtINTriggeringArg.class);
    private ServiceKey serviceKey;
    private MmDetectionPoint mmDetectionPoint;
    private ISDNAddressString originatingEntityNumber;
    private IMSI imsi;
    private ISDNAddressString mainMsisdn;
    private byte[] classMarkInfo;
    private LocationInformation locationInformation;
    private Category callingPartyCategory;
    private LocationUpdateType locationUpdateType;
    private byte[] subscriptionType;
    private byte[] rawData;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ServiceKey = ").append(serviceKey.getServiceKey()).append("\n")
                .append(";MmDetectionPoint = ").append(mmDetectionPoint).append("\n")
                .append(";OriginatingEntityNumber = ").append(originatingEntityNumber).append("\n")
                .append(";IMSI = ").append(imsi).append("\n")
                .append(";MainMsisdn = ").append(mainMsisdn).append("\n")
                .append(";LocationInformation = ").append(locationInformation).append("\n")
                .append(";CallingPartyCategory = ").append(callingPartyCategory).append("\n")
                .append(";LocationUpdateType = ").append(locationUpdateType);
        return sb.toString();
    }

    public MobilityMngtINTriggeringArg() {
    }

    public MobilityMngtINTriggeringArg(byte[] rawData) throws IncorrectSyntaxException,
            UnexpectedDataException {
        this.rawData = rawData;
        this.decode(rawData);
    }

    public MobilityMngtINTriggeringArg(ServiceKey serviceKey, MmDetectionPoint mmDetectionPoint,
            ISDNAddressString originatingEntityNumber, IMSI imsi, ISDNAddressString mainMsisdn) {
        this.serviceKey = serviceKey;
        this.mmDetectionPoint = mmDetectionPoint;
        this.originatingEntityNumber = originatingEntityNumber;
        this.imsi = imsi;
        this.mainMsisdn = mainMsisdn;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException, IllegalNumberFormatException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();
            serviceKey.encode(aos);
            aos.writeInteger(mmDetectionPoint.value());
            originatingEntityNumber.encode(aos);
            imsi.encode(aos);
            mainMsisdn.encode(aos);
            if (classMarkInfo != null && classMarkInfo.length > 0) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 0, classMarkInfo);
            }
            if (locationInformation != null) {
                locationInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
            if (callingPartyCategory != null) {
                callingPartyCategory.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            if (locationUpdateType != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 3, locationUpdateType.value());
            }
            if (subscriptionType != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 4, subscriptionType);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.rawData = data;
        try {
            AsnInputStream ais = new AsnInputStream(data);
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSLA], "
                        + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this._decode(ais.readSequenceStream());
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, AsnException,
            UnexpectedDataException, IOException, IllegalNumberFormatException {
        if (ais.getTag() != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSLA], "
                    + "found Tag[%s] TagClass[%s]", ais.getTag(), ais.getTagClass()));
        }

        this._decode(ais);
    }

    private void _decode(AsnInputStream ais) throws IOException, AsnException, IncorrectSyntaxException, UnexpectedDataException, IllegalNumberFormatException {
        int tag = ais.readTag();
        if (tag == Tag.INTEGER && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.serviceKey = new ServiceKey();
            serviceKey.decode(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[INTEGER] TagClass[UNIVERSAL],"
                    + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
        }

        tag = ais.readTag();
        if (tag == Tag.INTEGER && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.mmDetectionPoint = MmDetectionPoint.getInstance((int) ais.readInteger());
        } else {
            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[INTEGER] TagClass[UNIVERSAL],"
                    + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
        }

        tag = ais.readTag();
        if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.originatingEntityNumber = new ISDNAddressString(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[STRING_OCTET] TagClass[UNIVERSAL],"
                    + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
        }

        tag = ais.readTag();
        if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.imsi = new IMSI(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[STRING_OCTET] TagClass[UNIVERSAL],"
                    + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
        }

        tag = ais.readTag();
        if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.mainMsisdn = new ISDNAddressString(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[STRING_OCTET] TagClass[UNIVERSAL],"
                    + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
        }

        while (ais.available() > 0) {
            tag = ais.readTag();
            if (!(ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC)) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting TagClass[CONTEXT],"
                        + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.classMarkInfo = ais.readOctetString();
                    break;
                case 1:
                    this.locationInformation = new LocationInformation();
                    locationInformation.decode(ais);
                    break;
                case 2:
                    this.callingPartyCategory = new Category();
                    callingPartyCategory.decode(ais);
                    break;
                case 3:
                    this.locationUpdateType = LocationUpdateType.getInstance((int) ais.readInteger());
                    break;
                case 4:
                    this.subscriptionType = ais.readOctetString();
                    break;
            }
        }
    }

    @Override
    public byte[] getRequestData() {
        if (rawData == null) {
            AsnOutputStream aos = new AsnOutputStream();
            try {
                this.encode(aos);
                rawData = aos.toByteArray();
            } catch (Exception ex) {
                logger.error("ErrorOccured: ", ex);
            }
        }
        return rawData;
    }

    @Override
    public boolean isRequestCorrupted() {
        return false;
    }

    public Category getCallingPartyCategory() {
        return callingPartyCategory;
    }

    public void setCallingPartyCategory(Category callingPartyCategory) {
        this.callingPartyCategory = callingPartyCategory;
    }

    public byte[] getClassMarkInfo() {
        return classMarkInfo;
    }

    public void setClassMarkInfo(byte[] classMarkInfo) {
        this.classMarkInfo = classMarkInfo;
    }

    public IMSI getImsi() {
        return imsi;
    }

    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    public LocationInformation getLocationInformation() {
        return locationInformation;
    }

    public void setLocationInformation(LocationInformation locationInformation) {
        this.locationInformation = locationInformation;
    }

    public LocationUpdateType getLocationUpdateType() {
        return locationUpdateType;
    }

    public void setLocationUpdateType(LocationUpdateType locationUpdateType) {
        this.locationUpdateType = locationUpdateType;
    }

    public ISDNAddressString getMainMsisdn() {
        return mainMsisdn;
    }

    public void setMainMsisdn(ISDNAddressString mainMsisdn) {
        this.mainMsisdn = mainMsisdn;
    }

    public MmDetectionPoint getMmDetectionPoint() {
        return mmDetectionPoint;
    }

    public void setMmDetectionPoint(MmDetectionPoint mmDetectionPoint) {
        this.mmDetectionPoint = mmDetectionPoint;
    }

    public ISDNAddressString getOriginatingEntityNumber() {
        return originatingEntityNumber;
    }

    public void setOriginatingEntityNumber(ISDNAddressString originatingEntityNumber) {
        this.originatingEntityNumber = originatingEntityNumber;
    }

    public void setServiceKey(ServiceKey serviceKey) {
        this.serviceKey = serviceKey;
    }

    public ServiceKey getServiceKey() {
        return serviceKey;
    }

    public void setSubscriptionType(byte[] subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public byte[] getSubscriptionType() {
        return subscriptionType;
    }

}
