/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class SubscribedPeriodicLAUTimer {

    /**
     * SubscribedPeriodicLAUtimer ::= INTEGER (0..4294967295)
     * -- This parameter carries the subscribed periodic LAU timer value in
     * seconds.
     */
    private long timerValue;
    public final long MIN_VALUE = 0;
    public final long MAX_VALUE = 4294967295l;

    public SubscribedPeriodicLAUTimer() {
    }

    public SubscribedPeriodicLAUTimer(int timerValue) {
        this.timerValue = timerValue;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws UnexpectedDataException, IncorrectSyntaxException {
        if (timerValue >= MIN_VALUE
                && timerValue <= MAX_VALUE) {
            try {
                aos.writeInteger(tagClass, tag, this.timerValue);
            } catch (IOException | AsnException ex) {
                throw new IncorrectSyntaxException(ex.getMessage());
            }
        } else {
            throw new UnexpectedDataException(String.format("SubscribedPeriodicLAUTimer value [%s] is out of range[0..4294967295].", this.timerValue));
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            this.timerValue = ais.readInteger();
            if (timerValue < MIN_VALUE
                    && timerValue > MAX_VALUE) {
                throw new UnexpectedDataException(String.format("SubscribedPeriodicLAUTimer value [%s] is out of range[0..4294967295].", this.timerValue));
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public long getTimerValue() {
        return this.timerValue;
    }

}
