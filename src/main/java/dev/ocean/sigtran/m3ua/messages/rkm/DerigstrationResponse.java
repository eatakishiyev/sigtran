/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.messages.rkm;

import dev.ocean.sigtran.m3ua.MessageClass;
import dev.ocean.sigtran.m3ua.MessageType;
import dev.ocean.sigtran.m3ua.Message;
import dev.ocean.sigtran.m3ua.io.M3UAMessageByteArrayOutputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.parameters.DeregistrationResult;
import java.io.IOException;

/**
 *
 * @author root
 */
public class DerigstrationResponse extends Message  {

    private DeregistrationResult[] deregistrationResult;

    @Override
    public void encode(M3UAMessageByteArrayOutputStream baos) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MessageClass getMessageClass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MessageType getMessageType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
