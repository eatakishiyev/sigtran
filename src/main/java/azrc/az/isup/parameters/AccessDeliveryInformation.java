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
public class AccessDeliveryInformation implements IsupParameter {

    private boolean setUpMessageGenerated;//0

    public AccessDeliveryInformation() {
    }

    public AccessDeliveryInformation(boolean setUpMessageGenerated) {
        this.setUpMessageGenerated = setUpMessageGenerated;
    }

    public void setSetUpMessageGenerated(boolean setUpMessageGenerated) {
        this.setUpMessageGenerated = setUpMessageGenerated;
    }

    @Override
    public byte[] encode() throws Exception {
        return new byte[]{(byte) (setUpMessageGenerated ? 0 : 1)};
    }

    @Override
    public void decode(byte[] data) throws Exception {
        setUpMessageGenerated = data[0] == 0;
    }

    @Override
    public int getParameterCode() {
        return ACCESS_DELIVERY_INFORMATION;
    }

}
