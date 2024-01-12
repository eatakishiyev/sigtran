/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.parameters.depricated.Time;
import azrc.az.sigtran.map.parameters.location.information.LocationInformation;
import azrc.az.sigtran.map.parameters.location.information.LocationInformationEPS;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * SubscriberInfo::= SEQUENCE { locationInformation [0] LocationInformation
 * OPTIONAL, subscriberState [1] SubscriberState OPTIONAL, extensionContainer
 * [2] ExtensionContainer OPTIONAL, ... , locationInformationGPRS [3]
 * LocationInformationGPRS OPTIONAL, ps-SubscriberState [4] PS-SubscriberState
 * OPTIONAL, imei [5] IMEI OPTIONAL, ms-Classmark2 [6] MS-Classmark2 OPTIONAL,
 * gprs-MS-Class [7] GPRSMSClass OPTIONAL, mnpInfoRes [8] MNPInfoRes OPTIONAL,
 * imsVoiceOverPS-SessionsIndication [9] IMS-VoiceOverPS-SessionsInd OPTIONAL,
 * lastUE-ActivityTime [10] Time OPTIONAL, lastRAT-Type [11] Used-RAT-Type
 * OPTIONAL, eps-SubscriberState [12] PS-SubscriberState OPTIONAL,
 * locationInformationEPS [13] LocationInformationEPS OPTIONAL } -- If the HLR
 * receives locationInformation, subscriberState or ms-Classmark2 from an SGSN
 * or -- MME (via an IWF), it shall discard them. -- If the HLR receives
 * locationInformationGPRS, ps-SubscriberState, gprs-MS-Class or --
 * locationInformationEPS (outside the locationInformation IE) from a VLR, it
 * shall -- discard them. -- If the HLR receives parameters which it has not
 * requested, it shall discard them.
 *
 * @author eatakishiyev
 */
public class SubscriberInfo {

    private LocationInformation locationInformation;
    private SubscriberState subscriberState;
    private ExtensionContainer extensionContainer;
    private LocationInformationGPRS locationInformationGPRS;
    private PSSubscriberState psSubscriberState;
    private IMEI imei;
    private MSClassmark2 msClassmark2;
    private GPRSMSClass gprsMsClass;
    private MNPInfoRes mnpInfoRes;
    private IMSVoiceOverPSSessionInd imsVoiceOverPsSessionIndication;
    private Time lastUEActivityTime;
    private UsedRATType lastRATType;
    private PSSubscriberState epsSubscriberState;
    private LocationInformationEPS locationInformationEPS;
    private TimeZone timeZone;
    private DaylightSavingTime daylightSavingTime;

    public SubscriberInfo() {
    }

