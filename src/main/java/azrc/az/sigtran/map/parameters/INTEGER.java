/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public abstract class INTEGER  implements MAPParameter{

    private int value;

    public INTEGER() {
    }

    public INTEGER(int value) {
        this.value = value;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws UnexpectedDataException {
        if (value < getMin() || value > getMax()) {
            throw new UnexpectedDataException("Value must be in range [1..50]. Current value is " + value);
        }
        try {
            aos.writeInteger(tagClass, tag, tag);
        } catch (AsnException | IOException ex) {
            throw new UnexpectedDataException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws UnexpectedDataException {
        try {
            this.value = (int) ais.readInteger();
        } catch (AsnException | IOException ex) {
            throw new UnexpectedDataException(ex);
        }
        if (value < getMin() || value > getMax()) {
            throw new UnexpectedDataException("Value must be in range [1..50]. Current value is " + value);
        }
    }

    public int getValue() {
        return value;
    }

    public abstract int getMin();

    public abstract int getMax();

}
