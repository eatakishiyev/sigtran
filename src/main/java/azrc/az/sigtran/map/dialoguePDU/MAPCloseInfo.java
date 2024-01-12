/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.dialoguePDU;

import java.io.IOException;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class MAPCloseInfo implements MAPDialoguePDU {

    private ExtensionContainer extensionContainer;

    public MAPCloseInfo() {
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0x02);
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

    public MAPCloseInfo(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
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