    public SubscriberInfo(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException, IllegalNumberFormatException{
        this.decode(ais);
    }
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, IllegalNumberFormatException, UnexpectedDataException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, IllegalNumberFormatException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int pos = aos.StartContentDefiniteLength();
            if (locationInformation != null) {
                locationInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }
            if (subscriberState != null) {
                subscriberState.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            if (locationInformationGPRS != null) {
                locationInformationGPRS.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }
            if (psSubscriberState != null) {
                psSubscriberState.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }
            if (imei != null) {
                imei.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            }
            if (msClassmark2 != null) {
                msClassmark2.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }
            if (gprsMsClass != null) {
                gprsMsClass.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }
            if (mnpInfoRes != null) {
                mnpInfoRes.encode(Tag.CLASS_CONTEXT_SPECIFIC, 8, aos);
            }
            if (imsVoiceOverPsSessionIndication != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 9, imsVoiceOverPsSessionIndication.value());
            }
            if (lastUEActivityTime != null) {
                lastUEActivityTime.encode(Tag.CLASS_CONTEXT_SPECIFIC, 10, aos);
            }
            if (lastRATType != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 11, lastRATType.value());
            }
            if (epsSubscriberState != null) {
                epsSubscriberState.encode(Tag.CLASS_CONTEXT_SPECIFIC, 12, aos);
            }
            if (locationInformationEPS != null) {
                locationInformationEPS.encode(Tag.CLASS_CONTEXT_SPECIFIC, 13, aos);
            }
            if (timeZone != null) {
                timeZone.encode(Tag.CLASS_CONTEXT_SPECIFIC, 14, aos);
            }
            if (daylightSavingTime != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 15, daylightSavingTime.value());
            }

            aos.FinalizeContent(pos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public final void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException, IllegalNumberFormatException {
        try {
            this._decode(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IOException, IncorrectSyntaxException, AsnException, UnexpectedDataException, IllegalNumberFormatException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new IncorrectSyntaxException(String.format("Unexpected Class received. Expecting Class[CONTEXT], received Tag[%d] Class[%d]", tag, ais.getTagClass()));
            }
            switch (tag) {
                case 0:
                    locationInformation = new LocationInformation();
                    locationInformation.decode(ais);
                    break;
                case 1:
                    subscriberState = SubscriberState.create();
                    subscriberState.decode(ais);
                    break;
                case 2:
                    extensionContainer = new ExtensionContainer();
                    extensionContainer.decode(ais);
                    break;
                case 3:
                    locationInformationGPRS = new LocationInformationGPRS();
                    locationInformationGPRS.decode(ais);
                    break;
                case 4:
                    psSubscriberState = new PSSubscriberState();
                    psSubscriberState.decode(ais);
                    break;
                case 5:
                    imei = new IMEI();
                    imei.decode(ais);
                    break;
                case 6:
                    msClassmark2 = new MSClassmark2();
                    msClassmark2.decode(ais);
                    break;
                case 7:
                    gprsMsClass = new GPRSMSClass();
                    gprsMsClass.decode(ais);
                    break;
                case 8:
                    mnpInfoRes = new MNPInfoRes();
                    mnpInfoRes.decode(ais);
                    break;
                case 9:
                    imsVoiceOverPsSessionIndication = IMSVoiceOverPSSessionInd.getInstance((int) ais.readInteger());
                    break;
                case 10:
                    lastUEActivityTime = new Time();
                    lastUEActivityTime.decode(ais);
                    break;
                case 11:
                    lastRATType = UsedRATType.getInstance((int) ais.readInteger());
                    break;
                case 12:
                    epsSubscriberState = new PSSubscriberState();
                    epsSubscriberState.decode(ais);
                    break;
                case 13:
                    locationInformationEPS = new LocationInformationEPS();
                    locationInformationEPS.decode(ais);
                    break;
                case 14:
                    timeZone = new TimeZone();
                    timeZone.decode(ais);
                    break;
                case 15:
                    daylightSavingTime = DaylightSavingTime.getInstance((int) ais.readInteger());
                    break;
                default:
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Class[CONTEXT], received Tag[%d] Class[%d]", tag, ais.getTagClass()));
            }
        }
    }

    public DaylightSavingTime getDaylightSavingTime() {
        return daylightSavingTime;
    }

    public void setDaylightSavingTime(DaylightSavingTime daylightSavingTime) {
        this.daylightSavingTime = daylightSavingTime;
    }

    public PSSubscriberState getEpsSubscriberState() {
        return epsSubscriberState;
    }

    public void setEpsSubscriberState(PSSubscriberState epsSubscriberState) {
        this.epsSubscriberState = epsSubscriberState;
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public GPRSMSClass getGprsMsClass() {
        return gprsMsClass;
    }

    public void setGprsMsClass(GPRSMSClass gprsMsClass) {
        this.gprsMsClass = gprsMsClass;
    }

    public IMEI getImei() {
        return imei;
    }

    public void setImei(IMEI imei) {
        this.imei = imei;
    }

    public IMSVoiceOverPSSessionInd getImsVoiceOverPsSessionIndication() {
        return imsVoiceOverPsSessionIndication;
    }

    public void setImsVoiceOverPsSessionIndication(IMSVoiceOverPSSessionInd imsVoiceOverPsSessionIndication) {
        this.imsVoiceOverPsSessionIndication = imsVoiceOverPsSessionIndication;
    }

    public UsedRATType getLastRATType() {
        return lastRATType;
    }

    public void setLastRATType(UsedRATType lastRATType) {
        this.lastRATType = lastRATType;
    }

    public Time getLastUEActivityTime() {
        return lastUEActivityTime;
    }

    public void setLastUEActivityTime(Time lastUEActivityTime) {
        this.lastUEActivityTime = lastUEActivityTime;
    }

    public LocationInformation getLocationInformation() {
        return locationInformation;
    }

    public void setLocationInformation(LocationInformation locationInformation) {
        this.locationInformation = locationInformation;
    }

    public LocationInformationEPS getLocationInformationEPS() {
        return locationInformationEPS;
    }

    public void setLocationInformationEPS(LocationInformationEPS locationInformationEPS) {
        this.locationInformationEPS = locationInformationEPS;
    }

    public LocationInformationGPRS getLocationInformationGPRS() {
        return locationInformationGPRS;
    }

    public void setLocationInformationGPRS(LocationInformationGPRS locationInformationGPRS) {
        this.locationInformationGPRS = locationInformationGPRS;
    }

    public MNPInfoRes getMnpInfoRes() {
        return mnpInfoRes;
    }

    public void setMnpInfoRes(MNPInfoRes mnpInfoRes) {
        this.mnpInfoRes = mnpInfoRes;
    }

    public void setMsClassmark2(MSClassmark2 msClassmark2) {
        this.msClassmark2 = msClassmark2;
    }

    public MSClassmark2 getMsClassmark2() {
        return msClassmark2;
    }

    public void setPsSubscriberState(PSSubscriberState psSubscriberState) {
        this.psSubscriberState = psSubscriberState;
    }

    public PSSubscriberState getPsSubscriberState() {
        return psSubscriberState;
    }

    public void setSubscriberState(SubscriberState subscriberState) {
        this.subscriberState = subscriberState;
    }

    public SubscriberState getSubscriberState() {
        return subscriberState;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

}
