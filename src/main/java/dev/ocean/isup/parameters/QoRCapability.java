/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class QoRCapability implements IsupParameter {

    private byte[] qoRSupport;

    public QoRCapability() {
    }

    @Override
    public byte[] encode() throws IOException {
        for (int i = 0; i < qoRSupport.length; i++) {
            qoRSupport[i] = (byte) (qoRSupport[i] & 0b01111111);
        }
        qoRSupport[qoRSupport.length - 1] = (byte) (qoRSupport[qoRSupport.length - 1] | 0b10000000);
        return qoRSupport;
    }

    @Override
    public void decode(byte[] data) {
        this.qoRSupport = data;
    }

    public byte[] getQoRSupport() {
        return qoRSupport;
    }

    public void setQoRSupport(byte[] qoRSupport) {
        this.qoRSupport = qoRSupport;
    }

    public static boolean isQoRSupported(byte b) {
        return (b & 0b00000001) == 1;
    }

    public static byte encodeQorSupportedByte(boolean qoRSupport) {
        return (byte) (qoRSupport ? 1 : 0);
    }

    @Override
    public int getParameterCode() {
        return QOR_CAPABILITY;
    }

}
