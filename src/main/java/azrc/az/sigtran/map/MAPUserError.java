/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface MAPUserError {

    public void encode(AsnOutputStream aos) throws Exception;

    public void decode(AsnInputStream ais) throws Exception;

    public MAPUserErrorValues getMAPUserErrorValue();
}
