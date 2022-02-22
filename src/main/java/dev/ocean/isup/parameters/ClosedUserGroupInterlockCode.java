/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class ClosedUserGroupInterlockCode implements IsupParameter {

    private String networkIdentity;
    private byte[] binaryCode;

    public ClosedUserGroupInterlockCode() {
    }

    public ClosedUserGroupInterlockCode(String networkIdentity, byte[] binaryCode) {
        this.networkIdentity = networkIdentity;
        this.binaryCode = binaryCode;
    }

    @Override
    public byte[] encode() throws IncorrectSyntaxException, IOException {
        byte[] data = new byte[4];
        int firstOctet = encodeDigit(networkIdentity.charAt(0));
        firstOctet = (firstOctet << 4) | encodeDigit(networkIdentity.charAt(1));
        data[0] = (byte) (firstOctet);

        int secondOctet = encodeDigit(networkIdentity.charAt(2));
        secondOctet = (secondOctet << 4) | encodeDigit(networkIdentity.charAt(3));
        data[1] = (byte) (secondOctet);

        data[2] = binaryCode[0];
        data[3] = binaryCode[1];
        return data;
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, IOException {
        int tmp = data[0] & 0xFF;
        StringBuilder sb = new StringBuilder();

        sb.append(decodeDigit((tmp >> 4) & 0b00001111));
        sb.append(decodeDigit((tmp) & 0b00001111));

        tmp = data[1] & 0xFF;
        sb.append(decodeDigit((tmp >> 4) & 0b00001111));
        sb.append(decodeDigit((tmp) & 0b00001111));

        this.networkIdentity = sb.toString();

        this.binaryCode = new byte[2];
        binaryCode[0] = (byte) (data[2] & 0xFF);
        binaryCode[1] = (byte) (data[3] & 0xFF);
    }

    public byte[] getBinaryCode() {
        return binaryCode;
    }

    public String getNetworkIdentity() {
        return networkIdentity;
    }

    public void setBinaryCode(byte[] binaryCode) {
        this.binaryCode = binaryCode;
    }

    public void setNetworkIdentity(String networkIdentity) {
        this.networkIdentity = networkIdentity;
    }

    private int encodeDigit(char d) throws IncorrectSyntaxException {
        switch (d) {
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
            default:
                throw new IncorrectSyntaxException("Unexpected digit received:" + d);
        }
    }

    private char decodeDigit(int d) throws IncorrectSyntaxException {
        switch (d) {
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
            default:
                throw new IncorrectSyntaxException("Unexpected digit received:" + d);
        }
    }

    @Override
    public int getParameterCode() {
        return CLOSED_USER_GROUP_INTERLOCK_CODE;
    }

}
