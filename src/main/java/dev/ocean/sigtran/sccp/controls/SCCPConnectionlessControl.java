/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.controls;

import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.exception.RoutingFailureException;
import dev.ocean.sigtran.sccp.general.ErrorReason;
import dev.ocean.sigtran.sccp.messages.connectionless.SCCPMessage;
import dev.ocean.sigtran.sccp.messages.management.ManagementMessage;

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
