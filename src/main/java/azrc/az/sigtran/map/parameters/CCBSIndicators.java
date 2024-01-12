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
 * CCBS-Indicators ::= SEQUENCE {
 * ccbs-Possible [0] NULL OPTIONAL,
 * keepCCBS-CallIndicator [1] NULL OPTIONAL,
 * extensionContainer [2] ExtensionContainer OPTIONAL,
 * ...}
 * @author eatakishiyev
 */
public class CCBSIndicators  implements MAPParameter {

    private boolean ccbsPossible;
    private boolean keepCCBSCallIndicator;
    private ExtensionContainer extensionContainer;

    public CCBSIndicators() {
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            if (ccbsPossible) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0);
            }
            if (keepCCBSCallIndicator) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 1);
            }
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == 0 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.ccbsPossible = true;
                    ais.readNull();
                } else if (tag == 1 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.keepCCBSCallIndicator = true;
                    ais.readNull();
                } else if (tag == 2 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.extensionContainer = new ExtensionContainer(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the ccbsPossible
     */
    public boolean isCcbsPossible() {
        return ccbsPossible;
    }

    /**
     * @param ccbsPossible the ccbsPossible to set
     */
    public void setCcbsPossible(boolean ccbsPossible) {
        this.ccbsPossible = ccbsPossible;
    }

    /**
     * @return the keepCCBSCallIndicator
     */
    public boolean isKeepCCBSCallIndicator() {
        return keepCCBSCallIndicator;
    }

    /**
     * @param keepCCBSCallIndicator the keepCCBSCallIndicator to set
     */
    public void setKeepCCBSCallIndicator(boolean keepCCBSCallIndicator) {
        this.keepCCBSCallIndicator = keepCCBSCallIndicator;
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
