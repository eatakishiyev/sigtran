/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * Ext-CallBarInfo ::= SEQUENCE {
 * ss-Code SS-Code,
 * callBarringFeatureList Ext-CallBarFeatureList,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...}
 *
 *
 * @author eatakishiyev
 */
public class ExtCallBarInfo implements MAPParameter {
//    

    private SSCode ssCode;
    private ExtCallBarringFeatureList callBarringFeatureList;
    private ExtensionContainer extensionContainer;

    public ExtCallBarInfo() {
    }

    public ExtCallBarInfo(SSCode ssCode, ExtCallBarringFeatureList callBarringFeatureList) {
        this.ssCode = ssCode;
        this.callBarringFeatureList = callBarringFeatureList;

    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            ssCode.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            this.callBarringFeatureList.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);

            if (extensionContainer != null) {
                this.extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {

            int tag = ais.readTag();
            if (tag == Tag.STRING_OCTET
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.ssCode = new SSCode();
                this.ssCode.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Tag[OCTET_STRING] Class[UNIVERSAL]"
                        + ". Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == Tag.SEQUENCE
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.callBarringFeatureList = new ExtCallBarringFeatureList();
                this.callBarringFeatureList.decode(ais.readSequenceStream());
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Tag[SEQUENCE] Class[UNIVERSAL]"
                        + ". Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            if (ais.available() > 0) {
                tag = ais.readTag();
                if (tag == Tag.SEQUENCE
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    this.extensionContainer = new ExtensionContainer(ais.readSequenceStream());
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Tag[SEQUENCE] Class[UNIVERSAL]"
                            + ". Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the ssCode
     */
    public SSCode getSsCode() {
        return ssCode;
    }

    /**
     * @param ssCode the ssCode to set
     */
    public void setSsCode(SSCode ssCode) {
        this.ssCode = ssCode;
    }

    /**
     * @return the callBarringFeatureList
     */
    public ExtCallBarringFeatureList getCallBarringFeatureList() {
        return callBarringFeatureList;
    }

    /**
     * @param callBarringFeatureList the callBarringFeatureList to set
     */
    public void setCallBarringFeatureList(ExtCallBarringFeatureList callBarringFeatureList) {
        this.callBarringFeatureList = callBarringFeatureList;
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
