/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua.parameters;

import java.io.IOException;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import azrc.az.sigtran.m3ua.io.M3UAParameterByteArrayOutputStream;

/**
 *
 * @author root
 */
public class CorrelationID implements Parameter {

    private final ParameterTag tag = ParameterTag.CORRELATION_ID;
    private int correlationID;

    public CorrelationID() {
    }

    public CorrelationID(int correlationID) {
        this.correlationID = correlationID;
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        byte[] data = new byte[length];
        bais.readData(data);

        this.correlationID = data[0] & 0xFF;
        this.correlationID = this.correlationID << 8;
        this.correlationID = this.correlationID | (data[1] & 0xFF);
        this.correlationID = this.correlationID << 8;
        this.correlationID = this.correlationID | (data[2] & 0xFF);
        this.correlationID = this.correlationID << 8;
        this.correlationID = this.correlationID | (data[3] & 0xFF);
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        byte[] data = new byte[4];
        data[0] = (byte) ((correlationID >> 24) & 0xFF);
        data[1] = (byte) ((correlationID >> 16) & 0xFF);
        data[2] = (byte) ((correlationID >> 8) & 0xFF);
        data[3] = (byte) (correlationID & 0xFF);

        baos.encode(tag, data);
    }

    /**
     * @return the correlationID
     */
    public int getCorrelationID() {
        return correlationID;
    }

    /**
     * @param correlationID the correlationID to set
     */
    public void setCorrelationID(int correlationID) {
        this.correlationID = correlationID;
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    @Override
    public String toString() {
        return String.format("CorrelationId [%s]", this.correlationID);
    }
}
