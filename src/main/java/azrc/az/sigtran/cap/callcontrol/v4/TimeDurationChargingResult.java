/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v4;

import azrc.az.sigtran.cap.api.ITimeDurationChargingResult;
import azrc.az.sigtran.cap.parameters.AChChargingAddress;
import azrc.az.sigtran.cap.parameters.LegId;
import azrc.az.sigtran.cap.parameters.ReceivingSideID;
import azrc.az.sigtran.cap.parameters.TimeInformation;

import java.io.IOException;

import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class TimeDurationChargingResult implements ITimeDurationChargingResult {

    private ReceivingSideID partyToCharge;
    private TimeInformation timeInformation;
    private boolean legActive = true;
    private boolean callLegReleasedAtTcpExpiry;
    private byte[] extensions;
    private AChChargingAddress aChChargingAddress;

    public TimeDurationChargingResult() {
    }

    public TimeDurationChargingResult(ReceivingSideID partyToCharge, TimeInformation timeInformation, boolean legActive, AChChargingAddress aChChargingAddress) {
        this.partyToCharge = partyToCharge;
        this.timeInformation = timeInformation;
        this.legActive = legActive;
        this.aChChargingAddress = aChChargingAddress;
    }

    @Override
    public void encode(AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException {
        aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0);
        int lenPos = aos.StartContentDefiniteLength();

        this.partyToCharge.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        this.timeInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
        aos.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, 2, legActive);

        if (callLegReleasedAtTcpExpiry) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 3);
        }

        if (extensions != null) {
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 4, extensions);
        }

        if (aChChargingAddress != null) {
            this.aChChargingAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
        }

        aos.FinalizeContent(lenPos);
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
                    this.legActive = ais.readBoolean();
                    break;
                case 3:
                    this.callLegReleasedAtTcpExpiry = true;
                    ais.readNull();
                    break;
                case 4:
                    this.extensions = ais.readSequence();
                    break;
                case 5:
                    this.aChChargingAddress = new AChChargingAddress();
                    this.aChChargingAddress.decode(ais);
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received, TagClass[%s], Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

    /**
     * @return the partyToCharge
     */
    @Override
    public ReceivingSideID getPartyToCharge() {
        return partyToCharge;
    }

    /**
     * @param partyToCharge the partyToCharge to set
     */
    @Override
    public void setPartyToCharge(ReceivingSideID partyToCharge) {
        this.partyToCharge = partyToCharge;
    }

    /**
     * @return the timeInformation
     */
    @Override
    public TimeInformation getTimeInformation() {
        return timeInformation;
    }

    /**
     * @param timeInformation the timeInformation to set
     */
    @Override
    public void setTimeInformation(TimeInformation timeInformation) {
        this.timeInformation = timeInformation;
    }

    /**
     * @return the legActive
     */
    @Override
    public boolean isLegActive() {
        return legActive;
    }

    /**
     * @param legActive the legActive to set
     */
    @Override
    public void setLegActive(boolean legActive) {
        this.legActive = legActive;
    }

    /**
     * @return the callLegReleasedAtTcpExpiry
     */
    public boolean getCallLegReleasedAtTcpExpiry() {
        return callLegReleasedAtTcpExpiry;
    }

    /**
     * @param callLegReleasedAtTcpExpiry the callLegReleasedAtTcpExpiry to
     * set
     */
    public void setCallLegReleasedAtTcpExpiry(boolean callLegReleasedAtTcpExpiry) {
        this.callLegReleasedAtTcpExpiry = callLegReleasedAtTcpExpiry;
    }

    /**
     * @return the extensions
     */
    public byte[] getExtensions() {
        return extensions;
    }

    /**
     * @param extensions the extensions to set
     */
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

    /**
     * @return the aChChargingAddress
     */
    public AChChargingAddress getaChChargingAddress() {
        return aChChargingAddress;
    }

    /**
     * @param aChChargingAddress the aChChargingAddress to set
     */
    public void setaChChargingAddress(AChChargingAddress aChChargingAddress) {
        this.aChChargingAddress = aChChargingAddress;
    }
}
