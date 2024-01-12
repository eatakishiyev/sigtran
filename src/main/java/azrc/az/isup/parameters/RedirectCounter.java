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
public class RedirectCounter implements IsupParameter {

    private int counter;

    public RedirectCounter() {
    }

    public RedirectCounter(int counter) {
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
        return REDIRECT_COUNTER;
    }

}
