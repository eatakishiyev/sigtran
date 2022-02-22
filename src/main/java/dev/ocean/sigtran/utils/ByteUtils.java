/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.utils;

/**
 *
 * @author eatakishiyev
 */
public class ByteUtils {

    private static char[] hex = "0123456789ABCDEF".toCharArray();

    public static String bytes2Hex(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < data.length; i++) {
            int b = data[i] & 0xFF;
            sb.append(hex[b >> 4] & 0x0F);
            sb.append(hex[b & 0x0F]);
            sb.append(" ");
        }
        return sb.toString();
    }

    public static byte[] parseHexBinary(String s) {
        final int len = s.length();

        // "111" is not a valid hex encoding.
        if (len % 2 != 0) {
            throw new IllegalArgumentException("hexBinary needs to be even-length: " + s);
        }

        byte[] out = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            int h = hexToBin(s.charAt(i));
            int l = hexToBin(s.charAt(i + 1));
            if (h == -1 || l == -1) {
                throw new IllegalArgumentException("contains illegal character for hexBinary: " + s);
            }

            out[i / 2] = (byte) (h * 16 + l);
        }

        return out;
    }

    private static int hexToBin(char ch) {
        if ('0' <= ch && ch <= '9') {
            return ch - '0';
        }
        if ('A' <= ch && ch <= 'F') {
            return ch - 'A' + 10;
        }
        if ('a' <= ch && ch <= 'f') {
            return ch - 'a' + 10;
        }
        return -1;
    }
}
