/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.messages;

import java.io.IOException;
import dev.ocean.sigtran.tcap.TransactionIdUtil;
import dev.ocean.sigtran.tcap.dialogueAPDU.DialoguePortionImpl;
import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.tcap.primitives.tc.PAbortCause;
import dev.ocean.sigtran.utils.ByteUtils;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class AbortMessageImpl implements AbortMessage {

    private Long destinationTransactionId;
    private byte[] uAbortCause;
    private PAbortCause pAbortCause = null;

    protected AbortMessageImpl() {

    }

    protected AbortMessageImpl(Long destinationTransactionId) {
        this();
        this.destinationTransactionId = destinationTransactionId;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Abort[");
        sb.append("Dtid = ").append(destinationTransactionId)
                .append(",UAbortCause = ").append(ByteUtils.bytes2Hex(uAbortCause))
                .append(", PAbortCause = ").append(pAbortCause);
        return sb.toString();
    }

    @Override
    public Long getOriginatingTransactionId() {
        return null;
    }

    @Override
    public void setOriginatingTransactionId(Long id) {

    }

    @Override
    public Long getDestinationTransactionId() {
        return this.destinationTransactionId;
    }

    @Override
    public void setDestinationTransactionId(Long trid) {
        this.destinationTransactionId = trid;
    }

    @Override
    public byte[] getUAbortCause() {
        return this.uAbortCause;
    }

    @Override
    public void setUAbortCause(byte[] uAbortCause) {
        this.uAbortCause = uAbortCause;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {

        if (destinationTransactionId == null) {
            throw new IncorrectSyntaxException("Missing mandatory parameter. DestTransactionId.");
        }

        try {
            aos.writeTag(Tag.CLASS_APPLICATION, false, MessageType.ABORT.value());
            int position = aos.StartContentDefiniteLength();

            byte[] octetData = TransactionIdUtil.encode(destinationTransactionId);
            aos.writeOctetString(Tag.CLASS_APPLICATION, TransactionIdUtil.DESTINATION_TRANSACTION_ID_TAG, octetData);

            if (pAbortCause != null) {
                aos.writeInteger(Tag.CLASS_APPLICATION, 0x0A, pAbortCause.value());
            }

            if (uAbortCause != null) {
                aos.write(uAbortCause);
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
                    || tag != TransactionIdUtil.DESTINATION_TRANSACTION_ID_TAG) {
                throw new IncorrectSyntaxException(String.format("Incorrect tag. Waiting for DTID. CLASS = %d PRIMITIVE = %b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
            }

            this.destinationTransactionId = TransactionIdUtil.decode(tmpAis);

            while (tmpAis.available() > 0) {
                tag = tmpAis.readTag();

                if (tmpAis.getTagClass() == Tag.CLASS_APPLICATION
                        && tmpAis.isTagPrimitive()
                        && tag == 0x0A) {

                    Long _cause = tmpAis.readInteger();
                    this.pAbortCause = PAbortCause.getInstance(_cause.intValue());

                } else if (tmpAis.getTagClass() == Tag.CLASS_APPLICATION
                        && !tmpAis.isTagPrimitive()
                        && tag == DialoguePortionImpl.DIALOGUE_PORTION_TAG) {

                    this.uAbortCause = tmpAis.readSequence();

                } else {

                    throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));

                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public byte[] getComponents() {
        return null;
    }

    @Override
    public byte[] getDialogPortion() {
        return null;
    }

    /**
     * @return the abortSourceType
     */
    @Override
    public PAbortCause getPAbortCause() {
        return pAbortCause;
    }

    @Override
    public void setPAbortCause(PAbortCause pAbortCause) {
        this.pAbortCause = pAbortCause;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.ABORT;
    }
}
