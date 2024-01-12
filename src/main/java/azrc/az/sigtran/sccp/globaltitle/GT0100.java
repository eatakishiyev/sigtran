/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.globaltitle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import azrc.az.sigtran.sccp.address.GlobalTitleIndicator;
import azrc.az.sigtran.sccp.address.NatureOfAddress;
import azrc.az.sigtran.sccp.general.EncodingScheme;
import azrc.az.sigtran.sccp.general.NumberingPlan;
import lombok.Data;

/**
 *
 * @author root
 */
@Data
public final class GT0100 extends GlobalTitle {

    private static GlobalTitleIndicator globalTitleIndicator = GlobalTitleIndicator.TRANSLATION_TYPE_NP_ENC_NATURE_OF_ADDRESS_IND;
    private int translationType;
    private NumberingPlan numberingPlan;
    private EncodingScheme encodingScheme;
    private NatureOfAddress natureOfAddress;
    private String globalTitleAddressInformation;

    public GT0100() {
    }

    public GT0100(int translationType, NumberingPlan numberingPlan, NatureOfAddress natureOfAddress, String globalTitleAddressInformation) {
        this.translationType = translationType;
        this.numberingPlan = numberingPlan;
        this.natureOfAddress = natureOfAddress;
        this.setGlobalTitleAddressInformation(globalTitleAddressInformation);

    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws IOException {
        baos.write(this.translationType & 0xFF);
        int npEs = ((this.numberingPlan.value()) << 4) | this.encodingScheme.value();
        baos.write(npEs);
        int natureOfAddressOctet = this.natureOfAddress.value() & 0x7F;
        baos.write(natureOfAddressOctet);
        encodingScheme.encode(globalTitleAddressInformation, baos);
    }

    @Override
    public void decode(ByteArrayInputStream bais) throws IOException {
        this.translationType = bais.read() & 0xFF;
        int npEs = bais.read() & 0xFF;
        this.numberingPlan = NumberingPlan.getInstance((byte) ((npEs >> 4) & 0x0F));
        this.setEncodingScheme(EncodingScheme.getInstance((byte) (npEs & 0x0F)));
        this.natureOfAddress = NatureOfAddress.getInstance((byte) (bais.read() & 0x7F));
        this.globalTitleAddressInformation = encodingScheme.decode(bais);
    }

    /**
     * @return the TranslationType
     */
    @Override
    public int getTranslationType() {
        return translationType;
    }

    @Override
    public void setTranslationType(int translationType) {
        this.translationType = translationType;
    }

    /**
     * @return the NumberingPlan
     */
    @Override
    public NumberingPlan getNumberingPlan() {
        return numberingPlan;
    }

    @Override
    public void setNumberingPlan(NumberingPlan numberingPlan) {
        this.numberingPlan = numberingPlan;
    }

    /**
     * @return the EncodingScheme
     */
    @Override
    public EncodingScheme getEncodingScheme() {
        return encodingScheme;
    }

    /**
     * @return the NatureOfAddress
     */
    @Override
    public NatureOfAddress getNatureOfAddress() {
        return natureOfAddress;
    }

    @Override
    public void setNatureOfAddress(NatureOfAddress natureOfAddress) {
        this.natureOfAddress = natureOfAddress;
    }

    /**
     * @return the GlobalTitleAddressInformation
     */
    @Override
    public String getGlobalTitleAddressInformation() {
        return globalTitleAddressInformation;
    }

    @Override
    public void setGlobalTitleAddressInformation(String globalTitleAddressInformation) {
        this.globalTitleAddressInformation = globalTitleAddressInformation;
        if (globalTitleAddressInformation.length() % 2 == 0) {
            encodingScheme = EncodingScheme.BCD_EVEN;
        } else {
            encodingScheme = EncodingScheme.BCD_ODD;
        }
    }

    @Override
    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return GT0100.globalTitleIndicator;
    }


    /**
     * @param encodingScheme the encodingScheme to set
     */
    public void setEncodingScheme(EncodingScheme encodingScheme) {
        this.encodingScheme = encodingScheme;
    }

}
