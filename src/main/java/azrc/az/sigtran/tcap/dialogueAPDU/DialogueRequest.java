/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.dialogueAPDU;

import azrc.az.sigtran.tcap.dialogues.intrefaces.DialogRequestAPDU;
import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.tcap.parameters.*;

import java.io.IOException;

import azrc.az.sigtran.tcap.parameters.interfaces.ApplicationContext;
import azrc.az.sigtran.tcap.parameters.interfaces.ProtocolVersion;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class DialogueRequest implements DialogRequestAPDU {

    private ProtocolVersionImpl protocolVersion;
    private ApplicationContext applicationContext;
    private UserInformation userInformation;

    protected DialogueRequest() {
        this.protocolVersion = ParameterFactory.createProtocolVersion();
    }

    protected DialogueRequest(ApplicationContext applicationContext) {
        this();
        this.applicationContext = applicationContext;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DialogRequest[");
        sb.append(protocolVersion).append(",")
                .append(applicationContext).append(",")
                .append(userInformation)
                .append("]");
        return sb.toString();
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (this.applicationContext == null) {
            throw new IncorrectSyntaxException("Missing mandatory parameter");
        }
        try {
            aos.writeTag(Tag.CLASS_APPLICATION, false, DIALOGUE_REQUEST_APDU_TAG);
            int position = aos.StartContentDefiniteLength();

            if (this.protocolVersion != null) {
                this.protocolVersion.encode(aos);
            }

            this.applicationContext.encode(aos);

            if (this.userInformation != null) {
                this.userInformation.encode(aos);
            }

            aos.FinalizeContent(position);
        } catch (AsnException ex) {
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

            if (tag == ProtocolVersionImpl.TAG) {
                this.protocolVersion = ParameterFactory.createProtocolVersion(tmpAis);
                tag = tmpAis.readTag();
            }

            if (tag == ApplicationContextImpl.TAG) {
                this.applicationContext = ParameterFactory.createApplicationContext(tmpAis);
            } else {
                throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
            }

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

    @Override
    public int getDialogueType() {
        return DIALOGUE_REQUEST_APDU_TAG;
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return this.protocolVersion;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) {
        this.applicationContext = ac;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    @Override
    public UserInformation getUserInformation() {
        return this.userInformation;
    }

    @Override
    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }
}
