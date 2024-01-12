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
 * NAEA-PreferredCI ::= SEQUENCE {
 * naea-PreferredCIC [0] NAEA-CIC,
 * extensionContainer [1] ExtensionContainer OPTIONAL,
 * ...}
 * @author eatakishiyev
 */
public class NAEAPrefferedCI {

    private NAEACIC naeaPrefferedCIC;
    private ExtensionContainer extensionContainer;

    public NAEAPrefferedCI() {
    }

    public NAEAPrefferedCI(NAEACIC naeaPrefferedCIC) {
        this.naeaPrefferedCIC = naeaPrefferedCIC;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            naeaPrefferedCIC.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            if (tag == 0
                    && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.naeaPrefferedCIC = new NAEACIC();
                naeaPrefferedCIC.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[0] Class[CONTEXT]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            if (ais.available() > 0) {
                tag = ais.readTag();
                if (tag == 1
                        && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.extensionContainer = new ExtensionContainer(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[1] Class[CONTEXT]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the naeaPrefferedCIC
     */
    public NAEACIC getNaeaPrefferedCIC() {
        return naeaPrefferedCIC;
    }

    /**
     * @param naeaPrefferedCIC the naeaPrefferedCIC to set
     */
    public void setNaeaPrefferedCIC(NAEACIC naeaPrefferedCIC) {
        this.naeaPrefferedCIC = naeaPrefferedCIC;
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
