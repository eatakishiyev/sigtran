/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.messages.connectionless;

import azrc.az.sigtran.m3ua.NetworkIndicator;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.general.Encodable;
import azrc.az.sigtran.sccp.messages.MessageHandling;
import azrc.az.sigtran.sccp.messages.ProtocolClassEnum;
import azrc.az.sigtran.sccp.messages.MessageType;
import azrc.az.sigtran.sccp.parameters.HopCounter;
import azrc.az.sigtran.sccp.parameters.Importance;

/**
 *
 * @author root
 */
public abstract class SCCPMessage implements Encodable {

    protected SCCPAddress calledPartyAddress;
    protected SCCPAddress callingPartyAddress;
    protected MessageHandling messageHandling = MessageHandling.RETURN_MESSAGE_ON_ERROR;
    protected NetworkIndicator networkIndicator;
    protected byte[] data;//TCAP MAP data
    protected int opc;
    protected int dpc;
    protected ProtocolClassEnum protocolClass = ProtocolClassEnum.CLASS0;
    private boolean modified = false;
    private boolean segmented = false;

    public abstract MessageType getType();

    public final SCCPAddress getCalledPartyAddress() {
        return this.calledPartyAddress;
    }

    public final void setCalledPartyAddress(SCCPAddress address) {
        this.calledPartyAddress = address;
    }

    public final SCCPAddress getCallingPartyAddress() {
        return this.callingPartyAddress;
    }

    public final void setCallingPartyAddress(SCCPAddress address) {
        this.callingPartyAddress = address;
    }

    /**
     * @return the opc
     */
    public final int getOpc() {
        return this.opc;
    }

    /**
     * @param opc the opc to set
     */
    public final void setOpc(int opc) {
        this.opc = opc;
    }

    /**
     * @return the dpc
     */
    public final int getDpc() {
        return this.dpc;
    }

    /**
     * @param dpc the dpc to set
     */
    public final void setDpc(int dpc) {
        this.dpc = dpc;
    }

    /**
     * @return the sls
     */
    public abstract Integer getSls();

    /**
     * @param sls the sls to set
     */
    public abstract void setSls(Integer sls);

    public final NetworkIndicator getNi() {
        return this.networkIndicator;
    }

    public final void setNi(NetworkIndicator ni) {
        this.networkIndicator = ni;
    }

    public final void setMessageHandling(MessageHandling messageHandling) {
        this.messageHandling = messageHandling;
    }

    public final MessageHandling getMessageHandling() {
        return this.messageHandling;
    }

    public final byte[] getData() {
        return this.data;
    }

    public final void setData(byte[] data) {
        this.data = data;
    }

    public final void setProtocolClass(ProtocolClassEnum protocolClass) {
        this.protocolClass = protocolClass;
    }

    public final ProtocolClassEnum getProtocolClass() {
        return protocolClass;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isModified() {
        return modified;
    }

    public void setSegmented(boolean segmented) {
        this.segmented = segmented;
    }

    public boolean isSegmented() {
        return segmented;
    }

    public abstract HopCounter getHopCounter();

    public abstract void setHopCounter(int hopCounter);

    public abstract Importance getImportance();

    public abstract void setImportance(Importance importance);

    //Maximum importance value for message from ITU Q.714 Table 2
    public abstract int getMaxImportance();

    //Maximum importance value for message from ITU Q.714 Table 2
    public abstract int getDefaultImportance();
    
    public abstract boolean isConnectionless();
}
