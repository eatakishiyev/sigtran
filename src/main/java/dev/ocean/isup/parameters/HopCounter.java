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
public class HopCounter implements IsupParameter {

    private int hopCount = 0;

    public HopCounter() {
    }

    public HopCounter(int hopCount) {
        this.hopCount = hopCount;
    }

    @Override
    public byte[] encode() {
        return new byte[]{(byte) (hopCount & 0b00011111)};
    }

    @Override
    public void decode(byte[] data) {
        this.hopCount = data[0] & 0b00011111;
    }

    public int getHopCount() {
        return hopCount;
    }

    public void setHopCount(int hopCount) {
        this.hopCount = hopCount;
    }

    @Override
    public int getParameterCode() {
        return HOP_COUNTER;
    }

}
