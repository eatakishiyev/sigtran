/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author eatakishiyev
 */
public class M3UAParameterByteArrayInputStream {

    private ByteArrayInputStream bais = null;
    private int parameterLength;
//    private int messageLength = 0;

    public M3UAParameterByteArrayInputStream(byte[] buf) {
        bais = new ByteArrayInputStream(buf);
    }

    public int readParameterTag() {
        int parameterTag = bais.read();
        parameterTag = parameterTag << 8;
        parameterTag = parameterTag | bais.read();
        return parameterTag;
    }

    public int readParameterLength() throws IOException {

        byte[] lengthB = new byte[2];
        bais.read(lengthB);
        parameterLength = ((lengthB[0] & 0xff) << 8) | (lengthB[1] & 0xFF);
        parameterLength = parameterLength - 4;// parameter length - 2 tag - 2 length
        return parameterLength;

    }

    private int skipPadding() {
        int padded = (this.parameterLength) % 4 > 0 ? 4 - (this.parameterLength) % 4 : 0;
        bais.skip(padded);
        return padded;
    }

    public int available() {
        return bais.available();
    }

    public int readData(byte[] buf) throws IOException {
        int readed = bais.read(buf);
        skipPadding();
        return readed;
    }

    public int read() {
        return this.bais.read();
    }
}
