/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

/**
 *
 * @author eatakishiyev
 */
public class CallHistoryInformation implements IsupParameter {

    private int value;

    public CallHistoryInformation() {
    }

    public CallHistoryInformation(int value) {
        this.value = value;
    }

    @Override
    public byte[] encode() {
        return new byte[]{(byte) (value >> 8), (byte) (value & 0b11111111)};
    }

    @Override
    public void decode(byte[] data) {
        this.value = data[0] & 0xFF;
        this.value = (value << 8) | (data[1] & 0xFF);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int getParameterCode() {
        return CALL_HISTORY_INFORMATION;
    }

}
