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
 * // CUG-Info ::= SEQUENCE {
 * // cug-SubscriptionList CUG-SubscriptionList,
 * // cug-FeatureList CUG-FeatureList OPTIONAL,
 * // extensionContainer [0] ExtensionContainer OPTIONAL,
 * // ...}
 *
 * @author eatakishiyev
 *
 *
 */
public class CUGInfo implements MAPParameter {

    private CUGSubscriptionList cugSubscriptionList;
    private CUGFeatureList cugFeatureList;
    private ExtensionContainer extensionContainer;
    public final static int EXTENSION_CONTAINER_TAG = 0x00;

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            this.cugSubscriptionList.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);

            if (cugFeatureList != null) {
                cugFeatureList.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }
            if (extensionContainer != null) {
                this.extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, EXTENSION_CONTAINER_TAG, aos);
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
            if (tag == Tag.SEQUENCE
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                cugSubscriptionList = new CUGSubscriptionList();
                cugSubscriptionList.decode(ais.readSequenceStream());
            }

            if (ais.available() > 0) {
                tag = ais.readTag();
                switch (tag) {
                    case Tag.SEQUENCE:
                        if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                            this.cugFeatureList = new CUGFeatureList();
                            this.cugFeatureList.decode(ais.readSequenceStream());
                        } else {
                            throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting Tag[SEQUENCE] Class[UNIVERSAL]."
                                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                    case EXTENSION_CONTAINER_TAG:
                        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                            this.extensionContainer = new ExtensionContainer();
                            this.extensionContainer.decode(ais);
                        } else {
                            throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting Tag[0] Class[CONTEXT]."
                                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the cugSubscriptionList
     */
    public CUGSubscriptionList getCugSubscriptionList() {
        return cugSubscriptionList;
    }

    /**
     * @param cugSubscriptionList the cugSubscriptionList to set
     */
    public void setCugSubscriptionList(CUGSubscriptionList cugSubscriptionList) {
        this.cugSubscriptionList = cugSubscriptionList;
    }

    /**
     * @return the cugFeatureList
     */
    public CUGFeatureList getCugFeatureList() {
        return cugFeatureList;
    }

    /**
     * @param cugFeatureList the cugFeatureList to set
     */
    public void setCugFeatureList(CUGFeatureList cugFeatureList) {
        this.cugFeatureList = cugFeatureList;
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
