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
public class AllCallSegmentsWithExtension {

    private Cause allCallSegments;
    private byte[] extensions;

    public AllCallSegmentsWithExtension() {
    }

    public AllCallSegmentsWithExtension(Cause allCallSegments) {
        this.allCallSegments = allCallSegments;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
        this.allCallSegments.encode(baos);
        aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 0);
        int lenPos_ = aos.StartContentDefiniteLength();
        aos.write(baos.toByteArray());
        aos.FinalizeContent(lenPos_);

        if (extensions != null) {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 1);
            lenPos_ = aos.StartContentDefiniteLength();
            aos.write(extensions);
            aos.FinalizeContent(lenPos_);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Received incorrect tag. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    byte[] data = new byte[ais.readLength()];
                    ais.read(data);
                    this.allCallSegments = new Cause();
                    this.allCallSegments.decode(new ByteArrayInputStream(data));
                    break;
                case 1:
                    this.extensions = new byte[ais.readLength()];
                    ais.read(extensions);
                    break;
                default:
                    throw new AsnException(String.format("Received unexpected tag. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
        }
    }

    /**
     * @return the allCallSegments
     */
    public Cause getAllCallSegments() {
        return allCallSegments;
    }

    /**
     * @param allCallSegments the allCallSegments to set
     */
    public void setAllCallSegments(Cause allCallSegments) {
        this.allCallSegments = allCallSegments;
    }

    /**
     * @return the extensions
     */
    public byte[] getExtensions() {
        return extensions;
    }

    /**
     * @param extensions the extensions to set
     */
    public void setExtensions(byte[] extensions) {
        this.extensions = extensions;
    }

}
