/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import dev.ocean.gsm.string.utils.bcd.BCDStringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * @author root
 */
public class AddressStringImpl implements AddressString {

    private AddressNature addressNature;
    private NumberingPlan numberingPlan;
    private String address;
    private boolean isExtension;
    public int NO_EXTENSION_MASK = 0x80;//8 bit =1
    public int NATURE_OF_ADDRESS_MASK = 0x70;//567 bits = 111
    public int NUMBERING_PLAN_MASK = 0x0F;//4321 bits = 1111

    public AddressStringImpl(AddressNature addressNature, NumberingPlan numberingPlan, String address) {
        this.address = address;
        this.numberingPlan = numberingPlan;
        this.addressNature = addressNature;
    }

    public AddressStringImpl() {
    }

    @Override
    public AddressNature getAddressNature() {
        return this.addressNature;
    }

    @Override
    public NumberingPlan getNumberingPlan() {
        return this.numberingPlan;
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    @Override
    public void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            int length = ais.readLength();

            if (length < getMinLength() || length > getMaxLength()) {
                throw new UnexpectedDataException("Unexpected parameter length");
            }

            byte[] data = ais.readOctetStringData(length);
            this.decode(data);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void decode(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        int nature = bais.read();
        if ((nature & NO_EXTENSION_MASK) == 0x80) {
            this.isExtension = false;
        } else {
            this.isExtension = true;
        }

        int natureOfAddressInd = (nature & NATURE_OF_ADDRESS_MASK) >> 4;
        this.addressNature = AddressNature.getAddressNature(natureOfAddressInd);
        int numberingPlanInd = (nature & NUMBERING_PLAN_MASK);
        this.numberingPlan = NumberingPlan.getNumberingPlan(numberingPlanInd);
        this.address = BCDStringUtils.fromTBCDToString(bais);
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream(272);
            int nature = 1;
            if (this.isIsExtension()) {
                nature = 0;
            }
            nature = nature << 7;
            nature = nature | (this.addressNature.value() << 4);
            nature = nature | (this.numberingPlan.value());
            tmpBaos.write(nature);

            BCDStringUtils.toTBCDString(address, tmpBaos);

            byte[] data = tmpBaos.toByteArray();

            if (data.length < getMinLength() || data.length > getMaxLength()) {
                throw new IncorrectSyntaxException("Unexpected parameter length");
            }

            aos.writeOctetString(tagClass, tag, data);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }

    }

    public int getLength() {
        return this.address.length();
    }

    /**
     * @return the isExtension
     */
    public boolean isIsExtension() {
        return isExtension;
    }

    /**
     * @param isExtension the isExtension to set
     */
    public void setIsExtension(boolean isExtension) {
        this.isExtension = isExtension;
    }

    /**
     * @param addressNature the addressNature to set
     */
    public void setAddressNature(AddressNature addressNature) {
        this.addressNature = addressNature;
    }

    /**
     * @param numberingPlan the numberingPlan to set
     */
    public void setNumberingPlan(NumberingPlan numberingPlan) {
        this.numberingPlan = numberingPlan;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int getMaxLength() {
        return 20;
    }

    @Override
    public int getMinLength() {
        return 1;
    }

    @Override
    public String toString() {
        return "AddressStringImpl{" +
                "addressNature=" + addressNature +
                ", numberingPlan=" + numberingPlan +
                ", address='" + address + '\'' +
                ", isExtension=" + isExtension +
                '}';
    }
}
