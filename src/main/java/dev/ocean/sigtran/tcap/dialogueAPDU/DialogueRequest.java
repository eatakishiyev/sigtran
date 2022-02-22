/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.dialogueAPDU;

import dev.ocean.sigtran.tcap.dialogues.intrefaces.DialogRequestAPDU;
import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.tcap.parameters.ApplicationContextImpl;
import dev.ocean.sigtran.tcap.parameters.ProtocolVersionImpl;
import dev.ocean.sigtran.tcap.parameters.UserInformationImpl;
import java.io.IOException;
import dev.ocean.sigtran.tcap.parameters.ParameterFactory;
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
    private ApplicationContextImpl applicationContext;
    private UserInformationImpl userInformation;

    protected DialogueRequest() {
        this.protocolVersion = ParameterFactory.createProtocolVersion();
    }

    protected DialogueRequest(ApplicationContextImpl applicationContext) {
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
    public ProtocolVersionImpl getProtocolVersion() {
        return this.protocolVersion;
    }

    @Override
    public void setApplicationContext(ApplicationContextImpl ac) {
        this.applicationContext = ac;
    }

    @Override
    public ApplicationContextImpl getApplicationContext() {
        return this.applicationContext;
    }

    @Override
    public UserInformationImpl getUserInformation() {
        return this.userInformation;
    }

    @Override
    public void setUserInformation(UserInformationImpl userInformation) {
        this.userInformation = userInformation;
    }
}
