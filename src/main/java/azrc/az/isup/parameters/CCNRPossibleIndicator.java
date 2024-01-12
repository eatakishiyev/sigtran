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
public class CCNRPossibleIndicator implements IsupParameter {

    private boolean ccnrPossible;

    public CCNRPossibleIndicator() {
    }

    public CCNRPossibleIndicator(boolean ccnrPossible) {
        this.ccnrPossible = ccnrPossible;
    }

    public boolean isCcnrPossible() {
        return ccnrPossible;
    }

    public void setCcnrPossible(boolean ccnrPossible) {
        this.ccnrPossible = ccnrPossible;
    }

    @Override
    public int getParameterCode() {
        return CCNR_POSSIBLE_INDICATOR;
    }

    @Override
    public byte[] encode() throws Exception {
        return new byte[]{(byte) (ccnrPossible ? 1 : 0)};
    }

    @Override
    public void decode(byte[] data) throws Exception {
        this.ccnrPossible = data[0] == 0;
    }

}
