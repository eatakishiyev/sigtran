/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.smscontrol.v3;

import java.io.IOException;
import dev.ocean.sigtran.cap.parameters.SMSFailureSpecificInfo;
import dev.ocean.sigtran.cap.parameters.SMSSubmissionSpecificInfo;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class EventSpecificInformationSMS {

    private SMSFailureSpecificInfo oSMSFailureSpecificInfo;
    private SMSSubmissionSpecificInfo oSMSSubmittedSpecificInfo;

    public EventSpecificInformationSMS() {
    }

    public EventSpecificInformationSMS(SMSFailureSpecificInfo oSMSFailureSpecificInfo) {
        this.oSMSFailureSpecificInfo = oSMSFailureSpecificInfo;
    }

    public EventSpecificInformationSMS(SMSSubmissionSpecificInfo oSMSSubmissionSpecificInfo) {
        this.oSMSSubmittedSpecificInfo = oSMSSubmissionSpecificInfo;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        if (this.oSMSFailureSpecificInfo != null) {
            oSMSFailureSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        } else if (this.oSMSSubmittedSpecificInfo != null) {
            oSMSSubmittedSpecificInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
        }
        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException {
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
                    this.oSMSSubmittedSpecificInfo = new SMSSubmissionSpecificInfo();
                    this.oSMSSubmittedSpecificInfo.decode(ais);
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received, TagClass[%s] Tag[%s]", ais.getTagClass(), ais.getTag()));
            }
        }
    }

    public SMSFailureSpecificInfo getOSMSFailureSpecificInfo() {
        return oSMSFailureSpecificInfo;
    }

    public SMSSubmissionSpecificInfo getOSMSSubmittedSpecificInfo() {
        return oSMSSubmittedSpecificInfo;
    }

}
