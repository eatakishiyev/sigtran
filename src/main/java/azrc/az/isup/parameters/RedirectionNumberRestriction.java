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
public class RedirectionNumberRestriction implements IsupParameter {

    private boolean presentationAllwed;//0

    @Override
    public byte[] encode() throws Exception {
        return new byte[]{(byte) (presentationAllwed ? 0 : 1)};
    }

    @Override
    public void decode(byte[] data) throws Exception {
        this.presentationAllwed = data[0] == 0;
    }

    @Override
    public int getParameterCode() {
        return REDIRECTION_NUMBER_RESTRICTION;
    }

    public boolean isPresentationAllwed() {
        return presentationAllwed;
    }

    public void setPresentationAllwed(boolean presentationAllwed) {
        this.presentationAllwed = presentationAllwed;
    }
}
