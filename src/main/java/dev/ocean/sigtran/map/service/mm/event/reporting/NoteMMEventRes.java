/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.service.mm.event.reporting;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.services.common.MAPResponse;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 *
 * NoteMM-EventRes ::= SEQUENCE {
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...}
 */
public class NoteMMEventRes implements MAPResponse {

    private ExtensionContainer extensionContainer;
    private boolean responseCorrupted = false;
    private byte[] rawData;

    public NoteMMEventRes() {
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    @Override
    public byte[] getResponseData() {
        return this.rawData;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();
            if (extensionContainer != null) {
                extensionContainer.encode(aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] rawData) throws IncorrectSyntaxException {
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

    private void _decode(AsnInputStream ais) throws IOException, IncorrectSyntaxException {
        int tag = ais.readTag();
        if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.extensionContainer = new ExtensionContainer(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received."
                    + "Expecting Tag[SEQUENCE] Class[UNIVERSAL]", tag, ais.getTagClass()));
        }
    }

    @Override
    public boolean isResponseCorrupted() {
        return responseCorrupted;
    }
}
