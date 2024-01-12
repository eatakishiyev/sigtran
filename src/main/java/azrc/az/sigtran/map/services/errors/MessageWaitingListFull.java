/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors;

import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.MAPUserErrorValues;
import azrc.az.sigtran.map.services.errors.params.MessageWaitListFullParam;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * messageWaitingListFull ERROR ::= {
 * PARAMETER
 * MessageWaitListFullParam
 * -- optional
 * CODE local:33 }
 *
 * @author eatakishiyev
 */
public class MessageWaitingListFull implements MAPUserError {

    private MessageWaitListFullParam messageWaitListFullParam;

    public MessageWaitingListFull() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (this.messageWaitListFullParam != null) {
            messageWaitListFullParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        this.messageWaitListFullParam = new MessageWaitListFullParam();
        messageWaitListFullParam.decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.MESSAGE_WAITING_LIST_FULL;
    }

    public void setMessageWaitListFullParam(MessageWaitListFullParam messageWaitListFullParam) {
        this.messageWaitListFullParam = messageWaitListFullParam;
    }

    public MessageWaitListFullParam getMessageWaitListFullParam() {
        return messageWaitListFullParam;
    }

    @Override
    public String toString() {
        return "MessageWaitingListFull{" +
                "messageWaitListFullParam=" + messageWaitListFullParam +
                '}';
    }
}
