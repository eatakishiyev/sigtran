/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v3;

import java.io.IOException;
import dev.ocean.sigtran.cap.api.EventReportBCSMArg;
import dev.ocean.sigtran.cap.api.EventSpecificInformationBCSM;
import dev.ocean.sigtran.cap.callcontrol.general.EventTypeBCSM;
import dev.ocean.sigtran.cap.parameters.LegId;
import dev.ocean.sigtran.cap.parameters.MiscCallInfo;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * EventReportBCSMArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
 * eventTypeBCSM [0] EventTypeBCSM,
 * eventSpecificInformationBCSM [2] EventSpecificInformationBCSM {bound} OPTIONAL,
 * legID [3] ReceivingSideID OPTIONAL,
 * miscCallInfo [4] MiscCallInfo DEFAULT {messageType request},
 * extensions [5] Extensions {bound} OPTIONAL, ... }
 *
 * @author eatakishiyev
 */
public class EventReportBCSMArgImpl implements EventReportBCSMArg {

    private EventTypeBCSM eventTypeBCSM;
    private EventSpecificInformationBCSMImpl eventSpecificInformationBCSM;
    private LegId legId;
    private MiscCallInfo miscCallInfo;
    private byte[] extensions;

    public EventReportBCSMArgImpl() {
    }

    public EventReportBCSMArgImpl(EventTypeBCSM eventTypeBCSM) {
        this.eventTypeBCSM = eventTypeBCSM;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, eventTypeBCSM.toInt());

            if (eventSpecificInformationBCSM != null) {
                eventSpecificInformationBCSM.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }

            if (legId != null) {
                legId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }

            if (miscCallInfo != null) {
                miscCallInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }

            if (extensions != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 5);
                int lenPos_ = aos.StartContentDefiniteLength();
                aos.write(extensions);
                aos.FinalizeContent(lenPos_);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new AsnException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL], found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());
        } catch (AsnException | IOException | IllegalNumberFormatException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();

                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new AsnException(String.format("Receiving incorrect tag. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
                }

                switch (tag) {
                    case 0:
                        this.eventTypeBCSM = EventTypeBCSM.fromInt((int) ais.readInteger());
                        break;
                    case 2:
                        this.eventSpecificInformationBCSM = EventSpecificInformationBCSMImpl.create();
                        this.eventSpecificInformationBCSM.decode(ais);
                        break;
                    case 3:
                        this.legId = LegId.createLegId(ais.readSequenceStream());
                        break;
                    case 4:
                        this.miscCallInfo = new MiscCallInfo();
                        this.miscCallInfo.decode(ais);
                        break;
                    case 5:
                        this.extensions = new byte[ais.readLength()];
                        ais.read(extensions);
                        break;
                    default:
                        throw new AsnException(String.format("Received incorrect tag. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the eventTypeBCSM
     */
    public EventTypeBCSM getEventTypeBCSM() {
        return eventTypeBCSM;
    }

    /**
     * @param eventTypeBCSM the eventTypeBCSM to set
     */
    public void setEventTypeBCSM(EventTypeBCSM eventTypeBCSM) {
        this.eventTypeBCSM = eventTypeBCSM;
    }

    /**
     * @return the eventSpecificInformation
     */
    @Override
    public EventSpecificInformationBCSM getEventSpecificInformationBCSM() {
        return eventSpecificInformationBCSM;
    }

    /**
     * @param eventSpecificInformation the eventSpecificInformation to set
     */
    public void setEventSpecificInformation(EventSpecificInformationBCSMImpl eventSpecificInformation) {
        this.eventSpecificInformationBCSM = eventSpecificInformation;
    }

    /**
     * @return the legId
     */
    @Override
    public LegId getLegId() {
        return legId;
    }

    /**
     * @param legId the legId to set
     */
    @Override
    public void setLegId(LegId legId) {
        this.legId = legId;
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
     * @return the extension
     */
    @Override
    public byte[] getExtensions() {
        return extensions;
    }

    /**
     * @param extension the extension to set
     */
    @Override
    public void setExtensions(byte[] extension) {
        this.extensions = extension;
    }
}
