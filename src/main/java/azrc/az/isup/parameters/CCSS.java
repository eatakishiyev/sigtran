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
public class CCSS implements IsupParameter {

    private boolean ccssCall;

    public CCSS() {
    }

    public CCSS(boolean ccssCall) {
        this.ccssCall = ccssCall;
    }

    @Override
    public byte[] encode() {
        return new byte[]{(byte) (ccssCall ? 1 : 0)};
    }

    @Override
    public void decode(byte[] data) {
        this.ccssCall = (data[0] & 0b00000001) == 1;
    }

    public boolean isCcssCall() {
        return ccssCall;
    }

    public void setCcssCall(boolean ccssCall) {
        this.ccssCall = ccssCall;
    }

    @Override
    public int getParameterCode() {
        return CCSS;
    }

}
