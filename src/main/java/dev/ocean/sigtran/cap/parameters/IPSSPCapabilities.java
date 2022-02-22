/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class IPSSPCapabilities {

    private boolean ipRoutingAddressSupported;
    private boolean voiceBackSupported;
    private boolean voiceInformationSupportedViaSpeech;
    private boolean voiceInformationSupportedViaVoice;
    private boolean generationOfVoiceAnnouncmentFromTextSupported;
    private byte[] bileteralPart;
    private int ipsspCapabilities = -1;

    public IPSSPCapabilities() {

    }

    public IPSSPCapabilities(int ipsspCapabilities) {
        this.ipsspCapabilities = ipsspCapabilities;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            if (ipsspCapabilities >= 0) {
                aos.writeInteger(tagClass, tag, ipsspCapabilities);
            } else {
                aos.writeTag(tagClass, true, tag);
                int lenPos = aos.StartContentDefiniteLength();
                ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
                this.encode(baos);
                aos.write(baos.toByteArray());
                aos.FinalizeContent(lenPos);
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(ByteArrayOutputStream baos) throws IOException {
        if (ipsspCapabilities >= 0) {
            baos.write(ipsspCapabilities);
        } else {
            int firstOctet = 0;//End of standart part
            firstOctet = firstOctet << 2;//reserved
            firstOctet = firstOctet | (generationOfVoiceAnnouncmentFromTextSupported ? 1 : 0);
            firstOctet = firstOctet << 1;
            firstOctet = firstOctet | (voiceInformationSupportedViaVoice ? 1 : 0);
            firstOctet = firstOctet << 1;
            firstOctet = firstOctet | (voiceInformationSupportedViaSpeech ? 1 : 0);
            firstOctet = firstOctet << 1;
            firstOctet = firstOctet | (voiceBackSupported ? 1 : 0);
            firstOctet = firstOctet << 1;
            firstOctet = firstOctet | (ipRoutingAddressSupported ? 1 : 0);
            baos.write(firstOctet);
        }
        if (bileteralPart != null && bileteralPart.length > 0) {
            baos.write(bileteralPart);
        }
    }

    public void decode(ByteArrayInputStream bais) throws IOException {
        this.ipsspCapabilities = bais.read();
        this.generationOfVoiceAnnouncmentFromTextSupported = ((ipsspCapabilities >> 4) & 0x01) != 0;
        this.voiceInformationSupportedViaVoice = ((ipsspCapabilities >> 3) & 0x01) != 0;
        this.voiceInformationSupportedViaSpeech = ((ipsspCapabilities >> 2) & 0x01) != 0;
        this.voiceBackSupported = ((ipsspCapabilities >> 1) & 0x01) != 0;
        this.ipRoutingAddressSupported = ((ipsspCapabilities) & 0x01) != 0;

        if (bais.available() > 0) {
            bileteralPart = new byte[bais.available()];
            bais.read(bileteralPart);
        }
    }

    public void decode(AsnInputStream ais) throws IOException {
        byte[] data = new byte[ais.readLength()];
        ais.read(data);

        this.decode(new ByteArrayInputStream(data));
    }

    /**
     * @return the ipRoutingAddress
     */
    public boolean isIpRoutingAddressSupported() {
        return ipRoutingAddressSupported;
    }

    /**
     * @param ipRoutingAddressSupported
     */
    public void setIpRoutingAddressSupported(boolean ipRoutingAddressSupported) {
        this.ipRoutingAddressSupported = ipRoutingAddressSupported;
    }

    /**
     * @return the voiceBack
     */
    public boolean isVoiceBackSupported() {
        return voiceBackSupported;
    }

    /**
     * @param voiceBackSupported
     */
    public void setVoiceBackSupported(boolean voiceBackSupported) {
        this.voiceBackSupported = voiceBackSupported;
    }

    /**
     * @return the voiceInformation
     */
    public boolean isVoiceInformationSupportedViaSpeech() {
        return voiceInformationSupportedViaSpeech;
    }

    /**
     * @param voiceInformationSupportedViaSpeech
     */
    public void setVoiceInformationSupportedViaSpeech(boolean voiceInformationSupportedViaSpeech) {
        this.voiceInformationSupportedViaSpeech = voiceInformationSupportedViaSpeech;
    }

    /**
     * @return the generationOfVoiceAnnouncmentFromText
     */
    public boolean isGenerationOfVoiceAnnouncmentFromTextSupported() {
        return generationOfVoiceAnnouncmentFromTextSupported;
    }

    /**
     * @param generationOfVoiceAnnouncmentFromTextSupported
     */
    public void setGenerationOfVoiceAnnouncmentFromTextSupported(boolean generationOfVoiceAnnouncmentFromTextSupported) {
        this.generationOfVoiceAnnouncmentFromTextSupported = generationOfVoiceAnnouncmentFromTextSupported;
    }

    /**
     * @return the voiceInformationSupportedViaVoice
     */
    public boolean isVoiceInformationSupportedViaVoice() {
        return voiceInformationSupportedViaVoice;
    }

    /**
     * @param voiceInformationSupportedViaVoice the
     * voiceInformationSupportedViaVoice to set
     */
    public void setVoiceInformationSupportedViaVoice(boolean voiceInformationSupportedViaVoice) {
        this.voiceInformationSupportedViaVoice = voiceInformationSupportedViaVoice;
    }

    /**
     * @return the BileteralPart
     */
    public byte[] getBileteralPart() {
        return bileteralPart;
    }

    /**
     * @param BileteralPart the BileteralPart to set
     */
    public void setBileteralPart(byte[] BileteralPart) {
        this.bileteralPart = BileteralPart;
    }

    public int getIpSspCapabilities() {
        return this.ipsspCapabilities;
    }
}
