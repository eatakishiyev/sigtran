/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.address;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.sigtran.sccp.general.Encodable;
import org.mobicents.protocols.asn.AsnException;

/**
 *
 * @author eatakishiyev
 */
public class AddressIndicator implements Encodable {

    private byte routingIndicator;
    private byte globalTitleIndicator;
    private boolean ssnIndicator;
    private boolean pointCodeIndicator;

    public AddressIndicator(RoutingIndicator routingIndicator) {
        this.routingIndicator = routingIndicator.value();
    }

    public AddressIndicator() {
    }

    @Override
    public void encode(ByteArrayOutputStream baos) {
        /*
         * Encoding Address Indicator
         * |----8----|----7----|----6----|----5----|----4----|----3----|----2----|----1----|
         * |Reserved |Routing | | SSN |PointCode| |for nat. |Indicator| Global
         * Title Indicator |Indicator|Indicator| |use | | | | |
         * |---------|---------|---------------------------------------|---------|---------|
         */
        byte addressIndicator = 0;
        addressIndicator |= 0;//Reserved
        addressIndicator <<= 1;
        addressIndicator |= getRoutingIndicator().value() & 0xFF;//Routing Indicator
        addressIndicator <<= 4;
        addressIndicator |= getGlobalTitleIndicator().value() & 0xFF;//Global Title Indicator
        addressIndicator <<= 1;
        addressIndicator |= (this.ssnIndicator ? 0x01 : 0x00) & 0xFF;
        addressIndicator <<= 1;
        addressIndicator |= (this.pointCodeIndicator ? 0x01 : 0x00) & 0xFF;
        baos.write(addressIndicator);
    }

    /**
     *
     * @param bais
     * @throws AsnException
     * @throws IOException
     */
    @Override
    public void decode(ByteArrayInputStream bais) throws AsnException, IOException {
        /*
         * Decoding AddressIndicator byte
         * |----8----|----7----|----6----|----5----|----4----|----3----|----2----|----1----|
         * |Reserved |Routing | | SSN |PointCode| |for nat. |Indicator| Global
         * Title Indicator |Indicator|Indicator| |use | | | | |
         * |-------------------------------------------------------------------------------|
         */
        byte addressInd = (byte) (bais.read() & 0xFF);
        byte reserved = (byte) (addressInd >> 7);//Reserved
        this.setRoutingIndicator(RoutingIndicator.getInstance((byte) ((addressInd & 0x40) >> 6)));//Routing Indicator
        this.setGlobalTitleIndicator(GlobalTitleIndicator.getInstance((byte) ((addressInd & 0x3C) >> 2)));//Global Title Indicator
        this.setSsnIndicator((((addressInd & 0x02) >> 1) == 1));//SSN Indicator
        this.setPointCodeIndicator(((addressInd & 0x01) == 1));//Point Code Indicator
    }

    /**
     * @return the RoutingIndicator
     */
    public RoutingIndicator getRoutingIndicator() {
        return RoutingIndicator.getInstance(routingIndicator);
    }

    /**
     * @return the GlobalTitleIndicator
     */
    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return GlobalTitleIndicator.getInstance(globalTitleIndicator);
    }

    /**
     * @return the SSNIndicator
     */
    public boolean getSSNIndicator() {
        return ssnIndicator;
    }

    /**
     * @return the PointCodeIndicator
     */
    public boolean getPointCodeIndicator() {
        return pointCodeIndicator;
    }

    /**
     * @param routingIndicator the RoutingIndicator to set
     */
    public void setRoutingIndicator(RoutingIndicator routingIndicator) {
        this.routingIndicator = routingIndicator.value();
    }

    @Override
    public String toString() {
        return "AddressIndicator:[RoutingIndicator = " + routingIndicator + 
                ";GlobalTitleIndicator = " + globalTitleIndicator + 
                ";SSNIndicator = " + ssnIndicator + ";PointCodeIndicator = " + pointCodeIndicator +
                "]";
    }

    /**
     * @param globalTitleIndicator the globalTitleIndicator to set
     */
    public void setGlobalTitleIndicator(GlobalTitleIndicator globalTitleIndicator) {
        this.globalTitleIndicator = globalTitleIndicator.value();
    }

    /**
     * @param ssnIndicator the ssnIndicator to set
     */
    public void setSsnIndicator(boolean ssnIndicator) {
        this.ssnIndicator = ssnIndicator;
    }

    /**
     * @param pointCodeIndicator the pointCodeIndicator to set
     */
    public void setPointCodeIndicator(boolean pointCodeIndicator) {
        this.pointCodeIndicator = pointCodeIndicator;
    }
}
