/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.EndToEndInformationIndicator;
import dev.ocean.isup.enums.EndToEndMethodIndicator;
import dev.ocean.isup.enums.ISDNAccessIndicator;
import dev.ocean.isup.enums.ISDNUserPartIndicator;
import dev.ocean.isup.enums.ISDNUserPartPreferenceIndicator;
import dev.ocean.isup.enums.InterworkingIndicator;
import dev.ocean.isup.enums.NationalInternationalCallIndicator;
import dev.ocean.isup.enums.SCCPMethodIndicator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class ForwardCallIndicators implements IsupParameter {

    private ISDNUserPartPreferenceIndicator iSDNUserPartPreferenceIndicator;
    private ISDNUserPartIndicator iSDNUserPartIndicator;
    private EndToEndInformationIndicator endToEndInformationIndicator;
    private InterworkingIndicator interworkingIndicator;
    private EndToEndMethodIndicator endToEndMethodIndicator;
    private NationalInternationalCallIndicator nationalInternationalCallIndicator;
    private ISDNAccessIndicator iSDNAccessIndicator;
    private SCCPMethodIndicator sccpMethodIndicator;

    public ForwardCallIndicators() {
    }

    public ForwardCallIndicators(NationalInternationalCallIndicator nationalInternationalCallIndicator, EndToEndMethodIndicator endToEndMethodIndicator,
            InterworkingIndicator interworkingIndicator, EndToEndInformationIndicator endToEndInformationIndicator, ISDNUserPartIndicator iSDNUserPartIndicator,
            ISDNUserPartPreferenceIndicator iSDNUserPartPreferenceIndicator, ISDNAccessIndicator iSDNAccessIndicator,
            SCCPMethodIndicator sccpMethodIndicator) {
        this.iSDNUserPartPreferenceIndicator = iSDNUserPartPreferenceIndicator;
        this.iSDNUserPartIndicator = iSDNUserPartIndicator;
        this.endToEndInformationIndicator = endToEndInformationIndicator;
        this.interworkingIndicator = interworkingIndicator;
        this.endToEndMethodIndicator = endToEndMethodIndicator;
        this.nationalInternationalCallIndicator = nationalInternationalCallIndicator;
        this.iSDNAccessIndicator = iSDNAccessIndicator;
        this.sccpMethodIndicator = sccpMethodIndicator;
    }

    @Override
    public byte[] encode() {
        byte[] data = new byte[2];
        int result = iSDNUserPartPreferenceIndicator.value();
        result = (result << 1) | iSDNUserPartIndicator.value();
        result = (result << 1) | endToEndInformationIndicator.value();
        result = (result << 1) | interworkingIndicator.value();
        result = (result << 2) | endToEndMethodIndicator.value();
        result = (result << 1) | nationalInternationalCallIndicator.value();
        data[0] = (byte) (result);

        result = 0;
        result = result | sccpMethodIndicator.value();
        result = (result << 1) | iSDNAccessIndicator.value();
        data[1] = (byte) (result);
        return data;
    }

    @Override
    public void decode(byte[] data) {
        int tmp = data[0] & 0xFF;
        this.iSDNUserPartPreferenceIndicator = ISDNUserPartPreferenceIndicator.getInstance((tmp >> 6) & 0b00000011);
        this.iSDNUserPartIndicator = ISDNUserPartIndicator.getInstance((tmp >> 5) & 0b00000001);
        this.endToEndInformationIndicator = EndToEndInformationIndicator.getInstance((tmp >> 4) & 0b00000001);
        this.interworkingIndicator = InterworkingIndicator.getInstance((tmp >> 3) & 0b00000001);
        this.endToEndMethodIndicator = EndToEndMethodIndicator.getInstance((tmp >> 1) & 0b00000011);
        this.nationalInternationalCallIndicator = NationalInternationalCallIndicator.getInstance(tmp & 0b00000001);

        tmp = data[1] & 0xFF;
        this.sccpMethodIndicator = SCCPMethodIndicator.getInstance((tmp >> 1) & 0b00000011);
        this.iSDNAccessIndicator = ISDNAccessIndicator.getInstance(tmp & 0b00000001);
    }

    public void decode(ByteArrayInputStream bais) {
        int tmp = bais.read();
        this.iSDNUserPartPreferenceIndicator = ISDNUserPartPreferenceIndicator.getInstance((tmp >> 6) & 0b00000011);
        this.iSDNUserPartIndicator = ISDNUserPartIndicator.getInstance((tmp >> 5) & 0b00000001);
        this.endToEndInformationIndicator = EndToEndInformationIndicator.getInstance((tmp >> 4) & 0b00000001);
        this.interworkingIndicator = InterworkingIndicator.getInstance((tmp >> 3) & 0b00000001);
        this.endToEndMethodIndicator = EndToEndMethodIndicator.getInstance((tmp >> 1) & 0b00000011);
        this.nationalInternationalCallIndicator = NationalInternationalCallIndicator.getInstance(tmp & 0b00000001);

        tmp = bais.read();
        this.sccpMethodIndicator = SCCPMethodIndicator.getInstance((tmp >> 1) & 0b00000011);
        this.iSDNAccessIndicator = ISDNAccessIndicator.getInstance(tmp & 0b00000001);
    }

    public EndToEndInformationIndicator getEndToEndInformationIndicator() {
        return endToEndInformationIndicator;
    }

    public EndToEndMethodIndicator getEndToEndMethodIndicator() {
        return endToEndMethodIndicator;
    }

    public InterworkingIndicator getInterworkingIndicator() {
        return interworkingIndicator;
    }

    public NationalInternationalCallIndicator getNationalInternationalCallIndicator() {
        return nationalInternationalCallIndicator;
    }

    public SCCPMethodIndicator getSccpMethodIndicator() {
        return sccpMethodIndicator;
    }

    public ISDNAccessIndicator getiSDNAccessIndicator() {
        return iSDNAccessIndicator;
    }

    public ISDNUserPartIndicator getiSDNUserPartIndicator() {
        return iSDNUserPartIndicator;
    }

    public ISDNUserPartPreferenceIndicator getiSDNUserPartPreferenceIndicator() {
        return iSDNUserPartPreferenceIndicator;
    }

    @Override
    public int getParameterCode() {
        return FORWARD_CALL_INDICATORS;
    }

}
