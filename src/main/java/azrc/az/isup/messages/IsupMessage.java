/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.messages;

import azrc.az.isup.enums.MessageType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author eatakishiyev
 */
public interface IsupMessage extends Cloneable{

    public void encode(ByteArrayOutputStream baos) throws Exception;

    public void decode(ByteArrayInputStream bais) throws Exception;

    public MessageType getMessageType();
}
