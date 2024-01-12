/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.mobility.sms;

import java.io.IOException;
import java.util.Arrays;

import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.IMSI;
import azrc.az.sigtran.map.parameters.IP_SM_GW_Guidance;
import azrc.az.sigtran.map.parameters.LocationInfoWithLMSI;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import azrc.az.sigtran.map.services.common.MAPResponse;

/**
 *
 * @author eatakishiyev
 */
public class SendRoutingInfoForSMRes implements MAPResponse {

    private IMSI imsi;//M
    private LocationInfoWithLMSI locationInfoWithLMSI;//M
    private ExtensionContainer extensionContainer;//O
    private IP_SM_GW_Guidance ipSMGWGuidance;//O
    private byte[] responseData;
    protected boolean responseCorrupted = false;

    public SendRoutingInfoForSMRes() {
    }

    public SendRoutingInfoForSMRes(IMSI imsi, LocationInfoWithLMSI locationInfoWithLMSI) {
        this.imsi = imsi;
        this.locationInfoWithLMSI = locationInfoWithLMSI;

    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (this.imsi == null || this.locationInfoWithLMSI == null) {
                throw new IncorrectSyntaxException("One or more mandatory parameters are absent");
            }

            this.imsi.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            this.locationInfoWithLMSI.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x00, aos);

            if (this.extensionContainer != null) {
                this.extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x04, aos);
            }

            if (this.ipSMGWGuidance != null) {
                this.ipSMGWGuidance.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x05, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(byte[] data) throws UnexpectedDataException, IncorrectSyntaxException {
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

    private void decode_(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException, IOException {
        int tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET) {
            throw new IncorrectSyntaxException("One or more mandatory parameters are absent");
        }
        this.imsi = new IMSI();
        this.imsi.decode(ais);

        tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive() || tag != 0x00) {
            throw new IncorrectSyntaxException("Mistyped parameter");
        }
        this.locationInfoWithLMSI = new LocationInfoWithLMSI();
        this.locationInfoWithLMSI.decode(ais);

        while (ais.available() > 0) {
            tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && !ais.isTagPrimitive() && tag == 0x04) {
                this.extensionContainer = new ExtensionContainer();
                this.extensionContainer.decode(ais);
            } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && !ais.isTagPrimitive() && tag == 0x05) {
                this.ipSMGWGuidance = new IP_SM_GW_Guidance();
                this.ipSMGWGuidance.decode(ais);
            } else {
                throw new IncorrectSyntaxException("Unexpected parameter received.");
            }
        }
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

    /**
     * @return the locationInfoWithLMSI
     */
    public LocationInfoWithLMSI getLocationInfoWithLMSI() {
        return locationInfoWithLMSI;
    }

    /**
     * @param locationInfoWithLMSI the locationInfoWithLMSI to set
     */
    public void setLocationInfoWithLMSI(LocationInfoWithLMSI locationInfoWithLMSI) {
        this.locationInfoWithLMSI = locationInfoWithLMSI;
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
     * @return the ipSMGWGuidance
     */
    public IP_SM_GW_Guidance getIpSMGWGuidance() {
        return ipSMGWGuidance;
    }

    /**
     * @param ipSMGWGuidance the ipSMGWGuidance to set
     */
    public void setIpSMGWGuidance(IP_SM_GW_Guidance ipSMGWGuidance) {
        this.ipSMGWGuidance = ipSMGWGuidance;
    }

    public byte[] getResponseData() {
        return responseData;
    }

    public boolean isResponseCorrupted() {
        return responseCorrupted;
    }

    @Override
    public String toString() {
        return "SendRoutingInfoForSMRes{" +
                "imsi=" + imsi +
                ", locationInfoWithLMSI=" + locationInfoWithLMSI +
                ", extensionContainer=" + extensionContainer +
                ", ipSMGWGuidance=" + ipSMGWGuidance +
                ", responseData=" + Arrays.toString(responseData) +
                ", responseCorrupted=" + responseCorrupted +
                '}';
    }
}
