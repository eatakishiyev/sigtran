/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * Ext-ForwFeature ::= SEQUENCE {
 * basicService Ext-BasicServiceCode OPTIONAL,
 * ss-Status [4] Ext-SS-Status,
 * forwardedToNumber [5] ISDN-AddressString OPTIONAL,
 * -- When this data type is sent from an HLR which supports CAMEL Phase 2
 * -- to a VLR that supports CAMEL Phase 2 the VLR shall not check the
 * -- format of the number
 * forwardedToSubaddress [8] ISDN-SubaddressString OPTIONAL,
 * forwardingOptions [6] Ext-ForwOptions OPTIONAL,
 * noReplyConditionTime [7] Ext-NoRepCondTime OPTIONAL,
 * extensionContainer [9] ExtensionContainer OPTIONAL,
 * ...,
 * longForwardedToNumber [10] FTN-AddressString OPTIONAL
 * }
 *
 * @author eatakishiyev
 */
public class ExtForwFeature implements MAPParameter {

    private ExtBasicServiceCode basicServiceCode;
    private ExtSSStatus ssStatus;
    private ISDNAddressString forwardedToNumber;
    private ISDNSubAddressString forwardedToSubaddress;
    private ExtForwOptions forwardingOptions;
    private ExtNoRepCondTime noReplyCondTime;
    private ExtensionContainer extensionContainer;
    private FTNAddressString longForwardedToNumber;

    public ExtForwFeature() {
    }

    public ExtForwFeature(ExtSSStatus ssStatus) {
        this.ssStatus = ssStatus;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws Exception {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (basicServiceCode != null) {
            basicServiceCode.encode(aos);
        }

        ssStatus.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);

        if (forwardedToNumber != null) {
            forwardedToNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
        }

        if (forwardedToSubaddress != null) {
            forwardedToSubaddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 8, aos);
        }

        if (forwardingOptions != null) {
            forwardingOptions.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
        }

        if (noReplyCondTime != null) {
            noReplyCondTime.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
        }

        if (extensionContainer != null) {
            extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 9, aos);
        }

        if (longForwardedToNumber != null) {
            longForwardedToNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 10, aos);
        }
        aos.FinalizeContent(lenPos);
    }

    @Override
    public void decode(AsnInputStream ais) throws Exception {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            switch (tag) {
                case ExtBasicServiceCode.EXT_BEARER_SERVICE_TAG:
                case ExtBasicServiceCode.EXT_TELE_SERVICE_TAG:
                    this.basicServiceCode = new ExtBasicServiceCode();
                    basicServiceCode.decode(ais, tag);
                    break;
                case 4:
                    this.ssStatus = new ExtSSStatus();
                    ssStatus.decode(ais);
                    break;
                case 5:
                    this.forwardedToNumber = new ISDNAddressString();
                    forwardedToNumber.decode(ais);
                    break;
                case 8:
                    this.forwardedToSubaddress = new ISDNSubAddressString();
                    forwardedToSubaddress.decode(ais);
                    break;
                case 6:
                    this.forwardingOptions = new ExtForwOptions();
                    forwardingOptions.decode(ais);
                    break;
                case 7:
                    this.noReplyCondTime = new ExtNoRepCondTime();
                    noReplyCondTime.decode(ais);
                    break;
                case 9:
                    this.extensionContainer = new ExtensionContainer();
                    extensionContainer.decode(ais);
                    break;
                case 10:
                    this.longForwardedToNumber = new FTNAddressString();
                    longForwardedToNumber.decode(ais);
                    break;
                default:
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, ais.getTagClass()));
            }
        }
    }

    public ExtBasicServiceCode getBasicServiceCode() {
        return basicServiceCode;
    }

    public void setBasicServiceCode(ExtBasicServiceCode basicServiceCode) {
        this.basicServiceCode = basicServiceCode;
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public ISDNAddressString getForwardedToNumber() {
        return forwardedToNumber;
    }

    public void setForwardedToNumber(ISDNAddressString forwardedToNumber) {
        this.forwardedToNumber = forwardedToNumber;
    }

    public ISDNSubAddressString getForwardedToSubaddress() {
        return forwardedToSubaddress;
    }

    public void setForwardedToSubaddress(ISDNSubAddressString forwardedToSubaddress) {
        this.forwardedToSubaddress = forwardedToSubaddress;
    }

    public ExtForwOptions getForwardingOptions() {
        return forwardingOptions;
    }

    public void setForwardingOptions(ExtForwOptions forwardingOptions) {
        this.forwardingOptions = forwardingOptions;
    }

    public FTNAddressString getLongForwardedToNumber() {
        return longForwardedToNumber;
    }

    public void setLongForwardedToNumber(FTNAddressString longForwardedToNumber) {
        this.longForwardedToNumber = longForwardedToNumber;
    }

    public ExtNoRepCondTime getNoReplyCondTime() {
        return noReplyCondTime;
    }

    public void setNoReplyCondTime(ExtNoRepCondTime noReplyCondTime) {
        this.noReplyCondTime = noReplyCondTime;
    }

    public ExtSSStatus getSsStatus() {
        return ssStatus;
    }

    public void setSsStatus(ExtSSStatus ssStatus) {
        this.ssStatus = ssStatus;
    }

}
