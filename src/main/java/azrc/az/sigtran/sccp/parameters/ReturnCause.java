/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.parameters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author root
 */
public class ReturnCause {

    private int value;

    public ReturnCause() {
    }

    public ReturnCause(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void decode(InputStream in) throws IOException {
        if (in.read() != 1) {
            throw new IOException();
        }

        int b = in.read() & 0xFf;

        this.value = b;
    }

    public void encode(OutputStream out) throws IOException {
        byte b = (byte) (this.value);
        out.write(1);
        out.write(b);
    }

    public void decode(byte[] bb) throws IOException {
        int b = bb[0] & 0xff;

        this.value = b;
    }

    public byte[] encode() throws IOException {
        return new byte[]{(byte) this.value};
    }
}
