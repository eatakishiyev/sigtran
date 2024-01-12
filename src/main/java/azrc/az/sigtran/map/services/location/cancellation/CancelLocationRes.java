/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.location.cancellation;

import java.io.IOException;

import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.services.common.MAPResponse;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class CancelLocationRes implements MAPResponse {

    private ExtensionContainer extensionContainer;
    private byte[] responseData;
    protected boolean responseCorrupted = false;

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            if (extensionContainer != null) {
                aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
                int lenPos = aos.StartContentDefiniteLength();
                extensionContainer.encode(aos);
                aos.FinalizeContent(lenPos);
            }
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.responseData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                this.extensionContainer = new ExtensionContainer(ais.readSequenceStream());
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public byte[] getResponseData() {
        return responseData;
    }

    @Override
    public boolean isResponseCorrupted() {
        return responseCorrupted;
    }

}
