/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.IncomingEchoControlDeviceInformationIndicator;
import azrc.az.isup.enums.IncomingEchoControlDeviceRequestIndicator;
import azrc.az.isup.enums.OutgoingEchoControlDeviceInformationIndicator;
import azrc.az.isup.enums.OutgoingEchoControlDeviceRequestIndicator;

/**
 *
 * @author eatakishiyev
 */
public class EchoControlInformation implements IsupParameter {

    private OutgoingEchoControlDeviceInformationIndicator outgoingEchoControlDeviceInformationIndicator;
    private IncomingEchoControlDeviceInformationIndicator incomingEchoControlDeviceInformationIndicator;
    private OutgoingEchoControlDeviceRequestIndicator outgoingEchoControlDeviceRequestIndicator;
    private IncomingEchoControlDeviceRequestIndicator incomingEchoControlDeviceRequestIndicator;

    public EchoControlInformation() {
    }

    public EchoControlInformation(OutgoingEchoControlDeviceInformationIndicator outgoingEchoControlDeviceInformationIndicator, IncomingEchoControlDeviceInformationIndicator incomingEchoControlDeviceInformationIndicator, OutgoingEchoControlDeviceRequestIndicator outgoingEchoControlDeviceRequestIndicator, IncomingEchoControlDeviceRequestIndicator incomingEchoControlDeviceRequestIndicator) {
        this.outgoingEchoControlDeviceInformationIndicator = outgoingEchoControlDeviceInformationIndicator;
        this.incomingEchoControlDeviceInformationIndicator = incomingEchoControlDeviceInformationIndicator;
        this.outgoingEchoControlDeviceRequestIndicator = outgoingEchoControlDeviceRequestIndicator;
        this.incomingEchoControlDeviceRequestIndicator = incomingEchoControlDeviceRequestIndicator;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EchoControlInformation[")
                .append("OutgoingEchoControlDeviceInformationIndicator:").append(outgoingEchoControlDeviceInformationIndicator)
                .append("; IncomingEchoControlDeviceInformationIndicator:").append(incomingEchoControlDeviceInformationIndicator)
                .append("; OutgoingEchoControlDeviceRequestIndicator:").append(outgoingEchoControlDeviceRequestIndicator)
                .append("; IncomingEchoControlDeviceRequestIndicator:").append(incomingEchoControlDeviceRequestIndicator)
                .append("]");
        return sb.toString();
    }

    @Override
    public byte[] encode() {
        int tmp = incomingEchoControlDeviceRequestIndicator.value();
        tmp = tmp << 2;
        tmp = tmp | outgoingEchoControlDeviceRequestIndicator.value();

        tmp = tmp << 2;
        tmp = tmp | incomingEchoControlDeviceInformationIndicator.value();

        tmp = tmp << 2;
        tmp = tmp | outgoingEchoControlDeviceInformationIndicator.value();

        return new byte[]{(byte) tmp};
    }

    @Override
    public void decode(byte[] data) {
        this.incomingEchoControlDeviceRequestIndicator = IncomingEchoControlDeviceRequestIndicator.getInstance((data[0] & 0xFF) >> 6);
        this.outgoingEchoControlDeviceRequestIndicator = OutgoingEchoControlDeviceRequestIndicator.getInstance(((data[0] >> 4) & 0b00000011));
        this.incomingEchoControlDeviceInformationIndicator = IncomingEchoControlDeviceInformationIndicator.getInstance((data[0] >> 2) & 0b00000011);
        this.outgoingEchoControlDeviceInformationIndicator = OutgoingEchoControlDeviceInformationIndicator.getInstance(data[0] & 0b00000011);
    }

    public IncomingEchoControlDeviceInformationIndicator getIncomingEchoControlDeviceInformationIndicator() {
        return incomingEchoControlDeviceInformationIndicator;
    }

    public void setIncomingEchoControlDeviceInformationIndicator(IncomingEchoControlDeviceInformationIndicator incomingEchoControlDeviceInformationIndicator) {
        this.incomingEchoControlDeviceInformationIndicator = incomingEchoControlDeviceInformationIndicator;
    }

    public IncomingEchoControlDeviceRequestIndicator getIncomingEchoControlDeviceRequestIndicator() {
        return incomingEchoControlDeviceRequestIndicator;
    }

    public void setIncomingEchoControlDeviceRequestIndicator(IncomingEchoControlDeviceRequestIndicator incomingEchoControlDeviceRequestIndicator) {
        this.incomingEchoControlDeviceRequestIndicator = incomingEchoControlDeviceRequestIndicator;
    }

    public OutgoingEchoControlDeviceInformationIndicator getOutgoingEchoControlDeviceInformationIndicator() {
        return outgoingEchoControlDeviceInformationIndicator;
    }

    public void setOutgoingEchoControlDeviceInformationIndicator(OutgoingEchoControlDeviceInformationIndicator outgoingEchoControlDeviceInformationIndicator) {
        this.outgoingEchoControlDeviceInformationIndicator = outgoingEchoControlDeviceInformationIndicator;
    }

    public OutgoingEchoControlDeviceRequestIndicator getOutgoingEchoControlDeviceRequestIndicator() {
        return outgoingEchoControlDeviceRequestIndicator;
    }

    public void setOutgoingEchoControlDeviceRequestIndicator(OutgoingEchoControlDeviceRequestIndicator outgoingEchoControlDeviceRequestIndicator) {
        this.outgoingEchoControlDeviceRequestIndicator = outgoingEchoControlDeviceRequestIndicator;
    }

    @Override
    public int getParameterCode() {
        return ECHO_CONTROL_INFORMATION;
    }

}
