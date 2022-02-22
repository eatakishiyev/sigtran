/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.address;

import java.io.Serializable;
import dev.ocean.sigtran.sccp.globaltitle.GlobalTitle;

/**
 *
 * @author eatakishiyev
 */
public class SCCPAddressFactory implements Serializable{
    private SCCPAddressFactory(){
        
    }
    
    
    public static SCCPAddress createSCCPAddress() {
        return new SCCPAddress();
    }

    public static SCCPAddress createSCCPAddress(RoutingIndicator routingIndicator, Integer spc, GlobalTitle globalTitle, SubSystemNumber ssn) {
        return new SCCPAddress(routingIndicator, spc, globalTitle, ssn);
    }
}
