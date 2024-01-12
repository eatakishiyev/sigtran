/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * GPRSMSClass ::= SEQUENCE { mSNetworkCapability [0] MSNetworkCapability,
 * mSRadioAccessCapability [1] MSRadioAccessCapability OPTIONAL }
 *
 * @author eatakishiyev
 */
public class GPRSMSClass  implements MAPParameter{

    private MSNetworkCapability msNetworkCapability;
    private MSRadioAccessCapability msRadioAccessCapability;

    public GPRSMSClass() {
    }

    public GPRSMSClass(MSNetworkCapability msNetworkCapability) {
        this.msNetworkCapability = msNetworkCapability;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            msNetworkCapability.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);

            if (msRadioAccessCapability != null) {
                msRadioAccessCapability.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            this._decode(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag == 0 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.msNetworkCapability = new MSNetworkCapability();
                msNetworkCapability.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received."
                        + "Expecting Tag[0] Class[CONTEXT].", tag, ais.getTagClass()));
            }

            if (ais.available() > 0) {
                tag = ais.readTag();
                if (tag == 1 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.msRadioAccessCapability = new MSRadioAccessCapability();
                    msRadioAccessCapability.decode(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received."
                            + "Expecting Tag[1] Class[CONTEXT].", tag, ais.getTagClass()));
                }
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the msNetworkCapability
     */
    public MSNetworkCapability getMsNetworkCapability() {
        return msNetworkCapability;
    }

    /**
     * @param msNetworkCapability the msNetworkCapability to set
     */
    public void setMsNetworkCapability(MSNetworkCapability msNetworkCapability) {
        this.msNetworkCapability = msNetworkCapability;
    }

    /**
     * @return the msRadioAccessCapability
     */
    public MSRadioAccessCapability getMsRadioAccessCapability() {
        return msRadioAccessCapability;
    }

    /**
     * @param msRadioAccessCapability the msRadioAccessCapability to set
     */
    public void setMsRadioAccessCapability(MSRadioAccessCapability msRadioAccessCapability) {
        this.msRadioAccessCapability = msRadioAccessCapability;
    }

}
