 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.smscontrol.v4;

import java.io.IOException;

import azrc.az.sigtran.cap.parameters.SMSDeliverSpecificInfo;
import azrc.az.sigtran.cap.parameters.SMSFailureSpecificInfo;
import azrc.az.sigtran.cap.parameters.SMSSubmissionSpecificInfo;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * EventSpecificInformationSMS ::= CHOICE { o-smsFailureSpecificInfo [0]
 * SEQUENCE { failureCause [0] MO-SMSCause OPTIONAL, ... },
 * o-smsSubmissionSpecificInfo [1] SEQUENCE { -- no specific info defined— ...
 * }, t-smsFailureSpecificInfo [2] SEQUENCE { failureCause [0] MT-SMSCause
 * OPTIONAL, ... }, t-smsDeliverySpecificInfo [3] SEQUENCE { -- no specific info
 * defined— ... } }
 *
 * @author eatakishiyev
 */
public class EventSpecificInformationSMS {

    private SMSFailureSpecificInfo oSMSFailureSpecificInfo;
    private SMSFailureSpecificInfo tSMSFailureSpecificInfo;
    private SMSSubmissionSpecificInfo oSMSSubmissionSpecificInfo;
    private SMSDeliverSpecificInfo tSMSDeliverSpecificInfo;

    private EventSpecificInformationSMS() {

    }

    public static EventSpecificInformationSMS createOSMSFailureSpecificInformationSMS(SMSFailureSpecificInfo sMSFailureSpecificInfo) {
        EventSpecificInformationSMS instance = new EventSpecificInformationSMS();
        instance.setoSMSFailureSpecificInfo(sMSFailureSpecificInfo);
        return instance;
    }

    public static EventSpecificInformationSMS createTSMSFailureSpecificInformationSMS(SMSFailureSpecificInfo sMSFailureSpecificInfo) {
        EventSpecificInformationSMS instance = new EventSpecificInformationSMS();
        instance.settSMSFailureSpecificInfo(sMSFailureSpecificInfo);
        return instance;
    }

    public static EventSpecificInformationSMS createOSMSSubmissionSpecificInformationSMS(SMSSubmissionSpecificInfo sMSSubmissionSpecificInfo) {
        EventSpecificInformationSMS instance = new EventSpecificInformationSMS();
        instance.setoSMSSubmissionSpecificInfo(sMSSubmissionSpecificInfo);
        return instance;
    }

    public static EventSpecificInformationSMS createTSMSDeliverSpecificInformationSMS(SMSDeliverSpecificInfo sMSDeliverSpecificInfo) {
        EventSpecificInformationSMS instance = new EventSpecificInformationSMS();
        instance.settSMSDeliverSpecificInfo(sMSDeliverSpecificInfo);
        return instance;
    }

    public static EventSpecificInformationSMS create() {
        EventSpecificInformationSMS instance = new EventSpecificInformationSMS();
        return instance;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        if (this.oSMSFailureSpecificInfo != null) {
            this.oSMSFailureSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        } else if (this.oSMSSubmissionSpecificInfo != null) {
            this.oSMSSubmissionSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
        } else if (tSMSFailureSpecificInfo != null) {
            this.tSMSFailureSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
        } else {
            this.tSMSDeliverSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
        }
        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        while (ais.available() > 0) {
            ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received, expecting TagClass[2], TagClass[%s]]", ais.getTagClass()));
            }

            switch (ais.getTag()) {
                case 0:
                    this.oSMSFailureSpecificInfo = new SMSFailureSpecificInfo();
                    this.oSMSFailureSpecificInfo.decode(ais.readSequenceStream());
                    break;
                case 1:
                    this.oSMSSubmissionSpecificInfo = new SMSSubmissionSpecificInfo();
                    this.oSMSSubmissionSpecificInfo.decode(ais);
                    break;
                case 2:
                    this.tSMSFailureSpecificInfo = new SMSFailureSpecificInfo();
                    this.tSMSFailureSpecificInfo.decode(ais.readSequenceStream());
                    break;
                case 3:
                    this.tSMSDeliverSpecificInfo = new SMSDeliverSpecificInfo();
                    this.tSMSDeliverSpecificInfo.decode(ais);
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received, TagClass[%s] Tag[%s]", ais.getTagClass(), ais.getTag()));
            }
        }
    }

    /**
     * @return the oSMSFailureSpecificInfo
     */
    public SMSFailureSpecificInfo getoSMSFailureSpecificInfo() {
        return oSMSFailureSpecificInfo;
    }

    /**
     * @param oSMSFailureSpecificInfo the oSMSFailureSpecificInfo to set
     */
    public void setoSMSFailureSpecificInfo(SMSFailureSpecificInfo oSMSFailureSpecificInfo) {
        this.oSMSFailureSpecificInfo = oSMSFailureSpecificInfo;
    }

    /**
     * @return the tSMSFailureSpecificInfo
     */
    public SMSFailureSpecificInfo gettSMSFailureSpecificInfo() {
        return tSMSFailureSpecificInfo;
    }

    /**
     * @param tSMSFailureSpecificInfo the tSMSFailureSpecificInfo to set
     */
    public void settSMSFailureSpecificInfo(SMSFailureSpecificInfo tSMSFailureSpecificInfo) {
        this.tSMSFailureSpecificInfo = tSMSFailureSpecificInfo;
    }

    /**
     * @return the oSMSSubmissionSpecificInfo
     */
    public SMSSubmissionSpecificInfo getoSMSSubmissionSpecificInfo() {
        return oSMSSubmissionSpecificInfo;
    }

    /**
     * @param oSMSSubmissionSpecificInfo the oSMSSubmissionSpecificInfo to set
     */
    public void setoSMSSubmissionSpecificInfo(SMSSubmissionSpecificInfo oSMSSubmissionSpecificInfo) {
        this.oSMSSubmissionSpecificInfo = oSMSSubmissionSpecificInfo;
    }

    /**
     * @return the tSMSDeliverSpecificInfo
     */
    public SMSDeliverSpecificInfo gettSMSDeliverSpecificInfo() {
        return tSMSDeliverSpecificInfo;
    }

    /**
     * @param tSMSDeliverSpecificInfo the tSMSDeliverSpecificInfo to set
     */
    public void settSMSDeliverSpecificInfo(SMSDeliverSpecificInfo tSMSDeliverSpecificInfo) {
        this.tSMSDeliverSpecificInfo = tSMSDeliverSpecificInfo;
    }

}
