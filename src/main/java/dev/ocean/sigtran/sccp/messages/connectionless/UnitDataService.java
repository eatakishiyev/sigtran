/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.messages.connectionless;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.address.SCCPAddressFactory;
import dev.ocean.sigtran.sccp.general.ErrorReason;
import dev.ocean.sigtran.sccp.messages.MessageType;
import dev.ocean.sigtran.sccp.parameters.HopCounter;
import dev.ocean.sigtran.sccp.parameters.Importance;
import dev.ocean.sigtran.utils.ByteUtils;
import org.mobicents.protocols.asn.AsnException;

/**
 *
 * @author root
 */
public class UnitDataService extends SCCPMessage {

    public static MessageType messageType = MessageType.UDTS;
    private ErrorReason returnCause;

    private int sls;
    private int mp;

    protected UnitDataService() {
    }

    protected UnitDataService(ByteArrayInputStream bais) throws Exception {
        this.decode(bais);
    }

    protected UnitDataService(ErrorReason returnCause, SCCPAddress calledPartyAddress, SCCPAddress callingPartyAddress, byte[] data) {
        this();
        this.returnCause = returnCause;
        this.calledPartyAddress = calledPartyAddress;
        this.callingPartyAddress = callingPartyAddress;
        this.data = data;
    }

    @Override
    public String toString() {
        return "UnitDataService: [CalledParty = " + calledPartyAddress
                + "; CallingParty = " + callingPartyAddress
                + "; MessageHandling = " + messageHandling
                + "; OPC = " + opc
                + "; DPC = " + dpc
                + "; SLS = " + sls
                + "; NI = " + networkIndicator
                + "; UserData = " + ByteUtils.bytes2Hex(data)
                + "; Modified = " + isModified() + "]";
    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws IOException, Exception {
        ByteArrayOutputStream tmpSccpAddress = new ByteArrayOutputStream(128);
        int PointerToPosition = 3;
        baos.write(messageType.value() & 0xFF);
        baos.write(returnCause.value() & 0xFF);
        baos.write(PointerToPosition);
        this.calledPartyAddress.encode(tmpSccpAddress);
        byte[] cdpa = tmpSccpAddress.toByteArray();
        tmpSccpAddress.reset();
        PointerToPosition += cdpa.length;
        baos.write(PointerToPosition);
        this.callingPartyAddress.encode(tmpSccpAddress);
        byte[] cnpa = tmpSccpAddress.toByteArray();
        PointerToPosition += cnpa.length;
        baos.write(PointerToPosition);
        baos.write(cdpa.length);
        baos.write(cdpa);
        baos.write(cnpa.length);
        baos.write(cnpa);
        baos.write(data.length);
        baos.write(data);
    }

    /**
     *
     * @param bais
     * @throws Exception
     */
    @Override
    public final void decode(ByteArrayInputStream bais) throws IOException, AsnException, Exception {
        this.returnCause = ErrorReason.getInstance(bais.read());

        int cpaPointer = bais.read() & 0xff;
        bais.mark(bais.available());

        bais.skip(cpaPointer - 1);
        int len = bais.read() & 0xff;

        byte[] buffer = new byte[len];
        bais.read(buffer);

        calledPartyAddress = SCCPAddressFactory.createSCCPAddress();
        calledPartyAddress.decode(new ByteArrayInputStream(buffer));

        bais.reset();
        cpaPointer = bais.read() & 0xff;
        bais.mark(bais.available());

        bais.skip(cpaPointer - 1);
        len = bais.read() & 0xff;

        buffer = new byte[len];
        bais.read(buffer);

        callingPartyAddress = SCCPAddressFactory.createSCCPAddress();
        callingPartyAddress.decode(new ByteArrayInputStream(buffer));

        bais.reset();
        cpaPointer = bais.read() & 0xff;

        bais.skip(cpaPointer - 1);
        len = bais.read() & 0xff;

        data = new byte[len];
        bais.read(data);
    }

    /**
     * @return the returnCause
     */
    public ErrorReason getReturnCause() {
        return returnCause;
    }

    @Override
    public MessageType getType() {
        return messageType;
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
    public HopCounter getHopCounter() {
        return null;
    }

    @Override
    public void setHopCounter(int hopCounter) {

    }

    @Override
    public Importance getImportance() {
        return null;
    }

    @Override
    public void setImportance(Importance importance) {

    }

    @Override
    public int getDefaultImportance() {
        return 3;
    }

    @Override
    public int getMaxImportance() {
        return 0;
    }

    @Override
    public boolean isConnectionless() {
        return true;
    }

}
