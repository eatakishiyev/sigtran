/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v1;

import dev.ocean.sigtran.cap.callcontrol.general.MonitorMode;
import dev.ocean.sigtran.cap.api.RequestReportBCSMEventArg;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import dev.ocean.sigtran.cap.callcontrol.general.EventTypeBCSM;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class RequestReportBCSMEventArgImpl implements RequestReportBCSMEventArg {

    private final List<BCSMEvent> bcsmEvents = new ArrayList();
    private byte[] extensions;

    public BCSMEvent requestOAnswerEvent(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.O_ANSWER, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestODisconnectEvent(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.O_DISCONNECT, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestTAnswerEvent(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.T_ANSWER, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    public BCSMEvent requestTDisconnectEvent(MonitorMode monitorMode) {
        BCSMEvent event = new BCSMEvent(EventTypeBCSM.T_DISCONNECT, monitorMode);
        bcsmEvents.add(event);
        return event;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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
