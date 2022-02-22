/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.service.anytime.information.enquiry;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.SubscriberInfo;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import dev.ocean.sigtran.map.services.common.MAPResponse;

/**
 * AnyTimeInterrogationRes ::= SEQUENCE {
 * subscriberInfo SubscriberInfo,
 * extensionContainer ExtensionContainer OPTIONAL, ...}
 *
 * @author eatakishiyev
 */
public class AnyTimeInterrogationRes implements MAPResponse {

    private SubscriberInfo subcriberInfo;
    private ExtensionContainer extensionContainer;
    private byte[] responseData;
    protected boolean responseCorrupted;

    public AnyTimeInterrogationRes() {
    }

    public AnyTimeInterrogationRes(SubscriberInfo subcriberInfo) {
        this.subcriberInfo = subcriberInfo;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException, IllegalNumberFormatException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();
            subcriberInfo.encode(aos);
            if (extensionContainer != null) {
                extensionContainer.encode(aos);
            }
            aos.FinalizeContent(lenPos);
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
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
            this._decode(ais.readSequenceStream());
        } catch (IOException | AsnException | IllegalNumberFormatException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IOException, IncorrectSyntaxException, UnexpectedDataException, IllegalNumberFormatException {

        int tag = ais.readTag();
        if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            subcriberInfo = new SubscriberInfo();
            subcriberInfo.decode(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Unexpected TagClass received. Expecting Class[UNIVERSAL], found Tag[%d] Class[%d]", tag, ais.getTagClass()));
        }

        if (ais.available() > 0) {
            tag = ais.readTag();
            if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                extensionContainer = new ExtensionContainer();
                extensionContainer.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected TagClass received. Expecting Class[UNIVERSAL], found Tag[%d] Class[%d]", tag, ais.getTagClass()));
            }

        }
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public SubscriberInfo getSubcriberInfo() {
        return subcriberInfo;
    }

    public void setSubcriberInfo(SubscriberInfo subcriberInfo) {
        this.subcriberInfo = subcriberInfo;
    }

    @Override
    public byte[] getResponseData() {
        return responseData;
    }

    @Override
    public boolean isResponseCorrupted() {
        return this.responseCorrupted;
    }

}
