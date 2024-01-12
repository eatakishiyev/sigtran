/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v2;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class ReleaseIfDurationExceeded {

    private boolean tone = false;
    private byte[] extensions;

    public ReleaseIfDurationExceeded() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        aos.writeBoolean(tone);

        if (extensions != null) {
            aos.writeSequence(Tag.CLASS_CONTEXT_SPECIFIC, 10, extensions);
        }

        aos.FinalizeContent(lenPos);
    }

    public void encode(AsnOutputStream aos) throws AsnException, IOException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        int tag = tmpAis.readTag();
        if (tag == Tag.BOOLEAN && tmpAis.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.tone = tmpAis.readBoolean();
        } else {
            throw new AsnException(String.format("Expecting Tag[1] Class[0], found Tag[%s] Class[%s]", tag, tmpAis.getTagClass()));
        }

        if (tmpAis.available() > 0) {
            tag = tmpAis.readTag();
            if (tag == 10 && tmpAis.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                extensions = tmpAis.readSequence();
            } else {
                throw new AsnException(String.format("Expecting Tag[10] Class[2], found Tag[%s] Class[%s]", tag, tmpAis.getTagClass()));
            }
        }
    }

    public byte[] getExtensions() {
        return extensions;
    }

    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

    public boolean isTone() {
        return tone;
    }

    public void setTone(boolean tone) {
        this.tone = tone;
    }

}
