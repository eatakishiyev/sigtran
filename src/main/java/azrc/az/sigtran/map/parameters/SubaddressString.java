/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class SubaddressString {

    private ByteArrayOutputStream subAddressInformation;
    private TypeOfSubaddress typeOfSubaddress;

    private Integer isEven() {
        return subAddressInformation.size() % 2;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IOException, AsnException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(272)) {
            int firstOctet = 1;
            firstOctet = firstOctet << 3;
            firstOctet = firstOctet | typeOfSubaddress.getValue();
            firstOctet = firstOctet << 1;
            firstOctet = firstOctet | isEven();
            firstOctet = firstOctet << 3;
            baos.write(firstOctet);
            baos.write(subAddressInformation.toByteArray());
            aos.writeTag(tagClass, true, tag);
            aos.writeLength(baos.size());
            aos.write(baos.toByteArray());
            baos.flush();
        }

    }

    public void encode(ByteArrayOutputStream baos) throws IOException {
        int firstOctet = 1;
        firstOctet = firstOctet << 3;
        firstOctet = firstOctet | typeOfSubaddress.getValue();
        firstOctet = firstOctet << 1;
        firstOctet = firstOctet | isEven();
        firstOctet = firstOctet << 3;
        baos.write(firstOctet);
        byte[] data = new byte[subAddressInformation.size()];
        baos.write(data);
    }

    public void decode(ByteArrayInputStream bais) throws IOException {
        int firstOctet = bais.read();
        int extension = firstOctet >> 7;
        this.typeOfSubaddress = TypeOfSubaddress.getInstance((firstOctet >> 4) & 0x07);
        byte[] data = new byte[bais.available()];
        bais.read(data);
        this.subAddressInformation = new ByteArrayOutputStream(272);
        this.subAddressInformation.write(data);

    }

    /**
     * @return the subAddressInformation
     */
    public ByteArrayOutputStream getSubAddressInformation() {
        return subAddressInformation;
    }

    /**
     * @param subAddressInformation the subAddressInformation to set
     */
    public void setSubAddressInformation(ByteArrayOutputStream subAddressInformation) {
        this.subAddressInformation = subAddressInformation;
    }

    /**
     * @return the typeOfSubaddress
     */
    public TypeOfSubaddress getTypeOfSubaddress() {
        return typeOfSubaddress;
    }

    /**
     * @param typeOfSubaddress the typeOfSubaddress to set
     */
    public void setTypeOfSubaddress(TypeOfSubaddress typeOfSubaddress) {
        this.typeOfSubaddress = typeOfSubaddress;
    }

    public enum TypeOfSubaddress {

        NSAP(0),
        UserSpecific(2);
        private int value;

        private TypeOfSubaddress(int value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        public static TypeOfSubaddress getInstance(int value) {
            switch (value) {
                case 0:
                    return NSAP;
                case 2:
                    return UserSpecific;
                default:
                    return null;
            }
        }
    }
}
