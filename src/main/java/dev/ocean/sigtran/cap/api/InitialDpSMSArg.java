/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.api;

import dev.ocean.sigtran.cap.callcontrol.general.EventTypeSMS;
import dev.ocean.sigtran.cap.parameters.CalledPartyBCDNumber;
import dev.ocean.sigtran.cap.parameters.SMSAddressString;
import dev.ocean.sigtran.cap.parameters.ServiceKey;
import dev.ocean.sigtran.cap.parameters.TimeAndTimeZone;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.IMSI;
import dev.ocean.sigtran.map.parameters.ISDNAddressString;
import dev.ocean.sigtran.map.parameters.location.information.LocationInformation;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface InitialDpSMSArg extends CAPMessage{

    public void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException;

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, IllegalNumberFormatException, UnexpectedDataException;

    /**
     * @return the callingPartyNumber
     */
    public SMSAddressString getCallingPartyNumber();

    /**
     * @return the destinationSubscriberNumber
     */
    public CalledPartyBCDNumber getDestinationSubscriberNumber();

    /**
     * @return the extensions
     */
    public byte[] getExtensions();

    /**
     * @return the imsi
     */
    public IMSI getImsi();

    /**
     * @return the locationInformationGPRS
     */
    public byte[] getLocationInformationGPRS();

    /**
     * @return the locationInformationMSC
     */
    public LocationInformation getLocationInformationMSC();

    /**
     * @return the mscAddress
     */
    public ISDNAddressString getMscAddress();

    /**
     * @return the serviceKey
     */
    public ServiceKey getServiceKey();

    /**
     * @return the sgsnNumber
     */
    public ISDNAddressString getSgsnNumber();

    /**
     * @return the smsReferenceNumber
     */
    public byte[] getSmsReferenceNumber();

    /**
     * @return the smscAddress
     */
    public ISDNAddressString getSmscAddress();

    /**
     * @return the timeAndTimeZone
     */
    public TimeAndTimeZone getTimeAndTimeZone();

    /**
     * @return the tPDataCodingScheme
     */
    public byte gettPDataCodingScheme();

    /**
     * @return the tPProtocolIdentifier
     */
    public byte gettPProtocolIdentifier();

    /**
     * @return the tPShortMessageSpecificInfo
     */
    public byte gettPShortMessageSpecificInfo();

    /**
     * @return the tPValidityPeriod
     */
    public byte[] gettPValidityPeriod();

    /**
     * @param callingPartyNumber the callingPartyNumber to set
     */
    public void setCallingPartyNumber(SMSAddressString callingPartyNumber);

    /**
     * @param destinationSubscriberNumber the destinationSubscriberNumber to set
     */
    public void setDestinationSubscriberNumber(CalledPartyBCDNumber destinationSubscriberNumber);

    /**
     * @param extensions the extensions to set
     */
    public void setExtensions(byte[] extensions);

    /**
     * @param imsi the imsi to set
     */
    public void setImsi(IMSI imsi);

    /**
     * @param locationInformationGPRS the locationInformationGPRS to set
     */
    public void setLocationInformationGPRS(byte[] locationInformationGPRS);

    /**
     * @param locationInformationMSC the locationInformationMSC to set
     */
    public void setLocationInformationMSC(LocationInformation locationInformationMSC);

    /**
     * @param mscAddress the mscAddress to set
     */
    public void setMscAddress(ISDNAddressString mscAddress);

    /**
     * @param serviceKey the serviceKey to set
     */
    public void setServiceKey(ServiceKey serviceKey);

    /**
     * @param sgsnNumber the sgsnNumber to set
     */
    public void setSgsnNumber(ISDNAddressString sgsnNumber);

    /**
     * @param smsReferenceNumber the smsReferenceNumber to set
     */
    public void setSmsReferenceNumber(byte[] smsReferenceNumber);

    /**
     * @param smscAddress the smscAddress to set
     */
    public void setSmscAddress(ISDNAddressString smscAddress);

    /**
     * @param timeAndTimeZone the timeAndTimeZone to set
     */
    public void setTimeAndTimeZone(TimeAndTimeZone timeAndTimeZone);

    /**
     * @param tPDataCodingScheme the tPDataCodingScheme to set
     */
    public void settPDataCodingScheme(byte tPDataCodingScheme);

    /**
     * @param tPProtocolIdentifier the tPProtocolIdentifier to set
     */
    public void settPProtocolIdentifier(byte tPProtocolIdentifier);

    /**
     * @param tPShortMessageSpecificInfo the tPShortMessageSpecificInfo to set
     */
    public void settPShortMessageSpecificInfo(byte tPShortMessageSpecificInfo);

    /**
     * @param tPValidityPeriod the tPValidityPeriod to set
     */
    public void settPValidityPeriod(byte[] tPValidityPeriod);

    public EventTypeSMS getEventTypeSMS();
}
