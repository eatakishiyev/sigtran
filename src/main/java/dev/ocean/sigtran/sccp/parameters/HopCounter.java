/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.parameters;

import dev.ocean.sigtran.sccp.general.Encodable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author root
 */
public class HopCounter implements Encodable {

    public static final int PARAMETER_NAME = 0x11;
    private int hopCounter;
    public static int MAX_COUNT_OF_HOPS = 15;
    public static int MIN_COUNT_OF_HOPS = 1;

    public HopCounter() {
    }

    public HopCounter(int hopCounter) {
        this.hopCounter = hopCounter;
    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws Exception {
        if (this.hopCounter <= 0 || this.hopCounter > 15) {
            throw new IOException("HopCounter range is 1..15");
        }
        baos.write(this.hopCounter & 0xFF);
    }

    @Override
    public void decode(ByteArrayInputStream bais) throws Exception {
        this.hopCounter = bais.read() & 0xFF;
        if (this.hopCounter <= 0 || this.hopCounter > 15) {
            throw new IOException("HopCounter range is 1..15");
        }
    }

    /**
     * @return the hopCounter
     */
    public int getHopCounter() {
        return hopCounter;
    }

    @Override
    public String toString() {
        return String.format("HopCounter = %s", hopCounter);
    }
}
