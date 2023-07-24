/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors.params;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * IllegalSubscriberParam ::= SEQUENCE {
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...}
 * @author eatakishiyev
 */
public class IllegalSubscriberParam {

    private ExtensionContainer extensionContainer;

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_UNIVERSAL
                    || tag != Tag.SEQUENCE) {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. "
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            AsnInputStream _ais = ais.readSequenceStream();

            tag = _ais.readTag();
            if (tag == Tag.SEQUENCE
                    && _ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.extensionContainer = new ExtensionContainer(_ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. "
                        + "Received Tag[%s] Class[%s]", tag, _ais.getTagClass()));
            }
        } catch (IOException | AsnException ex) {
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

    @Override
    public String toString() {
        return "IllegalSubscriberParam{" +
                "extensionContainer=" + extensionContainer +
                '}';
    }
}
