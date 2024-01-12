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
 * SS-CamelData ::= SEQUENCE {
 * ss-EventList SS-EventList,
 * gsmSCF-Address ISDN-AddressString,
 * extensionContainer [0] ExtensionContainer OPTIONAL,
 * ...}
 * @author eatakishiyev
 */
public class SSCamelData {

    private SSEventList ssEventList;
    private ISDNAddressString gsmSCFAddress;
    private ExtensionContainer extensionContainer;

    public SSCamelData() {
    }

    public SSCamelData(SSEventList ssEventList, ISDNAddressString gsmSCFAddress) {
        this.ssEventList = ssEventList;
        this.gsmSCFAddress = gsmSCFAddress;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            ssEventList.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            gsmSCFAddress.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.SEQUENCE
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.ssEventList = new SSEventList();
                ssEventList.decode(ais.readSequenceStream());
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == Tag.STRING_OCTET
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.gsmSCFAddress = new ISDNAddressString(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[OCTET] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            if (ais.available() > 0) {
                if (tag == 0
                        && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.extensionContainer = new ExtensionContainer(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[0] Class[CONTEXT]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the ssEventList
     */
    public SSEventList getSsEventList() {
        return ssEventList;
    }

    /**
     * @param ssEventList the ssEventList to set
     */
    public void setSsEventList(SSEventList ssEventList) {
        this.ssEventList = ssEventList;
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
