/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v2;

import java.io.IOException;

import azrc.az.sigtran.cap.api.ITimeDurationChargingResult;
import azrc.az.sigtran.cap.parameters.LegId;
import azrc.az.sigtran.cap.parameters.ReceivingSideID;
import azrc.az.sigtran.cap.parameters.TimeInformation;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * timeDurationChargingResult	[0] SEQUENCE {
 * partyToCharge	[0] ReceivingSideID,
 * timeInformation	[1] TimeInformation,
 * callActive	[2] BOOLEAN DEFAULT TRUE
 * }
 *
 * @author eatakishiyev
 */
public class TimeDurationChargingResult implements ITimeDurationChargingResult {

    private ReceivingSideID partyToCharge;
    private TimeInformation timeInformation;
    private boolean callActive = true;

    public TimeDurationChargingResult() {
    }

    public TimeDurationChargingResult(ReceivingSideID partyToCharge, TimeInformation timeInformation) {
        this.partyToCharge = partyToCharge;
        this.timeInformation = timeInformation;
    }

    @Override
    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received, expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.partyToCharge = (ReceivingSideID) LegId.createLegId(ais.readSequenceStream());
                    break;
                case 1:
                    this.timeInformation = new TimeInformation();
                    this.timeInformation.decode(ais);
                    break;
                case 2:
                    this.callActive = ais.readBoolean();
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received, TagClass[%s], Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

    @Override
    public void encode(AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException {
        aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0);
        int lenPos = aos.StartContentDefiniteLength();

        this.partyToCharge.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        this.timeInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
        aos.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, 2, callActive);
        aos.FinalizeContent(lenPos);
    }

    @Override
    public ReceivingSideID getPartyToCharge() {
        return this.partyToCharge;
    }

    @Override
    public TimeInformation getTimeInformation() {
        return this.timeInformation;
    }

    @Override
    public boolean isLegActive() {
        return callActive;
    }

    @Override
    public void setLegActive(boolean legActive) {
        this.callActive = legActive;
    }

    @Override
    public void setPartyToCharge(ReceivingSideID partyToCharge) {
        this.partyToCharge = partyToCharge;
    }

    @Override
    public void setTimeInformation(TimeInformation timeInformation) {
        this.timeInformation = timeInformation;
    }

}
