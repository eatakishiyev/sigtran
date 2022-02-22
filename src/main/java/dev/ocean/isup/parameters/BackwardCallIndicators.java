/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.CalledPartysCategoryIndicator;
import dev.ocean.isup.enums.CalledPartysStatusIndicator;
import dev.ocean.isup.enums.ChargeIndicator;
import dev.ocean.isup.enums.EchoControlDeviceIndicator;
import dev.ocean.isup.enums.EndToEndInformationIndicator;
import dev.ocean.isup.enums.EndToEndMethodIndicator;
import dev.ocean.isup.enums.ISDNAccessIndicator;
import dev.ocean.isup.enums.ISDNUserPartIndicator;
import dev.ocean.isup.enums.InterworkingIndicator;
import dev.ocean.isup.enums.SCCPMethodIndicator;

/**
 *
 * @author eatakishiyev
 */
public class BackwardCallIndicators implements IsupParameter {

    private ChargeIndicator chargeIndicator;
    private CalledPartysStatusIndicator calledPartysStatusIndicator;
    private CalledPartysCategoryIndicator calledPartysCategoryIndicator;
    private EndToEndMethodIndicator endToEndMethodIndicator;
    private InterworkingIndicator interworkingIndicator;
    private EndToEndInformationIndicator endToEndInformationIndicator;
    private ISDNUserPartIndicator iSDNUserPartIndicator;
    private boolean holdingRequested;//1
    private ISDNAccessIndicator isdnAccessIndicator;
    private EchoControlDeviceIndicator echoControlDeviceIndicator;
    private SCCPMethodIndicator sCCPMethodIndicator;

    public BackwardCallIndicators() {
    }

    public BackwardCallIndicators(ChargeIndicator chargeIndicator, CalledPartysStatusIndicator calledPartysStatusIndicator, CalledPartysCategoryIndicator calledPartysCategoryIndicator, EndToEndMethodIndicator endToEndMethodIndicator, InterworkingIndicator interworkingIndicator, EndToEndInformationIndicator endToEndInformationIndicator, ISDNUserPartIndicator iSDNUserPartIndicator, boolean holdingRequested, ISDNAccessIndicator isdnAccessIndicator, EchoControlDeviceIndicator echoControlDeviceIndicator, SCCPMethodIndicator sCCPMethodIndicator) {
        this.chargeIndicator = chargeIndicator;
        this.calledPartysStatusIndicator = calledPartysStatusIndicator;
        this.calledPartysCategoryIndicator = calledPartysCategoryIndicator;
        this.endToEndMethodIndicator = endToEndMethodIndicator;
        this.interworkingIndicator = interworkingIndicator;
        this.endToEndInformationIndicator = endToEndInformationIndicator;
        this.iSDNUserPartIndicator = iSDNUserPartIndicator;
        this.holdingRequested = holdingRequested;
        this.isdnAccessIndicator = isdnAccessIndicator;
        this.echoControlDeviceIndicator = echoControlDeviceIndicator;
        this.sCCPMethodIndicator = sCCPMethodIndicator;
    }

    public SCCPMethodIndicator getsCCPMethodIndicator() {
        return sCCPMethodIndicator;
    }

    public void setsCCPMethodIndicator(SCCPMethodIndicator sCCPMethodIndicator) {
        this.sCCPMethodIndicator = sCCPMethodIndicator;
    }

    public EchoControlDeviceIndicator getEchoControlDeviceIndicator() {
        return echoControlDeviceIndicator;
    }

    public void setEchoControlDeviceIndicator(EchoControlDeviceIndicator echoControlDeviceIndicator) {
        this.echoControlDeviceIndicator = echoControlDeviceIndicator;
    }

    public boolean isHoldingRequested() {
        return holdingRequested;
    }

    public void setHoldingRequested(boolean holdingRequested) {
        this.holdingRequested = holdingRequested;
    }

    public ISDNUserPartIndicator getiSDNUserPartIndicator() {
        return iSDNUserPartIndicator;
    }

    public void setiSDNUserPartIndicator(ISDNUserPartIndicator iSDNUserPartIndicator) {
        this.iSDNUserPartIndicator = iSDNUserPartIndicator;
    }

    public ISDNAccessIndicator getIsdnAccessIndicator() {
        return isdnAccessIndicator;
    }

    public void setIsdnAccessIndicator(ISDNAccessIndicator isdnAccessIndicator) {
        this.isdnAccessIndicator = isdnAccessIndicator;
    }

