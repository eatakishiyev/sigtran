/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.service.roaming.number.enquiry;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.ISDNAddressString;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import dev.ocean.sigtran.map.services.common.MAPResponse;

/**
 * ProvideRoamingNumberRes ::= SEQUENCE {
 * roamingNumber ISDN-AddressString,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...,
 * releaseResourcesSupported NULL OPTIONAL,
 * vmsc-Address ISDN-AddressString OPTIONAL }
 *
 * @author eatakishiyev
 */
public class ProvideRoamingNumberRes implements MAPResponse {

    private ISDNAddressString roamingNumber;
    private ExtensionContainer extensionContainer;
    private boolean releaseResourcesSupported;
    private ISDNAddressString vmscAddress;

    private byte[] responseData;
    protected boolean responseCorrupted;

    public ProvideRoamingNumberRes() {
    }

    public ProvideRoamingNumberRes(ISDNAddressString roamingNumber) {
        this.roamingNumber = roamingNumber;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();
            roamingNumber.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            if (extensionContainer != null) {
                extensionContainer.encode(aos);
            }
            if (releaseResourcesSupported) {
                aos.writeNull();
            }
            if (vmscAddress != null) {
                vmscAddress.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws UnexpectedDataException, IncorrectSyntaxException {
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

    private void _decode(AsnInputStream ais) throws IOException, IncorrectSyntaxException, UnexpectedDataException, AsnException {
        int tag = ais.readTag();
        if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.roamingNumber = new ISDNAddressString(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received."
                    + "Expecting Tag[OCTET] Class[UNIVERSAL]", tag, ais.getTagClass()));
        }

        while (ais.available() > 0) {
            tag = ais.readTag();
            if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.extensionContainer = new ExtensionContainer(ais);
            } else if (tag == Tag.NULL && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.releaseResourcesSupported = true;
                ais.readNull();
            } else if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.vmscAddress = new ISDNAddressString(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, ais.getTagClass()));
            }
        }
    }

    /**
     * @return the roamingNumber
     */
    public ISDNAddressString getRoamingNumber() {
        return roamingNumber;
    }

    /**
     * @param roamingNumber the roamingNumber to set
     */
    public void setRoamingNumber(ISDNAddressString roamingNumber) {
        this.roamingNumber = roamingNumber;
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
     * @return the releaseResourcesSupported
     */
    public boolean isReleaseResourcesSupported() {
        return releaseResourcesSupported;
    }

    /**
     * @param releaseResourcesSupported the releaseResourcesSupported to set
     */
    public void setReleaseResourcesSupported(boolean releaseResourcesSupported) {
        this.releaseResourcesSupported = releaseResourcesSupported;
    }

    /**
     * @return the vmscAddress
     */
    public ISDNAddressString getVmscAddress() {
        return vmscAddress;
    }

    /**
     * @param vmscAddress the vmscAddress to set
     */
    public void setVmscAddress(ISDNAddressString vmscAddress) {
        this.vmscAddress = vmscAddress;
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
