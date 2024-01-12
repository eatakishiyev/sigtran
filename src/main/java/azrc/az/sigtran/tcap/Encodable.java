/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap;

import java.io.IOException;
import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author root
 */
public interface Encodable {

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException;

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, AsnException, IOException;
}
