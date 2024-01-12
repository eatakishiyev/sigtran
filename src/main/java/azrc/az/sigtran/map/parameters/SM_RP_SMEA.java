/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class SM_RP_SMEA {

    private byte[] data;

    public SM_RP_SMEA() {
    }

    public SM_RP_SMEA(byte[] data) {
        this.data = data;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        if (data == null || data.length < 1 || data.length > 12) {
            throw new IncorrectSyntaxException();
        }
        try {
            aos.writeOctetString(tagClass, tag, this.data);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int length = ais.readLength();
            if (length < 1 || length > 12) {
                throw new IncorrectSyntaxException();
            }
            this.data = ais.readOctetStringData(length);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException();
        }
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }
}
