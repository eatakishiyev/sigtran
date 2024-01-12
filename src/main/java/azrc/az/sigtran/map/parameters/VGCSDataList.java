/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class VGCSDataList {

    private final List<VoiceGroupCallData> voiceGroupCallDataList;

    public VGCSDataList() {
        this.voiceGroupCallDataList = new ArrayList();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws UnexpectedDataException, IncorrectSyntaxException {
        if (voiceGroupCallDataList.size() < 1
                || voiceGroupCallDataList.size() > Constants.maxNumOfVGCSGroupIds) {
            throw new UnexpectedDataException(String.format("VoiceGroupCallData count must be in [1..50] interval. Current count is" + voiceGroupCallDataList.size()));
        }

        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (VoiceGroupCallData voiceGroupCallData : this.voiceGroupCallDataList) {
                voiceGroupCallData.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
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
                    VoiceGroupCallData voiceGroupCallData = new VoiceGroupCallData();
                    voiceGroupCallData.decode(ais.readSequenceStream());
                    this.voiceGroupCallDataList.add(voiceGroupCallData);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting Tag[SEQUENCE] Class[UNIVERSAL]."
                            + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
            if (voiceGroupCallDataList.size() < 1
                    || voiceGroupCallDataList.size() > Constants.maxNumOfVGCSGroupIds) {
                throw new UnexpectedDataException(String.format("VoiceGroupCallData count must be in [1..50] interval. Current count is" + voiceGroupCallDataList.size()));
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public List<VoiceGroupCallData> getVoiceGroupCallDataList() {
        return voiceGroupCallDataList;
    }

}
