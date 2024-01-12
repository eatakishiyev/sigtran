/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 *
 * @author eatakishiyev
 */
public interface Encodable extends Serializable {

    public void encode(ByteArrayOutputStream baos) throws Exception;

    public void decode(ByteArrayInputStream bais) throws Exception;
}
