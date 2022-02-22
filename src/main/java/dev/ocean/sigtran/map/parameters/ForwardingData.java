/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * ForwardingData ::= SEQUENCE {
 * forwardedToNumber [5] ISDN-AddressString OPTIONAL,
 * -- When this datatype is sent from an HLR which supports CAMEL Phase 2
 * -- to a GMSC which supports CAMEL Phase 2 the GMSC shall not check the
 * -- format of the number
 * forwardedToSubaddress [4] ISDN-SubaddressString OPTIONAL,
 * forwardingOptions [6] ForwardingOptions OPTIONAL,
 * extensionContainer [7] ExtensionContainer OPTIONAL,
 * ...,
 * longForwardedToNumber [8] FTN-AddressString OPTIONAL}
 *
 * @author eatakishiyev
 */
public class ForwardingData  implements MAPParameter{

    private ISDNAddressString forwardedToNumber;
    private ISDNSubAddressString forwardedToSubAddress;
    private ForwardingOptions forwardingOptions;
    private ExtensionContainer extensionContainer;
    private FTNAddressString longForwardedToNumber;

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            if (forwardedToNumber != null) {
                forwardedToNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            }
            if (forwardedToSubAddress != null) {
                forwardedToSubAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }
            if (forwardingOptions != null) {
                forwardingOptions.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }
            if (longForwardedToNumber != null) {
                longForwardedToNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 8, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new IncorrectSyntaxException(String.format("Unexpected tag Class[%s] received. Expecting Class[CONTEXT].", ais.getTagClass()));
                }
                switch (tag) {
                    case 5:
                        this.forwardedToNumber = new ISDNAddressString(ais);
                        break;
                    case 4:
                        this.forwardedToSubAddress = new ISDNSubAddressString();
                        forwardedToSubAddress.decode(ais);
                        break;
                    case 6:
                        this.forwardingOptions = new ForwardingOptions();
                        forwardingOptions.decode(ais);
                        break;
                    case 7:
                        this.extensionContainer = new ExtensionContainer(ais);
                        break;
                    case 8:
                        this.longForwardedToNumber = new FTNAddressString();
                        longForwardedToNumber.decode(ais);
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] received.", tag));
                }
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the forwardedToNumber
     */
    public ISDNAddressString getForwardedToNumber() {
        return forwardedToNumber;
    }

    /**
     * @param forwardedToNumber the forwardedToNumber to set
     */
    public void setForwardedToNumber(ISDNAddressString forwardedToNumber) {
        this.forwardedToNumber = forwardedToNumber;
    }

    /**
     * @return the forwardedToSubAddress
     */
    public ISDNSubAddressString getForwardedToSubAddress() {
        return forwardedToSubAddress;
    }

    /**
     * @param forwardedToSubAddress the forwardedToSubAddress to set
     */
    public void setForwardedToSubAddress(ISDNSubAddressString forwardedToSubAddress) {
        this.forwardedToSubAddress = forwardedToSubAddress;
    }

    /**
     * @return the forwardingOptions
     */
    public ForwardingOptions getForwardingOptions() {
        return forwardingOptions;
    }

    /**
     * @param forwardingOptions the forwardingOptions to set
     */
    public void setForwardingOptions(ForwardingOptions forwardingOptions) {
        this.forwardingOptions = forwardingOptions;
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
     * @return the longForwardedToNumber
     */
    public FTNAddressString getLongForwardedToNumber() {
        return longForwardedToNumber;
    }

    /**
     * @param longForwardedToNumber the longForwardedToNumber to set
     */
    public void setLongForwardedToNumber(FTNAddressString longForwardedToNumber) {
        this.longForwardedToNumber = longForwardedToNumber;
    }
}
