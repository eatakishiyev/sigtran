/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.dialoguePDU;

import java.io.IOException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class MAPAcceptInfo implements MAPDialoguePDU {

    private ExtensionContainer extensionContainer;

    public MAPAcceptInfo() {
    }

    public MAPAcceptInfo(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0x01);
            int position = aos.StartContentDefiniteLength();
            if (extensionContainer != null) {
                this.extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }
            aos.FinalizeContent(position);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            AsnInputStream tmpAis = ais.readSequenceStream();

            if (tmpAis.available() > 0) {
                int tag = tmpAis.readTag();
                
                if (tmpAis.getTagClass() != Tag.CLASS_UNIVERSAL || tmpAis.isTagPrimitive() || tag != Tag.SEQUENCE) {
                    throw new IncorrectSyntaxException();
                }
                
                this.extensionContainer = new ExtensionContainer();
                this.extensionContainer.decode(tmpAis);
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }
}
