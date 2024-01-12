/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.controls;

import azrc.az.sigtran.m3ua.MTPTransferMessage;
import azrc.az.sigtran.sccp.messages.connectionless.SCCPMessage;

/**
 *
 * @author eatakishiyev
 */
public interface SCCPRoutingControl {

    public void onMtpTransferIndication(MTPTransferMessage mTPTransferMessage);

    public void connectionlessMessage(SCCPMessage message, boolean localOrigiator) throws Exception;
}