    public InterworkingIndicator getInterworkingIndicator() {
        return interworkingIndicator;
    }

    public void setInterworkingIndicator(InterworkingIndicator interworkingIndicator) {
        this.interworkingIndicator = interworkingIndicator;
    }

    public CalledPartysStatusIndicator getCalledPartysStatusIndicator() {
        return calledPartysStatusIndicator;
    }

    public void setCalledPartysCategoryIndicator(CalledPartysCategoryIndicator calledPartysCategoryIndicator) {
        this.calledPartysCategoryIndicator = calledPartysCategoryIndicator;
    }

    public CalledPartysCategoryIndicator getCalledPartysCategoryIndicator() {
        return calledPartysCategoryIndicator;
    }

    public void setCalledPartysStatusIndicator(CalledPartysStatusIndicator calledPartysStatusIndicator) {
        this.calledPartysStatusIndicator = calledPartysStatusIndicator;
    }

    public EndToEndMethodIndicator getEndToEndMethodIndicator() {
        return endToEndMethodIndicator;
    }

    public void setEndToEndMethodIndicator(EndToEndMethodIndicator endToEndMethodIndicator) {
        this.endToEndMethodIndicator = endToEndMethodIndicator;
    }

    public EndToEndInformationIndicator getEndToEndInformationIndicator() {
        return endToEndInformationIndicator;
    }

    public void setEndToEndInformationIndicator(EndToEndInformationIndicator endToEndInformationIndicator) {
        this.endToEndInformationIndicator = endToEndInformationIndicator;
    }

    public ChargeIndicator getChargeIndicator() {
        return chargeIndicator;
    }

    public void setChargeIndicator(ChargeIndicator chargeIndicator) {
        this.chargeIndicator = chargeIndicator;
    }

    @Override
    public byte[] encode() throws Exception {
        byte[] data = new byte[2];
        data[0] = (byte) endToEndMethodIndicator.value();
        data[0] = (byte) ((data[0] << 2) | calledPartysCategoryIndicator.value());
        data[0] = (byte) ((data[0] << 2) | calledPartysStatusIndicator.value());
        data[0] = (byte) ((data[0] << 2) | chargeIndicator.value());

        data[1] = (byte) (sCCPMethodIndicator.value());
        data[1] = (byte) ((data[1] << 1) | echoControlDeviceIndicator.value());
        data[1] = (byte) ((data[1] << 1) | isdnAccessIndicator.value());
        data[1] = (byte) ((data[1] << 1) | (holdingRequested ? 1 : 0));
        data[1] = (byte) ((data[1] << 1) | iSDNUserPartIndicator.value());
        data[1] = (byte) ((data[1] << 1) | endToEndInformationIndicator.value());
        data[1] = (byte) ((data[1] << 1) | interworkingIndicator.value());

        return data;
    }

    @Override
    public void decode(byte[] data) throws Exception {
        this.chargeIndicator = ChargeIndicator.getInstance(data[0] & 0b00000011);
        this.calledPartysStatusIndicator = CalledPartysStatusIndicator.getInstance((data[0] >> 2) & 0b00000011);
        this.calledPartysCategoryIndicator = CalledPartysCategoryIndicator.getInstance((data[0] >> 4) & 0b00000011);
        this.endToEndMethodIndicator = EndToEndMethodIndicator.getInstance((data[0] >> 6) & 0b00000011);

        this.interworkingIndicator = InterworkingIndicator.getInstance(data[1] & 0b00000001);
        this.endToEndInformationIndicator = EndToEndInformationIndicator.getInstance((data[1] >> 1) & 0b00000001);
        this.iSDNUserPartIndicator = ISDNUserPartIndicator.getInstance((data[1] >> 2) & 0b00000001);
        this.holdingRequested = ((data[1] >> 3) & 0b00000001) == 1;
        this.isdnAccessIndicator = ISDNAccessIndicator.getInstance(((data[1] >> 4) & 0b00000001));
        this.echoControlDeviceIndicator = EchoControlDeviceIndicator.getInstance(((data[1] >> 5) & 0b00000001));
        this.sCCPMethodIndicator = SCCPMethodIndicator.getInstance(((data[1] >> 6) & 0b00000011));
    }

    @Override
    public int getParameterCode() {
        return BACKWARD_CALL_INDICATORS;
    }
}
