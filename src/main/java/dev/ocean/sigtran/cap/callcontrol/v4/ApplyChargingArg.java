/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v4;

import java.io.IOException;
import dev.ocean.sigtran.cap.api.AChBillingChargingCharacteristics;
import dev.ocean.sigtran.cap.api.IApplyChargingArg;
import dev.ocean.sigtran.cap.parameters.AChChargingAddress;
import dev.ocean.sigtran.cap.parameters.LegId;
import dev.ocean.sigtran.cap.parameters.LegType;
import dev.ocean.sigtran.cap.parameters.SendingSideID;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * ApplyChargingArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
 * aChBillingChargingCharacteristics [0] AChBillingChargingCharacteristics{bound},
 * partyToCharge [2] SendingSideID DEFAULT sendingSideID : leg1,
 * extensions [3] Extensions {bound} OPTIONAL,
 * aChChargingAddress [50] AChChargingAddress {bound} DEFAULT legID:sendingSideID:leg1, ... }
 *
 * @author eatakishiyev
 */
public class ApplyChargingArg implements IApplyChargingArg {

    private AChBillingChargingCharacteristics aChBillingChargingCharacteristics;
    private SendingSideID partyToCharge;
    private byte[] extensions;
    private AChChargingAddress aChChargingAddress = new AChChargingAddress(new SendingSideID(LegType.LEG1));

    public ApplyChargingArg() {
    }

    public ApplyChargingArg(AChBillingChargingCharacteristics aChBillingChargingCharacteristics, SendingSideID partytoCharge) {
        this.aChBillingChargingCharacteristics = aChBillingChargingCharacteristics;
        this.partyToCharge = partytoCharge;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws ParameterOutOfRangeException, IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            this.aChBillingChargingCharacteristics.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

            if (this.partyToCharge != null) {
                this.partyToCharge.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }

            if (this.extensions != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 3, this.extensions);
            }

            if (this.aChChargingAddress != null
                    && !(this.aChChargingAddress.getLegId() instanceof SendingSideID
                    && this.aChChargingAddress.getLegId().getLegType() == LegType.LEG1)) {
                this.aChChargingAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 50, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }

    }

    @Override
    public void encode(AsnOutputStream aos) throws ParameterOutOfRangeException, IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    @Override
    public void decode(AsnInputStream ais) throws ParameterOutOfRangeException, IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new AsnException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL], found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        this.partyToCharge = new SendingSideID(LegType.LEG1);//default value is sendingSideID:leg1
        this.aChChargingAddress = new AChChargingAddress(new SendingSideID(LegType.LEG1));//default value is legID:sedinginSideID:leg1

        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2] , found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.aChBillingChargingCharacteristics = new AChBillingChargingCharacteristics();
                    aChBillingChargingCharacteristics.decode(ais);
                    break;
                case 2:
                    this.partyToCharge = (SendingSideID) LegId.createLegId(ais.readSequenceStream());
                    break;
                case 3:
                    this.extensions = ais.readSequence();
                    break;
                case 50:
                    this.aChChargingAddress = new AChChargingAddress();
                    this.getaChChargingAddress().decode(ais);
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));

            }
        }
    }

    /**
     * @return the partyToCharge
     */
    @Override
    public SendingSideID getPartyToCharge() {
        return partyToCharge;
    }

    /**
     * @return the extensions
     */
    @Override
    public byte[] getExtensions() {
        return extensions;
    }

    /**
     * @return the aChChargingAddress
     */
    public AChChargingAddress getaChChargingAddress() {
        return aChChargingAddress;
    }

    @Override
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

    @Override
    public void setPartyToCharge(SendingSideID partyToCharge) {
        this.partyToCharge = partyToCharge;
    }

    public void setaChChargingAddress(AChChargingAddress aChChargingAddress) {
        this.aChChargingAddress = aChChargingAddress;
    }

    @Override
    public AChBillingChargingCharacteristics getaChBillingChargingCharacteristics() {
        return this.aChBillingChargingCharacteristics;
    }

}
