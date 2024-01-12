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
public final class GT0011 extends GlobalTitle {

    private static GlobalTitleIndicator globalTitleIndicator = GlobalTitleIndicator.TRANSLATION_TYPE_NP_ENC;
    private int translationType;
    private NumberingPlan numberingPlan;
    private EncodingScheme encodingScheme;
    private String globalTitleAddressInformation;

    public GT0011() {
    }

    public GT0011(int translationType, NumberingPlan numberingPlan, String globalTitleAddressInformation) {
        this.translationType = translationType;
        this.numberingPlan = numberingPlan;
        this.setGlobalTitleAddressInformation(globalTitleAddressInformation);
    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws IOException {
        baos.write(this.translationType & 0xFF);
        int npEs = this.numberingPlan.value() << 4 | this.encodingScheme.value();
        baos.write(npEs);
        encodingScheme.encode(globalTitleAddressInformation, baos);
    }

    @Override
    public void decode(ByteArrayInputStream bais) throws IOException {
        this.translationType = bais.read() & 0xFF;
        int npEs = bais.read() & 0xFF;
        this.numberingPlan = NumberingPlan.getInstance((byte) ((npEs >> 4) & 0xF));
        this.encodingScheme = EncodingScheme.getInstance((byte) (npEs & 0xF));
        this.globalTitleAddressInformation = encodingScheme.decode(bais);
    }

    /**
     * @return the EncodingScheme
     */
    @Override
    public EncodingScheme getEncodingScheme() {
        return encodingScheme;
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
            this.encodingScheme = EncodingScheme.BCD_EVEN;
        } else {
            this.encodingScheme = EncodingScheme.BCD_ODD;
        }
    }

    @Override
    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return GT0011.globalTitleIndicator;
    }

    @Override
    public void setNumberingPlan(NumberingPlan numberingPlan) {
        this.numberingPlan = numberingPlan;
    }

    /**
     * @return the NumberingPlan
     */
    @Override
    public NumberingPlan getNumberingPlan() {
        return numberingPlan;
    }

    @Override
    public void setTranslationType(int translationType) {
        this.translationType = translationType;
    }

    /**
     * @return the TranslationType
     */
    @Override
    public int getTranslationType() {
        return translationType;
    }

    @Override
    public void setNatureOfAddress(NatureOfAddress natureOfAddress) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NatureOfAddress getNatureOfAddress() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
