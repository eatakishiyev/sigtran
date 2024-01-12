/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v2;

import azrc.az.sigtran.cap.api.AChBillingChargingCharacteristics;
import azrc.az.sigtran.cap.api.IApplyChargingArg;
import azrc.az.sigtran.cap.parameters.LegId;
import azrc.az.sigtran.cap.parameters.LegType;
import azrc.az.sigtran.cap.parameters.SendingSideID;

import java.io.IOException;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * ApplyChargingArg	::= SEQUENCE { aChBillingChargingCharacteristics	[0]
 * AChBillingChargingCharacteristics, partyToCharge	[2] SendingSideID DEFAULT
 * sendingSideID : leg1, extensions	[3] SEQUENCE SIZE(1..numOfExtensions)	OF
 * ExtensionField PTIONAL, ... }
 *
 * @author eatakishiyev
 */
public class ApplyChargingArg implements IApplyChargingArg {

    private AChBillingChargingCharacteristics aChBillingChargingCharacteristics;
    private SendingSideID partyToCharge;
    private byte[] extensions;

    public ApplyChargingArg() {
    }

    public ApplyChargingArg(AChBillingChargingCharacteristics aChBillingChargingCharacteristics, SendingSideID partyToCharge) {
        this.aChBillingChargingCharacteristics = aChBillingChargingCharacteristics;
        this.partyToCharge = partyToCharge;
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
                aos.writeSequence(Tag.CLASS_CONTEXT_SPECIFIC, 3, this.extensions);
            }

            aos.FinalizeContent(lenPos);
        } catch (Exception ex) {
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
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        this.partyToCharge = new SendingSideID(LegType.LEG1);//default value is sendingSideID:leg1

        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2] , found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.aChBillingChargingCharacteristics = new AChBillingChargingCharacteristics();
                    this.getaChBillingChargingCharacteristics().decode(ais);
                    break;
                case 2:
                    this.partyToCharge = (SendingSideID) LegId.createLegId(ais.readSequenceStream());
                    break;
                case 3:
                    this.extensions = ais.readSequence();
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
        }
    }

    @Override
    public byte[] getExtensions() {
        return extensions;
    }

    @Override
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

    /**
     * The partyToCharge parameter indicates the party in the call to which the
     * ApplyCharging operation should be applied
     *
     * @return
     */
    @Override
    public SendingSideID getPartyToCharge() {
        return partyToCharge;
    }

    /**
     * The partyToCharge parameter indicates the party in the call to which the
     * ApplyCharging operation should be applied
     *
     * @param partyToCharge
     */
    @Override
    public void setPartyToCharge(SendingSideID partyToCharge) {
        this.partyToCharge = partyToCharge;
    }

    @Override
    public AChBillingChargingCharacteristics getaChBillingChargingCharacteristics() {
        return this.aChBillingChargingCharacteristics;
    }

    public void setaChBillingChargingCharacteristics(AChBillingChargingCharacteristics aChBillingChargingCharacteristics) {
        this.aChBillingChargingCharacteristics = aChBillingChargingCharacteristics;
    }

}
