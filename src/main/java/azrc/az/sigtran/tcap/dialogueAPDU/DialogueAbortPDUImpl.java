/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.dialogueAPDU;

import azrc.az.sigtran.tcap.dialogues.intrefaces.DialogueAbortPDU;
import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.tcap.parameters.AbortSource;
import azrc.az.sigtran.tcap.parameters.UserInformation;
import azrc.az.sigtran.tcap.parameters.UserInformationImpl;
import java.io.IOException;

import azrc.az.sigtran.tcap.parameters.ParameterFactory;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class DialogueAbortPDUImpl implements DialogueAbortPDU {

    private AbortSource abortSource;
    private UserInformation userInformation;

    protected DialogueAbortPDUImpl() {
    }

    protected DialogueAbortPDUImpl(AbortSource abortSource) {
        this.abortSource = abortSource;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DialogAbort[")
                .append("AbortSource = ").append(abortSource).append(",")
                .append("UserInformation = ").append(userInformation).append("]");
        return sb.toString();
    }

    @Override
    public void setAbortSource(AbortSource abortSource) {
        this.abortSource = abortSource;
    }

    @Override
    public AbortSource getAbortSource() {
        return this.abortSource;
    }

    @Override
    public UserInformation getUserInformation() {
        return this.userInformation;
    }

    @Override
    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }

    @Override
    public int getDialogueType() {
        return DIALOGUE_ABORT_APDU_TAG;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (abortSource == null) {
            throw new IncorrectSyntaxException("Required abort source");
        }
        try {

            aos.writeTag(Tag.CLASS_APPLICATION, false, DIALOGUE_ABORT_APDU_TAG);
            int position = aos.StartContentDefiniteLength();

            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0x00, abortSource.value());

            if (this.userInformation != null) {
                this.userInformation.encode(aos);
            }

            aos.FinalizeContent(position);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        if (ais.getTagClass() != Tag.CLASS_APPLICATION || ais.isTagPrimitive()) {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", ais.getTagClass(), ais.isTagPrimitive(), ais.getTag()));
        }

        try {
            AsnInputStream tmpAis = ais.readSequenceStream();

            int tag = tmpAis.readTag();

            if (tmpAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !tmpAis.isTagPrimitive() || tag != 0x00) {
                throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
            }

            Long _abortSource = tmpAis.readInteger();
            this.abortSource = AbortSource.getInstance(_abortSource.intValue());

            if (tmpAis.available() > 0) {
                tag = tmpAis.readTag();
                if (tag == UserInformationImpl.USERINFORMATION_TAG) {
                    this.userInformation = ParameterFactory.createUserInformation(tmpAis);
                } else {
                    throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }
}
