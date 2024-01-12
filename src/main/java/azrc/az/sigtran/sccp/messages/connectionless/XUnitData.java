/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.messages.connectionless;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import azrc.az.sigtran.utils.ByteUtils;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.messages.MessageHandling;
import azrc.az.sigtran.sccp.messages.MessageType;
import azrc.az.sigtran.sccp.messages.ProtocolClassEnum;
import azrc.az.sigtran.sccp.parameters.HopCounter;
import azrc.az.sigtran.sccp.parameters.Importance;
import azrc.az.sigtran.sccp.parameters.Segmentation;

/**
 *
 * @author eatakishiyev
 */
public class XUnitData extends SCCPMessage {

    public static final MessageType MESSAGE_TYPE = MessageType.XUDT;

    private HopCounter hopCounter;
    private Segmentation segmentation;
    private Importance importance;

    private final int END_OF_OPTIONAL_PARAMETERS = 0x00;
//    
    private Integer sls;//

    protected XUnitData() {
    }

    protected XUnitData(ByteArrayInputStream bais) throws Exception {
        this.decode(bais);
    }

    protected XUnitData(SCCPAddress calledPartyAddress, SCCPAddress callingPartyAddress,
            int hopCounter, byte[] data) {
        this.calledPartyAddress = calledPartyAddress;
        this.callingPartyAddress = callingPartyAddress;
        this.hopCounter = new HopCounter(hopCounter);
        this.data = data;
    }

    protected XUnitData(SCCPAddress calledPartyAddress, SCCPAddress callingPartyAddress,
            int hopCounter) {
        this.calledPartyAddress = calledPartyAddress;
        this.callingPartyAddress = callingPartyAddress;
        this.hopCounter = new HopCounter(hopCounter);
    }

    @Override
    public String toString() {
        return "XUnitData: [CalledParty = " + calledPartyAddress
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
                + "; Segmentation = " + segmentation + "]";
    }

    @Override
    public MessageType getType() {
        return MESSAGE_TYPE;
    }

    @Override
    public Integer getSls() {
        return this.sls;
    }

    @Override
    public void setSls(Integer sls) {
        this.sls = sls;
    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws Exception {
        int pointerPosition = 4;
        baos.write(XUnitData.MESSAGE_TYPE.value());
        int iProtocolClass = ((messageHandling.value() << 4) | protocolClass.value()) & 0xFF;
        baos.write(iProtocolClass);//Mandatory fixed

        hopCounter.encode(baos);

        baos.write(pointerPosition);//Pointer to First Mandatory Variable length parameter(Called party address)

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
        int iProtocolClass = bais.read() & 0xff;

        this.protocolClass = ProtocolClassEnum.getInstance(iProtocolClass & 0x0f);
        this.messageHandling = MessageHandling.getInstance((iProtocolClass >> 4) & 0x0f);

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
//
//            if (bais.available() == 1) {
//                break;
//            }
        }
    }

    /**
     * @return the hopCounter
     */
    @Override
    public HopCounter getHopCounter() {
        return hopCounter;
    }

    /**
     * @param hopCounter the hopCounter to set
     */
    @Override
    public void setHopCounter(int hopCounter) {
        this.hopCounter = new HopCounter(hopCounter);
    }

    /**
     * @return the segmentation
     */
    public Segmentation getSegmentation() {
        return segmentation;
    }

    /**
     * @param segmentation the segmentation to set
     */
    public void setSegmentation(Segmentation segmentation) {
        this.segmentation = segmentation;
    }

    @Override
    public Importance getImportance() {
        return importance;
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
    public int getDefaultImportance() {
        return 4;
    }

    @Override
    public int getMaxImportance() {
        return 6;
    }

    @Override
    public boolean isConnectionless() {
        return true;
    }

}
