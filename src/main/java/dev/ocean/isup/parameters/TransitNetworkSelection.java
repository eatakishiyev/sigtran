/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.NetworkIdentificationPlan;
import dev.ocean.isup.enums.TypeOfNetworkIdentification;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class TransitNetworkSelection implements IsupParameter {

    private boolean even;
    private TypeOfNetworkIdentification typeOfNetworkIdentification;
    private NetworkIdentificationPlan networkIdentificationPlan;
    private String networkIdentification;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TransitNetworkSelection[")
                .append("Even:").append(even)
                .append("; TypeOfNetworkdIdentification:").append(typeOfNetworkIdentification)
                .append("; NetworkIdentificationPlan:").append(networkIdentificationPlan)
                .append("; NetworkIdentification:").append(networkIdentification)
                .append("]");
        return sb.toString();
    }

    public TransitNetworkSelection() {
    }

    public TransitNetworkSelection(TypeOfNetworkIdentification typeOfNetworkIdentification, NetworkIdentificationPlan networkIdentificationPlan, String networkIdentification) {
        this.typeOfNetworkIdentification = typeOfNetworkIdentification;
        this.networkIdentificationPlan = networkIdentificationPlan;
        this.networkIdentification = networkIdentification;
    }

    public void encode(ByteArrayOutputStream baos) throws IllegalNumberFormatException, IOException {
        Address address = new Address(networkIdentification); //networkIdentification digits with stop signal
        byte[] data = address.encode();

        this.even = (data.length % 2) == 0;
        int firstByte = even ? 0 : 1;
        firstByte = firstByte << 3;
        firstByte = firstByte | typeOfNetworkIdentification.value();
        firstByte = firstByte << 4;
        firstByte = firstByte | networkIdentificationPlan.value();
        baos.write(firstByte);
        baos.write(data);

    }

    @Override
    public byte[] encode() throws IllegalNumberFormatException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.encode(baos);
        return baos.toByteArray();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IllegalNumberFormatException, IOException {
        aos.writeTag(tagClass, true, tag);
        int lenpos = aos.StartContentDefiniteLength();
        aos.write(this.encode());
        aos.FinalizeContent(lenpos);
    }

    @Override
    public void decode(byte[] data) throws IllegalNumberFormatException {
        int firstByte = data[0] & 0xFF;
        this.even = (firstByte >> 7) == 0;
        this.typeOfNetworkIdentification = TypeOfNetworkIdentification.getInstance((firstByte >> 4) & 0b00000111);
        this.networkIdentificationPlan = NetworkIdentificationPlan.getInstance((firstByte) & 0b00001111);
        Address address = new Address();
        address.decode(data, 1);
        this.networkIdentification = address.getAddress();
    }

    public void decode(AsnInputStream ais) throws IOException, IllegalNumberFormatException {
        byte[] data = new byte[ais.readLength()];
        ais.read(data);
        this.decode(data);
    }

    public boolean isEven() {
        return even;
    }

    public String getNetworkIdentification() {
        return networkIdentification;
    }

    public NetworkIdentificationPlan getNetworkIdentificationPlan() {
        return networkIdentificationPlan;
    }

    public TypeOfNetworkIdentification getTypeOfNetworkIdentification() {
        return typeOfNetworkIdentification;
    }

    public void setNetworkIdentification(String networkIdentification) {
        this.networkIdentification = networkIdentification;
    }

    public void setNetworkIdentificationPlan(NetworkIdentificationPlan networkIdentificationPlan) {
        this.networkIdentificationPlan = networkIdentificationPlan;
    }

    public void setTypeOfNetworkIdentification(TypeOfNetworkIdentification typeOfNetworkIdentification) {
        this.typeOfNetworkIdentification = typeOfNetworkIdentification;
    }

    @Override
    public int getParameterCode() {
        return TRANSIT_NETWORK_SELECTION;
    }

}
