/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnInputStream;

/**
 *
 * @author eatakishiyev
 */
public class ISDNAddressString extends AddressStringImpl {

    public ISDNAddressString(AddressNature addressNature, NumberingPlan numberingPlan, String address) {
        super(addressNature, numberingPlan, address);
    }

    public ISDNAddressString(int addressNature, int numberingPlan, String address) {
        super(AddressNature.getAddressNature(addressNature), NumberingPlan.getNumberingPlan(numberingPlan), address);
    }

    public ISDNAddressString() {
    }

    public ISDNAddressString(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        super.decode(ais);
    }

    @Override
    public int getMaxLength() {
        return 9; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getMinLength() {
        return 1;//To change body of generated methods, choose Tools | Templates.
    }

}
