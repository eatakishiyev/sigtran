/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import dev.ocean.isup.enums.ConferenceTreatmentIndicator;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class BackwardServiceInteractionInd {

    private ConferenceTreatmentIndicator conferenceTreatmentIndicator;
    private CallCompletionTreatmentIndicator callCompletionTreatmentIndicator;

    public BackwardServiceInteractionInd() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (conferenceTreatmentIndicator != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, this.conferenceTreatmentIndicator.value());
        }

        if (callCompletionTreatmentIndicator != null) {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 2, this.callCompletionTreatmentIndicator.value());
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
                    this.callCompletionTreatmentIndicator = CallCompletionTreatmentIndicator.getInstance((int) ais.readInteger());
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
     * @return the callCompletionTreatmentIndicator
     */
    public CallCompletionTreatmentIndicator getCallCompletionTreatmentIndicator() {
        return callCompletionTreatmentIndicator;
    }

    /**
     * @param callCompletionTreatmentIndicator the
     * callCompletionTreatmentIndicator to set
     */
    public void setCallCompletionTreatmentIndicator(CallCompletionTreatmentIndicator callCompletionTreatmentIndicator) {
        this.callCompletionTreatmentIndicator = callCompletionTreatmentIndicator;
    }


    public enum CallCompletionTreatmentIndicator {

        ACCEPT_CALL_COMPLETION_SERVICE_REQUEST(0b00000001),
        REJECT_CALL_COMPLETION_SERVICE_REQUEST(0b00000010),
        UNKNOWN(-1);

        private int value;

        private CallCompletionTreatmentIndicator(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static CallCompletionTreatmentIndicator getInstance(int value) {
            switch (value) {
                case 0b00000001:
                    return ACCEPT_CALL_COMPLETION_SERVICE_REQUEST;
                case 0b00000010:
                    return REJECT_CALL_COMPLETION_SERVICE_REQUEST;
                default:
                    return UNKNOWN;
            }
        }
    }
}
