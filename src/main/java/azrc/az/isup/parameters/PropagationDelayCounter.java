/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

/**
 *
 * @author eatakishiyev
 */
public class PropagationDelayCounter implements IsupParameter {

    private int value;

    public PropagationDelayCounter() {
    }

    public PropagationDelayCounter(int value) {
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
        return PROPAGATION_DELAY_COUNTER;
    }

}
