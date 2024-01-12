/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 *
 * @author eatakishiyev
 */
public class GPRSChargingID extends OctetString {

    public GPRSChargingID() {
        super();
    }

    public GPRSChargingID(byte[] value) {
        super(value);
    }

    @Override
    public int getMinLength() {
        return 4;
    }

    @Override
    public int getMaxLength() {
        return 4;
    }

}
