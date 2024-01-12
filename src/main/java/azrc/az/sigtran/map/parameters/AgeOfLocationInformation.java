/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * AgeOfLocationInformation::= INTEGER (0..32767) -- the value represents the
 * elapsed time in minutes since the last -- network contact of the mobile
 * station (i.e. the actuality of the -- location information). -- value “0”
 * indicates that the MS is currently in contact with the -- network -- value
 * “32767” indicates that the location information is at least -- 32767 minutes
 * old
 *
 * @author eatakishiyev
 */
public class AgeOfLocationInformation implements MAPParameter {

    private int value;

    public AgeOfLocationInformation(int value) {
        this.value = value;
    }

    public AgeOfLocationInformation() {
    }

    @Override
    public void encode(int tag, int tagClass, AsnOutputStream aos) throws IOException, AsnException {
        if (this.value < 0 || this.value > 32767) {
            throw new AsnException("Parameter AgeOfLocationInformation out of the range [0..32767]");
        }

        aos.writeInteger(tagClass, tag, value);
    }

    @Override
    public void decode(AsnInputStream ais) throws AsnException, IOException {
        this.value = (int) ais.readInteger();

        if (this.value < 0 || this.value > 32767) {
            throw new AsnException("Parameter AgeOfLocationInformation out of the range [0..32767]");
        }
    }

    public int getValue() {
        return value;
    }

}
