/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.Serializable;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * @author eatakishiyev
 */
public interface MAPParameter extends Serializable {

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws Exception;

    public void decode(AsnInputStream ais) throws Exception;


}
