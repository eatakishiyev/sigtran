/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters.depricated;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class Time {

    private byte[] time;

    public Time() {
    }

    public Time(byte[] time) {
        this.time = time;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            if (time == null || time.length != 4) {
                throw new IncorrectSyntaxException("Unexpected parameter length. Allowed length is 4");
            }

            aos.writeOctetString(tagClass, tag, time);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int length = ais.readLength();
            if (length != 4) {
                throw new IncorrectSyntaxException("Unexpected parameter length");
            }
            this.time = ais.readOctetStringData(length);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the time
     */
    public byte[] getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(byte[] time) {
        this.time = time;
    }
}
