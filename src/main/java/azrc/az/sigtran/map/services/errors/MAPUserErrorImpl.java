/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors;

import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.MAPUserErrorValues;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class MAPUserErrorImpl implements MAPUserError {

    private final MAPUserErrorValues userError;

    public MAPUserErrorImpl(MAPUserErrorValues userError) {
        this.userError = userError;
    }
    
    
    @Override
    public void encode(AsnOutputStream aos) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void decode(AsnInputStream ais) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MAPUserErrorValues getMAPUserErrorValue() {
        return userError;
    }
    
}
