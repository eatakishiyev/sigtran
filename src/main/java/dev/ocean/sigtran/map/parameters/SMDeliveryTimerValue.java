/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import java.io.Serializable;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class SMDeliveryTimerValue implements Serializable {

    private long deliveryTimer;

    public SMDeliveryTimerValue() {
    }

    public SMDeliveryTimerValue(long deliveryTimer) {
        this.deliveryTimer = deliveryTimer;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws  IncorrectSyntaxException {
        try {
            if (deliveryTimer < 30 || deliveryTimer > 600) {
                throw new IncorrectSyntaxException("Unexpected parameter value");
            }
            aos.writeInteger(tagClass, tag, deliveryTimer);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            this.deliveryTimer = ais.readInteger();
            if (deliveryTimer < 30 || deliveryTimer > 600) {
                throw new UnexpectedDataException("Unexpected parameter value");
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the deliveryTimer
     */
    public long getDeliveryTimer() {
        return deliveryTimer;
    }

    /**
     * @param deliveryTimer the deliveryTimer to set
     */
    public void setDeliveryTimer(int deliveryTimer) {
        this.deliveryTimer = deliveryTimer;
    }
}
