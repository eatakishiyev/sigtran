/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.Screening;
import azrc.az.isup.enums.NumberingPlan;
import azrc.az.isup.enums.NatureOfAddress;
import azrc.az.isup.enums.AddressPresentationRestricted;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class CallingPartyNumber implements IsupParameter {

    //ITU Q.763 3.10
    private boolean even;
    private NatureOfAddress natureOfAddress;
    private boolean numberComplete;
    private NumberingPlan numberingPlan;
    private AddressPresentationRestricted addressPresentationRestricted;
    private Screening screening;
    private String address;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CallingPartyNumber [")
                .append("Even:").append(even)
                .append("; NatureOfAddress:").append(natureOfAddress)
                .append("; NumberIncomplete:").append(numberComplete)
                .append("; NumberingPlan:").append(numberingPlan)
                .append("; AddressPresentationRestricted:").append(addressPresentationRestricted)
                .append("; Screening:").append(screening)
                .append("; Address:").append(address)
                .append("]");
        return sb.toString();
    }

    public CallingPartyNumber() {
    }

    /**
     *
     * @param natureOfAddress
     * @param numberComplete 0 complete, 1 - incomplete
     * @param numberingPlan
     * @param addressPresentation
     * @param screening
     * @param address;
     */
    public CallingPartyNumber(NatureOfAddress natureOfAddress,
            boolean numberComplete, NumberingPlan numberingPlan,
            AddressPresentationRestricted addressPresentation, Screening screening,
            String address) {

        this.natureOfAddress = natureOfAddress;
        this.numberComplete = numberComplete;
        this.numberingPlan = numberingPlan;
        this.addressPresentationRestricted = addressPresentation;
        this.screening = screening;
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
        this.numberComplete = (secondByte >> 7) == 0;
        this.numberingPlan = NumberingPlan.getInstance((secondByte >> 4) & 0x07);
        this.addressPresentationRestricted = AddressPresentationRestricted.getInstance((secondByte >> 2) & 0x03);
        this.screening = Screening.getInstance(secondByte & 0x03);

        Address address_ = new Address();
        address_.decode(data, 2);
        if (even) {
            this.address = address_.getAddress();
        } else {
            this.address = address_.getAddress().substring(0, address_.getAddress().length() - 1);
        }
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IllegalNumberFormatException, IOException {
        aos.writeOctetString(tagClass, tag, encode());
    }

    @Override
    public byte[] encode() throws IllegalNumberFormatException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Address address_ = new Address(address);
        byte[] data = address_.encode();

        this.even = (data.length % 2) == 0;
        int firstByte = even ? 0 : 1;
        firstByte = firstByte << 7;
        firstByte |= natureOfAddress.getValue();
        baos.write(firstByte);

        int secondByte = (numberComplete) ? 0 : 1;
        secondByte = secondByte << 3;
        secondByte |= numberingPlan.getValue();
        secondByte = secondByte << 2;
        secondByte |= addressPresentationRestricted.value();
        secondByte = secondByte << 2;
        secondByte |= screening.getValue();
        baos.write(secondByte);

        baos.write(data);

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
     * @return the numberIncomplete
     */
    public boolean isNumberIncomplete() {
        return numberComplete;
    }

    /**
     * @param numberIncomplete the numberIncomplete to set
     */
    public void setNumberIncomplete(boolean numberIncomplete) {
        this.numberComplete = numberIncomplete;
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
        return CALLING_PARTY_NUMBER;
    }

}
