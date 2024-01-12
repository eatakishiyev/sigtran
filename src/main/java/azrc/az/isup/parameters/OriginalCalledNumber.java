/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.NumberingPlan;
import azrc.az.isup.enums.NatureOfAddress;
import azrc.az.isup.enums.AddressPresentationRestricted;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class OriginalCalledNumber implements IsupParameter {

    boolean even;
    NatureOfAddress natureOfAddress;
    NumberingPlan numberingPlan;
    AddressPresentationRestricted addressPresentationRestricted;
    String address;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OriginalCalledNumber [")
                .append("Even:").append(even)
                .append("; NatureOfAddress:").append(natureOfAddress)
                .append("; NumberingPlan:").append(numberingPlan)
                .append("; AddressPresentationRestricted:").append(addressPresentationRestricted)
                .append("; Address:").append(address)
                .append("]");
        return sb.toString();
    }

    public OriginalCalledNumber() {
    }

    /**
     *
     * @param natureOfAddress
     * @param numberingPlan
     * @param addressPresentation
     * @param address;
     */
    public OriginalCalledNumber(NatureOfAddress natureOfAddress,
            NumberingPlan numberingPlan,
            AddressPresentationRestricted addressPresentation,
            String address) {

        this.natureOfAddress = natureOfAddress;
        this.numberingPlan = numberingPlan;
        this.addressPresentationRestricted = addressPresentation;
        this.address = address;

    }

    public void decode(AsnInputStream ais) throws IOException, IllegalNumberFormatException, AsnException {
        this.decode(ais.readOctetString());
    }

    @Override
    public void decode(byte[] data) throws IOException, IllegalNumberFormatException {
        int firstByte = data[0] & 0xFF;
        this.even = (firstByte >> 7) == 0;
        this.natureOfAddress = NatureOfAddress.getInstance(firstByte & 0x7F);

        int secondByte = data[1] & 0xFF;
        this.numberingPlan = NumberingPlan.getInstance((secondByte >> 4) & 0x07);
        this.addressPresentationRestricted = AddressPresentationRestricted.getInstance((secondByte >> 2) & 0x03);

        Address address_ = new Address();
        address_.decode(data, 2);
        if (even) {
            this.address = address_.getAddress();
        } else {
            this.address = address_.getAddress().substring(0, address_.getAddress().length() - 1);
        }
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IllegalNumberFormatException, IOException {
        aos.writeTag(tagClass, true, tag);
        int lenPos = aos.StartContentDefiniteLength();

        ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
        this.encode(baos);
        aos.write(baos.toByteArray());

        aos.FinalizeContent(lenPos);
    }

    public void encode(ByteArrayOutputStream baos) throws IllegalNumberFormatException, IOException {
        Address address_ = new Address(address);
        byte[] data = address_.encode();

        this.even = (data.length % 2) == 0;
        int firstByte = even ? 0 : 1;
        firstByte = firstByte << 7;
        firstByte |= natureOfAddress.getValue();
        baos.write(firstByte);

        int secondByte = 0;
        secondByte = secondByte << 3;
        secondByte |= numberingPlan.getValue();
        secondByte = secondByte << 2;
        secondByte |= addressPresentationRestricted.value();
        secondByte = secondByte << 2;
        secondByte |= 0;//spare
        baos.write(secondByte);

        baos.write(data);
    }

    @Override
    public byte[] encode() throws IllegalNumberFormatException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.encode(baos);
        return baos.toByteArray();
    }

    /**
     * @return the natureOfAddress
     */
    public NatureOfAddress getNatureOfAddress() {
        return natureOfAddress;
    }

    /**
     * @param natureOfAddress the natureOfAddress to set
     */
    public void setNatureOfAddress(NatureOfAddress natureOfAddress) {
        this.natureOfAddress = natureOfAddress;
    }

    /**
     * @return the numberingPlan
     */
    public NumberingPlan getNumberingPlan() {
        return numberingPlan;
    }

    /**
     * @param numberingPlan the numberingPlan to set
     */
    public void setNumberingPlan(NumberingPlan numberingPlan) {
        this.numberingPlan = numberingPlan;
    }

    /**
     * @return the addressPresentation
     */
    public AddressPresentationRestricted getAddressPresentationRestricted() {
        return addressPresentationRestricted;
    }

    /**
     * @param addressPresentation the addressPresentation to set
     */
    public void setAddressPresentationRestricted(AddressPresentationRestricted addressPresentation) {
        this.addressPresentationRestricted = addressPresentation;
    }

    /**
     * @return the addressSignal
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the even
     */
    public boolean isEven() {
        return even;
    }

    @Override
    public int getParameterCode() {
        return ORIGINAL_CALLED_NUMBER;
    }

}
