/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.mobility.sms;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.IMSI;
import dev.ocean.sigtran.map.parameters.SMDeliveryTimerValue;
import dev.ocean.sigtran.map.parameters.SM_RP_DA;
import dev.ocean.sigtran.map.parameters.SM_RP_OA;
import dev.ocean.sigtran.map.parameters.SignalInfo;
import dev.ocean.sigtran.map.parameters.depricated.Time;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * MO-ForwardSM-Arg ::= SEQUENCE {
 * sm-RP-DA SM-RP-DA,
 * sm-RP-OA SM-RP-OA,
 * sm-RP-UI SignalInfo,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ... ,
 * imsi IMSI OPTIONAL }
 *
 * @author eatakishiyev
 */
public class MOForwardSMArg implements MAPForwardSmArg {

    private SM_RP_DA smRPDA;//M
    private SM_RP_OA smRPOA;//M
    private SignalInfo smRPUI;//M
    private ExtensionContainer extensionContainer;//O
    private IMSI imsi;//O
    private byte[] requestData;
    protected boolean requestCorrupted = false;

    public MOForwardSMArg() {
    }

    public MOForwardSMArg(SM_RP_DA smRPDA, SM_RP_OA smRPOA, SignalInfo smRPUI) {
        this.smRPDA = smRPDA;
        this.smRPOA = smRPOA;
        this.smRPUI = smRPUI;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (smRPDA == null || smRPOA == null || smRPUI == null) {
                throw new IncorrectSyntaxException("One or more mandatory parameters are absent");
            }

            smRPDA.encode(aos);
            smRPOA.encode(aos);
            smRPUI.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            if (this.extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }
            if (imsi != null) {
                imsi.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(byte[] data) throws UnexpectedDataException, IncorrectSyntaxException {
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

            this.smRPDA = new SM_RP_DA();
            this.smRPDA.decode(ais);

            this.smRPOA = new SM_RP_OA();
            this.smRPOA.decode(ais);

            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET) {
                throw new IncorrectSyntaxException("Unexpected parameter. Waiting for SM-RP-UI[UNIVERSAL|PRIMITIVE|STRING_OCTET]");
            }
            this.smRPUI = new SignalInfo();
            this.smRPUI.decode(ais);

            while (ais.available() > 0) {
                tag = ais.readTag();
                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive()
                        && tag == Tag.SEQUENCE) {
                    this.extensionContainer = new ExtensionContainer();
                    this.extensionContainer.decode(ais);
                }

                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive()
                        && tag == Tag.STRING_OCTET) {
                    this.imsi = new IMSI();
                    this.imsi.decode(ais);
                }
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException();
        }
    }

    /**
     * @return the smRPDA
     */
    @Override
    public SM_RP_DA getSmRPDA() {
        return smRPDA;
    }

    /**
     * @param smRPDA the smRPDA to set
     */
    @Override
    public void setSmRPDA(SM_RP_DA smRPDA) {
        this.smRPDA = smRPDA;
    }

    /**
     * @return the smRPOA
     */
    @Override
    public SM_RP_OA getSmRPOA() {
        return smRPOA;
    }

    /**
     * @param smRPOA the smRPOA to set
     */
    @Override
    public void setSmRPOA(SM_RP_OA smRPOA) {
        this.smRPOA = smRPOA;
    }

    /**
     * @return the smRPUI
     */
    @Override
    public SignalInfo getSmRPUI() {
        return smRPUI;
    }

    /**
     * @param smRPUI the smRPUI to set
     */
    @Override
    public void setSmRPUI(SignalInfo smRPUI) {
        this.smRPUI = smRPUI;
    }

    /**
     * @return the imsi
     */
    @Override
    public IMSI getImsi() {
        return imsi;
    }

    /**
     * @param imsi the imsi to set
     */
    @Override
    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    /**
     * @return the extensionContainer
     */
    @Override
    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    /**
     * @param extensionContainer the extensionContainer to set
     */
    @Override
    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    @Override
    public void setMoreMessageToSend(Boolean moreMessageToSend) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SMDeliveryTimerValue getsMDeliveryTimer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setsMDeliveryTimer(SMDeliveryTimerValue sMDeliveryTimer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Time getSmDeliveryStartTime() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSmDeliveryStartTime(Time smDeliveryStartTime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isMoreMessageToSend() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMoreMessageToSend(boolean moreMessageToSend) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MO-ForwardSM-Arg[");
        sb.append(smRPDA.toString());
        sb.append(smRPOA.toString());
        sb.append(smRPUI.toString());
        if (imsi != null) {
            sb.append(imsi.toString());
        }
        sb.append("]");
        return sb.toString();
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public boolean isRequestCorrupted() {
        return requestCorrupted;
    }
}
