/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.dialoguePDU;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.ISDNAddressString;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * MAP-OpenInfo ::= SEQUENCE { destinationReference [0] AddressString OPTIONAL,
 * originationReference [1] AddressString OPTIONAL, ..., extensionContainer
 * ExtensionContainer OPTIONAL -- extensionContainer must not be used in version
 * 2 }
 *
 * @author eatakishiyev
 */
public class MAPOpenInfo implements MAPDialoguePDU {

    private ISDNAddressString destinationReference;
    private ISDNAddressString originationReference;
    private ExtensionContainer extensionContainer;

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, IOException {
        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0x00);
            int position = aos.StartContentDefiniteLength();

            if (destinationReference != null) {
                destinationReference.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x00, aos);
            }

            if (originationReference != null) {
                originationReference.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x01, aos);
            }

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            aos.FinalizeContent(position);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            AsnInputStream tmpAis = ais.readSequenceStream();

            while (tmpAis.available() > 0) {
                int tag = tmpAis.readTag();
                if (tmpAis.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && tmpAis.isTagPrimitive() && tag == 0x00) {
                    this.destinationReference = new ISDNAddressString();
                    this.destinationReference.decode(tmpAis);
                } else if (tmpAis.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && tmpAis.isTagPrimitive() && tag == 0x01) {
                    this.originationReference = new ISDNAddressString();
                    this.originationReference.decode(tmpAis);
                } else if (tmpAis.getTagClass() == Tag.CLASS_UNIVERSAL && !tmpAis.isTagPrimitive() && tag == Tag.SEQUENCE) {
                    this.extensionContainer = new ExtensionContainer();
                    this.extensionContainer.decode(tmpAis);
                } else {
                    throw new IncorrectSyntaxException();
                }
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the destinationReference
     */
    public ISDNAddressString getDestinationReference() {
        return destinationReference;
    }

    /**
     * @param destinationReference the destinationReference to set
     */
    public void setDestinationReference(ISDNAddressString destinationReference) {
        this.destinationReference = destinationReference;
    }

    /**
     * @return the originationReference
     */
    public ISDNAddressString getOriginationReference() {
        return originationReference;
    }

    /**
     * @param originationReference the originationReference to set
     */
    public void setOriginationReference(ISDNAddressString originationReference) {
        this.originationReference = originationReference;
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
