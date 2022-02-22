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
 * ATI-NotAllowedParam ::= SEQUENCE { extensionContainer ExtensionContainer
 * OPTIONAL, ...}
 *
 * @author eatakishiyev
 */
public class AtiNotAllowedParam {

    private ExtensionContainer extensionContainer;

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int pos = aos.StartContentDefiniteLength();
            if (extensionContainer != null) {
                extensionContainer.encode(aos);
            }
            aos.FinalizeContent(pos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            if (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    this.extensionContainer = new ExtensionContainer(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%d] Class[%d] received.", tag, ais.getTagClass()));
                }
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

}
