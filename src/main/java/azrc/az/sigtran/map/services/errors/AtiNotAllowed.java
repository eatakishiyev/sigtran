/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors;

import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.MAPUserErrorValues;
import azrc.az.sigtran.map.services.errors.params.AtiNotAllowedParam;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * ati-NotAllowed ERROR ::= { PARAMETER ATI-NotAllowedParam -- optional CODE
 * local:49 }
 *
 * @author eatakishiyev
 */
public class AtiNotAllowed implements MAPUserError {

    private AtiNotAllowedParam atiNotAllowedParam;

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        if (atiNotAllowedParam != null) {
            atiNotAllowedParam.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        atiNotAllowedParam = new AtiNotAllowedParam();
        atiNotAllowedParam.decode(ais);
    
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.ATI_NOT_ALLOWED;
    }

}
