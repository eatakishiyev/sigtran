/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;

/**
 *
 * @author eatakishiyev
 */
public class Address {

    private String address;

    private final int FILLER = 0x00;
    private final int STOP_SIGNAL = 0x0F;

    protected Address(String address) {
        this.address = address;
    }

    protected Address() {
    }

    public void encode(ByteArrayOutputStream baos) throws IllegalNumberFormatException {
        char[] digits = address.toCharArray();
        for (int i = 0; i < digits.length; i++) {
            char d = digits[i];
            int digit1 = this.encodeNumber(d);
            int digit2;
            if ((i + 1) == digits.length) {
                digit2 = FILLER;
            } else {
                char a = digits[++i];
                digit2 = this.encodeNumber(a);
            }
            int digit = (digit2 << 4) | digit1;
            baos.write(digit);
        }
    }

    public byte[] encode() throws IllegalNumberFormatException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.encode(baos);
        return baos.toByteArray();
    }

    public void decode(ByteArrayInputStream bais) throws IllegalNumberFormatException {
        StringBuilder address_ = new StringBuilder();
        while (bais.available() > 0) {
            int b = bais.read();
            if (b == STOP_SIGNAL) {
                continue;
            }

            int digit1 = (b & 0x0F);
            if (digit1 != STOP_SIGNAL) {
                address_.append(decodeNumber(digit1));
            }

            int digit2 = (b & 0xF0) >> 4;
            if (digit2 != STOP_SIGNAL) {
                address_.append(decodeNumber(digit2));
            }

            this.address = address_.toString();

        }
    }

    public void decode(byte[] data, int offset) throws IllegalNumberFormatException {
        int position = 0;
        StringBuilder address_ = new StringBuilder();
        while (position < data.length - offset) {
            int b = data[offset + (position++)];
            if (b == STOP_SIGNAL) {
                continue;
            }

            int digit1 = (b & 0x0F);
            if (digit1 != STOP_SIGNAL) {
                address_.append(decodeNumber(digit1));
            }

            int digit2 = (b & 0xF0) >> 4;
            if (digit2 != STOP_SIGNAL) {
                address_.append(decodeNumber(digit2));
            }

            this.address = address_.toString();

        }
    }

    public void decode(ByteArrayInputStream bais, int length) throws IllegalNumberFormatException {
        int i = 0;
        StringBuilder address_ = new StringBuilder();
        while (i < length) {
            int b = bais.read();
            i++;
            if (b == STOP_SIGNAL) {
                continue;
            }

            int digit1 = (b & 0x0F);
            if (digit1 != STOP_SIGNAL) {
                address_.append(decodeNumber(digit1));
            }

            int digit2 = (b & 0xF0) >> 4;
            if (digit2 != STOP_SIGNAL) {
                address_.append(decodeNumber(digit2));
            }

            this.address = address_.toString();

        }
    }

    private char decodeNumber(int i) throws IllegalNumberFormatException {
        switch (i) {
            case 0:
                return '0';
            case 1:
                return '1';
            case 2:
                return '2';
            case 3:
                return '3';
            case 4:
                return '4';
            case 5:
                return '5';
            case 6:
                return '6';
            case 7:
                return '7';
            case 8:
                return '8';
            case 9:
                return '9';
            case 10:
                return '*';
            case 11:
                return '#';
            case 12:
                return 'A';
            case 13:
                return 'B';
            case 14:
                return 'C';
            case 15:
                return 'F';
            default:
                throw new IllegalNumberFormatException(
                        "Integer should be between 0 - 15 for Telephony Binary Coded Decimal String. Received "
                        + i);

        }
    }

    private int encodeNumber(char c) throws IllegalNumberFormatException {
        switch (c) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case '*':
                return 10;
            case '#':
                return 11;
            case 'a':
            case 'A':
                return 12;
            case 'b':
            case 'B':
                return 13;
            case 'c':
            case 'C':
                return 14;
            case 'F':
            case 'f':
                return 15;
            default:
                throw new IllegalNumberFormatException(
                        "char should be between 0 - 9, *, #, a, b, c for Telephony Binary Coded Decimal String. Received "
                        + c);

        }
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
}
