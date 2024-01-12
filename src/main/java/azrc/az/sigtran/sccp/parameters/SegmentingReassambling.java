/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.parameters;

/**
 *
 * @author root
 */
public class SegmentingReassambling {

    public static final int ParameterName = 0x06;
    private byte moreData;

    public SegmentingReassambling(byte moreData) {
        this.moreData = moreData;
    }

    public SegmentingReassambling(byte[] data) {
        decode(data);
    }

    public byte[] encode() {
        return new byte[]{(byte) (this.moreData & 0x01)};
    }

    private void decode(byte[] data) {
        this.moreData = (byte) (data[0] & 0x01);

    }
}
