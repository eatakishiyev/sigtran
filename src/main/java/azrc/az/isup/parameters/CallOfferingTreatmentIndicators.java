/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.CallToBeOfferedIndicator;

import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class CallOfferingTreatmentIndicators implements IsupParameter {

    private CallToBeOfferedIndicator[] callToBeOfferedIndicators;

    public CallOfferingTreatmentIndicators() {
    }

    public CallOfferingTreatmentIndicators(CallToBeOfferedIndicator[] callToBeOfferedIndicators) {
        this.callToBeOfferedIndicators = callToBeOfferedIndicators;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CallOfferingTreatmentIndicators[");
        for (CallToBeOfferedIndicator callToBeOfferedIndicator : callToBeOfferedIndicators) {
            sb.append("; CallToBeOfferedIndicator:").append(callToBeOfferedIndicators).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public byte[] encode() throws IOException {
        byte[] data = new byte[callToBeOfferedIndicators.length];

        for (int i = 0; i < callToBeOfferedIndicators.length; i++) {
            data[i] = (byte) (callToBeOfferedIndicators[i].value() & 0b01111111);
        }

        data[data.length - 1] = (byte) (data[data.length] | (0b10000000));
        return data;
    }

    @Override
    public void decode(byte[] data) {
        this.callToBeOfferedIndicators = new CallToBeOfferedIndicator[data.length];
        for (int i = 0; i < data.length; i++) {
            callToBeOfferedIndicators[i] = CallToBeOfferedIndicator.getInstance(data[i] & 0b00000011);
        }
    }

    public CallToBeOfferedIndicator[] getCallToBeOfferedIndicator() {
        return callToBeOfferedIndicators;
    }

    public void setCallToBeOfferedIndicator(CallToBeOfferedIndicator[] callToBeOfferedIndicator) {
        this.callToBeOfferedIndicators = callToBeOfferedIndicator;
    }

    @Override
    public int getParameterCode() {
        return CALL_OFFERING_TREATMENT_INDICATORS;
    }

}
