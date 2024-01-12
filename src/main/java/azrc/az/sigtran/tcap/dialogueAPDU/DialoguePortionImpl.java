/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.dialogueAPDU;

import azrc.az.sigtran.tcap.dialogues.intrefaces.DialoguePDU;
import azrc.az.sigtran.tcap.dialogues.intrefaces.DialoguePortion;
import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;

import java.io.IOException;
import java.util.Arrays;
import org.mobicents.protocols.asn.*;

/**
 * NewClass
 *
 * @author root
 */
public class DialoguePortionImpl implements DialoguePortion {

    private long[] oid;
    private DialoguePDU dialoguePDU;
    private boolean isOid;
    private boolean isAsn;

    public DialoguePortionImpl() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("DialogPortion[");
        sb.append("OID = ").append(Arrays.toString(oid)).append(",")
                .append("DialogPDU = ").append(dialoguePDU).append(",")
                .append("isOID = ").append(isOid).append(",")
                .append("isASN = ").append(isAsn)
                .append("]");
        return sb.toString();
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_APPLICATION, false, DIALOGUE_PORTION_TAG);
            int position = aos.StartContentDefiniteLength();

            AsnOutputStream _aos = new AsnOutputStream();
            dialoguePDU.encode(_aos);

            External external = new External();

            external.setOid(this.isOid());
            external.setOidValue(this.oid);

            external.setAsn(this.isAsn());

            external.setEncodeType(_aos.toByteArray());

            external.encode(aos);

            aos.FinalizeContent(position);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream _ais) throws IncorrectSyntaxException {

        try {
            int tag = _ais.readTag();

            if (_ais.getTagClass() != Tag.CLASS_UNIVERSAL || _ais.isTagPrimitive() || tag != Tag.EXTERNAL) {
                throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", _ais.getTagClass(), _ais.isTagPrimitive(), _ais.getTag()));
            }

            External external = new External();
            external.decode(_ais);

            this.isAsn = external.isAsn();
            this.isOid = external.isOid();
            this.oid = external.getOidValue();

            byte[] dialoguePdu = external.getEncodeType();

            _ais = new AsnInputStream(dialoguePdu);

            this.dialoguePDU = DialogueFactory.createDialoguePDU(_ais);

        } catch (AsnException | IOException ex) {
            ex.printStackTrace();
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void setDialogAPDU(DialoguePDU dialogue) {
        this.dialoguePDU = dialogue;
    }

    @Override
    public DialoguePDU getDialogueAPDU() {
        return this.dialoguePDU;
    }

    @Override
    public void setOid(boolean isOid) {
        this.isOid = isOid;
    }

    @Override
    public void setAsn(boolean asn) {
        this.isAsn = asn;
    }

    @Override
    public void setOidValue(long[] oid) {
        this.oid = oid;
    }

    @Override
    public long[] getOidValue() {
        return this.oid;
    }

    /**
     * @return the Oid
     */
    @Override
    public boolean isOid() {
        return this.isOid;
    }

    /**
     * @return the Asn
     */
    @Override
    public boolean isAsn() {
        return isAsn;
    }
}
