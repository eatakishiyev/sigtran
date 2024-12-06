/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.messages;

import azrc.az.sigtran.tcap.TransactionIdUtil;
import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.utils.ByteUtils;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class UnknownMessage implements TCAPMessage {

    private final Logger logger = LoggerFactory.getLogger(UnknownMessage.class);
    private Long destinationTransactionId;
    private Long originatingTransactionId;
    private byte[] data;

    public UnknownMessage() {
    }

    @Override
    public String toString() {
        return String.format("UnknownMessage[DestinationTransactionId = %s; "
                + "OriginatingTransactionId = %s; Data = %s]", destinationTransactionId,
                originatingTransactionId, ByteUtils.bytes2Hex(data));
    }

    @Override
    public void decode(AsnInputStream ais) {
        try {
            int length = ais.readLength();
            AsnInputStream _ais = ais.readSequenceStreamData(length);

            int tag = _ais.readTag();
            if (tag == TransactionIdUtil.ORIGINATION_TRANSACTION_ID_TAG) {
                this.originatingTransactionId = TransactionIdUtil.decode(_ais);
            }

            if (_ais.available() > 0) {
                if (tag == TransactionIdUtil.DESTINATION_TRANSACTION_ID_TAG) {
                    this.destinationTransactionId = TransactionIdUtil.decode(_ais);
                }
            }

            if (_ais.available() > 0) {
                data = new byte[_ais.available()];
                _ais.read(data);
            }
        } catch (IncorrectSyntaxException | IOException | AsnException ex) {
            logger.error("{}", ex);
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.UNKNOWN;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the destinationTransactionId
     */
    public Long getDestinationTransactionId() {
        return destinationTransactionId;
    }

    /**
     * @param destinationTransactionId the destinationTransactionId to set
     */
    public void setDestinationTransactionId(Long destinationTransactionId) {
        this.destinationTransactionId = destinationTransactionId;
    }

    /**
     * @return the originatingTransactionId
     */
    public Long getOriginatingTransactionId() {
        return originatingTransactionId;
    }

    /**
     * @param originatingTransactionId the originatingTransactionId to set
     */
    public void setOriginatingTransactionId(Long originatingTransactionId) {
        this.originatingTransactionId = originatingTransactionId;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public byte[] getComponents() {
        return null;
    }

    @Override
    public byte[] getDialogPortion() {
        return null;
    }

}
