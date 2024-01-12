/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.dialogueAPDU;

import azrc.az.sigtran.tcap.dialogues.intrefaces.DialogueResponsePDU;
import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.tcap.parameters.*;
import azrc.az.sigtran.tcap.parameters.interfaces.ApplicationContext;
import azrc.az.sigtran.tcap.parameters.interfaces.AssociateSourceDiagnostic;
import azrc.az.sigtran.tcap.parameters.interfaces.DialogueServiceProviderDiagnostic;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * @author root
 */
public class DialogueResponsePDUImpl implements DialogueResponsePDU {

    private ProtocolVersionImpl protocolVersion;
    private ApplicationContext applicationContext;
    private AssociateResult result;
    private AssociateSourceDiagnostic resultSourceDiagnostics;
    private UserInformation userInformation;

    protected DialogueResponsePDUImpl() {
        this.protocolVersion = ParameterFactory.createProtocolVersion();
    }

    protected DialogueResponsePDUImpl(ApplicationContext applicationContext, AssociateResult result, AssociateSourceDiagnostic resultSourceDiagnostics) {
        this.applicationContext = applicationContext;
        this.result = result;
        this.resultSourceDiagnostics = resultSourceDiagnostics;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("DialogResponse[");
        sb.append(protocolVersion).append(",")
                .append(applicationContext).append(",")
                .append(result).append(",")
                .append(resultSourceDiagnostics).append(",")
                .append(userInformation)
                .append("]");
        return sb.toString();
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_APPLICATION, false, DIALOGUE_RESPONSE_APDU_TAG);
            int position = aos.StartContentDefiniteLength();

            if (this.protocolVersion != null) {
                this.protocolVersion.encode(aos);
            }

            this.applicationContext.encode(aos);

            this.result.encode(aos);

            this.resultSourceDiagnostics.encode(aos);

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
        if (ais.getTagClass() != Tag.CLASS_APPLICATION || ais.isTagPrimitive()) {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", ais.getTagClass(), ais.isTagPrimitive(), ais.getTag()));
        }
        try {
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

            tag = tmpAis.readTag();
            if (tag == AssociateResult.RESULT_TAG) {
                this.result = ParameterFactory.createAssociateResult(tmpAis);
            } else {
                throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", tmpAis.getTagClass(), tmpAis.isTagPrimitive(), tmpAis.getTag()));
            }

            tag = tmpAis.readTag();
            if (tag == AssociateSourceDiagnosticImpl.RESULT_SOURCE_DIAGNOSTIC_TAG) {
                this.resultSourceDiagnostics = ParameterFactory.createResultSourceDiagnostics(tmpAis);
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

        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public ProtocolVersionImpl getProtocolVersion() {
        return this.protocolVersion;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) {
        this.applicationContext = ac;
    }

    @Override
    public AssociateResult getResult() {
        return this.result;
    }

    @Override
    public void setResult(AssociateResult result) {
        this.result = result;
    }

    @Override
    public AssociateSourceDiagnostic getResutlSourceDiagnostic() {
        return this.resultSourceDiagnostics;
    }

    @Override
    public void setResultSourceDiagnostic(AssociateSourceDiagnostic asd) {
        this.resultSourceDiagnostics = asd;
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
        return DIALOGUE_RESPONSE_APDU_TAG;
    }

    public boolean isUserAssociateSource() {
        return this.resultSourceDiagnostics != null
                && resultSourceDiagnostics.getDialogueServiceUserDiagnosticValue() != null;
    }

    public boolean isProviderAssociateSource() {
        return this.resultSourceDiagnostics != null
                && resultSourceDiagnostics.getDialogueServiceProviderDiagnosticValue() != null;
    }

    public boolean isNoCommonDialogePortion() {
        return this.resultSourceDiagnostics != null
                && resultSourceDiagnostics.getDialogueServiceProviderDiagnosticValue() != null
                && resultSourceDiagnostics.getDialogueServiceProviderDiagnosticValue() == DialogueServiceProviderDiagnostic.NO_COMMON_DIALOGUE_PORTION;
    }
}
