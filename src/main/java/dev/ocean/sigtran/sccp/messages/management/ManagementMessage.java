/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.messages.management;

import dev.ocean.sigtran.sccp.general.Encodable;
import dev.ocean.sigtran.sccp.parameters.SCMGFormatIdentifiers;

/**
 *
 * @author eatakishiyev
 */
public interface ManagementMessage extends Encodable{
    
    public SCMGFormatIdentifiers getMessageType();
    
}
