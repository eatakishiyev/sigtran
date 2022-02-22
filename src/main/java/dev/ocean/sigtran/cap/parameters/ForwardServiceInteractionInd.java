/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import dev.ocean.isup.enums.ConferenceTreatmentIndicator;
import dev.ocean.isup.enums.CallDiversionTreatmentIndicator;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class ForwardServiceInteractionInd {

    private ConferenceTreatmentIndicator conferenceTreatmentIndicator;
    private CallDiversionTreatmentIndicator callDiversionTreatmentIndicator;
    private CallingPartyRestrictionIndicator callingPartyRestrictionIndicator;

    public ForwardServiceInteractionInd() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (conferenceTreatmentIndicator != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, this.conferenceTreatmentIndicator.value());
        }

        if (callDiversionTreatmentIndicator != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 2, callDiversionTreatmentIndicator.value());
        }

        if (callingPartyRestrictionIndicator != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 4, callingPartyRestrictionIndicator.value());
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException("Expecting Context specific[2] tag. Get" + ais.getTagClass());
            }
            switch (tag) {
                case 1:
                    this.conferenceTreatmentIndicator = ConferenceTreatmentIndicator.getInstance((int) ais.readInteger());
                    break;
                case 2:
                    this.callDiversionTreatmentIndicator = CallDiversionTreatmentIndicator.getInstance((int) ais.readInteger());
                    break;
                case 4:
                    this.callingPartyRestrictionIndicator = CallingPartyRestrictionIndicator.getInstance((int) ais.readInteger());
                    break;

            }
        }
    }

    /**
     * @return the conferenceTreatmentIndicator
     */
    public ConferenceTreatmentIndicator getConferenceTreatmentIndicator() {
        return conferenceTreatmentIndicator;
    }

    /**
     * @param conferenceTreatmentIndicator the conferenceTreatmentIndicator to
     * set
     */
    public void setConferenceTreatmentIndicator(ConferenceTreatmentIndicator conferenceTreatmentIndicator) {
        this.conferenceTreatmentIndicator = conferenceTreatmentIndicator;
    }

    /**
     * @return the callDiversionTreatmentIndicator
     */
    public CallDiversionTreatmentIndicator getCallDiversionTreatmentIndicator() {
        return callDiversionTreatmentIndicator;
    }

    /**
     * @param callDiversionTreatmentIndicator the
     * callDiversionTreatmentIndicator to set
     */
    public void setCallDiversionTreatmentIndicator(CallDiversionTreatmentIndicator callDiversionTreatmentIndicator) {
        this.callDiversionTreatmentIndicator = callDiversionTreatmentIndicator;
    }

    /**
     * @return the callingPartyRestrictionIndicator
     */
    public CallingPartyRestrictionIndicator getCallingPartyRestrictionIndicator() {
        return callingPartyRestrictionIndicator;
    }

    /**
     * @param callingPartyRestrictionIndicator the
     * callingPartyRestrictionIndicator to set
     */
    public void setCallingPartyRestrictionIndicator(CallingPartyRestrictionIndicator callingPartyRestrictionIndicator) {
        this.callingPartyRestrictionIndicator = callingPartyRestrictionIndicator;
    }

}
