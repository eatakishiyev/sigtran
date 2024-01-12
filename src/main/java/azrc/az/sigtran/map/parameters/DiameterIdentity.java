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
public class DiameterIdentity implements MAPParameter {

    private String identity;
    public final int MIN_LENGTH = 9;
    public final int MAX_LENGTH = 55;

    public DiameterIdentity(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        this.decode(ais);
    }

    public DiameterIdentity(String identity) {
        this.identity = identity;
    }

    public DiameterIdentity() {
    }

    @Override
    public final void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        int octetLengh = identity.getBytes().length;
        try {
            if (octetLengh >= MIN_LENGTH
                    && octetLengh <= MAX_LENGTH) {
                aos.writeOctetString(tagClass, tag, identity.getBytes());
            } else {
                throw new UnexpectedDataException(String.format("Unexpected length identity value received. MinLength = 9 , MaxLength = 55, Current = %s", octetLengh));
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public final void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            byte[] value = ais.readOctetString();
            if (value.length < MIN_LENGTH
                    && value.length > MAX_LENGTH) {
                throw new UnexpectedDataException(String.format("Unexpected length identity value received. MinLength = 9 , MaxLength = 55, Current = %s", value.length));
            }
            this.identity = new String(value);

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public String getIdentity() {
        return identity;
    }
}
