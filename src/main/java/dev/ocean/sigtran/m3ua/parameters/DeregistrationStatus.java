/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.parameters;

import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author root
 */
public class DeregistrationStatus implements Parameter {

    private ParameterTag tag = ParameterTag.DEREGISTRATION_STATUS;
    private long deregistrationStatus;

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }
}
