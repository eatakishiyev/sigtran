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
 *
 * @author eatakishiyev
 */
public class MTForwardSMArg implements MAPForwardSmArg {

    private SM_RP_DA smRPDA;//M
    private SM_RP_OA smRPOA;//M
    private SignalInfo smRPUI;//M
    private boolean moreMessageToSend = false;//O
    private ExtensionContainer extensionContainer;//O
    private SMDeliveryTimerValue sMDeliveryTimer;//O
    private Time smDeliveryStartTime;//O
    private IMSI imsi;//O
    private byte[] requestData;
    protected boolean requestCorrupted = false;

    public MTForwardSMArg() {
    }

    public MTForwardSMArg(SM_RP_DA smRPDA, SM_RP_OA smRPOA, SignalInfo smRPUI) {
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

            if (moreMessageToSend) {
                try {
                    aos.writeNull();
                } catch (IOException | AsnException ex) {
                    throw new IncorrectSyntaxException(ex.getMessage());
                }
            }

            if (this.extensionContainer != null) {
                this.extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            if (this.sMDeliveryTimer != null) {
                this.sMDeliveryTimer.encode(Tag.CLASS_UNIVERSAL, Tag.INTEGER, aos);
            }

            if (this.smDeliveryStartTime != null) {
                this.smDeliveryStartTime.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException {
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

    private void decode_(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            this.smRPDA = new SM_RP_DA();
            this.smRPDA.decode(ais);

            this.smRPOA = new SM_RP_OA();
            this.smRPOA.decode(ais);

            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive()
                    || tag != Tag.STRING_OCTET) {
                throw new IncorrectSyntaxException("Unexpected parameter. Waiting for SM-RP-UI[UNIVERSAL|PRIMITIVE|STRING_OCTET]");
            }
            this.smRPUI = new SignalInfo();
            this.smRPUI.decode(ais);

            while (ais.available() > 0) {
                tag = ais.readTag();
                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive() && tag == Tag.NULL) {
                    this.moreMessageToSend = true;
                }

                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && !ais.isTagPrimitive() && tag == Tag.SEQUENCE) {
                    this.extensionContainer = new ExtensionContainer();
                    this.extensionContainer.decode(ais);
                }

                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive() && tag == Tag.INTEGER) {
                    this.sMDeliveryTimer = new SMDeliveryTimerValue();
                    this.sMDeliveryTimer.decode(ais);
                }

                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive() && tag == Tag.STRING_OCTET) {
                    this.smDeliveryStartTime = new Time();
                    this.smDeliveryStartTime.decode(ais);
                }
            }
        } catch (IOException | UnexpectedDataException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @param moreMessageToSend the moreMessageToSend to set
     */
    public void setMoreMessageToSend(Boolean moreMessageToSend) {
        this.setMoreMessageToSend((boolean) moreMessageToSend);
    }

    /**
     * @return the sMDeliveryTimer
     */
    public SMDeliveryTimerValue getsMDeliveryTimer() {
        return sMDeliveryTimer;
    }

    /**
     * @param sMDeliveryTimer the sMDeliveryTimer to set
     */
    public void setsMDeliveryTimer(SMDeliveryTimerValue sMDeliveryTimer) {
        this.sMDeliveryTimer = sMDeliveryTimer;
    }

    /**
     * @return the smDeliveryStartTime
     */
    public Time getSmDeliveryStartTime() {
        return smDeliveryStartTime;
    }

    /**
     * @param smDeliveryStartTime the smDeliveryStartTime to set
     */
    public void setSmDeliveryStartTime(Time smDeliveryStartTime) {
        this.smDeliveryStartTime = smDeliveryStartTime;
    }

    /**
     * @return the smRPDA
     */
    public SM_RP_DA getSmRPDA() {
        return smRPDA;
    }

    /**
     * @param smRPDA the smRPDA to set
     */
    public void setSmRPDA(SM_RP_DA smRPDA) {
        this.smRPDA = smRPDA;
    }

    /**
     * @return the smRPOA
     */
    public SM_RP_OA getSmRPOA() {
        return smRPOA;
    }

    /**
     * @param smRPOA the smRPOA to set
     */
    public void setSmRPOA(SM_RP_OA smRPOA) {
        this.smRPOA = smRPOA;
    }

    /**
     * @return the smRPUI
     */
    public SignalInfo getSmRPUI() {
        return smRPUI;
    }

    /**
     * @param smRPUI the smRPUI to set
     */
    public void setSmRPUI(SignalInfo smRPUI) {
        this.smRPUI = smRPUI;
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
     * @return the moreMessageToSend
     */
    public boolean isMoreMessageToSend() {
        return moreMessageToSend;
    }

    /**
     * @param moreMessageToSend the moreMessageToSend to set
     */
    public void setMoreMessageToSend(boolean moreMessageToSend) {
        this.moreMessageToSend = moreMessageToSend;
    }

    /**
     * @return the imsi
     */
    public IMSI getImsi() {
        return imsi;
    }

    /**
     * @param imsi the imsi to set
     */
    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public boolean isRequestCorrupted() {
        return requestCorrupted;
    }
}
