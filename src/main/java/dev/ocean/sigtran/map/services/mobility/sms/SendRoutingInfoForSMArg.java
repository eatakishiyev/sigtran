/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.mobility.sms;

import java.io.IOException;
import dev.ocean.sigtran.map.parameters.AddressStringImpl;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.ISDNAddressString;
import dev.ocean.sigtran.map.parameters.SM_DeliveryNotIntended;
import dev.ocean.sigtran.map.parameters.SM_RP_MTI;
import dev.ocean.sigtran.map.parameters.SM_RP_SMEA;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import dev.ocean.sigtran.map.services.common.MAPArgument;

/**
 *
 * @author eatakishiyev
 */
public class SendRoutingInfoForSMArg implements MAPArgument {

    private ISDNAddressString msisdn;//M
    private boolean smRPPRI = false;//M
    private AddressStringImpl serviceCentreAddress;//M
    private ExtensionContainer extensionContainer;//O
    private boolean gprsSupportIndicator = false;//O
    private SM_RP_MTI smRPMTI;//O
    private SM_RP_SMEA smRPSMEA;//O
    private SM_DeliveryNotIntended sM_DeliveryNotIntended = null;//O
    private boolean ipSMGWGuidanceIndicator = false;//O
    private byte[] requestData;
    protected boolean requestCorrupted = false;

    /*
     * Mandatory fields in the constructor
     */
    public SendRoutingInfoForSMArg() {
    }

    public SendRoutingInfoForSMArg(ISDNAddressString msisdn, boolean smRPPRI, AddressStringImpl serviceCentreAddress) {
        this.msisdn = msisdn;
        this.smRPPRI = smRPPRI;
        this.serviceCentreAddress = serviceCentreAddress;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (getMsisdn() == null || getServiceCentreAddress() == null) {
                throw new IncorrectSyntaxException("One or more mandatory parameters are absent");
            }
            getMsisdn().encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x00, aos);

