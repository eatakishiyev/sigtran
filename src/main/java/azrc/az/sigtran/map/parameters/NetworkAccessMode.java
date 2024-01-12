/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class NetworkAccessMode {

    private NetworkAccessModes networkAccessMode;

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IOException, AsnException {
        aos.writeInteger(tagClass, tag, tag);
    }

    public void encode(ByteArrayOutputStream baos) throws IOException, AsnException {
        baos.write(networkAccessMode.getValue());
    }

    public void decode(ByteArrayInputStream ais) throws AsnException, IOException {
        this.networkAccessMode = NetworkAccessModes.getInstance(ais.read());
    }

    /**
     * @return the networkAccessMode
     */
    public NetworkAccessModes getNetworkAccessMode() {
        return networkAccessMode;
    }

    /**
     * @param networkAccessMode the networkAccessMode to set
     */
    public void setNetworkAccessMode(NetworkAccessModes networkAccessMode) {
        this.networkAccessMode = networkAccessMode;
    }

    public enum NetworkAccessModes {

        PACKED_AND_CIRCUIT(0),
        ONLY_CIRCUIT(1),
        ONLY_PACKET(2);
        private int value;

        private NetworkAccessModes(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static NetworkAccessModes getInstance(int value) {
            switch (value) {
                case 0:
                    return PACKED_AND_CIRCUIT;
                case 1:
                    return ONLY_CIRCUIT;
                case 2:
                    return ONLY_PACKET;
                default:
                    return null;
            }
        }
    }
}
