/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.address;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author root
 */
public class SignallingPointCode {

    private int signallingPointCode = -1;

    /*   
     * |---8---|---7----|---6----|---5----|---4----|---3----|----2----|---1----|
     * |                                                                 LSB   |
     * |-----------------------------------------------------------------------|
     * |   0   |   0    |   MSB                                                |
     * |-----------------------------------------------------------------------|
     */
    //Example point code 0064(HEX) is encoded as 6400(HEX)
    public SignallingPointCode() {
    }

    public SignallingPointCode(int signallingPointCode) {
        this.signallingPointCode = signallingPointCode;
    }

    public void encode(ByteArrayOutputStream baos) {
        baos.write(this.signallingPointCode & 0xFF);
        baos.write((this.signallingPointCode >> 8) & 0x3F);
    }

    public void decode(ByteArrayInputStream bais) {
        int p1 = bais.read() & 0xFF;
        int p2 = bais.read() & 0xFF;
        signallingPointCode = ((p2 & 0x3F) << 8) | p1;
    }

    /**
     * @return the signallingPointCode
     */
    public int getSignallingPointCode() {
        return signallingPointCode;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("SignallingPointCode:[")
                .append(signallingPointCode).append("]").toString();
    }
}
