/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class Tone {

    private Integer4 toneId;
    private Integer4 duration;

    public Tone() {
    }

    public Tone(Integer4 toneId) {
        this.toneId = toneId;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        this.toneId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

        if (duration != null) {
            this.duration.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        this.decode_(ais.readSequenceStream());
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.toneId = new Integer4();
                    this.toneId.decode(ais);
                    break;
                case 1:
                    this.duration = new Integer4();
                    this.duration.decode(ais);
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

    /**
     * @return the toneId
     */
    public Integer4 getToneId() {
        return toneId;
    }

    /**
     * @param toneId the toneId to set
     */
    public void setToneId(Integer4 toneId) {
        this.toneId = toneId;
    }

    /**
     * @return the duration
     */
    public Integer4 getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(Integer4 duration) {
        this.duration = duration;
    }

}
