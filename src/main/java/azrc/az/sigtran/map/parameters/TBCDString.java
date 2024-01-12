/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public abstract class TBCDString {

    private static int encodeNumber(char c) throws IOException {
        switch (c) {
            case '0':
                return 0x00;
            case '1':
                return 0x01;
            case '2':
                return 0x02;
            case '3':
                return 0x03;
            case '4':
                return 0x04;
            case '5':
                return 0x05;
            case '6':
                return 0x06;
            case '7':
                return 0x07;
            case '8':
                return 0x08;
            case '9':
                return 0x09;
            case '*':
                return 0x0B;
            case '#':
                return 0x0C;
            case 'A':
                return 0x0A;
            case 'B':
                return 0x0D;
            case 'C':
                return 0x0E;
            default:
                throw new IOException(
                        "char should be between 0 - 9, *, #, a, b, c for Telephony Binary Coded Decimal String. Received "
                        + c);
        }
    }

    private static char decodeNumber(int i) throws IOException {
        switch (i) {
            case 0x00:
                return '0';
            case 0x01:
                return '1';
            case 0x02:
                return '2';
            case 0x03:
                return '3';
            case 0x04:
                return '4';
            case 0x05:
                return '5';
            case 0x06:
                return '6';
            case 0x07:
                return '7';
            case 0x08:
                return '8';
            case 0x09:
                return '9';
            case 0x0B:
                return '*';
            case 0x0C:
                return '#';
            case 0x0A:
                return 'A';
            case 0x0D:
                return 'B';
            case 0x0E:
                return 'C';
            case 0x0F:
                return 'F';
            default:
                throw new IOException(
                        "Integer should be between 0 - 14 for Telephony Binary Coded Decimal String. Received "
                        + i);

        }
    }

     public void encodeTBCD(AsnOutputStream out) throws IOException {
        char[] digits = getValue().toCharArray();
        for (int i = 0; i < digits.length; i++) {
            char d = digits[i];
            int digit1 = encodeNumber(d);
            int digit2;
            if ((i + 1) == digits.length) {
                digit2 = 0x0f;
            } else {
                char a = digits[++i];
                digit2 = encodeNumber(a);
            }
            int digit = (digit2 << 4) | digit1;
            out.write(digit);
        }
    }

    public String decodeTBCD(AsnInputStream ais) throws IOException, AsnException {
        byte[] data = ais.readOctetString();

        StringBuilder address = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int b = data[i];
            int digit1 = (b & 0x0F);
            address.append(decodeNumber(digit1));
            int digit2 = (b & 0xF0) >> 4;
            if (digit2 != 15) {
                address.append(decodeNumber(digit2));
            }
        }
        return address.toString();
    }

    public abstract String getValue();
}
