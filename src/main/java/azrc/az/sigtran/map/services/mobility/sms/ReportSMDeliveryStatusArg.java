/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.mobility.sms;

import java.io.IOException;

import azrc.az.sigtran.map.parameters.AddressStringImpl;
import azrc.az.sigtran.map.parameters.SMDeliveryOutcome;
import azrc.az.sigtran.map.parameters.AbsentSubscriberDiagnosticSM;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
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
public class ReportSMDeliveryStatusArg implements MAPArgument {

    private ISDNAddressString msisdn;
    private AddressStringImpl serviceCentreAddress;
    private SMDeliveryOutcome sMDeliveryOutcome;
    private AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM;
    private ExtensionContainer extensionContainer;
    private Boolean gprsSupportIndicator = false;
    private Boolean deliveryOutcomeIndicator = false;
    private SMDeliveryOutcome additionalSMDeliveryOutcome;
    private AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM;
    private Boolean ipSmGwIndicator = false;
    private SMDeliveryOutcome ipSmGwSmDeliveryOutcome;
    private AbsentSubscriberDiagnosticSM ipSmGwAbsentSubscriberDiagnosticSM;
    private byte[] requestData;
    protected boolean requestCorrupted = false;

    public ReportSMDeliveryStatusArg() {
    }

    /**
     * Constructor with mandatory parameters
     *
     * @param msisdn
     * @param serviceCentreAddress
     * @param sMDeliveryOutcome
     */
    public ReportSMDeliveryStatusArg(ISDNAddressString msisdn, AddressStringImpl serviceCentreAddress, SMDeliveryOutcome sMDeliveryOutcome) {
        this.msisdn = msisdn;
        this.serviceCentreAddress = serviceCentreAddress;
        this.sMDeliveryOutcome = sMDeliveryOutcome;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (this.msisdn == null || this.serviceCentreAddress == null || this.sMDeliveryOutcome == null) {
                throw new IncorrectSyntaxException("One or more mandatory parameters are absent");
            }
            this.msisdn.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            this.serviceCentreAddress.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            this.sMDeliveryOutcome.encode(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, aos);

            if (this.absentSubscriberDiagnosticSM != null) {
                this.absentSubscriberDiagnosticSM.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x00, aos);
            }

