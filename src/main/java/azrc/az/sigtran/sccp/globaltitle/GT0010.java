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
public final class GT0010 extends GlobalTitle {

    private static GlobalTitleIndicator globalTitleIndicator = GlobalTitleIndicator.TRANSLATION_TYPE_ONLY;
    private int translationType;
    private String globalTitleAddressInformation;

    public GT0010() {
    }

    public GT0010(int TranslationType, String GlobalTitleAddressInformation) {
        this.translationType = TranslationType;
        this.globalTitleAddressInformation = GlobalTitleAddressInformation;
    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws IOException {
        baos.write(translationType & 0xFF);
        baos.write(this.getGlobalTitleAddressInformation().getBytes());
    }

    @Override
    public void decode(ByteArrayInputStream bais) throws IOException {
        this.translationType = bais.read() & 0xFF;
        byte[] gtai = new byte[bais.available()];
        bais.read(gtai);
        this.globalTitleAddressInformation = new String(gtai);

    }

    /**
     * @return the GlobalTitleAddressInformation
     */
    @Override
    public String getGlobalTitleAddressInformation() {
        return this.globalTitleAddressInformation;
    }

    @Override
    public void setGlobalTitleAddressInformation(String globalTitleAddressInformation) {
        this.globalTitleAddressInformation = globalTitleAddressInformation;
    }

    @Override
    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return GT0010.globalTitleIndicator;
    }

    /**
     * @return the TranslationType
     */
    @Override
    public int getTranslationType() {
        return this.translationType;
    }

    @Override
    public void setTranslationType(int translationType) {
        this.translationType = translationType;
    }

    @Override
    public void setNumberingPlan(NumberingPlan numberingPlan) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NumberingPlan getNumberingPlan() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setNatureOfAddress(NatureOfAddress natureOfAddress) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NatureOfAddress getNatureOfAddress() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EncodingScheme getEncodingScheme() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
