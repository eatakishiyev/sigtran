/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.service.purging;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.services.common.MAPResponse;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * PurgeMS-Res ::= SEQUENCE {
 * freezeTMSI [0] NULL OPTIONAL,
 * freezeP-TMSI [1] NULL OPTIONAL,
 * extensionContainer ExtensionContainer OPTIONAL
 * ...,
 * freezeM-TMSI [2] NULL OPTIONAL
 * }
 *
 * @author eatakishiyev
 */
public class PurgeMSRes implements MAPResponse {

    private Boolean freezeTMSI;
    private Boolean freezePTMSI;
    private ExtensionContainer extensionContainer;
    private Boolean freezeMTMSI;
    private byte[] rawData;

    public PurgeMSRes() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (freezeTMSI != null && freezeTMSI) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0);
            }

            if (freezePTMSI != null && freezePTMSI) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 1);
            }

            if (extensionContainer != null) {
                extensionContainer.encode(aos);
            }

            if (freezeMTMSI != null && freezeMTMSI) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 2);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] rawData) throws UnexpectedDataException,
            IncorrectSyntaxException {
        this.rawData = rawData;

        AsnInputStream ais = new AsnInputStream(rawData);
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this._decode(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IOException, AsnException,
            IncorrectSyntaxException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            switch (tag) {
                case 0:
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                        throw new IncorrectSyntaxException(String.format("Received unexpected "
                                + "tag. Expecting Tag[0] TagClass[CONTEXT]"
                                + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                    }
                    this.freezeTMSI = true;
                    ais.readNull();
                    break;
                case 1:
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                        throw new IncorrectSyntaxException(String.format("Received unexpected "
                                + "tag. Expecting Tag[1] TagClass[CONTEXT]"
                                + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                    }
                    this.freezePTMSI = true;
                    ais.readNull();
                    break;
                case Tag.SEQUENCE:
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                        throw new IncorrectSyntaxException(String.format("Received unexpected "
                                + "tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                                + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                    }
                    this.extensionContainer = new ExtensionContainer(ais);
                    break;
                case 2:
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                        throw new IncorrectSyntaxException(String.format("Received unexpected "
                                + "tag. Expecting Tag[2] TagClass[CONTEXT]"
                                + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                    }
                    this.freezeMTMSI = true;
                    ais.readNull();
                    break;
            }
        }
    }

    @Override
    public byte[] getResponseData() {
        return this.rawData;
    }

    @Override
    public boolean isResponseCorrupted() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Boolean getFreezeTMSI() {
        return freezeTMSI;
    }

    public void setFreezeTMSI(Boolean freezeTMSI) {
        this.freezeTMSI = freezeTMSI;
    }

    public Boolean getFreezeMTMSI() {
        return freezeMTMSI;
    }

    public void setFreezeMTMSI(Boolean freezeMTMSI) {
        this.freezeMTMSI = freezeMTMSI;
    }

    public Boolean getFreezePTMSI() {
        return freezePTMSI;
    }

    public void setFreezePTMSI(Boolean freezePTMSI) {
        this.freezePTMSI = freezePTMSI;
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

}
