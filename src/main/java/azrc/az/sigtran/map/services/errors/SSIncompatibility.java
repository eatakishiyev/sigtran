/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors;

import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.MAPUserErrorValues;
import azrc.az.sigtran.map.services.errors.params.SSIncompatibilityCause;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * ss-Incompatibility ERROR ::= {
 * PARAMETER
 * SS-IncompatibilityCause
 * -- optional
 * CODE local:20 }
 *
 * @author eatakishiyev
 */
public class SSIncompatibility implements MAPUserError {

    private SSIncompatibilityCause ssIncompatiblityCause;

    public SSIncompatibility() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws Exception {
        if (ssIncompatiblityCause != null) {
            ssIncompatiblityCause.encode(aos);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        this.ssIncompatiblityCause = new SSIncompatibilityCause();
        ssIncompatiblityCause.decode(ais);
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return MAPUserErrorValues.SS_INCOMPATIBILITY;
    }

    public SSIncompatibilityCause getSsIncompatiblityCause() {
        return ssIncompatiblityCause;
    }

    public void setSsIncompatiblityCause(SSIncompatibilityCause ssIncompatiblityCause) {
        this.ssIncompatiblityCause = ssIncompatiblityCause;
    }

}