            aos.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, 0x01, isSmRPPRI());

            getServiceCentreAddress().encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x02, aos);

            if (this.getExtensionContainer() != null) {
                this.getExtensionContainer().encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x06, aos);
            }

            if (isGprsSupportIndicator()) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0x07);
            }

            if (this.getSmRPMTI() != null) {
                this.getSmRPMTI().encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x08, aos);
            }

            if (this.getSmRPSMEA() != null) {
                this.getSmRPSMEA().encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x09, aos);
            }

            if (getsM_DeliveryNotIntended() != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0x0A, this.sM_DeliveryNotIntended.value());
            }

            if (this.isIpSMGWGuidanceIndicator()) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0x0B);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
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
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws AsnException, IOException, IncorrectSyntaxException, UnexpectedDataException {
        int tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()
                || tag != 0x00) {
            throw new IncorrectSyntaxException();
        }
        this.setMsisdn(new ISDNAddressString());
        this.getMsisdn().decode(ais);

        tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()
                || tag != 0x01) {
            throw new IncorrectSyntaxException();
        }
        this.setSmRPPRI(ais.readBoolean());

        tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()
                || tag != 0x02) {
            throw new IncorrectSyntaxException();
        }
        this.setServiceCentreAddress(new AddressStringImpl());
        this.getServiceCentreAddress().decode(ais);

        while (ais.available() > 0) {
            tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && !ais.isTagPrimitive() && tag == 0x06) {
                this.setExtensionContainer(new ExtensionContainer());
                this.getExtensionContainer().decode(ais);
            }

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && ais.isTagPrimitive() && tag == 0x07) {
                this.setGprsSupportIndicator(true);
                ais.readNull();
            }

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && ais.isTagPrimitive() && tag == 0x08) {
                this.setSmRPMTI(new SM_RP_MTI());
                this.getSmRPMTI().decode(ais);
            }

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && ais.isTagPrimitive() && tag == 0x09) {
                this.setSmRPSMEA(new SM_RP_SMEA());
                this.getSmRPSMEA().decode(ais);
            }

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && ais.isTagPrimitive() && tag == 0x0A) {
                int iVal = ((Long) ais.readInteger()).intValue();
                this.setsM_DeliveryNotIntended(SM_DeliveryNotIntended.getInstance(iVal));
            }

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && ais.isTagPrimitive() && tag == 0x0B) {
                this.setIpSMGWGuidanceIndicator(true);
                ais.readNull();
            }
        }
    }

    /**
     * @return the msisdn
     */
    public ISDNAddressString getMsisdn() {
        return msisdn;
    }

    /**
     * @param msisdn the msisdn to set
     */
    public void setMsisdn(ISDNAddressString msisdn) {
        this.msisdn = msisdn;
    }

    /**
     * @return the smRPPRI
     */
    public boolean isSmRPPRI() {
        return smRPPRI;
    }

    /**
     * @param smRPPRI the smRPPRI to set
     */
    public void setSmRPPRI(boolean smRPPRI) {
        this.smRPPRI = smRPPRI;
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
     * @return the gprsSupportIndicator
     */
    public boolean isGprsSupportIndicator() {
        return gprsSupportIndicator;
    }

    /**
     * @param gprsSupportIndicator the gprsSupportIndicator to set
     */
    public void setGprsSupportIndicator(boolean gprsSupportIndicator) {
        this.gprsSupportIndicator = gprsSupportIndicator;
    }

    /**
     * @return the smRPMTI
     */
    public SM_RP_MTI getSmRPMTI() {
        return smRPMTI;
    }

    /**
     * @param smRPMTI the smRPMTI to set
     */
    public void setSmRPMTI(SM_RP_MTI smRPMTI) {
        this.smRPMTI = smRPMTI;
    }

    /**
     * @return the smRPSMEA
     */
    public SM_RP_SMEA getSmRPSMEA() {
        return smRPSMEA;
    }

    /**
     * @param smRPSMEA the smRPSMEA to set
     */
    public void setSmRPSMEA(SM_RP_SMEA smRPSMEA) {
        this.smRPSMEA = smRPSMEA;
    }

    /**
     * @return the sM_DeliveryNotIntended
     */
    public SM_DeliveryNotIntended getsM_DeliveryNotIntended() {
        return sM_DeliveryNotIntended;
    }

    /**
     * @param sM_DeliveryNotIntended the sM_DeliveryNotIntended to set
     */
    public void setsM_DeliveryNotIntended(SM_DeliveryNotIntended sM_DeliveryNotIntended) {
        this.sM_DeliveryNotIntended = sM_DeliveryNotIntended;
    }

    /**
     * @return the ipSMGWGuidanceIndicator
     */
    public boolean isIpSMGWGuidanceIndicator() {
        return ipSMGWGuidanceIndicator;
    }

    /**
     * @param ipSMGWGuidanceIndicator the ipSMGWGuidanceIndicator to set
     */
    public void setIpSMGWGuidanceIndicator(boolean ipSMGWGuidanceIndicator) {
        this.ipSMGWGuidanceIndicator = ipSMGWGuidanceIndicator;
    }

    @Override
    public String toString() {
        return String.format("SEND-ROUTING-INFO-FOR-SM-ARG: "
                + "MSISDN = %s "
                + "SM-RP-PRI = %s "
                + "SERVICE-CENTRE-ADDRESS = %s "
                + "GPRS-SUPPORT-INDICATOR = %s "
                + "SM-RP-MTI = %s "
                + "SM-RP-SMEA = %s "
                + "SM-DELIVERY-NOT-INTENDED = %s "
                + "IP-SM-GW-GUIDANCE-INDICATOR = %s",
                this.msisdn,
                this.smRPPRI,
                this.serviceCentreAddress,
                this.gprsSupportIndicator,
                this.smRPMTI,
                this.smRPSMEA,
                this.sM_DeliveryNotIntended,
                this.ipSMGWGuidanceIndicator);
    }

    @Override
    public byte[] getRequestData() {
        return requestData;
    }

    @Override
    public boolean isRequestCorrupted() {
        return requestCorrupted;
    }
}
