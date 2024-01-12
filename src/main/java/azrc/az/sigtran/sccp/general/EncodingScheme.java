/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.general;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author root
 */
public enum EncodingScheme {

    UNKNOWN((byte)0x00),
    BCD_ODD((byte)0x01),
    BCD_EVEN((byte)0x02),
    NATIONAL_SPEC((byte)0x03),
    RESERVED((byte)0xF);
    private final byte encodingScheme;

    EncodingScheme(byte encodingScheme) {
        this.encodingScheme = encodingScheme;
    }

    public byte value() {
        return this.encodingScheme;
    }

    public static EncodingScheme getInstance(byte value) {
        switch (value) {
            case 0:
                return UNKNOWN;
            case 1:
                return BCD_ODD;
            case 2:
                return BCD_EVEN;
            case 3:
                return NATIONAL_SPEC;
            case 15:
                return RESERVED;
            default:
                return RESERVED;
        }
    }

    public void encode(String GlobalTitleAddressInformation, ByteArrayOutputStream baos) {
        String gt = GlobalTitleAddressInformation;
        switch (encodingScheme) {
            case 1:
                gt = gt.concat("0");
                break;
            case 2:
                break;
        }
        for (int i = 0; i < gt.length(); i += 2) {
            String d1 = gt.substring(i, i + 1);
            String d2 = gt.substring(i + 1, i + 2);
            int iD1 = Integer.parseInt(d1, 16);
            int iD2 = Integer.parseInt(d2, 16);

            byte b = (byte) ((iD2 << 4) | iD1);
            baos.write(b);
        }
    }

    public String decode(ByteArrayInputStream bais) throws IOException {
        switch (encodingScheme) {
            case 1:
            case 2:
                return convertBCDEvenOdd(bais);
            default:
                return convertBCDUnknown(bais);
        }
    }

    private String convertBCDUnknown(ByteArrayInputStream bais) throws IOException {
        byte[] data = new byte[bais.available()];
        bais.read(data);
        return new String(data);
    }

    private String convertBCDEvenOdd(ByteArrayInputStream bais) {
        StringBuilder tmpGTAI = new StringBuilder();
        while (bais.available() > 0) {
            int bd1 = bais.read();
            int d1 = bd1 & 0xF;
            int d2 = (bd1 >> 4) & 0xF;
            tmpGTAI.append(Integer.toHexString(d1)).append(Integer.toHexString(d2));
        }
        if (encodingScheme == 1) {
            tmpGTAI.setLength(tmpGTAI.length() - 1);
        }
        return tmpGTAI.toString();
    }
}
