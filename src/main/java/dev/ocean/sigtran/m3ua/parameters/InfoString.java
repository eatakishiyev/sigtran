/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.parameters;

import java.io.IOException;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;

/**
 *
 * @author root
 */
public class InfoString implements Parameter {

    public static final ParameterTag TAG = ParameterTag.INFO_STRING;
    private String infoString;

    public InfoString() {
    }

    public InfoString(String infoString) {
        this.infoString = infoString;
    }

    public String getInfoString() {
        return this.infoString;
    }

    public void setInfoString(String infoString) {
        this.infoString = infoString;
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        byte[] data = new byte[length];
        bais.readData(data);
        this.infoString = new String(data, "UTF-8");
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        baos.encode(TAG, infoString.getBytes("UTF-8"));
    }

    @Override
    public ParameterTag getParameterTag() {
        return TAG;
    }

    @Override
    public String toString() {
        return String.format("InfoString[%s]", infoString);
    }

}
