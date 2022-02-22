/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class GSNAddress  implements MAPParameter{

    private GSNAddressType gSNAddressType;
    private int addressLength;
    private String address;

    public GSNAddress() {
    }

    public GSNAddress(AsnInputStream ais) throws IOException, AsnException {
        this.decode(ais);
    }

    public final void encode(int tagClass, int tag, AsnOutputStream aos) throws IOException, AsnException {
        int firstOctet = gSNAddressType.value;
        switch (gSNAddressType) {
            case IPv4:
                addressLength = 4;
                break;
            case IPv6:
                addressLength = 16;
                break;
        }
        firstOctet = firstOctet << 6;
        firstOctet = firstOctet | addressLength;

        ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
        baos.write(firstOctet);
        baos.write(address.getBytes());

        aos.writeOctetString(tagClass, tag, baos.toByteArray());
    }

    public final void decode(AsnInputStream ais) throws IOException, AsnException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ais.readOctetString());
        int firstOctet = bais.read();
        gSNAddressType = GSNAddressType.getInstance((firstOctet >> 6) & 0x03);
        switch (gSNAddressType) {
            case IPv4:
                addressLength = 4;
                break;
            case IPv6:
                addressLength = 16;
                break;
        }
        byte[] data = new byte[addressLength];
        bais.read(data);
        address = new String(data);
    }

    /**
     * @return the gSNAddressType
     */
    public GSNAddressType getgSNAddressType() {
        return gSNAddressType;
    }

    /**
     * @param gSNAddressType the gSNAddressType to set
     */
    public void setgSNAddressType(GSNAddressType gSNAddressType) {
        this.gSNAddressType = gSNAddressType;
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

    public enum GSNAddressType {

        IPv4(0),
        IPv6(1);
        private int value;

        private GSNAddressType(int value) {
            this.value = value;
        }

        public static GSNAddressType getInstance(int value) {
            switch (value) {
                case 0:
                    return IPv4;
                case 1:
                    return IPv6;
                default:
                    return null;
            }
        }
    }
}
