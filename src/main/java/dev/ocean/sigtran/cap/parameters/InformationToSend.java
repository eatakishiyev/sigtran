/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class InformationToSend {

    private InbandInfo inbandInfo;
    private Tone tone;

    public InformationToSend() {
    }

    public InformationToSend(InbandInfo inbandInfo) {
        this.inbandInfo = inbandInfo;
    }

    public InformationToSend(Tone tone) {
        this.tone = tone;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, ParameterOutOfRangeException, IllegalNumberFormatException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (inbandInfo != null) {
            this.inbandInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        } else {
            this.tone.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException, IllegalNumberFormatException {
        this.decode_(ais.readSequenceStream());
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException, IllegalNumberFormatException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Unexpected tag received. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    inbandInfo = new InbandInfo();
                    inbandInfo.decode(ais);
                    break;
                case 1:
                    tone = new Tone();
                    tone.decode(ais);
                    break;
                default:
                    throw new AsnException(String.format("Unexpected tag received. TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
            }
        }
    }

    /**
     * @return the inbandInfo
     */
    public InbandInfo getInbandInfo() {
        return inbandInfo;
    }

    /**
     * @param inbandInfo the inbandInfo to set
     */
    public void setInbandInfo(InbandInfo inbandInfo) {
        this.inbandInfo = inbandInfo;
    }

    /**
     * @return the tone
     */
    public Tone getTone() {
        return tone;
    }

    /**
     * @param tone the tone to set
     */
    public void setTone(Tone tone) {
        this.tone = tone;
    }
}
