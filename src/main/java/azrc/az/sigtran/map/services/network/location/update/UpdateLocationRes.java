/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.network.location.update;

import java.io.IOException;

import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.ISDNAddressString;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import azrc.az.sigtran.map.services.common.MAPResponse;

/**
 * UpdateLocationRes ::= SEQUENCE {
 * hlr-Number ISDN-AddressString,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...,
 * add-Capability NULL OPTIONAL,
 * pagingArea-Capability [0]NULL OPTIONAL }
 *
 * @author eatakishiyev
 */
public class UpdateLocationRes implements MAPResponse {

    private ISDNAddressString hlrNumber;
    private ExtensionContainer extensionContainer;
    private boolean addCapability;
    private boolean pagingAreaCapability;
    private byte[] responseData;
    protected boolean responseCorrupted = false;

    public UpdateLocationRes() {

    }

    public UpdateLocationRes(ISDNAddressString hlrNumber) {
        this.hlrNumber = hlrNumber;
    }

    @Override
    public final void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            hlrNumber.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);

            if (extensionContainer != null) {
                this.extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }

            if (addCapability) {
                aos.writeNull();
            }

            if (pagingAreaCapability) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0);
            }

            aos.FinalizeContent(lenPos);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public final void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.responseData = data;
        AsnInputStream ais = new AsnInputStream(data);
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

    private void _decode(AsnInputStream ais) throws IOException, UnexpectedDataException, IncorrectSyntaxException, AsnException {
        int tag = ais.readTag();
        if (tag == Tag.STRING_OCTET
                && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.hlrNumber = new ISDNAddressString(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                    + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
        }

        while (ais.available() > 0) {
            tag = ais.readTag();
            switch (tag) {
                case Tag.STRING_OCTET:
                    if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                        this.extensionContainer = new ExtensionContainer(ais);
                    } else {
                        throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                                + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                    }
                    break;
                case Tag.NULL:
                    if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                        this.addCapability = true;
                        ais.readNull();
                    } else {
                        throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                                + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                    }
                    break;
                case 0:
                    if (ais.getTagClass() == 0) {
                        this.pagingAreaCapability = true;
                        ais.readNull();
                    } else {
                        throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                                + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                    }
                    break;
            }
        }
    }

    /**
     * @return the hlrNumber
     */
    public ISDNAddressString getHlrNumber() {
        return hlrNumber;
    }

    /**
     * @param hlrNumber the hlrNumber to set
     */
    public void setHlrNumber(ISDNAddressString hlrNumber) {
        this.hlrNumber = hlrNumber;
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
     * @return the addCapability
     */
    public Boolean getAddCapability() {
        return addCapability;
    }

    /**
     * @param addCapability the addCapability to set
     */
    public void setAddCapability(Boolean addCapability) {
        this.addCapability = addCapability;
    }

    /**
     * @return the pagingAreaCapability
     */
    public Boolean getPagingAreaCapability() {
        return pagingAreaCapability;
    }

    /**
     * @param pagingAreaCapability the pagingAreaCapability to set
     */
    public void setPagingAreaCapability(Boolean pagingAreaCapability) {
        this.pagingAreaCapability = pagingAreaCapability;
    }

    public byte[] getResponseData() {
        return responseData;
    }

    public boolean isResponseCorrupted() {
        return responseCorrupted;
    }

}
