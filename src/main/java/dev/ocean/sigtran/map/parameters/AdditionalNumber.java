/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class AdditionalNumber implements MAPParameter {

    private ISDNAddressString mscNumber;
    private ISDNAddressString sgsnNumber;
    private AdditionalAddressType addressType;

    public AdditionalNumber(AdditionalAddressType addressType) {
        this.addressType = addressType;
    }

    public AdditionalNumber() {
    }

    @Override
    public void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            int length = ais.readLength();

            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
                throw new IncorrectSyntaxException("Incorrect encoded tag.");
            }
            this.addressType = AdditionalAddressType.getInstance(tag);

            if (addressType == AdditionalAddressType.UNKNOWN) {
                throw new IncorrectSyntaxException("Unknown address type. Expected MSC /SGSN number");
            }

            switch (addressType) {
                case MSC_NUMBER:
                    setMscNumber(new ISDNAddressString());
                    getMscNumber().decode(ais);
                    break;
                case SGSN_NUBMER:
                    setSgsnNumber(new ISDNAddressString());
                    getSgsnNumber().decode(ais);
                    break;
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, IOException, AsnException {
        if (this.mscNumber == null && this.sgsnNumber == null) {
            throw new IncorrectSyntaxException("One or more mandatory parametes are absent");
        }

        if (addressType == AdditionalAddressType.UNKNOWN) {
            throw new IncorrectSyntaxException("Unknown address type selected");
        }
        AsnOutputStream tmpAos = new AsnOutputStream();
        switch (addressType) {
            case MSC_NUMBER:
                if (this.mscNumber == null) {
                    throw new IncorrectSyntaxException("MSC_NUMBER address type selected but mscNumber is not set.");
                }
                this.mscNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x00, tmpAos);
                break;
            case SGSN_NUBMER:
                if (this.sgsnNumber == null) {
                    throw new IncorrectSyntaxException("SGSN_NUMBER address type selected but sgsnNumber is not set.");
                }
                this.sgsnNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x01, tmpAos);
                break;
        }
        byte[] data = tmpAos.toByteArray();

        try {
            aos.writeTag(tagClass, false, tag);
            aos.writeLength(data.length);
            aos.write(data);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the addressType
     */
    /**
     * @return the addressType
     */
    public AdditionalAddressType getAddressType() {
        return addressType;
    }

    /**
     * @param addressType the addressType to set
     */
    public void setAddressType(AdditionalAddressType addressType) {
        this.addressType = addressType;
    }

    /**
     * @return the mscNumber
     */
    public ISDNAddressString getMscNumber() {
        return mscNumber;
    }

    /**
     * @param mscNumber the mscNumber to set
     */
    public void setMscNumber(ISDNAddressString mscNumber) {
        this.mscNumber = mscNumber;
    }

    /**
     * @return the sgsnNumber
     */
    public ISDNAddressString getSgsnNumber() {
        return sgsnNumber;
    }

    /**
     * @param sgsnNumber the sgsnNumber to set
     */
    public void setSgsnNumber(ISDNAddressString sgsnNumber) {
        this.sgsnNumber = sgsnNumber;
    }

    public enum AdditionalAddressType {

        MSC_NUMBER(0),
        SGSN_NUBMER(1),
        UNKNOWN(-1);
        private int value;

        private AdditionalAddressType(int value) {
            this.value = value;
        }

        public static AdditionalAddressType getInstance(int value) {
            switch (value) {
                case 0:
                    return MSC_NUMBER;
                case 1:
                    return SGSN_NUBMER;
                default:
                    return UNKNOWN;
            }
        }

        public int value() {
            return this.value;
        }
    }

    @Override
    public String toString() {
        return "AdditionalNumber{" +
                "mscNumber=" + mscNumber +
                ", sgsnNumber=" + sgsnNumber +
                ", addressType=" + addressType +
                '}';
    }
}
