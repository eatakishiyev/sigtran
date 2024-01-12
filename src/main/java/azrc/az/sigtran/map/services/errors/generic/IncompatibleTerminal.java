/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors.generic;

import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.MAPUserErrorValues;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class IncompatibleTerminal implements MAPUserError {

    private ExtensionContainer extensionContainer;

    @Override
    public void encode(AsnOutputStream aos) throws Exception {
        if (this.extensionContainer != null) {
            this.getExtensionContainer().encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
        }

    }

    @Override
    public void decode(AsnInputStream ais) throws Exception {

        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && !ais.isTagPrimitive() && tag == Tag.SEQUENCE) {
                this.extensionContainer = new ExtensionContainer();
                this.extensionContainer.decode(ais);
            } else {
                throw new IncorrectSyntaxException();
            }
        }
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.INCOMPATIBLE_TERMINAL;
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
