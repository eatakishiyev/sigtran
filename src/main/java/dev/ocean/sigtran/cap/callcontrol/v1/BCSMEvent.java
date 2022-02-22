/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v1;

import dev.ocean.sigtran.cap.callcontrol.general.MonitorMode;
import java.io.IOException;
import dev.ocean.sigtran.cap.callcontrol.general.EventTypeBCSM;
import dev.ocean.sigtran.cap.parameters.LegId;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class BCSMEvent {

    private EventTypeBCSM eventTypeBCSM;
    private MonitorMode monitorMode;
    private LegId legId;

    public BCSMEvent() {
    }

    public BCSMEvent(EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode) {
        this.eventTypeBCSM = eventTypeBCSM;
        this.monitorMode = monitorMode;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {

        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, eventTypeBCSM.toInt());
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, monitorMode.value());
        if (legId != null) {
            this.legId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
        }
        aos.FinalizeContent(lenPos);

    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this._decode(tmpAis);
    }

    private void _decode(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            int tagClass = ais.getTagClass();

            if (tagClass != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException("Expecting tag class 2, found " + tagClass);
            }

            switch (tag) {
                case 0:
                    this.eventTypeBCSM = (EventTypeBCSM) EventTypeBCSM.fromInt((int) ais.readInteger());
                    break;
                case 1:
                    this.monitorMode = MonitorMode.getInstance((int) ais.readInteger());
                    break;
                case 2:
                    this.legId = LegId.createLegId(ais.readSequenceStream());
                    break;
            }
        }
    }

    public EventTypeBCSM getEventTypeBCSM() {
        return eventTypeBCSM;
    }

    public void setEventTypeBCSM(EventTypeBCSM eventTypeBCSM) {
        this.eventTypeBCSM = eventTypeBCSM;
    }

    public LegId getLegId() {
        return legId;
    }

    public void setLegId(LegId legId) {
        this.legId = legId;
    }

    public MonitorMode getMonitorMode() {
        return monitorMode;
    }

    public void setMonitorMode(MonitorMode monitorMode) {
        this.monitorMode = monitorMode;
    }

}
