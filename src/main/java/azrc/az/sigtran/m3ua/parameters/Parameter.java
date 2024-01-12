/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.parameters;

import java.io.IOException;
import java.io.Serializable;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;

/**
 * this
 *
 * @author root
 */
public interface Parameter extends Serializable{

    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException;

    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException;

    public ParameterTag getParameterTag();
}
