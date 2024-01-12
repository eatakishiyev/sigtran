/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.controls;

import azrc.az.sigtran.sccp.exception.RoutingFailureException;
import azrc.az.sigtran.sccp.messages.connectionless.SCCPMessage;
import azrc.az.sigtran.sccp.messages.management.ManagementMessage;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.general.ErrorReason;

/**
 *
 * @author eatakishiyev
 */
public interface SCCPConnectionlessControl {

    public void sendConnectionlessMessage(SCCPMessage message) throws Exception;

    public void messageForLocalSubsystem(SCCPMessage message, boolean localOriginator) throws Exception;

    public void routingFailure(SCCPMessage message, ErrorReason sccpErrorReason, boolean localOriginator) throws RoutingFailureException;

    public void sendSccpManagementMessage(SCCPAddress callingParty, SCCPAddress calledParty, ManagementMessage message, boolean localOriginator);
}
