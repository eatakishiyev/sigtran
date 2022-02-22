/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class ServiceActivation implements IsupParameter {

    private byte[] featureCodes;

    public ServiceActivation() {
    }

    public ServiceActivation(byte[] featureCodes) {
        this.featureCodes = featureCodes;
    }

    @Override
    public byte[] encode() throws IOException {
        return (this.featureCodes);
    }

    @Override
    public void decode(byte[] data) {
        this.featureCodes = data;
    }

    public byte[] getFeatureCodes() {
        return featureCodes;
    }

    public void setFeatureCodes(byte[] featureCodes) {
        this.featureCodes = featureCodes;
    }

    @Override
    public int getParameterCode() {
        return SERVICE_ACTIVATION;
    }
}