            if (this.extensionContainer != null) {
                this.extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x01, aos);
            }

            if (this.gprsSupportIndicator) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0x02);
            }

            if (this.deliveryOutcomeIndicator) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0x03);
            }

            if (this.additionalSMDeliveryOutcome != null) {
                this.additionalSMDeliveryOutcome.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x04, aos);
            }

            if (this.additionalAbsentSubscriberDiagnosticSM != null) {
                this.additionalAbsentSubscriberDiagnosticSM.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x05, aos);
            }

            if (this.ipSmGwIndicator) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0x06);
            }

            if (ipSmGwSmDeliveryOutcome != null) {
                this.ipSmGwSmDeliveryOutcome.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x07, aos);
            }

            if (ipSmGwAbsentSubscriberDiagnosticSM != null) {
                this.ipSmGwAbsentSubscriberDiagnosticSM.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x08, aos);
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

    private void decode_(AsnInputStream ais) throws IncorrectSyntaxException, AsnException, IOException, UnexpectedDataException {
        int tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET) {
            throw new IncorrectSyntaxException("Incorrect formed tag");
        }
        this.msisdn = new ISDNAddressString();
        this.msisdn.decode(ais);

        tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET) {
            throw new IncorrectSyntaxException("Incorrect formed tag");
        }
        this.serviceCentreAddress = new AddressStringImpl();
        this.serviceCentreAddress.decode(ais);

        tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.ENUMERATED) {
            throw new IncorrectSyntaxException("Incorrect formed tag");
        }
        this.sMDeliveryOutcome = new SMDeliveryOutcome();
        this.sMDeliveryOutcome.decode(ais);

        while (ais.available() > 0) {
            tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && ais.isTagPrimitive() && tag == 0x00) {
                this.absentSubscriberDiagnosticSM = new AbsentSubscriberDiagnosticSM();
                this.absentSubscriberDiagnosticSM.decode(ais);
            } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && !ais.isTagPrimitive() && tag == 0x01) {
                this.extensionContainer = new ExtensionContainer();
                this.extensionContainer.decode(ais);
            } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && ais.isTagPrimitive() && tag == 0x02) {
                this.gprsSupportIndicator = true;
                ais.readNull();
            } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && ais.isTagPrimitive() && tag == 0x03) {
                this.deliveryOutcomeIndicator = true;
                ais.readNull();
            } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && ais.isTagPrimitive() && tag == 0x04) {
                this.additionalSMDeliveryOutcome = new SMDeliveryOutcome();
                this.additionalSMDeliveryOutcome.decode(ais);
            } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && ais.isTagPrimitive() && tag == 0x05) {
                this.additionalAbsentSubscriberDiagnosticSM = new AbsentSubscriberDiagnosticSM();
                this.additionalAbsentSubscriberDiagnosticSM.decode(ais);
            } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && ais.isTagPrimitive() && tag == 0x06) {
                this.ipSmGwIndicator = true;
                ais.readNull();
            } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && ais.isTagPrimitive() && tag == 0x07) {
                this.additionalSMDeliveryOutcome = new SMDeliveryOutcome();
                this.additionalSMDeliveryOutcome.decode(ais);
            } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && ais.isTagPrimitive() && tag == 0x08) {
                this.ipSmGwAbsentSubscriberDiagnosticSM = new AbsentSubscriberDiagnosticSM();
                this.ipSmGwAbsentSubscriberDiagnosticSM.decode(ais);
            } else {
                throw new IncorrectSyntaxException("Unknown parameter");
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
     * @return the sMDeliveryOutcome
     */
    public SMDeliveryOutcome getsMDeliveryOutcome() {
        return sMDeliveryOutcome;
    }

    /**
     * @param sMDeliveryOutcome the sMDeliveryOutcome to set
     */
    public void setsMDeliveryOutcome(SMDeliveryOutcome sMDeliveryOutcome) {
        this.sMDeliveryOutcome = sMDeliveryOutcome;
    }

    /**
     * @return the absentSubscriberDiagnosticSM
     */
    public AbsentSubscriberDiagnosticSM getAbsentSubscriberDiagnosticSM() {
        return absentSubscriberDiagnosticSM;
    }

    /**
     * @param absentSubscriberDiagnosticSM the absentSubscriberDiagnosticSM to
     * set
     */
    public void setAbsentSubscriberDiagnosticSM(AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM) {
        this.absentSubscriberDiagnosticSM = absentSubscriberDiagnosticSM;
    }

    /**
     * @return the gprsSupportIndicator
     */
    public Boolean getGprsSupportIndicator() {
        return gprsSupportIndicator;
    }

    /**
     * @param gprsSupportIndicator the gprsSupportIndicator to set
     */
    public void setGprsSupportIndicator(Boolean gprsSupportIndicator) {
        this.gprsSupportIndicator = gprsSupportIndicator;
    }

    /**
     * @return the deliveryOutcomeIndicator
     */
    public Boolean getDeliveryOutcomeIndicator() {
        return deliveryOutcomeIndicator;
    }

    /**
     * @param deliveryOutcomeIndicator the deliveryOutcomeIndicator to set
     */
    public void setDeliveryOutcomeIndicator(Boolean deliveryOutcomeIndicator) {
        this.deliveryOutcomeIndicator = deliveryOutcomeIndicator;
    }

    /**
     * @return the additionalDeliveryOutcome
     */
    public SMDeliveryOutcome getAdditionalDeliveryOutcome() {
        return additionalSMDeliveryOutcome;
    }

    /**
     * @param additionalDeliveryOutcome the additionalDeliveryOutcome to set
     */
    public void setAdditionalDeliveryOutcome(SMDeliveryOutcome additionalDeliveryOutcome) {
        this.additionalSMDeliveryOutcome = additionalDeliveryOutcome;
    }

    /**
     * @return the additionalAbsentSubscriberDiagnosticSM
     */
    public AbsentSubscriberDiagnosticSM getAdditionalAbsentSubscriberDiagnosticSM() {
        return additionalAbsentSubscriberDiagnosticSM;
    }

    /**
     * @param additionalAbsentSubscriberDiagnosticSM the
     * additionalAbsentSubscriberDiagnosticSM to set
     */
    public void setAdditionalAbsentSubscriberDiagnosticSM(AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM) {
        this.additionalAbsentSubscriberDiagnosticSM = additionalAbsentSubscriberDiagnosticSM;
    }

    /**
     * @return the ipSmGwIndicator
     */
    public Boolean getIpSmGwIndicator() {
        return ipSmGwIndicator;
    }

    /**
     * @param ipSmGwIndicator the ipSmGwIndicator to set
     */
    public void setIpSmGwIndicator(Boolean ipSmGwIndicator) {
        this.ipSmGwIndicator = ipSmGwIndicator;
    }

    /**
     * @return the ipSmGwSmDeliveryOutcome
     */
    public SMDeliveryOutcome getIpSmGwSmDeliveryOutcome() {
        return ipSmGwSmDeliveryOutcome;
    }

    /**
     * @param ipSmGwSmDeliveryOutcome the ipSmGwSmDeliveryOutcome to set
     */
    public void setIpSmGwSmDeliveryOutcome(SMDeliveryOutcome ipSmGwSmDeliveryOutcome) {
        this.ipSmGwSmDeliveryOutcome = ipSmGwSmDeliveryOutcome;
    }

    /**
     * @return the ipSmGwAbsentSubscriberDiagnosticSM
     */
    public AbsentSubscriberDiagnosticSM getIpSmGwAbsentSubscriberDiagnosticSM() {
        return ipSmGwAbsentSubscriberDiagnosticSM;
    }

    /**
     * @param ipSmGwAbsentSubscriberDiagnosticSM the
     * ipSmGwAbsentSubscriberDiagnosticSM to set
     */
    public void setIpSmGwAbsentSubscriberDiagnosticSM(AbsentSubscriberDiagnosticSM ipSmGwAbsentSubscriberDiagnosticSM) {
        this.ipSmGwAbsentSubscriberDiagnosticSM = ipSmGwAbsentSubscriberDiagnosticSM;
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
        return String.format("REPORT-SM-DELIVERY-STATUS-ARG: MSISDN = %s "
                + "SERVICE_CENTRE_ADDRESS = %s "
                + "SM_DELIVERY_OUTCOME = %s "
                + "ABSENT-SUBSCRIBER-DIAGNOSTICS-SM = %s "
                + "GPRS-SUPPORT-INDICATOR = %s "
                + "DELIVERY-OUTCOME-INDICATOR = %s "
                + "ADDITIONAL-SM-DELIVERY-OUTCOME = %s "
                + "ADDITIONAL-ABSENT-SUBSCRIBER-DIAGNOSTIC-SM = %s "
                + "IP-SM-GW-INDICATOR = %s "
                + "IP-SM-GW-ABSENT-SUBSCRIBER-DIAGNOSTIC-SM = %s",
                this.msisdn,
                this.serviceCentreAddress,
                this.sMDeliveryOutcome,
                this.absentSubscriberDiagnosticSM,
                this.gprsSupportIndicator,
                this.deliveryOutcomeIndicator,
                this.additionalSMDeliveryOutcome,
                this.additionalAbsentSubscriberDiagnosticSM,
                this.ipSmGwIndicator,
                this.ipSmGwAbsentSubscriberDiagnosticSM);
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public boolean isRequestCorrupted() {
        return requestCorrupted;
    }
}
