/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.smscontrol.v4;

import dev.ocean.sigtran.cap.callcontrol.general.EventTypeSMS;
import java.io.IOException;
import dev.ocean.sigtran.cap.api.IEventReportSMSArg;
import dev.ocean.sigtran.cap.parameters.MiscCallInfo;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * EventReportSMSArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { eventTypeSMS [0]
 * EventTypeSMS, eventSpecificInformationSMS [1] EventSpecificInformationSMS
 * OPTIONAL, miscCallInfo [2] MiscCallInfo DEFAULT {messageType request},
 * extensions [10] Extensions {bound} OPTIONAL, ... }
 *
 * @author eatakishiyev
 */
public class EventReportSMSArg implements IEventReportSMSArg{

    private EventTypeSMS eventTypeSMS;
    private EventSpecificInformationSMS eventSpecificInformationSMS;
    private MiscCallInfo miscCallInfo;
    private byte[] extensions;

    public EventReportSMSArg() {
    }

    public EventReportSMSArg(EventTypeSMS eventTypeSMS, MiscCallInfo miscCallInfo) {
        this.eventTypeSMS = eventTypeSMS;
        this.miscCallInfo = miscCallInfo;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, eventTypeSMS.value());
            if (eventSpecificInformationSMS != null) {
                this.eventSpecificInformationSMS.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }

            this.miscCallInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);

            if (extensions != null) {
                aos.writeOctetString(extensions);
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
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received, expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.eventTypeSMS = EventTypeSMS.getInstance((int) ais.readInteger());
                    break;
                case 1:
                    this.eventSpecificInformationSMS = EventSpecificInformationSMS.create();
                    this.eventSpecificInformationSMS.decode(ais.readSequenceStream());
                    break;
                case 2:
                    this.miscCallInfo = new MiscCallInfo();
                    this.miscCallInfo.decode(ais);
                    break;
                case 10:
                    this.extensions = ais.readSequence();
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received, TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
        }
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
     * @return the eventSpecificInformationSMS
     */
    public EventSpecificInformationSMS getEventSpecificInformationSMS() {
        return eventSpecificInformationSMS;
    }

    /**
     * @param eventSpecificInformationSMS the eventSpecificInformationSMS to set
     */
    public void setEventSpecificInformationSMS(EventSpecificInformationSMS eventSpecificInformationSMS) {
        this.eventSpecificInformationSMS = eventSpecificInformationSMS;
    }

    /**
     * @return the miscCallInfo
     */
    @Override
    public MiscCallInfo getMiscCallInfo() {
        return miscCallInfo;
    }

    /**
     * @param miscCallInfo the miscCallInfo to set
     */
    @Override
    public void setMiscCallInfo(MiscCallInfo miscCallInfo) {
        this.miscCallInfo = miscCallInfo;
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
}
