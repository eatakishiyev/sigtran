/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.TerminatingAccessIndicator;

/**
 *
 * @author eatakishiyev
 */
public class BackwardGVNS implements IsupParameter {

    private byte[] data;

    @Override
    public byte[] encode() throws Exception {
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (data[i] & 0b01111111);
        }
        data[data.length - 1] = (byte) (data[data.length - 1] | (0b10000000));
        return data;
    }

    @Override
    public void decode(byte[] data) throws Exception {
        this.data = data;
    }

    @Override
    public int getParameterCode() {
        return BACKWARD_GVNS;
    }

    public static TerminatingAccessIndicator decodeBackwardGVNSByte(int b) {
        return TerminatingAccessIndicator.getInstance(b & 0b00000011);
    }

    public static int encodeBackwardGVNS(TerminatingAccessIndicator terminatingAccessIndicator) {
        return terminatingAccessIndicator.value() & 0b00000011;
    }

}
