/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.mobility.sms;

import java.io.IOException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.ISDNAddressString;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import dev.ocean.sigtran.map.services.common.MAPResponse;

/**
 *
 * @author eatakishiyev
 */
public class ReportSMDeliveryStatusRes implements MAPResponse {

    private ISDNAddressString storedMsisdn;
    private ExtensionContainer extensionContainer;
    private byte[] responseData;
    protected boolean responseCorrupted = false;

    public ReportSMDeliveryStatusRes() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (storedMsisdn != null) {
                this.storedMsisdn.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }

            if (this.extensionContainer != null) {
                this.extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
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
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSLA], "
                        + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws IncorrectSyntaxException, IOException, UnexpectedDataException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive() && tag == Tag.STRING_OCTET) {
                this.storedMsisdn = new ISDNAddressString();
                this.storedMsisdn.decode(ais);
            } else if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && !ais.isTagPrimitive() && tag == Tag.SEQUENCE) {
                this.extensionContainer = new ExtensionContainer();
                this.extensionContainer.decode(ais);
            } else {
                throw new IncorrectSyntaxException("Unknown parameter");
            }
        }
    }

    /**
     * @return the storedMsisdn
     */
    public ISDNAddressString getStoredMsisdn() {
        return storedMsisdn;
    }

    /**
     * @param storedMsisdn the storedMsisdn to set
     */
    public void setStoredMsisdn(ISDNAddressString storedMsisdn) {
        this.storedMsisdn = storedMsisdn;
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

    @Override
    public String toString() {
        return String.format("REPORT-SM-DELIVERY-STATUS-RES: STORED_MSISDN = %s", this.storedMsisdn);
    }

    public byte[] getResponseData() {
        return responseData;
    }

    public boolean isResponseCorrupted() {
        return responseCorrupted;
    }

}
