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
 * CamelInfo::= SEQUENCE {
 * supportedCamelPhases SupportedCamelPhases,
 * suppress-T-CSI NULL OPTIONAL,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ... ,
 * offeredCamel4CSIs [0] OfferedCamel4CSIs OPTIONAL }
 *
 * @author eatakishiyev
 */
public class CamelInfo implements MAPParameter{

    private SupportedCamelPhases supportedCamelPhases;
    private boolean suppressTCSI;
    private ExtensionContainer extensionContainer;
    private OfferedCamel4CSIs offeredCamel4CSIs;

    public CamelInfo() {
    }

    public CamelInfo(SupportedCamelPhases supportedCamelPhases) {
        this.supportedCamelPhases = supportedCamelPhases;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            supportedCamelPhases.encode(aos);

            if (suppressTCSI) {
                aos.writeNull();
            }
            if (extensionContainer != null) {
                extensionContainer.encode(aos);
            }

            if (offeredCamel4CSIs != null) {
                offeredCamel4CSIs.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
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
            int tag = ais.readTag();
            if (tag == Tag.STRING_BIT && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.supportedCamelPhases = new SupportedCamelPhases();
                supportedCamelPhases.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received."
                        + "Expecting Tag[BITSTRING] Class[UNIVERSAL]", tag, ais.getTagClass()));
            }

            while (ais.available() > 0) {
                tag = ais.readTag();
                if (tag == Tag.NULL && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    this.suppressTCSI = true;
                    ais.readNull();
                } else if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    this.extensionContainer = new ExtensionContainer(ais);
                } else if (tag == 0 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.offeredCamel4CSIs = new OfferedCamel4CSIs();
                    offeredCamel4CSIs.decode(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Received unexpected Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the supportedCamelPhases
     */
    public SupportedCamelPhases getSupportedCamelPhases() {
        return supportedCamelPhases;
    }

    /**
     * @param supportedCamelPhases the supportedCamelPhases to set
     */
    public void setSupportedCamelPhases(SupportedCamelPhases supportedCamelPhases) {
        this.supportedCamelPhases = supportedCamelPhases;
    }

    /**
     * @return the suppressTCSI
     */
    public boolean isSuppressTCSI() {
        return suppressTCSI;
    }

    /**
     * @param suppressTCSI the suppressTCSI to set
     */
    public void setSuppressTCSI(boolean suppressTCSI) {
        this.suppressTCSI = suppressTCSI;
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
     * @return the offeredCamel4CSIs
     */
    public OfferedCamel4CSIs getOfferedCamel4CSIs() {
        return offeredCamel4CSIs;
    }

    /**
     * @param offeredCamel4CSIs the offeredCamel4CSIs to set
     */
    public void setOfferedCamel4CSIs(OfferedCamel4CSIs offeredCamel4CSIs) {
        this.offeredCamel4CSIs = offeredCamel4CSIs;
    }
}
