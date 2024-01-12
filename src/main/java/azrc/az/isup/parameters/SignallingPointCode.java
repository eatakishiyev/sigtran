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
public class SignallingPointCode implements IsupParameter {

    private int spc;

    public SignallingPointCode() {
    }

    public SignallingPointCode(int spc) {
        this.spc = spc;
    }

    @Override
    public byte[] encode() {
        return new byte[]{(byte) (spc), (byte) ((spc >> 8) & 0x3F)};
    }

    @Override
    public void decode(byte[] data) {
        this.spc = data[1] & 0x3F;
        this.spc = spc << 8;
        this.spc = spc | (data[0] & 0xFF);
    }

    public int getSpc() {
        return spc;
    }

    public void setSpc(int spc) {
        this.spc = spc;
    }

    @Override
    public int getParameterCode() {
        return ORIGINATION_ISC_POINT_CODE;
    }

}
