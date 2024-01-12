/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.messages.management;

import azrc.az.sigtran.sccp.general.Encodable;
import azrc.az.sigtran.sccp.parameters.SCMGFormatIdentifiers;

/**
 *
 * @author eatakishiyev
 */
public interface ManagementMessage extends Encodable{
    
    public SCMGFormatIdentifiers getMessageType();
    
}
