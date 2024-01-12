/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v4;

import azrc.az.sigtran.cap.api.RequestReportBCSMEventArg;
import azrc.az.sigtran.cap.callcontrol.general.EventTypeBCSM;
import azrc.az.sigtran.cap.callcontrol.general.MonitorMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class RequestReportBCSMEventArgImpl implements RequestReportBCSMEventArg {

    private final List<BCSMEvent> bcsmEvents = new ArrayList<>();
    private byte[] extensions;

    public RequestReportBCSMEventArgImpl() {
    }

    public BCSMEvent requestAnalyzedInformation(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.ANALYZED_INFORMATION, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestRouteSelectFailure(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.ROUTE_SELECT_FAILURE, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestOBusyEvent(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.O_CALLED_PARTY_BUSY, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestONoAnswer(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.O_NO_ANSWER, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestOAnswerEvent(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.O_ANSWER, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestOMidCall(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.O_MID_CALL, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestODisconnectEvent(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.O_DISCONNECT, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestOAbandon(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.O_ABANDON, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

//T-BCSM
    public BCSMEvent requestTBusyEvent(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.T_BUSY, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestTNoAnswerEvent(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.T_NO_ANSWER, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestTAnswerEvent(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.T_ANSWER, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestTMidCall(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.T_MID_CALL, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestTDisconnectEvent(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.T_DISCONNECT, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestTAbandon(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.T_ABANDON, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestOTermSeized(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.O_TERM_SEIZED, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestCallAccepted(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.CALL_ACCEPTED, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestOChangeOfPosition(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.O_CHANGE_OF_POSITION, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestOServiceChange(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.O_SERVICE_CHANGE, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestTChangeOfPosition(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.T_CHANGE_OF_POSITION, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestTServiceChange(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.T_SERVICE_CHANGE, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, ParameterOutOfRangeException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0);
            int _lenPos = aos.StartContentDefiniteLength();
            for (BCSMEvent bCSMEvent : bcsmEvents) {
                bCSMEvent.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }
            aos.FinalizeContent(_lenPos);

            if (extensions != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 2);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws ParameterOutOfRangeException, IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL], found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            switch (tag) {
                case 0:
                    this.decodeBCSMEvents(ais.readSequenceStream());
                    break;
                case 1:
                    this.extensions = new byte[ais.readLength()];
                    ais.read(extensions);
                    break;
            }
        }
    }

    private void decodeBCSMEvents(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            BCSMEvent bCSMEvent = new BCSMEvent();
            bCSMEvent.decode(ais);
            this.bcsmEvents.add(bCSMEvent);
        }
    }
}
