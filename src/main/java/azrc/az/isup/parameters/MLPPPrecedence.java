/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.Lfb;
import azrc.az.isup.enums.PrecedenceLevel;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;

/**
 *
 * @author eatakishiyev
 */
public class MLPPPrecedence implements IsupParameter {

    private Lfb lfb;
    private PrecedenceLevel precedenceLevel;
    private String networkIdentity;
    private int mlppServiceDomain;

    public MLPPPrecedence() {
    }

    public MLPPPrecedence(Lfb lfb, PrecedenceLevel precedenceLevel, String networkIdentity, int mlppServiceDomain) {
        this.lfb = lfb;
        this.precedenceLevel = precedenceLevel;
        this.networkIdentity = networkIdentity;
        this.mlppServiceDomain = mlppServiceDomain;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MLPPrecedence[")
                .append("LFB:").append(lfb)
                .append("; PrecedenceLevel:").append(precedenceLevel)
                .append("; NetworkIdentity:").append(networkIdentity)
                .append("; MLPPServiceDomain:").append(mlppServiceDomain)
                .append("]");
        return sb.toString();
    }

    @Override
    public byte[] encode() throws IncorrectSyntaxException {
        byte[] data = new byte[6];
        int b = 0;//spare
        b = (byte) ((b << 2) | lfb.value());
        b = (byte) ((b << 1));//spare
        b = (byte) ((b << 4) | precedenceLevel.value());
        data[0] = (byte) (b);
        //Network identity
        b = (byte) encodeDigit(networkIdentity.charAt(0));
        b = (byte) ((b << 4) | encodeDigit(networkIdentity.charAt(1)));
        data[1] = (byte) (b);
        b = (byte) encodeDigit(networkIdentity.charAt(2));
        b = (byte) ((b << 4) | encodeDigit(networkIdentity.charAt(3)));
        data[2] = (byte) (b);

        data[3] = (byte) (mlppServiceDomain >> 16);
        data[4] = (byte) ((mlppServiceDomain >> 8) & 0b11111111);
        data[5] = (byte) (mlppServiceDomain & 0b11111111);
        return data;
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException {
        this.lfb = Lfb.getInstance((data[0] >> 5) & 0b00000011);
        this.precedenceLevel = PrecedenceLevel.getInstance(data[0] & 0b00001111);

        int tmp = data[1] & 0xFF;
        StringBuilder sb = new StringBuilder();

        sb.append(decodeDigit((tmp >> 4) & 0b00001111));
        sb.append(decodeDigit((tmp) & 0b00001111));

        tmp = data[2] & 0xFF;
        sb.append(decodeDigit((tmp >> 4) & 0b00001111));
        sb.append(decodeDigit((tmp) & 0b00001111));

        this.networkIdentity = sb.toString();

        this.mlppServiceDomain = data[3] & 0b11111111;
        this.mlppServiceDomain = (mlppServiceDomain << 8) | (data[4] & 0b11111111);
        this.mlppServiceDomain = (mlppServiceDomain << 8) | (data[5] & 0b11111111);
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
        return MLPP_PRECEDENCE;
    }

}
