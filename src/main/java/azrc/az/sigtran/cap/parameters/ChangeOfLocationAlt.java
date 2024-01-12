/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * ChangeOfLocationAlt {PARAMETERS-BOUND : bound} ::= SEQUENCE { ... }
 *
 * @author eatakishiyev
 */
public class ChangeOfLocationAlt {

    public void encode(int tag, int tagClass, AsnOutputStream aos) throws IOException, AsnException {
        aos.writeSequence(tagClass, tag, new byte[]{});
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        byte[] data = ais.readSequence();
    }

}
