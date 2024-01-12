/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors.params;

import java.io.IOException;

import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * UnknownSubscriberParam ::= SEQUENCE {
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...,
 * unknownSubscriberDiagnostic UnknownSubscriberDiagnostic OPTIONAL}
 * @author eatakishiyev
 */
public class UnknownSubscriberParam {

    private ExtensionContainer extensionContainer;
    private UnknownSubscriberDiagnostic unknownSubscriberDiagnostic;

    public UnknownSubscriberParam() {
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (this.extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }
            if (this.unknownSubscriberDiagnostic != null) {
                aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, unknownSubscriberDiagnostic.value());
            }
            aos.FinalizeContent(lenPos);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE
                    || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. "
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            AsnInputStream _ais = ais.readSequenceStream();
            
            while (_ais.available() > 0) {
                tag = _ais.readTag();
                if (_ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                    throw new IncorrectSyntaxException(String.format("Expecting Class[UNIVERSAL] tag. Received Class[%s]", _ais.getTagClass()));
                }
                switch (tag) {
                    case Tag.SEQUENCE:
                        this.extensionContainer = new ExtensionContainer(_ais);
                        break;
                    case Tag.ENUMERATED:
                        this.unknownSubscriberDiagnostic = UnknownSubscriberDiagnostic.getInstance((int) _ais.readInteger());
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] received.", tag));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
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

    /**
     * @return the unknownSubscriberDiagnostic
     */
    public UnknownSubscriberDiagnostic getUnknownSubscriberDiagnostic() {
        return unknownSubscriberDiagnostic;
    }

    /**
     * @param unknownSubscriberDiagnostic the unknownSubscriberDiagnostic to set
     */
    public void setUnknownSubscriberDiagnostic(UnknownSubscriberDiagnostic unknownSubscriberDiagnostic) {
        this.unknownSubscriberDiagnostic = unknownSubscriberDiagnostic;
    }

    @Override
    public String toString() {
        return "UnknownSubscriberParam{" +
                "extensionContainer=" + extensionContainer +
                ", unknownSubscriberDiagnostic=" + unknownSubscriberDiagnostic +
                '}';
    }
}
