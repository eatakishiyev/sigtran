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
 * CUG-Index ::= INTEGER (0..32767)
 *
 * @author eatakishiyev
 */
public class CUGIndex implements MAPParameter {

    private int value;

    public CUGIndex() {
    }

    public CUGIndex(int value) {
        this.value = value;
    }

    @Override
    public final void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            if (value < 0
                    || value > 32767) {
                throw new UnexpectedDataException("CUGIndex value is out of [0..32767] range. Current value = " + value);
            }
            aos.writeInteger(tagClass, tag, value);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public final void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            this.value = (int) ais.readInteger();
            if (value < 0
                    || value > 32767) {
                throw new UnexpectedDataException("CUGIndex value is out of [0..32767] range. Current value = " + value);
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public int getValue() {
        return value;
    }
}
