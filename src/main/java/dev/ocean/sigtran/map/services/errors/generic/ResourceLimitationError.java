/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.errors.generic;

import java.io.IOException;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.MAPUserErrorValues;
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
public class ResourceLimitationError implements MAPUserError {

    private ExtensionContainer extensionContainer;

    public ResourceLimitationError() {
    }

    public ResourceLimitationError(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (this.extensionContainer != null) {
            this.extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || ais.isTagPrimitive() || tag != Tag.SEQUENCE) {
                throw new IncorrectSyntaxException();
            }

            this.extensionContainer = new ExtensionContainer();
            this.extensionContainer.decode(ais);
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.RESOURCE_LIMITATION;
    }
}
