/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.messages.connectionless;

import azrc.az.sigtran.utils.ByteUtils;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.general.ErrorReason;
import azrc.az.sigtran.sccp.messages.MessageType;
import azrc.az.sigtran.sccp.parameters.HopCounter;
import azrc.az.sigtran.sccp.parameters.Importance;
import azrc.az.sigtran.sccp.parameters.Segmentation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class XUnitDataService extends SCCPMessage {

    private final int END_OF_OPTIONAL_PARAMETERS = 0x00;
    public static final MessageType MESSAGE_TYPE = MessageType.XUDTS;
    private HopCounter hopCounter;
    private ErrorReason returnCause;
    private Segmentation segmentation;
    private Importance importance;
//    
    private Integer sls;

    protected XUnitDataService(ErrorReason returnCause, int hopCounter, SCCPAddress calledParty,
            SCCPAddress callingParty, byte[] data) {
        this.returnCause = returnCause;
        this.hopCounter = new HopCounter(hopCounter);
        this.calledPartyAddress = calledParty;
        this.callingPartyAddress = callingParty;
        this.data = data;
    }

    protected XUnitDataService(ByteArrayInputStream bais) throws Exception {
        this.decode(bais);
    }

    protected XUnitDataService() {

    }

    public ErrorReason getReturnCause() {
        return returnCause;
    }

    public void setReturnCause(ErrorReason returnCause) {
        this.returnCause = returnCause;
    }

    @Override
    public MessageType getType() {
        return MESSAGE_TYPE;
    }

    @Override
    public HopCounter getHopCounter() {
        return hopCounter;
    }

    @Override
    public void setHopCounter(int hopCounter) {
        this.hopCounter = new HopCounter(hopCounter);
    }

    @Override
    public Integer getSls() {
        return this.sls;
    }

    @Override
    public void setSls(Integer sls) {
        this.sls = sls;
    }

    public Segmentation getSegmentation() {
        return segmentation;
    }

    public void setSegmentation(Segmentation segmentation) {
        this.segmentation = segmentation;
    }

    @Override
    public final void encode(ByteArrayOutputStream baos) throws Exception {
        int pointerPosition = 4;
        baos.write(XUnitDataService.MESSAGE_TYPE.value());
        baos.write(returnCause.value());//Mandatory fixed

        hopCounter.encode(baos);

        baos.write(pointerPosition);//Pointer to First Mandatory Variable parameter(Called party address)

        byte[] cldPa = calledPartyAddress.encode();
        pointerPosition += cldPa.length;
        baos.write(pointerPosition);

        byte[] clngPa = callingPartyAddress.encode();
        pointerPosition += clngPa.length;
        baos.write(pointerPosition);

        int pointerToOptionalPart = 0;

        if (segmentation != null || importance != null) {
            //Pointer to to optional part is sum of length of 3 variable mandatory 
            //parameters plus 3 bytes of their length byte and plus 1
            pointerToOptionalPart = cldPa.length + clngPa.length + data.length
                    + 3 + 1;
        }

        baos.write(pointerToOptionalPart);

        baos.write(cldPa.length);
        baos.write(cldPa);

        baos.write(clngPa.length);
        baos.write(clngPa);

        baos.write(data.length);
        baos.write(data);

        if (segmentation != null) {
            segmentation.encode(baos);
        }

        if (importance != null) {
            importance.encode(baos);
        }

        baos.write(END_OF_OPTIONAL_PARAMETERS);
    }

    @Override
    public final void decode(ByteArrayInputStream bais) throws Exception {
        int iReturnCause = bais.read() & 0xff;

        this.returnCause = ErrorReason.getInstance(iReturnCause);

        hopCounter = new HopCounter();
        hopCounter.decode(bais);

        int calledPartyPointer = bais.read() & 0xff;
        bais.mark(bais.available());
        bais.skip(calledPartyPointer - 1);
        int len = bais.read() & 0xff;
        byte[] buffer = new byte[len];
        bais.read(buffer);
        calledPartyAddress = new SCCPAddress();
        calledPartyAddress.decode(new ByteArrayInputStream(buffer));

        bais.reset();
        int callingPartyPointer = bais.read() & 0xff;
        bais.mark(bais.available());
        bais.skip(callingPartyPointer - 1);
        len = bais.read() & 0xff;
        buffer = new byte[len];
        bais.read(buffer);
        callingPartyAddress = new SCCPAddress();
        callingPartyAddress.decode(new ByteArrayInputStream(buffer));

        bais.reset();
        int userDataPointer = bais.read() & 0xff;
        bais.mark(bais.available());
        bais.skip(userDataPointer - 1);
        len = bais.read() & 0xff;
        setData(new byte[len]);
        bais.read(data);

        bais.reset();

        int optionalParameterPointer = bais.read() & 0xff;

        if (optionalParameterPointer == END_OF_OPTIONAL_PARAMETERS) {
            return;//End of optional parameters
        }

        bais.skip(optionalParameterPointer - 1);

        while (bais.available() > 0) {
            int parameterType = bais.read() & 0xff;

            switch (parameterType) {
                case Segmentation.PARAMETER_NAME://segmentation
                    this.segmentation = new Segmentation();
                    this.segmentation.decode(bais);
                    break;
                case Importance.PARAMETER_NAME:
                    this.importance = new Importance();
                    importance.decode(bais);
                    break;
                case END_OF_OPTIONAL_PARAMETERS:
                    return;

            }
        }
    }

    /**
     *
     * @param importance
     */
    @Override
    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    @Override
    public Importance getImportance() {
        return importance;
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

    @Override
    public String toString() {
        return "XUnitDataService: [CalledParty = " + calledPartyAddress
                + "; CallingParty = " + callingPartyAddress
                + "; MessageHandling = " + messageHandling
                + "; ProtocolClass = " + protocolClass
                + "; Importance = " + importance
                + "; OPC = " + opc
                + "; DPC = " + dpc
                + "; SLS = " + sls
                + "; NI = " + networkIndicator
                + "; UserData = " + ByteUtils.bytes2Hex(data)
                + "; Modified = " + isModified()
                + "; Segmented = " + isSegmented()
                + "; HopCounter = " + hopCounter
                + "; Segmentation = " + segmentation
                + "; ReturnCause = " + returnCause + "]";
    }

}
