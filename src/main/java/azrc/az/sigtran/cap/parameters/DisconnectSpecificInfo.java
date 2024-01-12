/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import azrc.az.sigtran.cap.parameters.ITU.Q_850.Cause;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class DisconnectSpecificInfo {

    private Cause releaseCause;

    public DisconnectSpecificInfo() {
    }

    public DisconnectSpecificInfo(Cause releaseCause) {
        this.releaseCause = releaseCause;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (releaseCause != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            releaseCause.encode(baos);

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 0);
            aos.writeLength(baos.toByteArray().length);
            aos.write(baos.toByteArray());
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        while (tmpAis.available() > 0) {
            int tag = tmpAis.readTag();
            if (tag != 0 || tmpAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Incorrect tag received. Expecting Tag[0] Class[2], found Tag[%s] Class[%s]", tag, tmpAis.getTagClass()));
            }

            byte[] data = new byte[tmpAis.readLength()];
            tmpAis.read(data);

            this.releaseCause = new Cause();
            this.releaseCause.decode(new ByteArrayInputStream(data));
        }
    }

    /**
     * @return the releaseCause
     */
    public Cause getReleaseCause() {
        return releaseCause;
    }

    /**
     * @param releaseCause the releaseCause to set
     */
    public void setReleaseCause(Cause releaseCause) {
        this.releaseCause = releaseCause;
    }

}
