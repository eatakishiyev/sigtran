/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.sigtran.m3ua.parameters.ParameterTag;

/**
 *
 * @author eatakishiyev
 */
public class M3UAParameterByteArrayOutputStream extends ByteArrayOutputStream {

    private int length = 0;

    public M3UAParameterByteArrayOutputStream() {
    }

    public M3UAParameterByteArrayOutputStream(int size) {
        super(size);
    }

    public void writeParameterTag(ParameterTag tag) {
        super.write((tag.value() >> 8) & 0xFF);
        super.write(tag.value() & 0xFF);
    }

    /*
     * Parameter Length: 16 bits (unsigned integer) The Parameter Length field
     * contains the size of the parameter in octets, including the Parameter
     * Tag, Parameter Length, and Parameter Value fields. Thus, a parameter with
     * a zero-length Parameter Value field would have a Length field of 4. The
     * Parameter Length does not include any padding octets. If the parameter
     * contains subparameters, the Parameter Length field will include all the
     * octets of each subparameter, including subparameter padding octets (if
     * there are any).
     */
    private void writeParameterLength(int length) {
        this.length = length;
        super.write((this.length >> 8) & 0xFF);
        super.write(this.length & 0xFF);
    }

    private int writePadding() {
        int padding = 0;
        if (this.length % 4 > 0) {
            padding = (4 - this.length % 4);
        }
        
        while (padding > 0) {
            super.write((byte) 0x00);
            padding--;
        }
        return padding;
    }

    public void encode(ParameterTag tag, byte[] data) throws IOException {
        this.writeParameterTag(tag);
        this.writeParameterLength(data.length + 4);//parameter data length + 2 tag + 2 length
        super.write(data);
        this.writePadding();
    }
}
