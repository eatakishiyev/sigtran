/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 *
 * CUG-Subscription ::= SEQUENCE {
 * cug-Index CUG-Index,
 * cug-Interlock CUG-Interlock,
 * intraCUG-Options IntraCUG-Options,
 * basicServiceGroupList Ext-BasicServiceGroupList OPTIONAL,
 * extensionContainer [0] ExtensionContainer OPTIONAL,
 * ...}
 *
 * @author eatakishiyev
 */
public class CUGSubscription implements MAPParameter {

    private CUGIndex cugIndex;
    private CugInterLock cugInterLock;
    private IntraCUGOptions intraCUGOptions;
    private ExtBasicServiceGroupList basicServiceGroupList;
    private ExtensionContainer extensionContainer;
//    
    public final static int EXTENSION_CONTAINER_TAG = 0x00;

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.INTEGER
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.cugIndex = new CUGIndex();
                this.cugIndex.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting Tag[INTEGER] Class[UNIVERSAL]."
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == Tag.STRING_OCTET
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.cugInterLock = new CugInterLock();
                this.cugInterLock.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting Tag[OCTET] Class[UNIVERSAL]."
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == Tag.INTEGER
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.intraCUGOptions = IntraCUGOptions.getInstance(((Long) ais.readInteger()).intValue());
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting Tag[INTEGER] Class[UNIVERSAL]."
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            if (ais.available() > 0) {
                tag = ais.readTag();
                if (tag == Tag.SEQUENCE) {
                    if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                        this.basicServiceGroupList = new ExtBasicServiceGroupList();
                        this.basicServiceGroupList.decode(ais.readSequenceStream());
                    } else {
                        throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting Tag[SEQUENCE] Class[UNIVERSAL]."
                                + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                    }
                }
                if (tag == EXTENSION_CONTAINER_TAG) {
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        this.extensionContainer = new ExtensionContainer();
                        this.extensionContainer.decode(ais);
                    } else {
                        throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting Tag[0] Class[CONTEXT]."
                                + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                    }
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            this.cugIndex.encode(Tag.CLASS_UNIVERSAL, Tag.INTEGER, aos);

            this.cugInterLock.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);

            aos.writeInteger(intraCUGOptions.getValue());

            if (basicServiceGroupList != null) {
                basicServiceGroupList.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }
            if (extensionContainer != null) {
                this.extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, EXTENSION_CONTAINER_TAG, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the cugIndex
     */
    public CUGIndex getCugIndex() {
        return cugIndex;
    }

    /**
     * @param cugIndex the cugIndex to set
     */
    public void setCugIndex(CUGIndex cugIndex) {
        this.cugIndex = cugIndex;
    }

    /**
     * @return the cugInterLock
     */
    public CugInterLock getCugInterLock() {
        return cugInterLock;
    }

    /**
     * @param cugInterLock the cugInterLock to set
     */
    public void setCugInterLock(CugInterLock cugInterLock) {
        this.cugInterLock = cugInterLock;
    }

    /**
     * @return the intraCUGOptions
     */
    public IntraCUGOptions getIntraCUGOptions() {
        return intraCUGOptions;
    }

    /**
     * @param intraCUGOptions the intraCUGOptions to set
     */
    public void setIntraCUGOptions(IntraCUGOptions intraCUGOptions) {
        this.intraCUGOptions = intraCUGOptions;
    }

    /**
     * @return the basicServiceGroupList
     */
    public ExtBasicServiceGroupList getBasicServiceGroupList() {
        return basicServiceGroupList;
    }

    /**
     * @param basicServiceGroupList the basicServiceGroupList to set
     */
    public void setBasicServiceGroupList(ExtBasicServiceGroupList basicServiceGroupList) {
        this.basicServiceGroupList = basicServiceGroupList;
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

    public enum IntraCUGOptions {

        NO_CUG_RESTRICTIONS(0),
        CUG_IC_CALL_BARRED(1),
        CUG_OG_CALL_BARRED(2);
        
        private final int value;

        private IntraCUGOptions(int value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        public static IntraCUGOptions getInstance(int value) {
            switch (value) {
                case 0:
                    return NO_CUG_RESTRICTIONS;
                case 1:
                    return CUG_IC_CALL_BARRED;
                case 2:
                    return CUG_OG_CALL_BARRED;
                default:
                    return null;
            }
        }
    }
}
