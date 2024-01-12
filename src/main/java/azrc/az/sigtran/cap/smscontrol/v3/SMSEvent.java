/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.smscontrol.v3;

import java.io.IOException;

import azrc.az.sigtran.cap.callcontrol.general.EventTypeSMS;
import azrc.az.sigtran.cap.callcontrol.general.MonitorMode;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class SMSEvent {

    private EventTypeSMS eventTypeSMS;
    private MonitorMode monitorMode;

    public SMSEvent() {
    }

    public SMSEvent(EventTypeSMS eventTypeSMS, MonitorMode monitorMode) {
        this.eventTypeSMS = eventTypeSMS;
        this.monitorMode = monitorMode;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, eventTypeSMS.value());
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, monitorMode.value());

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        this.decode_(ais.readSequenceStream());
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            switch (tag) {
                case 0:
                    this.eventTypeSMS = EventTypeSMS.getInstance((int) ais.readInteger());
                    break;
                case 1:
                    this.monitorMode = MonitorMode.getInstance((int) ais.readInteger());
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received, TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

    public EventTypeSMS getEventTypeSMS() {
        return eventTypeSMS;
    }

    public void setEventTypeSMS(EventTypeSMS eventTypeSMS) {
        this.eventTypeSMS = eventTypeSMS;
    }

    public MonitorMode getMonitorMode() {
        return monitorMode;
    }

    public void setMonitorMode(MonitorMode monitorMode) {
        this.monitorMode = monitorMode;
    }

}
