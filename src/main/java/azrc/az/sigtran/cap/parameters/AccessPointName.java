/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import azrc.az.sigtran.tcap.Encodable;
import azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * AccessPointName {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE(
 * bound.&minAccessPointNameLength .. bound.&maxAccessPointNameLength)) --
 * Indicates the AccessPointName, refer to 3GPP TS 24.008 [9] for the encoding.
 * -- It shall be coded as in the value part defined in 3GPP TS 24.008, -- i.e.
 * the 3GPP TS 24.008 IEI and 3GPP TS 24.008 octet length indicator -- shall not
 * be included.
 *
 * @author eatakishiyev
 */
public class AccessPointName implements Encodable{
    private String name;

    public AccessPointName() {
    }

    public AccessPointName(String name) {
        this.name = name;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        
    }
    
    
    
    
    
}
