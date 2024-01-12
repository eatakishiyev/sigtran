/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.enquiry;

import java.io.IOException;

import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.ISDNAddressString;
import azrc.az.sigtran.map.parameters.RequestedInfo;
import azrc.az.sigtran.map.parameters.SubscriberIdentity;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import azrc.az.sigtran.map.services.common.MAPArgument;

/**
 * AnyTimeInterrogationArg ::= SEQUENCE { subscriberIdentity [0]
 * SubscriberIdentity, requestedInfo [1] RequestedInfo, gsmSCF-Address [3]
 * ISDN-AddressString, extensionContainer [2] ExtensionContainer OPTIONAL, ...}
 *
 * @author eatakishiyev
 */
public class AnyTimeInterrogationArg implements MAPArgument {

    private SubscriberIdentity subscriberIdentity;
    private RequestedInfo requestedInfo;
    private ISDNAddressString gsmSCFAddress;
    private ExtensionContainer extensionContainer;
    private byte[] argData;
    protected boolean corruptedArgument = false;

    public AnyTimeInterrogationArg() {
    }

    public AnyTimeInterrogationArg(SubscriberIdentity subscriberIdentity, RequestedInfo requestedInfo, ISDNAddressString gsmSCFAddress) {
        this.subscriberIdentity = subscriberIdentity;
        this.requestedInfo = requestedInfo;
        this.gsmSCFAddress = gsmSCFAddress;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int pos = aos.StartContentDefiniteLength();
            subscriberIdentity.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            requestedInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            gsmSCFAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            aos.FinalizeContent(pos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.argData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {

            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
            this._decode(ais.readSequenceStream());
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IOException, IncorrectSyntaxException, UnexpectedDataException, AsnException {
        int tag = ais.readTag();
        if (tag == 0 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            this.subscriberIdentity = new SubscriberIdentity();
            this.subscriberIdentity.decode(ais.readSequenceStream());
        } else {
            throw new IncorrectSyntaxException(String.format("Expecting Tag[0] Class[CONTEXT]."
                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
        }

        tag = ais.readTag();
        if (tag == 1 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            int v = (int) ais.readInteger();
            this.requestedInfo = new RequestedInfo();
            this.requestedInfo.decode(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Expecting Tag[1] Class[CONTEXT]."
                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
        }

        tag = ais.readTag();
        if (tag == 3 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            this.gsmSCFAddress = new ISDNAddressString(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Expecting Tag[3] Class[CONTEXT]."
                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
        }

        if (ais.available() > 0) {
            tag = ais.readTag();
            if (tag == 2 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.extensionContainer = new ExtensionContainer(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[2] Class[CONTEXT]."
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }
        }
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public ISDNAddressString getGsmSCFAddress() {
        return gsmSCFAddress;
    }

    public void setGsmSCFAddress(ISDNAddressString gsmSCFAddress) {
        this.gsmSCFAddress = gsmSCFAddress;
    }

    public RequestedInfo getRequestedInfo() {
        return requestedInfo;
    }

    public void setRequestedInfo(RequestedInfo requestedInfo) {
        this.requestedInfo = requestedInfo;
    }

    public SubscriberIdentity getSubscriberIdentity() {
        return subscriberIdentity;
    }

    public void setSubscriberIdentity(SubscriberIdentity subscriberIdentity) {
        this.subscriberIdentity = subscriberIdentity;
    }

    @Override
    public boolean isRequestCorrupted() {
        return corruptedArgument;
    }

    @Override
    public byte[] getRequestData() {
        return argData;
    }

}
