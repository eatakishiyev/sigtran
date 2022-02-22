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
public class PivotCounter implements IsupParameter {

    private int counter;

    public PivotCounter() {
    }

    public PivotCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public byte[] encode() {
        return new byte[]{(byte) (counter & 0b00011111)};
    }

    @Override
    public void decode(byte[] data) {
        this.counter = data[0] & 0b00011111;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public int getParameterCode() {
        return PIVOT_COUNTER;
    }

}
