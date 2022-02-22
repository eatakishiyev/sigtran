/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import java.io.ByteArrayOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class CallReference implements IsupParameter {

    private int callIdentity;
    private int spc;

    public CallReference() {
    }

    public CallReference(int callIdentity, int spc) {
        this.callIdentity = callIdentity;
        this.spc = spc;
    }

    public int getCallIdentity() {
        return callIdentity;
    }

    public void setCallIdentity(int callIdentity) {
        this.callIdentity = callIdentity;
    }

    @Override
    public byte[] encode() {
        byte[] data = new byte[5];
        data[0] = (byte) ((callIdentity >> 16) & 0b11111111);
        data[1] = (byte) ((callIdentity >> 8) & 0b11111111);
        data[2] = (byte) ((callIdentity) & 0b11111111);

        data[3] = (byte) (spc);
        data[4] = (byte) ((spc >> 8) & 0b00111111);
        return data;
    }

    @Override
    public void decode(byte[] data) {
        this.callIdentity = data[0] & 0b11111111;
        this.callIdentity = (callIdentity << 8) & (data[1] & 0b11111111);
        this.callIdentity = (callIdentity << 8) & (data[2] & 0b11111111);

        this.spc = data[3] & 0b00111111;
        this.spc = spc << 8;
        this.spc = spc | (data[4] & 0b11111111);
    }

    public int getSpc() {
        return spc;
    }

    public void setSpc(int spc) {
        this.spc = spc;
    }

    @Override
    public int getParameterCode() {
        return CALL_REFERENCE;
    }

}
