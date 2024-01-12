/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general;

import azrc.az.sigtran.sccp.messages.ProtocolClassEnum;
import azrc.az.sigtran.sccp.messages.connectionless.SCCPConnectionlessMessageFactory;
import azrc.az.sigtran.sccp.messages.connectionless.UnitData;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.address.SubSystemNumber;
import azrc.az.sigtran.sccp.messages.MessageHandling;

/**
 *
 * @author root
 */
public class SCCPProviderImpl implements SCCPProvider {

    private transient final SCCPStackImpl sccpStack;

    protected SCCPProviderImpl(SCCPStackImpl sccpStack) {
        this.sccpStack = sccpStack;
    }

    /**
     *
     * @param calledAddress
     * @param callingAddress
     * @param sequenceControl determine if required in sequence delivering. null
     * - if not required in sequence delivering
     * @param sequenceNumber
     * @param messageHandling@param userData correlationId using for logging purposes
     * to correlate up and down layer logs
     * @throws Exception
     */
    @Override
    public void send(SCCPAddress calledAddress, SCCPAddress callingAddress, boolean sequenceControl,
            Integer sequenceNumber, MessageHandling messageHandling, byte[] userData) throws Exception {

        ProtocolClassEnum protocolClass = ProtocolClassEnum.CLASS0;

        if (sequenceControl) {
            protocolClass = ProtocolClassEnum.CLASS1;
        }

        UnitData unitData = SCCPConnectionlessMessageFactory.createUnitData(calledAddress, callingAddress, userData);
        unitData.setProtocolClass(protocolClass);
        unitData.setMessageHandling(messageHandling);
        unitData.setSls(sequenceNumber);

        this.sccpStack.getSccpConnectionlessControl().
                sendConnectionlessMessage(unitData);
    }

    @Override
    public void removeSCCPUser(SubSystemNumber ssn) throws Exception {
        this.sccpStack.removeSCCPUser(ssn);
    }

    @Override
    public int generateSequence() {
        return this.sccpStack.getSlsGenerator().generate();
    }

    @Override
    public SCCPLayerManagementMBean getSCCPLayerManagement() {
        return sccpStack.getSccpLayerManagement();
    }

}
