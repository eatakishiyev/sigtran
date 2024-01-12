/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import azrc.az.gsm.string.utils.bcd.BCDStringUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class IMEI implements MAPParameter {

    private String imei;

    public IMEI() {
    }

    public IMEI(String imei) {
        this.imei = imei;
    }

    public IMEI(AsnInputStream ais) throws IOException, AsnException {
        this.decode(ais);
    }

    public void encode(ByteArrayOutputStream baos) throws IOException, AsnException {
        BCDStringUtils.toTBCDString(imei, baos);
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IOException, AsnException {
        AsnOutputStream tmpAos = new AsnOutputStream();
        BCDStringUtils.toTBCDString(imei, tmpAos);
        aos.writeTag(tagClass, true, tag);
        aos.writeLength(tmpAos.size());
        aos.write(tmpAos.toByteArray());
    }

    public void encode(AsnOutputStream aos) throws IOException, AsnException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
    }

    public final void decode(AsnInputStream ais) throws IOException, AsnException {
        int length = ais.readLength();
        byte[] data = new byte[length];
        ais.read(data);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        this.imei = BCDStringUtils.fromTBCDToString(bais);
    }

    public String getImei() {
        return imei;
    }

}
