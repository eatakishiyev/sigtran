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
 * Ext-SS-Data ::= SEQUENCE {
 * ss-Code SS-Code,
 * ss-Status [4] Ext-SS-Status,
 * ss-SubscriptionOption SS-SubscriptionOption OPTIONAL,
 * basicServiceGroupList Ext-BasicServiceGroupList OPTIONAL,
 * extensionContainer [5] ExtensionContainer OPTIONAL,
 * ...}
 *
 * @author eatakishiyev
 */
public class ExtSSData implements MAPParameter {

    private SSCode ssCode;
    private ExtSSStatus ssStatus;
    private SSSubscriptionOption ssSubscriptionOption;
    private ExtBasicServiceCodeList basicServiceGroupList;
    private ExtensionContainer extensionContainer;
//    
    public final static int EXT_SS_STATUS_TAG = 0x04;
    public final static int EXTENSION_CONTAINER_TAG = 0x05;

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.STRING_OCTET
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.ssCode = new SSCode();
                ssCode.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[STRING_OCTET] TagClass[UNIVERSAL]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == EXT_SS_STATUS_TAG
                    && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.ssStatus = new ExtSSStatus();
                ssStatus.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[4] TagClass[CONTEXT]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            while (ais.available() > 0) {
                tag = ais.readTag();
                switch (tag) {
                    case SSSubscriptionOption.OVERRIDE_CATEGORY:
                    case SSSubscriptionOption.CLI_RESTRICTION_OPTION_TAG:
                        this.ssSubscriptionOption = new SSSubscriptionOption();
                        this.ssSubscriptionOption.decode(ais);
                        break;
                    case Tag.SEQUENCE:
                        this.basicServiceGroupList = new ExtBasicServiceCodeList();
                        this.basicServiceGroupList.decode(ais.readSequenceStream());
                        break;
                    case EXTENSION_CONTAINER_TAG:
                        this.extensionContainer = new ExtensionContainer();
                        this.extensionContainer.decode(ais);
                        break;
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

            ssCode.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            ssStatus.encode(Tag.CLASS_CONTEXT_SPECIFIC, EXT_SS_STATUS_TAG, aos);
            if (ssSubscriptionOption != null) {
                ssSubscriptionOption.encode(aos);
            }
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
     * @return the ssStatus
     */
    public ExtSSStatus getSsStatus() {
        return ssStatus;
    }

    /**
     * @param ssStatus the ssStatus to set
     */
    public void setSsStatus(ExtSSStatus ssStatus) {
        this.ssStatus = ssStatus;
    }

    /**
     * @return the ssSubscriptionOption
     */
    public SSSubscriptionOption getSsSubscriptionOption() {
        return ssSubscriptionOption;
    }

    /**
     * @param ssSubscriptionOption the ssSubscriptionOption to set
     */
    public void setSsSubscriptionOption(SSSubscriptionOption ssSubscriptionOption) {
        this.ssSubscriptionOption = ssSubscriptionOption;
    }

    /**
     * @return the basicServiceGroupList
     */
    public ExtBasicServiceCodeList getBasicServiceGroupList() {
        return basicServiceGroupList;
    }

    /**
     * @param basicServiceGroupList the basicServiceGroupList to set
     */
    public void setBasicServiceGroupList(ExtBasicServiceCodeList basicServiceGroupList) {
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
}
