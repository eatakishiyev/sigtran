/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.mobility.sms;

import dev.ocean.sigtran.map.parameters.SignalInfo;
import java.io.IOException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import dev.ocean.sigtran.map.services.common.MAPResponse;

/**
 *
 * @author eatakishiyev
 */
public class MAPForwardSMResponse implements MAPResponse {

    private SignalInfo smRPUI;
    private ExtensionContainer extensionContainer;
    private byte[] responseData;
    protected boolean responseCorrupted = false;

    public MAPForwardSMResponse() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (smRPUI != null) {
                smRPUI.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException {
        this.responseData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSLA], "
                        + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());

        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws IncorrectSyntaxException {
        while (ais.available() > 0) {
            try {
                int tag = ais.readTag();

                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive()
                        && tag == Tag.STRING_OCTET) {
                    this.smRPUI = new SignalInfo();
                    this.smRPUI.decode(ais);
                }

                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && !ais.isTagPrimitive()
                        && tag == Tag.SEQUENCE) {
                    this.extensionContainer = new ExtensionContainer();
                    this.extensionContainer.decode(ais);
                }
            } catch (IOException ex) {
                throw new IncorrectSyntaxException(ex.getMessage());
            }
        }
    }

    /**
     * @return the smRPUI
     */
    public SignalInfo getSmRPUI() {
        return smRPUI;
    }

    /**
     * @param smRPUI the smRPUI to set
     */
    public void setSmRPUI(SignalInfo smRPUI) {
        this.smRPUI = smRPUI;
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

    public byte[] getResponseData() {
        return responseData;
    }

    public boolean isResponseCorrupted() {
        return responseCorrupted;
    }

}
