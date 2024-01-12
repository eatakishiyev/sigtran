/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.mobility.sms;

import java.io.IOException;

import azrc.az.sigtran.map.parameters.AddressStringImpl;
import azrc.az.sigtran.map.parameters.ISDNAddressString;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import azrc.az.sigtran.map.services.common.MAPArgument;

/**
 *
 * @author eatakishiyev
 */
public class AlertServiceCentreArg implements MAPArgument {

    private boolean withoutResult = false;// implemented for AlertServiceCentreWithoutResult(MAP V1)
    private ISDNAddressString msisdn;
    private AddressStringImpl serviceCentreAddress;
    private byte[] requestData;
    protected boolean requestCorrupted = false;

    public AlertServiceCentreArg() {
    }

    public AlertServiceCentreArg(ISDNAddressString msisdn, AddressStringImpl serviceCentreAddress) {
        this.msisdn = msisdn;
        this.serviceCentreAddress = serviceCentreAddress;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (msisdn == null || serviceCentreAddress == null) {
                throw new IncorrectSyntaxException("One or more mandatory parameters are absent");
            }

            msisdn.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            serviceCentreAddress.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.requestData = data;
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

    private void decode_(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET) {
                throw new IncorrectSyntaxException();
            }

            this.msisdn = new ISDNAddressString();
            this.msisdn.decode(ais);

            tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET) {
                throw new IncorrectSyntaxException();
            }
            this.serviceCentreAddress = new AddressStringImpl();
            this.serviceCentreAddress.decode(ais);

        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void setMsidn(ISDNAddressString msisdn) {
        this.msisdn = msisdn;
    }

    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    /**
     * @return the serviceCentreAddress
     */
    public AddressStringImpl getServiceCentreAddress() {
        return serviceCentreAddress;
    }

    /**
     * @param serviceCentreAddress the serviceCentreAddress to set
     */
    public void setServiceCentreAddress(AddressStringImpl serviceCentreAddress) {
        this.serviceCentreAddress = serviceCentreAddress;
    }

    /**
     * implemented for AlertServiceCentreWithoutResult(MAP V1)
     *
     * @return the withoutResult
     */
    public boolean isWithoutResult() {
        return withoutResult;
    }

    /**
     * implemented for AlertServiceCentreWithoutResult(MAP V1)
     *
     * @param withoutResult the withoutResult to set
     */
    public void setWithoutResult(boolean withoutResult) {
        this.withoutResult = withoutResult;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public boolean isRequestCorrupted() {
        return requestCorrupted;
    }

}
