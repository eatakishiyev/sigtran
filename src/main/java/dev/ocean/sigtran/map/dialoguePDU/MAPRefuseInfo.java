/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.dialoguePDU;

import java.io.IOException;
import dev.ocean.sigtran.map.dialoguePDU.MAPDialoguePDU;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextImpl;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class MAPRefuseInfo implements MAPDialoguePDU {

    private Reason reason;
    private ExtensionContainer extensionContainer;
    private MAPApplicationContextImpl alternativeApplicationContext;

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (reason == null) {
            throw new IncorrectSyntaxException();
        }

        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0x03);
            int position = aos.StartContentDefiniteLength();

            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, reason.value());

            if (extensionContainer != null) {
                this.extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            if (alternativeApplicationContext != null) {
                aos.writeObjectIdentifier(alternativeApplicationContext.getOid());
            }
            aos.FinalizeContent(position);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            AsnInputStream tmpAis = ais.readSequenceStream();

            int tag = tmpAis.readTag();
            if (tmpAis.getTagClass() == Tag.CLASS_UNIVERSAL && tmpAis.isTagPrimitive() && tag == Tag.ENUMERATED) {
                this.reason = Reason.getInstance(((Long) tmpAis.readInteger()).intValue());
            } else {
                throw new IncorrectSyntaxException();
            }

            if (this.reason == Reason.UNKNOWN) {
                throw new UnexpectedDataException();
            }

            while (tmpAis.available() > 0) {
                tag = tmpAis.readTag();
                if (tmpAis.getTagClass() == Tag.CLASS_UNIVERSAL && !tmpAis.isTagPrimitive() && tag == Tag.SEQUENCE) {
                    this.extensionContainer = new ExtensionContainer();
                    this.extensionContainer.decode(tmpAis);
                    break;
                } else if (tmpAis.getTagClass() == Tag.CLASS_UNIVERSAL && tmpAis.isTagPrimitive() && tag == Tag.OBJECT_IDENTIFIER) {
                    this.alternativeApplicationContext = new MAPApplicationContextImpl(tmpAis.readObjectIdentifier());
                    break;
                } else {
                    throw new IncorrectSyntaxException();
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the reason
     */
    public Reason getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(Reason reason) {
        this.reason = reason;
    }

    /**
     * @return the alternativeApplicationContet
     */
    public MAPApplicationContextImpl getAlternativeApplicationContext() {
        return alternativeApplicationContext;
    }

    /**
     * @param alternativeApplicationContet the alternativeApplicationContet to
     * set
     */
    public void setAlternativeApplicationContext(MAPApplicationContextImpl alternativeApplicationContet) {
        this.alternativeApplicationContext = alternativeApplicationContet;
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
