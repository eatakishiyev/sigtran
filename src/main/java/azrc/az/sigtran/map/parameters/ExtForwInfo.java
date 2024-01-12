/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * Ext-ForwInfo ::= SEQUENCE {
 * ss-Code SS-Code,
 * forwardingFeatureList Ext-ForwFeatureList,
 * extensionContainer [0] ExtensionContainer OPTIONAL,
 * ...}
 *
 * @author eatakishiyev
 */
public class ExtForwInfo implements MAPParameter {

//
    private SSCode ssCode;
    private ExtForwFeatureList forwardingFeatureList;
    private ExtensionContainer extensionContainer;

    public ExtForwInfo() {
    }

    public ExtForwInfo(SSCode ssCode, ExtForwFeatureList forwardingFeatureList) {
        this.ssCode = ssCode;
        this.forwardingFeatureList = forwardingFeatureList;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws Exception {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        ssCode.encode(aos);
        forwardingFeatureList.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);

        if (extensionContainer != null) {
            extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        }
        aos.FinalizeContent(lenPos);
    }

    @Override
    public void decode(AsnInputStream ais) throws Exception {
        int tag = ais.readTag();
        if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.ssCode = new SSCode();
            ssCode.decode(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Unexpected tag received Tag[%s] Class[%s]. Expecting Tag[STRING_OCTET] Class[UNIVERSAL]",
                    tag, ais.getTagClass()));
        }

        tag = ais.readTag();
        if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.forwardingFeatureList = new ExtForwFeatureList();
            forwardingFeatureList.decode(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Unexpected tag received Tag[%s] Class[%s]. Expecting Tag[SEQUENCE] Class[UNIVERSAL]",
                    tag, ais.getTagClass()));
        }

        if (ais.available() > 0) {
            tag = ais.readTag();
            if (tag == 0 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.extensionContainer = new ExtensionContainer();
                extensionContainer.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected tag received Tag[%s] Class[%s]. Expecting Tag[0] Class[CONTEXT]",
                        tag, ais.getTagClass()));
            }
        }
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setForwardingFeatureList(ExtForwFeatureList forwardingFeatureList) {
        this.forwardingFeatureList = forwardingFeatureList;
    }

    public ExtForwFeatureList getForwardingFeatureList() {
        return forwardingFeatureList;
    }

    public void setSsCode(SSCode ssCode) {
        this.ssCode = ssCode;
    }

    public SSCode getSsCode() {
        return ssCode;
    }

}
