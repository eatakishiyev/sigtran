/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class SMDeliveryOutcome {

    private Outcomes outcomes;

    public SMDeliveryOutcome() {
    }

    public SMDeliveryOutcome(Outcomes outcomes) {
        this.outcomes = outcomes;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeInteger(tagClass, tag, outcomes.value);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            this.outcomes = Outcomes.getInstance(((Long) ais.readInteger()).intValue());
            if (this.outcomes == Outcomes.UNKNOWN) {
                throw new IncorrectSyntaxException("Unknown outcome");
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the outcomes
     */
    public Outcomes getOutcomes() {
        return outcomes;
    }

    /**
     * @param outcomes the outcomes to set
     */
    public void setOutcomes(Outcomes outcomes) {
        this.outcomes = outcomes;
    }

    public enum Outcomes {

        MEMORY_CAPACITY_EXCEEDED(0),
        ABSENT_SUBSCRIBER(1),
        SUCCESSFUL_TRANSFER(2),
        UNKNOWN(-1);
        private int value;

        private Outcomes(int value) {
            this.value = value;
        }

        public static Outcomes getInstance(int value) {
            switch (value) {
                case 0:
                    return MEMORY_CAPACITY_EXCEEDED;
                case 1:
                    return ABSENT_SUBSCRIBER;
                case 2:
                    return SUCCESSFUL_TRANSFER;
                default:
                    return UNKNOWN;
            }
        }

        public int value() {
            return value;
        }
    }
}
