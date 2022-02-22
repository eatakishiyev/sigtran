/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class VBSDataList {

    private final List<VoiceBroadcastData> voiceBroadcastDataList;

    public VBSDataList() {
        this.voiceBroadcastDataList = new ArrayList();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            if (voiceBroadcastDataList.size() < 1
                    || voiceBroadcastDataList.size() > Constants.maxNumOfVBSGroupIds) {
                throw new UnexpectedDataException("VoiceBroadcastData count must be in [1..50] interval. Current count is " + voiceBroadcastDataList.size());
            }
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (VoiceBroadcastData voiceBroadcastData : this.voiceBroadcastDataList) {
                voiceBroadcastData.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.SEQUENCE
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    VoiceBroadcastData voiceBroadcastData = new VoiceBroadcastData();
                    voiceBroadcastData.decode(ais.readSequenceStream());
                    this.voiceBroadcastDataList.add(voiceBroadcastData);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting Tag[SEQUENCE] Class[UNIVERSA]."
                            + "Received Tag[%s] Class[%s]"));
                }
            }
            if (voiceBroadcastDataList.size() < 1
                    || voiceBroadcastDataList.size() > Constants.maxNumOfVBSGroupIds) {
                throw new UnexpectedDataException("VoiceBroadcastData count must be in [1..50] interval. Current count is " + voiceBroadcastDataList.size());
            }

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public List<VoiceBroadcastData> getVoiceBroadcastDataList() {
        return voiceBroadcastDataList;
    }
}
