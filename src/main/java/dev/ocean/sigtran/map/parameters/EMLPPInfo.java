/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * EMLPP-Info ::= SEQUENCE {
 * maximumentitledPriority EMLPP-Priority,
 * defaultPriority EMLPP-Priority,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...}
 *
 * @author eatakishiyev
 */
public class EMLPPInfo implements MAPParameter {

    private EMLPPPriority maximumentitledPriority;
    private EMLPPPriority defaultPriorit;
    private ExtensionContainer extensionContainer;

    @Override
    public void decode(AsnInputStream ais) throws Exception {
        int tag = ais.readTag();
        if (tag == Tag.INTEGER && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.maximumentitledPriority = EMLPPPriority.getInstance(((Long) ais.readInteger()).intValue());
        } else {
            throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received. Expecting Tag[INTEGER] Class[UNIVERSAL]",
                    tag, ais.getTagClass()));
        }

        tag = ais.readTag();
        if (tag == Tag.INTEGER && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.defaultPriorit = EMLPPPriority.getInstance(((Long) ais.readInteger()).intValue());
        } else {
            throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received. Expecting Tag[INTEGER] Class[UNIVERSAL]",
                    tag, ais.getTagClass()));
        }

        if (ais.available() > 0) {
            tag = ais.readTag();
            if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.extensionContainer = new ExtensionContainer();
                extensionContainer.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received. Expecting Tag[SEQUENCE] Class[UNIVERSAL]",
                        tag, ais.getTagClass()));
            }
        }
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws Exception {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        aos.writeInteger(maximumentitledPriority.getValue());
        aos.writeInteger(defaultPriorit.getValue());
        if (extensionContainer != null) {
            extensionContainer.encode(aos);
        }

        aos.FinalizeContent(lenPos);
    }

    /**
     * @return the maximumentitledPriority
     */
    public EMLPPPriority getMaximumentitledPriority() {
        return maximumentitledPriority;
    }

    /**
     * @param maximumentitledPriority the maximumentitledPriority to set
     */
    public void setMaximumentitledPriority(EMLPPPriority maximumentitledPriority) {
        this.maximumentitledPriority = maximumentitledPriority;
    }

    /**
     * @return the defaultPriorit
     */
    public EMLPPPriority getDefaultPriorit() {
        return defaultPriorit;
    }

    /**
     * @param defaultPriorit the defaultPriorit to set
     */
    public void setDefaultPriorit(EMLPPPriority defaultPriorit) {
        this.defaultPriorit = defaultPriorit;
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
