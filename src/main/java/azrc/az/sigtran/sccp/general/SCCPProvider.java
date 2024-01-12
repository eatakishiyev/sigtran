/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general;

import java.io.Serializable;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.address.SubSystemNumber;
import azrc.az.sigtran.sccp.messages.MessageHandling;

/**
 *
 * @author root
 */
public interface SCCPProvider extends Serializable {

    public void send(SCCPAddress calledAddress, SCCPAddress callingAddress, boolean sequenceControl,
            Integer sequenceNumber, MessageHandling messageHandling, byte[] userData) throws Exception;

    public void removeSCCPUser(SubSystemNumber ssn) throws Exception;

    public int generateSequence();

    public SCCPLayerManagementMBean getSCCPLayerManagement();
}
