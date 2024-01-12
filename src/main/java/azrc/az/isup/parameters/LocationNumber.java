/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.Screening;
import azrc.az.isup.enums.NumberingPlan;
import azrc.az.isup.enums.NatureOfAddress;
import azrc.az.isup.enums.AddressPresentationRestricted;
import java.io.ByteArrayInputStream;
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
public class LocationNumber implements IsupParameter {

    private boolean even;
    private NatureOfAddress natureOfAddress;
    private boolean routingToInternalNumberAllowed;
    private NumberingPlan numberingPlan;
    private AddressPresentationRestricted addressPresentation;
    private Screening screening;
    private String address;

    public LocationNumber() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocationNumber [")
                .append("Even:").append(even)
                .append("; NatureOfAddress:").append(natureOfAddress)
                .append("; RoutingToInternalNumberAllowed:").append(routingToInternalNumberAllowed)
                .append("; NumberingPlan:").append(numberingPlan)
                .append("; AddressPresentation:").append(addressPresentation)
                .append("; Screening:").append(screening)
                .append("; Address:").append(address)
                .append("]");
        return sb.toString();
    }

    /**
     *
     * @param natureOfAddress
     * @param routingToInternalNumberAllowed
     * @param numberingPlan - 0 routing to internal number allowed, 1 routing to
     * internal number not allowed
     * @param addressPresentation
     * @param screening
     * @param address
     */
    public LocationNumber(NatureOfAddress natureOfAddress, boolean routingToInternalNumberAllowed, NumberingPlan numberingPlan, AddressPresentationRestricted addressPresentation, Screening screening, String address) {
        this.natureOfAddress = natureOfAddress;
        this.routingToInternalNumberAllowed = routingToInternalNumberAllowed;
        this.numberingPlan = numberingPlan;
        this.addressPresentation = addressPresentation;
        this.screening = screening;
        this.address = address;
    }

    public void encode(ByteArrayOutputStream baos) throws IllegalNumberFormatException, IOException {
        Address address_ = new Address(address);
        byte[] data = address_.encode();

        this.even = (data.length % 2) == 0;
        int firstOctet = even ? 0 : 1;
        firstOctet = firstOctet << 7;
        firstOctet = firstOctet | natureOfAddress.getValue();
        baos.write(firstOctet);

        int secondOctet = routingToInternalNumberAllowed ? 0 : 1;
        secondOctet = secondOctet << 3;
        secondOctet = secondOctet | numberingPlan.getValue();
        secondOctet = secondOctet << 2;
        secondOctet = secondOctet | addressPresentation.value();
        secondOctet = secondOctet << 2;
        secondOctet = secondOctet | screening.getValue();
        baos.write(secondOctet);

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
        aos.write(encode());
        aos.FinalizeContent(lenpos);
    }

    public void decode(ByteArrayInputStream bais) throws IllegalNumberFormatException, IOException {
        int firstOctet = bais.read();
        this.even = (firstOctet >> 7) == 0;
        this.natureOfAddress = NatureOfAddress.getInstance(firstOctet & 0x7F);

        int secondtOctet = bais.read();
        this.routingToInternalNumberAllowed = (secondtOctet >> 7) == 0;
        this.numberingPlan = NumberingPlan.getInstance((secondtOctet >> 4) & 0x07);
        this.addressPresentation = AddressPresentationRestricted.getInstance((secondtOctet >> 2) & 0x03);
        this.screening = Screening.getInstance((secondtOctet) & 0x03);

        Address address_ = new Address();
        address_.decode(bais);
        if (even) {
            this.address = address_.getAddress();
        } else {
            this.address = address_.getAddress().substring(0, address_.getAddress().length() - 1);
        }

    }

    public void decode(AsnInputStream ais) throws IllegalNumberFormatException, IOException {
        byte[] data = new byte[ais.readLength()];
        ais.read(data);
        this.decode(new ByteArrayInputStream(data));
    }

    @Override
    public void decode(byte[] data) throws IllegalNumberFormatException, IOException {
        int firstOctet = data[0] & 0xFF;
        this.even = (firstOctet >> 7) == 0;
        this.natureOfAddress = NatureOfAddress.getInstance(firstOctet & 0x7F);

        int secondtOctet = data[1] & 0xFF;
        this.routingToInternalNumberAllowed = (secondtOctet >> 7) == 0;
        this.numberingPlan = NumberingPlan.getInstance((secondtOctet >> 4) & 0x07);
        this.addressPresentation = AddressPresentationRestricted.getInstance((secondtOctet >> 2) & 0x03);
        this.screening = Screening.getInstance((secondtOctet) & 0x03);

        Address address_ = new Address();
        address_.decode(data, 2);
        if (even) {
            this.address = address_.getAddress();
        } else {
            this.address = address_.getAddress().substring(0, address_.getAddress().length() - 1);
        }

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
     * @return the internalNetworkNumber
     */
    public boolean isRoutingToInternalNumberAllowed() {
        return routingToInternalNumberAllowed;
    }

    /**
     * @param routingToInternalNumberAllowed the internalNetworkNumber to set
     */
    public void setRoutingToInternalNumberAllowed(boolean routingToInternalNumberAllowed) {
        this.routingToInternalNumberAllowed = routingToInternalNumberAllowed;
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
    public AddressPresentationRestricted getAddressPresentation() {
        return addressPresentation;
    }

    /**
     * @param addressPresentation the addressPresentation to set
     */
    public void setAddressPresentation(AddressPresentationRestricted addressPresentation) {
        this.addressPresentation = addressPresentation;
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

    @Override
    public int getParameterCode() {
        return LOCATION_NUMBER;
    }

}
