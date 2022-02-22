/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.NumberPortabilityStatusIndicator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class NumberPortabilityForwardInformation implements IsupParameter {

    private byte[] numberPortabilityForwardInformations;

    public NumberPortabilityForwardInformation() {
    }

    @Override
    public byte[] encode() throws IOException {
        for (int i = 0; i < numberPortabilityForwardInformations.length; i++) {
            numberPortabilityForwardInformations[i] = (byte) (numberPortabilityForwardInformations[i] & 0b00001111);
        }

        numberPortabilityForwardInformations[numberPortabilityForwardInformations.length - 1]
                = (byte) (numberPortabilityForwardInformations[numberPortabilityForwardInformations.length - 1] | 0b10000000);
        return (numberPortabilityForwardInformations);
    }

    @Override
    public void decode(byte[] data) {
        this.numberPortabilityForwardInformations = data;
    }

    @Override
    public int getParameterCode() {
        return NUMBER_PORTABILITY_FORWARD_INFORMATION;
    }

    public static NumberPortabilityStatusIndicator getNumberPortabilityStatusIndicator(byte b) {
        return NumberPortabilityStatusIndicator.getInstance(b & 0b00001111);
    }

    public static byte encodeNumberPortabilityForwardInformationByte(NumberPortabilityStatusIndicator numberPortabilityStatusIndicator) {
        return (byte) (numberPortabilityStatusIndicator.value() & 0b00001111);
    }
}
