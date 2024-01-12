/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.AddressPresentationRestricted;
import azrc.az.isup.enums.Screening;
import azrc.az.isup.enums.NumberingPlan;
import azrc.az.isup.enums.NatureOfAddress;
import azrc.az.isup.enums.NumberQualifierIndicator;
import azrc.az.isup.enums.NumberIncompleteIndicator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;

/**
 *
 * @author eatakishiyev
 */
public class GenericNumber implements IsupParameter {

    private NumberQualifierIndicator numberQualifierIndicator;
    private boolean even;
    private NatureOfAddress natureOfAddress;
    private NumberIncompleteIndicator numberIncomplete;
    private NumberingPlan numberingPlan;
    private AddressPresentationRestricted addressPresentationRestricted;
    private Screening screening;
    private String address;

    public GenericNumber() {
    }

    public GenericNumber(NumberQualifierIndicator numberQualifierIndicator, NatureOfAddress natureOfAddress, NumberIncompleteIndicator numberIncomplete, NumberingPlan numberingPlan, AddressPresentationRestricted addressPresentationRestricted, Screening screening, String addres) {
        this.numberQualifierIndicator = numberQualifierIndicator;
        this.natureOfAddress = natureOfAddress;
        this.numberIncomplete = numberIncomplete;
        this.numberingPlan = numberingPlan;
        this.addressPresentationRestricted = addressPresentationRestricted;
        this.screening = screening;
        this.address = addres;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GenericNumber[")
                .append("NumberQualifierIndicator:").append(numberQualifierIndicator)
                .append(" ;Even:").append(even)
                .append(" ;NatureOfAddress:").append(natureOfAddress)
                .append(" ;NumberIncomplete:").append(numberIncomplete)
                .append(" ;NumberingPlan:").append(numberingPlan)
                .append(" ;AddressPresentationRestricted:").append(addressPresentationRestricted)
                .append(" ;Screening:").append(screening)
                .append(" ;Address:").append(address)
                .append("]");
        return sb.toString();
    }

    public void encode(ByteArrayOutputStream baos) throws IllegalNumberFormatException, IOException {
        int firstOctet = numberQualifierIndicator.getValue();
        baos.write(firstOctet);

        Address address_ = new Address(address);
        byte[] data = address_.encode();

        even = (byte) (data.length % 2) == 0;

        int secondOctet = even ? 0 : 1;
        secondOctet = secondOctet << 7;
        secondOctet = secondOctet | natureOfAddress.getValue();
        baos.write(secondOctet);

        int thirthOctet = numberIncomplete.ordinal();
        thirthOctet = thirthOctet << 3;
        thirthOctet = thirthOctet | numberingPlan.getValue();
        thirthOctet = thirthOctet << 2;
        thirthOctet = thirthOctet | addressPresentationRestricted.value();
        thirthOctet = thirthOctet << 2;
        thirthOctet = thirthOctet | screening.getValue();
        baos.write(thirthOctet);

        baos.write(data);

    }

    @Override
    public byte[] encode() throws IllegalNumberFormatException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.encode(baos);
        return baos.toByteArray();
    }

    @Override
    public void decode(byte[] data) throws IOException, IllegalNumberFormatException {
        int octet = data[0] & 0xFF;
        this.numberQualifierIndicator = NumberQualifierIndicator.getInstance(octet);

        octet = data[1] & 0xFF;
        this.even = (octet >> 7) == 0;
        this.natureOfAddress = NatureOfAddress.getInstance(octet & 0x7F);

        octet = data[2] & 0xFF;
        this.numberIncomplete = NumberIncompleteIndicator.valueOf((byte) (octet >> 7));
        this.numberingPlan = NumberingPlan.getInstance((octet >> 4) & 0x07);
        this.addressPresentationRestricted = AddressPresentationRestricted.getInstance((octet >> 2) & 0x03);
        this.screening = Screening.getInstance(octet & 0x03);

        Address address_ = new Address();
        address_.decode(data, 3);
        if (even) {
            this.address = address_.getAddress();
        } else {
            this.address = address_.getAddress().substring(0, address_.getAddress().length() - 1);
        }
    }

    /**
     * @return the numberQualifierIndicator
     */
    public NumberQualifierIndicator getNumberQualifierIndicator() {
        return numberQualifierIndicator;
    }

    /**
     * @param numberQualifierIndicator the numberQualifierIndicator to set
     */
    public void setNumberQualifierIndicator(NumberQualifierIndicator numberQualifierIndicator) {
        this.numberQualifierIndicator = numberQualifierIndicator;
    }

    /**
     * @return the even
     */
    public boolean isEven() {
        return even;
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
     * @return the numberIncomplete
     */
    public NumberIncompleteIndicator getNumberIncomplete() {
        return numberIncomplete;
    }

    /**
     * @param numberIncomplete the numberIncomplete to set
     */
    public void setNumberIncomplete(NumberIncompleteIndicator numberIncomplete) {
        this.numberIncomplete = numberIncomplete;
    }

    /**
     * @return the addressPresentationRestricted
     */
    public AddressPresentationRestricted getAddressPresentationRestricted() {
        return addressPresentationRestricted;
    }

    /**
     * @param addressPresentationRestricted the addressPresentationRestricted to
     * set
     */
    public void setAddressPresentationRestricted(AddressPresentationRestricted addressPresentationRestricted) {
        this.addressPresentationRestricted = addressPresentationRestricted;
    }

    /**
     * @return the screening
     */
    public Screening getScreening() {
        return screening;
    }

    /**
     * @param screening the screening to set
     */
    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    /**
     * @return the addres
     */
    public String getAddres() {
        return address;
    }

    /**
     * @param addres the addres to set
     */
    public void setAddres(String addres) {
        this.address = addres;
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

    @Override
    public int getParameterCode() {
        return GENERIC_NUMBER;
    }
}
