/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.messages.rkm;

import java.io.IOException;

import azrc.az.sigtran.m3ua.Message;
import azrc.az.sigtran.m3ua.MessageClass;
import azrc.az.sigtran.m3ua.MessageType;
import azrc.az.sigtran.m3ua.io.M3UAMessageByteArrayOutputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import azrc.az.sigtran.m3ua.parameters.RoutingContext;


/**
 *
 * @author root
 */
public class DerigstrationRequest extends Message {

    private RoutingContext routingContext;

    
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
