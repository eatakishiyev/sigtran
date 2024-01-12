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
public class PDPAddress extends OctetString {

    public PDPAddress() {
        super();
    }

    public PDPAddress(byte[] value) {
        super(value);
    }

    @Override
    public int getMinLength() {
        return 1;
    }

    @Override
    public int getMaxLength() {
        return 16;
    }

}
