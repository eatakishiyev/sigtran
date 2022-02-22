/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.api;

import dev.ocean.sigtran.cap.callcontrol.general.EventTypeBCSM;
import dev.ocean.sigtran.cap.parameters.AdditionalCallingPartyNumber;
import dev.ocean.sigtran.cap.parameters.CalledPartyBCDNumber;
import dev.ocean.isup.enums.CallingPartysCategory;
import dev.ocean.isup.parameters.CalledPartyNumber;
import dev.ocean.isup.parameters.CallingPartyNumber;
import dev.ocean.isup.parameters.LocationNumber;
import dev.ocean.isup.parameters.OriginalCalledNumber;
import dev.ocean.isup.parameters.RedirectingNumber;
import dev.ocean.isup.parameters.RedirectionInformation;
import dev.ocean.sigtran.cap.parameters.ServiceKey;
import dev.ocean.isup.parameters.HighLayerCompability;
import dev.ocean.sigtran.cap.parameters.TimeAndTimeZone;
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
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface InitialDpArg extends CAPMessage {

    public EventTypeBCSM getEventTypeBCSM();

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, ParameterOutOfRangeException, UnexpectedDataException, IncorrectSyntaxException;

    public void encode(AsnOutputStream aos) throws ParameterOutOfRangeException, IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDataException;

    /**
     * The calling party number provided by the access signalling system of the
     * calling user, e.g. provided by a PBX.
     *
     * @return the additionalCallingPartyNumber
     */
    public AdditionalCallingPartyNumber getAdditionalCallingPartyNumber();

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
    public byte[] getBearerCapability();

    /**
     * This parameter contains the call reference number assigned to the call by
     * the CCF.
     *
     * @return the callReferenceNumber
     */
    public CallReferenceNumber getCallReferenceNumber();

    /**
     * This parameter contains the number used to identify the called party in
     * the forward direction. It may also include service selection information,
     * including * and # characters.
     *
     * @return the calledPartyBCDNumber
     */
    public CalledPartyBCDNumber getCalledPartyBCDNumber();

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
    public CalledPartyNumber getCalledPartyNumber();

    /**
     * This parameter carries the calling party number to identify the calling
     * party or the origin of the call. See ETSI EN 300 356-1 [23] Calling Party
     * Number signalling information.
     *
     * @return the callingPartyNumber
     */
    public CallingPartyNumber getCallingPartyNumber();

    /**
     * Indicates the type of calling party (e.g. operator, pay phone, ordinary
     * subscriber). See ETSI EN 300 356-1 [23] Calling Party Category signalling
     * information.
     *
     * @return the callingPartysCategory
     */
    public CallingPartysCategory getCallingPartysCategory();

//
    /**
     * This parameter indicates the Basic Service Code.
     *
     * @return the extBasicServiceCode
     */
    public ExtBasicServiceCode getExtBasicServiceCode();

    /**
     * @return the extensions
     */
    public byte[] getExtensions();

    /**
     * This parameter indicates the type of the high layer compatibility, which
     * will be used to determine the ISDN -teleservice of a connected ISDN
     * terminal. The highlayerCompatibility can also be transported by ISUP
     * (e.g. within the ATP (see ITU-T Recommendation Q.763 [45]) parameter).
     *
     * @return the highLayerCompability
     */
    public HighLayerCompability getHighLayerCompability();

    /**
     * This parameter contains the IMSI of the mobile subscriber for which the
     * service is invoked
     *
     * @return the imsi
     */
    public IMSI getImsi();

    /**
     * This parameter indicates the location of the MS and the age of the
     * information defining the location.
     *
     * @return the locationInformation
     */
    public LocationInformation getLocationInformation();

    /**
     * This parameter is used to convey the geographical area address for
     * mobility services, see ITU-T Recommendation Q.762 [44]. It is used when
     * "callingPartyNumber" does not contain any information about the
     * geographical location of the calling party (e.g., origin dependent
     * routeing when the calling party is a mobile subscriber).
     *
     * @return the locationNumber
     */
    public LocationNumber getLocationNumber();

    /**
     * This parameter contains the mscId assigned to the MSC
     *
     * @return the mscAddress
     */
    public ISDNAddressString getMscAddress();

    /**
     * If the call has met call forwarding on the route to the gsmSSF, then this
     * parameter carries the dialled digits. Refer to EN 300 356-1[23] Original
     * Called Number signalling information.
     *
     * @return the originalCalledPartyID
     */
    public OriginalCalledNumber getOriginalCalledPartyID();

    /**
     * This parameter indicates the last directory number the call was
     * redirected from.
     *
     * @return the redirectingPartyID
     */
    public RedirectingNumber getRedirectingPartyID();

    /**
     * This parameter contains forwarding related information, such as
     * redirecting counter. See ITU-T Recommendation Q.763 [45] Redirection
     * Information signalling information.
     *
     * @return the redirectionInformation
     */
    public RedirectionInformation getRedirectionInformation();

    /**
     * This parameter indicates to the gsmSCF the requested IN service. It is
     * used to address the required application/SLP within the gsmSCF; this
     * parameter is not for SCP addressing
     *
     * @return the serviceKey
     */
    public ServiceKey getServiceKey();

    /**
     * This parameter indicates the state of the mobile subscriber for which the
     * service is invoked. The possible states are "busy", "idle" and "not
     * reachable".
     *
     * @return the subscriberState
     */
    public SubscriberState getSubscriberState();

    /**
     * @param additionalCallingPartyNumber the additionalCallingPartyNumber to
     * set
     *
     * The calling party number provided by the access signalling system of the
     * calling user, e.g. provided by a PBX.
     */
    public void setAdditionalCallingPartyNumber(AdditionalCallingPartyNumber additionalCallingPartyNumber);

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
    public void setBearerCapability(byte[] bearerCapability);

    /**
     * @param callReferenceNumber the callReferenceNumber to set This parameter
     * contains the call reference number assigned to the call by the CCF.
     */
    public void setCallReferenceNumber(CallReferenceNumber callReferenceNumber);

    /**
     * @param calledPartyBCDNumber the calledPartyBCDNumber to set This
     * parameter contains the number used to identify the called party in the
     * forward direction. It may also include service selection information,
     * including * and # characters.
     */
    public void setCalledPartyBCDNumber(CalledPartyBCDNumber calledPartyBCDNumber);

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
    public void setCalledPartyNumber(CalledPartyNumber calledPartyNumber);

    /**
     * @param callingPartyNumber This parameter carries the calling party number
     * to identify the calling party or the origin of the call. See ETSI EN 300
     * 356-1 [23] Calling Party Number signalling information.
     */
    public void setCallingPartyNumber(CallingPartyNumber callingPartyNumber);

    /**
     * Indicates the type of calling party (e.g. operator, pay phone, ordinary
     * subscriber). See ETSI EN 300 356-1 [23] Calling Party Category signalling
     * information.
     *
     * @param callingPartysCategory the callingPartysCategory to set
     */
    public void setCallingPartysCategory(CallingPartysCategory callingPartysCategory);

    /**
     * @param extBasicServiceCode the extBasicServiceCode to set This parameter
     * indicates the Basic Service Code.
     */
    public void setExtBasicServiceCode(ExtBasicServiceCode extBasicServiceCode);

    /**
     * @param extensions the extensions to set
     */
    public void setExtensions(byte[] extensions);

    /**
     * This parameter indicates the type of the high layer compatibility, which
     * will be used to determine the ISDN -teleservice of a connected ISDN
     * terminal. The highlayerCompatibility can also be transported by ISUP
     * (e.g. within the ATP (see ITU-T Recommendation Q.763 [45]) parameter).
     *
     * @param highLayerCompability the highLayerCompability to set
     */
    public void setHighLayerCompability(HighLayerCompability highLayerCompability);

    /**
     * @param imsi the imsi to set This parameter contains the IMSI of the
     * mobile subscriber for which the service is invoked
     */
    public void setImsi(IMSI imsi);

    /**
     * @param locationInformation the locationInformation to set This parameter
     * indicates the location of the MS and the age of the information defining
     * the location.
     */
    public void setLocationInformation(LocationInformation locationInformation);

    /**
     * @param locationNumber This parameter is used to convey the geographical
     * area address for mobility services, see ITU-T Recommendation Q.762 [44].
     * It is used when "callingPartyNumber" does not contain any information
     * about the geographical location of the calling party (e.g., origin
     * dependent routeing when the calling party is a mobile subscriber).
     */
    public void setLocationNumber(LocationNumber locationNumber);

    /**
     * @param mscAddress the mscAddress to set This parameter contains the mscId
     * assigned to the MSC
     */
    public void setMscAddress(ISDNAddressString mscAddress);

    /**
     * If the call has met call forwarding on the route to the gsmSSF, then this
     * parameter carries the dialled digits. Refer to EN 300 356-1[23] Original
     * Called Number signalling information.
     *
     * @param originalCalledPartyID the originalCalledPartyID to set
     */
    public void setOriginalCalledPartyID(OriginalCalledNumber originalCalledPartyID);

    /**
     * @param redirectingPartyID the redirectingPartyID to set This parameter
     * indicates the last directory number the call was redirected from.
     */
    public void setRedirectingPartyID(RedirectingNumber redirectingPartyID);

    /**
     * @param redirectionInformation the redirectionInformation to set This
     * parameter contains forwarding related information, such as redirecting
     * counter. See ITU-T Recommendation Q.763 [45] Redirection Information
     * signalling information.
     */
    public void setRedirectionInformation(RedirectionInformation redirectionInformation);

    /**
     * @param serviceKey This parameter indicates to the gsmSCF the requested IN
     * service. It is used to address the required application/SLP within the
     * gsmSCF; this parameter is not for SCP addressing
     */
    public void setServiceKey(ServiceKey serviceKey);

    /**
     * @param subscriberState the subscriberState to set This parameter
     * indicates the state of the mobile subscriber for which the service is
     * invoked. The possible states are "busy", "idle" and "not reachable".
     */
    public void setSubscriberState(SubscriberState subscriberState);

    /**
     * This parameter contains the time that the gsmSSF was triggered, and the
     * time zone that the invoking gsmSSF resides in.
     *
     * @return TimeAndTimeZone
     */
    public TimeAndTimeZone getEventTime();

    /**
     *
     * @param eventTime the timeAndTimezone to set This parameter contains the
     * time that the gsmSSF was triggered, and the time zone that the invoking
     * gsmSSF resides in.
     */
    public void setEventTime(TimeAndTimeZone eventTime);

}
