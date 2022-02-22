/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.controls;

import dev.ocean.sigtran.m3ua.MTPTransferMessage;
import dev.ocean.sigtran.sccp.messages.connectionless.SCCPMessage;

/**
 *
 * @author eatakishiyev
 */
public interface SCCPRoutingControl {

    public void onMtpTransferIndication(MTPTransferMessage mTPTransferMessage);

    public void connectionlessMessage(SCCPMessage message, boolean localOrigiator) throws Exception;
}
