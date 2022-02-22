/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters;

import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.tcap.parameters.interfaces.DialogueServiceProviderDiagnostic;
import dev.ocean.sigtran.tcap.parameters.interfaces.DialogueServiceUserDiagnostic;
import dev.ocean.sigtran.tcap.parameters.interfaces.IAssociateSourceDiagnostic;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class AssociateSourceDiagnostic implements IAssociateSourceDiagnostic {

    private DialogueServiceProviderDiagnostic dialogueServiceProviderDiagnostic;
    private DialogueServiceUserDiagnostic dialogueServiceUserDiagnostic;

    public AssociateSourceDiagnostic() {
    }

    public AssociateSourceDiagnostic(DialogueServiceProviderDiagnostic dialogueServiceProviderDiagnostic) {
        this.dialogueServiceProviderDiagnostic = dialogueServiceProviderDiagnostic;
    }

    public AssociateSourceDiagnostic(DialogueServiceUserDiagnostic dialogueServiceUserDiagnostic) {
        this.dialogueServiceUserDiagnostic = dialogueServiceUserDiagnostic;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("AssociateSourceDiagnostic[");
        sb.append("DialogServiceProviderDiagnostic = ").append(dialogueServiceProviderDiagnostic).append(",")
                .append("DialogServiceUserDiagnostic = ").append(dialogueServiceUserDiagnostic).append("]")
                .append("]");
        return sb.toString();
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (dialogueServiceProviderDiagnostic == null
                && dialogueServiceUserDiagnostic == null) {
            throw new IncorrectSyntaxException("Missing mandatory parameters");
        }
        try {

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0x03);
            int position = aos.StartContentDefiniteLength();

            if (dialogueServiceUserDiagnostic != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0x01);
                int _position = aos.StartContentDefiniteLength();
                aos.writeInteger(dialogueServiceUserDiagnostic.value());
                aos.FinalizeContent(_position);
            } else {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0x02);
                int _position = aos.StartContentDefiniteLength();
                aos.writeInteger(dialogueServiceProviderDiagnostic.value());
                aos.FinalizeContent(_position);
            }
            aos.FinalizeContent(position);

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", ais.getTagClass(), ais.isTagPrimitive(), ais.getTag()));
        }

        try {
            AsnInputStream tmpAis = ais.readSequenceStream();

            int tag = tmpAis.readTag();
            if (tmpAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || tmpAis.isTagPrimitive()) {
                throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
            }

            if (tag == DIALOGUE_SERVICE_USER_DIAGNOSTIC_TAG) {
                tmpAis = tmpAis.readSequenceStream();
                tag = tmpAis.readTag();
                int _value = (int) tmpAis.readInteger();
                this.dialogueServiceUserDiagnostic = DialogueServiceUserDiagnostic.getInstance(_value);
            } else if (tag == DIALOGUE_SERVICE_PROVIDER_DIAGNOSTIC_TAG) {
                tmpAis = tmpAis.readSequenceStream();
                tag = tmpAis.readTag();
                int _value = (int) tmpAis.readInteger();
                this.dialogueServiceProviderDiagnostic = DialogueServiceProviderDiagnostic.getInstance(_value);
            }

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void setDialogueServiceUserDiagnosticValue(DialogueServiceUserDiagnostic dialogueServiceUserDiagnostic) {
        this.dialogueServiceUserDiagnostic = dialogueServiceUserDiagnostic;
        this.dialogueServiceProviderDiagnostic = null;
    }

    @Override
    public DialogueServiceUserDiagnostic getDialogueServiceUserDiagnosticValue() {
        return this.dialogueServiceUserDiagnostic;
    }

    @Override
    public void setDialogueServiceProviderDiagnosticValue(DialogueServiceProviderDiagnostic dialogueServiceProviderDiagnostic) {
        this.dialogueServiceProviderDiagnostic = dialogueServiceProviderDiagnostic;
        this.dialogueServiceUserDiagnostic = null;
    }

    @Override
    public DialogueServiceProviderDiagnostic getDialogueServiceProviderDiagnosticValue() {
        return this.dialogueServiceProviderDiagnostic;
    }
}
