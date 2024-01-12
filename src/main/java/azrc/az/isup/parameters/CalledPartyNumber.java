/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.NumberingPlan;
import azrc.az.isup.enums.NatureOfAddress;
import azrc.az.isup.enums.InternationalNetworkNumberIndicator;
import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;

/**
 *
 * @author eatakishiyev
 */
public class CalledPartyNumber implements IsupParameter {

    boolean even;
    NatureOfAddress natureOfAddress;
    InternationalNetworkNumberIndicator internalNetworkNumberIndicator;
    NumberingPlan numberingPlan;
    String address;

    public CalledPartyNumber() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("CalledPartyNumber [ ")
                .append("Even:").append(even)
                .append("; NatureOfAddress:").append(natureOfAddress)
                .append("; InternationalNetworkNumberIndicator:").append(internalNetworkNumberIndicator)
                .append("; NumberingPlan:").append(numberingPlan)
                .append("; Address:").append(address)
                .append("]");

        return sb.toString();
    }

    /**
     *
     * @param natureOfAddress
     * @param internalNetworkNumberIndicator
     * @param numberingPlan
     * @param address
     */
    public CalledPartyNumber(NatureOfAddress natureOfAddress, InternationalNetworkNumberIndicator internalNetworkNumberIndicator, NumberingPlan numberingPlan, String address) {
        this.natureOfAddress = natureOfAddress;
        this.internalNetworkNumberIndicator = internalNetworkNumberIndicator;
        this.numberingPlan = numberingPlan;
        this.address = address;
    }

    @Override
    public byte[] encode() throws IllegalNumberFormatException {
        Address _address = new Address(address);//address digits with stop signal
        byte[] addressData = _address.encode();

        byte[] data = new byte[addressData.length + 2];
        this.even = (addressData.length % 2) == 0;
        int firstByte = even ? 0 : 1;
        firstByte = firstByte << 7;
        firstByte = firstByte | natureOfAddress.getValue();
        data[0] = (byte) (firstByte);

        int secondByte = internalNetworkNumberIndicator.ordinal();
        secondByte = secondByte << 3;
        secondByte = secondByte | numberingPlan.getValue();
        secondByte = secondByte << 4;
        data[1] = (byte) (secondByte);

        System.arraycopy(addressData, 0, data, 2, addressData.length);
        return data;
    }

    @Override
    public void decode(byte[] data) throws IOException, IllegalNumberFormatException {
        int firstByte = data[0] & 0xFF;
        this.even = (byte) (firstByte >> 7) == 0;
        this.natureOfAddress = NatureOfAddress.getInstance(firstByte & 0x7F);

        int secondByte = data[1] & 0xFF;
        this.internalNetworkNumberIndicator = InternationalNetworkNumberIndicator.valueOf((secondByte >> 7));
        this.numberingPlan = NumberingPlan.getInstance((secondByte >> 4) & 0x07);

        Address address_ = new Address();
        address_.decode(data, 2);
        this.address = address_.getAddress();
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
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
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

    /**
     * @return the internalNetworkNumber
     */
    public InternationalNetworkNumberIndicator getInternalNetworkNumberIndicator() {
        return internalNetworkNumberIndicator;
    }

    /**
     * @param internalNetworkNumberIndicator the internalNetworkNumberIndicator
     * to set
     */
    public void setInternalNetworkNumberIndicator(InternationalNetworkNumberIndicator internalNetworkNumberIndicator) {
        this.internalNetworkNumberIndicator = internalNetworkNumberIndicator;
    }

    @Override
    public int getParameterCode() {
        return CALLED_PARTY_NUMBER;
    }

}
