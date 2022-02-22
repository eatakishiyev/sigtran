/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class ForwardGVNS implements IsupParameter{

    private byte[] data;

    public ForwardGVNS() {
    }

    public ForwardGVNS(byte[] data) {
        this.data = data;
    }

    @Override
    public byte[] encode() throws IOException {
        return data;
    }

    @Override
    public void decode(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public int getParameterCode() {
        return FORWARD_GVNS;
    }

    
}
