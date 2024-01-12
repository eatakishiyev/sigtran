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
 *
 * @author eatakishiyev
 */
public class SMSDeliverSpecificInfo {

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IOException, AsnException {
        aos.writeNull(tagClass, tag);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        ais.readNull();
    }
}
