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
public class LocalRoutingKeyIdentifier implements Parameter {

    private ParameterTag tag = ParameterTag.LOCAL_ROUTING_KEY_IDENTIFIER;
    private int localRKIdentifier;

    public long getLocalRKIdentifier() {
        return this.localRKIdentifier;
    }

    @Override
    public void decode(M3UAParameterByteArrayInputStream bais) throws IOException {
        int length = bais.readParameterLength();
        byte[] data = new byte[length];
        bais.readData(data);

        this.localRKIdentifier = data[0] & 0xFF;
        this.localRKIdentifier = this.localRKIdentifier << 8;
        this.localRKIdentifier = this.localRKIdentifier | data[1] & 0xFF;
        this.localRKIdentifier = this.localRKIdentifier << 8;
        this.localRKIdentifier = this.localRKIdentifier | data[2] & 0xFF;
        this.localRKIdentifier = this.localRKIdentifier << 8;
        this.localRKIdentifier = this.localRKIdentifier | data[3] & 0xFF;
    }

    @Override
    public void encode(M3UAParameterByteArrayOutputStream baos) throws IOException {
        byte[] data = new byte[4];
        data[0] = (byte) ((localRKIdentifier >> 24) & 0xFF);
        data[1] = (byte) ((localRKIdentifier >> 16) & 0xFF);
        data[2] = (byte) ((localRKIdentifier >> 8) & 0xFF);
        data[3] = (byte) (localRKIdentifier & 0xFF);
        baos.encode(this.tag, data);
    }

    @Override
    public ParameterTag getParameterTag() {
        return this.tag;
    }
}
