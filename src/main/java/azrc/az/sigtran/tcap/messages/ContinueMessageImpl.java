/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.messages;

import azrc.az.sigtran.tcap.TransactionIdUtil;
import azrc.az.sigtran.tcap.dialogueAPDU.DialoguePortionImpl;
import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.tcap.parameters.interfaces.Component;
import azrc.az.sigtran.utils.ByteUtils;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class ContinueMessageImpl implements ContinueMessage {

    private Long originatingTransactionId;
    private Long destinationTransactionId;
    private byte[] components;
    private byte[] dialoguePortion;

    protected ContinueMessageImpl() {

    }

    protected ContinueMessageImpl(Long destinationTransactionId, Long originatingTransactionId) {
        this();
        this.destinationTransactionId = destinationTransactionId;
        this.originatingTransactionId = originatingTransactionId;
    }

    @Override
    public String toString() {
        return String.format("Continue[Otid = %s; Dtid = %s; DialogPortion = %s; Components = %s]",
                originatingTransactionId, destinationTransactionId,
                ByteUtils.bytes2Hex(dialoguePortion),
                ByteUtils.bytes2Hex(components));
    }

    @Override
    public Long getOriginatingTransactionId() {
        return this.originatingTransactionId;
    }

    @Override
    public void setOriginatingTransactionId(Long id) {
        this.originatingTransactionId = id;
    }

    @Override
    public Long getDestinationTransactionId() {
        return this.destinationTransactionId;
    }

    @Override
    public void setDestinationTransactionId(Long id) {
        this.destinationTransactionId = id;
    }

    @Override
    public byte[] getDialogPortion() {
        return this.dialoguePortion;
    }

    @Override
    public void setDialoguePortion(byte[] dp) {
        this.dialoguePortion = dp;
    }

    @Override
    public byte[] getComponents() {
        return this.components;
    }

    @Override
    public void setComponents(byte[] components) {
        this.components = components;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (originatingTransactionId == null) {
            throw new IncorrectSyntaxException("Missing mandatory parameter. OrigTransactionId");
        }
        if (destinationTransactionId == null) {
            throw new IncorrectSyntaxException("Missing mandatory parameter. DestTransactionId");
        }

        try {
            aos.writeTag(Tag.CLASS_APPLICATION, false, MessageType.CONTINUE.value());
            int position = aos.StartContentDefiniteLength();

            byte[] octetData = TransactionIdUtil.encode(this.originatingTransactionId);
            aos.writeOctetString(Tag.CLASS_APPLICATION, TransactionIdUtil.ORIGINATION_TRANSACTION_ID_TAG, octetData);

            octetData = TransactionIdUtil.encode(this.destinationTransactionId);
            aos.writeOctetString(Tag.CLASS_APPLICATION, TransactionIdUtil.DESTINATION_TRANSACTION_ID_TAG, octetData);

            if (dialoguePortion != null) {
                aos.write(dialoguePortion);
            }

            if (components != null) {
                aos.write(this.components);
            }

            aos.FinalizeContent(position);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {

        try {
            if (ais.getTagClass() != Tag.CLASS_APPLICATION || ais.isTagPrimitive()) {
                throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", ais.getTagClass(), ais.isTagPrimitive(), ais.getTag()));
            }

            AsnInputStream tmpAis = ais.readSequenceStream();

            int tag = tmpAis.readTag();
            if (tmpAis.getTagClass() != Tag.CLASS_APPLICATION
                    || !tmpAis.isTagPrimitive()
                    || tag != TransactionIdUtil.ORIGINATION_TRANSACTION_ID_TAG) {
                throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
            }
            this.originatingTransactionId = TransactionIdUtil.decode(tmpAis);

            tag = tmpAis.readTag();
            if (tmpAis.getTagClass() != Tag.CLASS_APPLICATION
                    || !tmpAis.isTagPrimitive()
                    || tag != TransactionIdUtil.DESTINATION_TRANSACTION_ID_TAG) {
                throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
            }
            this.destinationTransactionId = TransactionIdUtil.decode(tmpAis);

            while (tmpAis.available() > 0) {
                tag = tmpAis.readTag();

                if (tmpAis.getTagClass() != Tag.CLASS_APPLICATION
                        || tmpAis.isTagPrimitive()) {
                    throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
                }

                if (tag == DialoguePortionImpl.DIALOGUE_PORTION_TAG) {
                    this.dialoguePortion = tmpAis.readSequence();
                } else if (tag == Component.COMPONENT_PORTION_TAG) {
                    this.components = tmpAis.readSequence();
                } else {
                    throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CONTINUE;
    }
}
