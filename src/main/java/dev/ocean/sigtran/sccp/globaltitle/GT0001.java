/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.globaltitle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.sigtran.sccp.address.GlobalTitleIndicator;
import dev.ocean.sigtran.sccp.address.NatureOfAddress;
import dev.ocean.sigtran.sccp.general.EncodingScheme;
import dev.ocean.sigtran.sccp.general.NumberingPlan;

/**
 *
 * @author root
 */
public final class GT0001 extends GlobalTitle {

    private static GlobalTitleIndicator globalTitleIndicator = GlobalTitleIndicator.NATURE_OF_ADDRESS_IND_ONLY;
    private NatureOfAddress natureOfAddress;
    private String globalTitleAddressInformation;
    private boolean even;

    public GT0001() {
    }

    public GT0001(NatureOfAddress natureOfAddressInd, String GlobalTitleAddressInformation) {
        this.natureOfAddress = natureOfAddressInd;
        this.setGlobalTitleAddressInformation(globalTitleAddressInformation);
    }

    @Override
    public void decode(ByteArrayInputStream bais) throws IOException {
        int val = bais.read();
        this.even = (val & 0x80) == 0;
        EncodingScheme encodingScheme = even ? EncodingScheme.BCD_EVEN : EncodingScheme.BCD_ODD;
        this.natureOfAddress = NatureOfAddress.getInstance((byte) (val & 0x7F));
        this.globalTitleAddressInformation = encodingScheme.decode(bais);
    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws IOException {
        if (this.even) {
            baos.write(0x00);//Even number of address signalling
        } else {
            baos.write(0x01);//Odd number of address signalling
        }
        EncodingScheme encodingScheme = even ? EncodingScheme.BCD_EVEN : EncodingScheme.BCD_ODD;
        int frstOctet = this.natureOfAddress.value() | ((this.even ? 0x00 : 0x01) << 7);
        baos.write(frstOctet);//Nature of Address Ind
        encodingScheme.encode(globalTitleAddressInformation, baos);
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
        this.even = (this.globalTitleAddressInformation.length() % 2 == 0);
    }

    @Override
    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return GT0001.globalTitleIndicator;
    }

    @Override
    public String toString() {
        return "GT0001 : [Even = " + even
                + ";NatureOfAddressInd = " + natureOfAddress
                + ";GlobalTitleAddressInformation = " + globalTitleAddressInformation
                + "]";
    }

    /**
     * @return the even
     */
    protected boolean isEven() {
        return even;
    }

    @Override
    public void setNatureOfAddress(NatureOfAddress natureOfAddress) {
        this.natureOfAddress = natureOfAddress;
    }

    @Override
    public NatureOfAddress getNatureOfAddress() {
        return this.natureOfAddress;
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
    public void setTranslationType(int translationType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getTranslationType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EncodingScheme getEncodingScheme() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
