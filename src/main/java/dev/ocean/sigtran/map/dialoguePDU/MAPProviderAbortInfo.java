/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.dialoguePDU;

import java.io.IOException;
import dev.ocean.sigtran.map.dialoguePDU.MAPDialoguePDU;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
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
public class MAPProviderAbortInfo implements MAPDialoguePDU {

    private MapProviderAbortReason mapProviderAbortReason;
    private ExtensionContainer extensionContainer;

    public MAPProviderAbortInfo() {
    }

    public MAPProviderAbortInfo(MapProviderAbortReason mapProviderAbortReason) {
        this.mapProviderAbortReason = mapProviderAbortReason;
    }

    /**
     * @return the mapProviderAbortReason
     */
    public MapProviderAbortReason getMapProviderAbortReason() {
        return mapProviderAbortReason;
    }

    /**
     * @param mapProviderAbortReason the mapProviderAbortReason to set
     */
    public void setMapProviderAbortReason(MapProviderAbortReason mapProviderAbortReason) {
        this.mapProviderAbortReason = mapProviderAbortReason;
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (this.mapProviderAbortReason == null) {
            throw new IncorrectSyntaxException("Mandatory parameter missed");
        }
        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0x05);
            int position = aos.StartContentDefiniteLength();
            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, mapProviderAbortReason.value());

            if (this.extensionContainer != null) {
                this.extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
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
            if (tmpAis.getTagClass() != Tag.CLASS_UNIVERSAL || ais.isTagPrimitive() || tag != Tag.ENUMERATED) {
                throw new IncorrectSyntaxException();
            }
            this.mapProviderAbortReason = MapProviderAbortReason.getInstance(((Long) tmpAis.readInteger()).intValue());
            if (mapProviderAbortReason == MapProviderAbortReason.UNKNOWN) {
                throw new UnexpectedDataException();
            }

            if (tmpAis.available() > 0) {
                tag = tmpAis.readTag();
                if (tmpAis.getTagClass() != Tag.CLASS_UNIVERSAL || tmpAis.isTagPrimitive() || tag != Tag.SEQUENCE) {
                    throw new IncorrectSyntaxException();
                }
                this.extensionContainer = new ExtensionContainer();
                this.extensionContainer.decode(tmpAis);
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
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
