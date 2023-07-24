/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.messages;

import java.io.IOException;
import dev.ocean.sigtran.tcap.TransactionIdUtil;
import dev.ocean.sigtran.tcap.dialogueAPDU.DialoguePortionImpl;
import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.tcap.parameters.interfaces.Component;
import dev.ocean.sigtran.utils.ByteUtils;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class BeginMessageImpl implements BeginMessage {

    private Long originationTransactionId;
    private byte[] dialoguePortion;
    private byte[] components;

    protected BeginMessageImpl() {

    }

    protected BeginMessageImpl(Long originationTransactionId) {
        this();
        this.originationTransactionId = originationTransactionId;
    }

    @Override
    public String toString() {
        return String.format("BeginMessage[Otid = %s; DialogPortion = %s; Components = %s]",
                originationTransactionId, ByteUtils.bytes2Hex(dialoguePortion),
                ByteUtils.bytes2Hex(components));
    }

    @Override
    public Long getOriginatingTransactionId() {
        return this.originationTransactionId;
    }

    @Override
    public void setOriginatingTransactionId(Long id) {
        this.originationTransactionId = id;
    }

    @Override
    public void setComponents(byte[] components) {
        this.components = components;
    }

    @Override
    public byte[] getComponents() {
        return this.components;
    }

    @Override
    public byte[] getDialogPortion() {
        return this.dialoguePortion;
    }

    @Override
    public void setDialoguePortion(byte[] dialoguePortion) {
        this.dialoguePortion = dialoguePortion;
    }

    @Override
    public Long getDestinationTransactionId() {
        return null;
    }

    @Override
    public void setDestinationTransactionId(Long trid) {

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

            this.originationTransactionId = TransactionIdUtil.decode(tmpAis);

            while (tmpAis.available() > 0) {
                tag = tmpAis.readTag();

                if (tmpAis.getTagClass() != Tag.CLASS_APPLICATION || tmpAis.isTagPrimitive()) {
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

        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (this.originationTransactionId == null) {
            throw new IncorrectSyntaxException("Missing mandatory field. OrigTransactionId");
        }

        try {
            aos.writeTag(Tag.CLASS_APPLICATION, false, MessageType.BEGIN.value());
            int position = aos.StartContentDefiniteLength();

            byte[] octetData = TransactionIdUtil.encode(originationTransactionId);
            aos.writeOctetString(Tag.CLASS_APPLICATION, TransactionIdUtil.ORIGINATION_TRANSACTION_ID_TAG, octetData);

            if (dialoguePortion != null) {
                aos.write(dialoguePortion);
            }

            if (this.components != null) {
                aos.write(this.components);
            }
            aos.FinalizeContent(position);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BEGIN;
    }
}
