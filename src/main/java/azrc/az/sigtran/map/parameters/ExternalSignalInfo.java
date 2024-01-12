/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * ExternalSignalInfo ::= SEQUENCE {
 * protocolId ProtocolId,
 * signalInfo SignalInfo,
 * -- Information about the internal structure is given in
 * -- clause 7.6.9.
 * extensionContainer ExtensionContainer OPTIONAL,
 * -- extensionContainer must not be used in version 2
 * ...}
 *
 * @author eatakishiyev
 */
public class ExternalSignalInfo implements MAPParameter{

    private ProtocolId protocolId;
    private SignalInfo signalInfo;
    private ExtensionContainer extensionContainer;

    public ExternalSignalInfo() {
    }

    public ExternalSignalInfo(ProtocolId protocolId, SignalInfo signalInfo) {
        this.protocolId = protocolId;
        this.signalInfo = signalInfo;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, protocolId.getValue());

            signalInfo.encode(aos);

            if (extensionContainer != null) {
                extensionContainer.encode(aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.ENUMERATED && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.protocolId = ProtocolId.getInstance((int) ais.readInteger());
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received."
                        + "Expecting Tag[ENUM] Class[UNIVERSAL]"));
            }

            tag = ais.readTag();
            if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.signalInfo = new SignalInfo();
                signalInfo.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received."
                        + "Expecting Tag[OCTET] Class[UNIVERSAL]"));
            }

            if (ais.available() > 0) {
                tag = ais.readTag();
                if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    this.extensionContainer = new ExtensionContainer(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received."
                            + "Expecting Tag[SEQUENCE] Class[UNIVERSAL]"));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the protocolId
     */
    public ProtocolId getProtocolId() {
        return protocolId;
    }

    /**
     * @param protocolId the protocolId to set
     */
    public void setProtocolId(ProtocolId protocolId) {
        this.protocolId = protocolId;
    }

    /**
     * @return the signalInfo
     */
    public SignalInfo getSignalInfo() {
        return signalInfo;
    }

    /**
     * @param signalInfo the signalInfo to set
     */
    public void setSignalInfo(SignalInfo signalInfo) {
        this.signalInfo = signalInfo;
    }

    /**
     * @return the extensionContainer
     */
    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    /**
     * @param extensionContainer the extensionContainer to set
     */
    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }
}
