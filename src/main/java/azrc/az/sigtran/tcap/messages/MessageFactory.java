/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.messages;

import java.io.IOException;

import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;

/**
 *
 * @author eatakishiyev
 */
public class MessageFactory {

    private MessageFactory() {

    }

    public static TCAPMessage createMessage(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            MessageType messageType = MessageType.getInstance(tag);
            switch (messageType) {
                case BEGIN:
                    return createBegin(ais);
                case CONTINUE:
                    return createContinue(ais);
                case END:
                    return createEnd(ais);
                case ABORT:
                    return createAbort(ais);
                default:
                    return createUnknown(ais);

            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public static AbortMessageImpl createAbort() {
        return new AbortMessageImpl();
    }

    public static AbortMessageImpl createAbort(Long destinationTransactionId) {
        return new AbortMessageImpl(destinationTransactionId);
    }

    public static AbortMessageImpl createAbort(AsnInputStream ais) throws IncorrectSyntaxException {
        AbortMessageImpl abortMessage = new AbortMessageImpl();
        abortMessage.decode(ais);
        return abortMessage;
    }

    public static EndMessageImpl createEnd() {
        return new EndMessageImpl();
    }

    public static EndMessageImpl createEnd(Long destinationTransactionId) {
        return new EndMessageImpl(destinationTransactionId);
    }

    public static EndMessageImpl createEnd(AsnInputStream ais) throws IncorrectSyntaxException {
        EndMessageImpl endMessage = new EndMessageImpl();
        endMessage.decode(ais);
        return endMessage;
    }

    public static BeginMessageImpl createBegin() {
        return new BeginMessageImpl();
    }

    public static BeginMessageImpl createBegin(Long originatingTransactionId) {
        return new BeginMessageImpl(originatingTransactionId);
    }

    public static BeginMessageImpl createBegin(AsnInputStream ais) throws IncorrectSyntaxException {
        BeginMessageImpl beginMessage = new BeginMessageImpl();
        beginMessage.decode(ais);
        return beginMessage;
    }

    public static ContinueMessageImpl createContinue() {
        return new ContinueMessageImpl();
    }

    public static ContinueMessageImpl createContinue(Long originatingTransactionId, Long destinationTransactionId) {
        return new ContinueMessageImpl(destinationTransactionId, originatingTransactionId);
    }

    public static ContinueMessageImpl createContinue(AsnInputStream ais) throws IncorrectSyntaxException {
        ContinueMessageImpl continueMessage = new ContinueMessageImpl();
        continueMessage.decode(ais);
        return continueMessage;
    }

    public static UnknownMessage createUnknown(AsnInputStream ais) {
        UnknownMessage unknownMessage = new UnknownMessage();
        unknownMessage.decode(ais);
        return unknownMessage;
    }
}
