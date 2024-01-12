/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.smscontrol.v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import azrc.az.sigtran.cap.api.IRequestReportSMSEventArg;
import azrc.az.sigtran.cap.callcontrol.general.EventTypeSMS;
import azrc.az.sigtran.cap.callcontrol.general.MonitorMode;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class RequestReportSMSEventArg implements IRequestReportSMSEventArg {

    private final List<SMSEvent> smsEvents = new ArrayList<>();
    private byte[] extensions;

    public RequestReportSMSEventArg() {
    }

    public SMSEvent requestOSMSFailureEvent(MonitorMode monitorMode) {
        SMSEvent smsEvent = new SMSEvent(EventTypeSMS.O_SMS_FAILURE, monitorMode);
        smsEvents.add(smsEvent);
        return smsEvent;
    }

    public SMSEvent requestOSMSSubmissionEvent(MonitorMode monitorMode) {
        SMSEvent smsEvent = new SMSEvent(EventTypeSMS.O_SMS_SUBMISSION, monitorMode);
        smsEvents.add(smsEvent);
        return smsEvent;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0);
            int lenPos_ = aos.StartContentDefiniteLength();
            for (SMSEvent sMSEvent : smsEvents) {
                sMSEvent.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }
            aos.FinalizeContent(lenPos_);

            if (extensions != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 10, this.extensions);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new AsnException(String.format("Unexpected tag received, expecting TagClass[0] Tag[4], found TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
            this.decode_(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            switch (tag) {
                case 0:
                    AsnInputStream tmpAis = ais.readSequenceStream();
                    while (tmpAis.available() > 0) {
                        tag = tmpAis.readTag();
                        if (tag != Tag.SEQUENCE || tmpAis.getTagClass() != Tag.CLASS_UNIVERSAL) {
                            throw new AsnException(String.format("Unexpected tag received, expecting TagClass[0] Tag[16], found TagClass[%s] Tag[%s]", tmpAis.getTagClass(), tag));
                        }
                        SMSEvent sMSEvent = new SMSEvent();
                        sMSEvent.decode(tmpAis);
                        this.smsEvents.add(sMSEvent);
                    }
                    break;
                case 1:
                    this.extensions = ais.readSequence();
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received, TagClass[%s] Tag[%s]", ais.getTagClass(), tag));

            }
        }
    }

    @Override
    public byte[] getExtensions() {
        return extensions;
    }

    @Override
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

}
