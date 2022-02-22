/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.messages.connectionless;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.messages.MessageHandling;
import dev.ocean.sigtran.sccp.messages.MessageType;
import dev.ocean.sigtran.sccp.messages.ProtocolClassEnum;
import dev.ocean.sigtran.sccp.parameters.HopCounter;
import dev.ocean.sigtran.sccp.parameters.Importance;
import dev.ocean.sigtran.utils.ByteUtils;

/**
 *
 * @author root
 */
public class UnitData extends SCCPMessage {

    /*
     * Note - Each pointer is encoded as a single octet or two in the case of
     * LUDT and LUDTS. In the case of two - octet pointer, the less significant
     * octet shall be transmitted before the more significant octet.
     *
     */
    public static final MessageType MESSAGE_TYPE = MessageType.UDT;
//    
    private Integer sls;//

    protected UnitData() {
    }

    protected UnitData(ByteArrayInputStream bais) throws Exception {
        this.decode(bais);
    }

    protected UnitData(SCCPAddress calledPartyAddress, SCCPAddress callingPartyAddress, byte[] data) {
        this.calledPartyAddress = calledPartyAddress;
        this.callingPartyAddress = callingPartyAddress;
        this.data = data;
    }

    protected UnitData(SCCPAddress calledPartyAddress, SCCPAddress callingPartyAddress) {
        this.calledPartyAddress = calledPartyAddress;
        this.callingPartyAddress = callingPartyAddress;
    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws Exception {
        ByteArrayOutputStream tmpSccpAddress = new ByteArrayOutputStream(272);
        int pointerPosition = 3;
        baos.write(UnitData.MESSAGE_TYPE.value());//Mandatory fixed

        int iProtocolClass = ((messageHandling.value() << 4) | protocolClass.value()) & 0xFF;
        baos.write(iProtocolClass);//Mandatory fixed
        baos.write(pointerPosition);//Pointer to First Mandatory Variable parameter(Called party address)

        this.calledPartyAddress.encode(tmpSccpAddress);
        byte[] cdpa = tmpSccpAddress.toByteArray();
        tmpSccpAddress.reset();
        pointerPosition += cdpa.length;
        baos.write(pointerPosition);//Pointer to Second Mandatory Variable parameter(Calling party address)

        this.callingPartyAddress.encode(tmpSccpAddress);
        byte[] cnpa = tmpSccpAddress.toByteArray();
        tmpSccpAddress.reset();
        pointerPosition += cnpa.length;
        baos.write(pointerPosition);//Pointer to Third Mandatory Variable parameter(Data);

        baos.write(cdpa.length);//Length indicator of Parameter Called party address
        baos.write(cdpa);//Parameter Called party address

        baos.write(cnpa.length);//Length indicator of Parameter Calling party address
        baos.write(cnpa);//Parameter Calling party address

        baos.write(getData().length);
        baos.write(getData());
    }

    @Override
    public final void decode(ByteArrayInputStream bais) throws Exception {
        int iProtocolClass = bais.read() & 0xff;

        this.protocolClass = ProtocolClassEnum.getInstance(iProtocolClass & 0x0f);
        this.messageHandling = MessageHandling.getInstance((iProtocolClass >> 4) & 0x0f);

        int cpaPointer = bais.read() & 0xff;
        bais.mark(bais.available());

        bais.skip(cpaPointer - 1);
        int len = bais.read() & 0xff;

        byte[] buffer = new byte[len];
        bais.read(buffer);

        calledPartyAddress = new SCCPAddress();
        calledPartyAddress.decode(new ByteArrayInputStream(buffer));

        bais.reset();
        cpaPointer = bais.read() & 0xff;
        bais.mark(bais.available());

        bais.skip(cpaPointer - 1);
        len = bais.read() & 0xff;

        buffer = new byte[len];
        bais.read(buffer);

        callingPartyAddress = new SCCPAddress();
        callingPartyAddress.decode(new ByteArrayInputStream(buffer));

        bais.reset();
        cpaPointer = bais.read() & 0xff;

        bais.skip(cpaPointer - 1);
        len = bais.read() & 0xff;

        data = new byte[len];
        bais.read(data);
    }

    @Override
    public MessageType getType() {
        return UnitData.MESSAGE_TYPE;
    }

    /**
     * @return the sls
     */
    @Override
    public Integer getSls() {
        return sls;
    }

    /**
     * @param sls the sls to set
     */
    @Override
    public void setSls(Integer sls) {
        this.sls = sls;
    }

    @Override
    public String toString() {
        return "UnitData: [CalledParty = " + calledPartyAddress
                + "; CallingParty = " + callingPartyAddress
                + "; MessageHandling = " + messageHandling
                + "; ProtocolClass = " + protocolClass
                + "; OPC = " + opc
                + "; DPC = " + dpc
                + "; SLS = " + sls
                + "; NI = " + networkIndicator
                + "; UserData = " + ByteUtils.bytes2Hex(data)
                + "; Modified = " + isModified() + "]";
    }

    @Override
    public HopCounter getHopCounter() {
        return null;
    }

    @Override
    public void setHopCounter(int hopCounter) {

    }

    @Override
    public void setImportance(Importance importance) {

    }

    @Override
    public Importance getImportance() {
        return null;
    }

    @Override
    public int getMaxImportance() {
        return 6;
    }

    @Override
    public int getDefaultImportance() {
        return 4;
    }

    @Override
    public boolean isConnectionless() {
        return true;
    }

}
