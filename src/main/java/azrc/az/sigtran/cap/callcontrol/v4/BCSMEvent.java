/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v4;

import azrc.az.sigtran.cap.parameters.LegId;
import azrc.az.sigtran.cap.callcontrol.general.EventTypeBCSM;
import azrc.az.sigtran.cap.callcontrol.general.MonitorMode;
import java.io.IOException;

import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * BCSMEvent{PARAMETERS-BOUND : bound} ::= SEQUENCE { eventTypeBCSM [0]
 * EventTypeBCSM, monitorMode [1] MonitorMode, legID [2] LegID OPTIONAL,
 * dpSpecificCriteria [30] DpSpecificCriteria {bound} OPTIONAL, automaticRearm
 * [50] NULL OPTIONAL, ... }
 *
 * @author eatakishiyev
 */
public class BCSMEvent {

    private EventTypeBCSM eventTypeBCSM;
    private MonitorMode monitorMode;
    private LegId legID;
    private DpSpecificCriteria dpSpecificCriteria;
    private boolean automaticRearm;

    public BCSMEvent() {
    }

    public BCSMEvent(EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode) {
        this.eventTypeBCSM = eventTypeBCSM;
        this.monitorMode = monitorMode;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException {
        this.doCheck();

        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, eventTypeBCSM.toInt());
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, monitorMode.value());
        if (legID != null) {
            this.legID.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
        }
        if (dpSpecificCriteria != null) {
            this.dpSpecificCriteria.encode(Tag.CLASS_CONTEXT_SPECIFIC, 30, aos);
        }
        
        if (automaticRearm) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 50);
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
                    this.eventTypeBCSM = EventTypeBCSM.fromInt((int) ais.readInteger());
                    break;
                case 1:
                    this.monitorMode = MonitorMode.getInstance((int) ais.readInteger());
                    break;
                case 2:
                    this.legID = LegId.createLegId(ais.readSequenceStream());
                    break;
                case 30:
                    this.dpSpecificCriteria = new DpSpecificCriteria();
                    this.dpSpecificCriteria.decode(ais);
                    break;
                case 50:
                    this.automaticRearm = true;
                    ais.readNull();
                    break;
            }
        }
    }

    /**
     * @return the eventTypeBCSM
     */
    public EventTypeBCSM getEventTypeBCSM() {
        return eventTypeBCSM;
    }

    /**
     * @return the monitorMode
     */
    public MonitorMode getMonitorMode() {
        return monitorMode;
    }

    /**
     * @return the legID
     */
    public LegId getLegID() {
        return legID;
    }

    /**
     * @param legID the legID to set
     */
    public void setLegId(LegId legID) {
        this.legID = legID;
    }

    /**
     * @return the dpSpecificCriteria
     */
    public DpSpecificCriteria getDpSpecificCriteria() {
        return dpSpecificCriteria;
    }

    /**
     * @param dpSpecificCriteria the dpSpecificCriteria to set
     */
    public void setDpSpecificCriteria(DpSpecificCriteria dpSpecificCriteria) {
        this.dpSpecificCriteria = dpSpecificCriteria;
    }

    /**
     * @return the automaticRearm
     */
    public Boolean getAutomaticRearm() {
        return automaticRearm;
    }

    /**
     * @param automaticRearm the automaticRearm to set
     */
    public void setAutomaticRearm(Boolean automaticRearm) {
        this.automaticRearm = automaticRearm;
    }

    private void doCheck() throws AsnException {
        if (this.eventTypeBCSM == null || this.monitorMode == null) {
            throw new AsnException("One of mandatory parameter is absent;");
        }
    }
}
