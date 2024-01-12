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
public class NetworkAppearance implements Parameter {

    private final ParameterTag tag = ParameterTag.NETWORK_APPEARANCE;
    private int networkAppearance;

    public NetworkAppearance() {
    }

    public NetworkAppearance(Integer networkAppearance) {
        this.networkAppearance = networkAppearance;
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        byte[] data = new byte[4];
        data[0] = (byte) ((networkAppearance >> 24) & 0xFF);
        data[1] = (byte) ((networkAppearance >> 16) & 0xFF);
        data[2] = (byte) ((networkAppearance >> 8) & 0xFF);
        data[3] = (byte) (networkAppearance & 0xFF);
        baos.encode(tag, data);
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        byte[] data = new byte[length];
        bais.readData(data);

        this.networkAppearance = data[0] & 0xFF;
        this.networkAppearance = this.networkAppearance << 8;
        this.networkAppearance = this.networkAppearance | data[1] & 0xFF;
        this.networkAppearance = this.networkAppearance << 8;
        this.networkAppearance = this.networkAppearance | data[2] & 0xFF;
        this.networkAppearance = this.networkAppearance << 8;
        this.networkAppearance = this.networkAppearance | data[3] & 0xFF;
    }

    public long getNetworkAppearance() {
        return this.networkAppearance;
    }

    /**
     * @param networkAppearance the networkAppearance to set
     */
    public void setNetworkAppearance(Integer networkAppearance) {
        this.networkAppearance = networkAppearance;
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }

    @Override
    public String toString() {
        return String.format("NetworkAppearance [%s]", networkAppearance);
    }
}
