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
 * DP-AnalysedInfoCriterium::= SEQUENCE {
 * dialledNumber ISDN-AddressString,
 * serviceKey ServiceKey,
 * gsmSCF-Address ISDN-AddressString,
 * defaultCallHandling DefaultCallHandling,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...}
 * @author eatakishiyev
 */
public class DPAnalyzedInfoCriterium  implements MAPParameter{

    private ISDNAddressString dialedNumber;
    private Integer serviceKey;
    private ISDNAddressString gsmSCFAddress;
    private DefaultCallHandling defaultCallHandling;
    private ExtensionContainer extensionContainer;

    public DPAnalyzedInfoCriterium() {
    }

    public DPAnalyzedInfoCriterium(ISDNAddressString dialedNumber, Integer serviceKey, ISDNAddressString gsmSCFAddress, DefaultCallHandling defaultCallHandling) {
        this.dialedNumber = dialedNumber;
        this.serviceKey = serviceKey;
        this.gsmSCFAddress = gsmSCFAddress;
        this.defaultCallHandling = defaultCallHandling;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            dialedNumber.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);

            aos.writeInteger(serviceKey);

            gsmSCFAddress.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);

            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, defaultCallHandling.getValue());

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.STRING_OCTET
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.dialedNumber = new ISDNAddressString(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[OCTET] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == Tag.INTEGER
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.serviceKey = (int) ais.readInteger();
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[INTEGER] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == Tag.STRING_OCTET
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.gsmSCFAddress = new ISDNAddressString(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[OCTET] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == Tag.ENUMERATED
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.defaultCallHandling = DefaultCallHandling.getInstance((int) ais.readInteger());
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[ENUMERATED] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            if (ais.available() > 0) {
                tag = ais.readTag();
                if (tag == Tag.SEQUENCE
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    this.extensionContainer = new ExtensionContainer(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the dialedNumber
     */
    public ISDNAddressString getDialedNumber() {
        return dialedNumber;
    }

    /**
     * @param dialedNumber the dialedNumber to set
     */
    public void setDialedNumber(ISDNAddressString dialedNumber) {
        this.dialedNumber = dialedNumber;
    }

    /**
     * @return the serviceKey
     */
    public Integer getServiceKey() {
        return serviceKey;
    }

    /**
     * @param serviceKey the serviceKey to set
     */
    public void setServiceKey(Integer serviceKey) {
        this.serviceKey = serviceKey;
    }

    /**
     * @return the gsmSCFAddress
     */
    public ISDNAddressString getGsmSCFAddress() {
        return gsmSCFAddress;
    }

    /**
     * @param gsmSCFAddress the gsmSCFAddress to set
     */
    public void setGsmSCFAddress(ISDNAddressString gsmSCFAddress) {
        this.gsmSCFAddress = gsmSCFAddress;
    }

    /**
     * @return the defaultCallHandling
     */
    public DefaultCallHandling getDefaultCallHandling() {
        return defaultCallHandling;
    }

    /**
     * @param defaultCallHandling the defaultCallHandling to set
     */
    public void setDefaultCallHandling(DefaultCallHandling defaultCallHandling) {
        this.defaultCallHandling = defaultCallHandling;
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
