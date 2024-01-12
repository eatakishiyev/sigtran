/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * SendingSideID ::= CHOICE {sendingSideID [0] LegType} -- used to identify
 * LegID in operations sent from gsmSCF to gsmSSF
 *
 * @author eatakishiyev
 */
public class SendingSideID extends LegId {

    private LegType sendingSideId;

    public SendingSideID() {
    }

    public SendingSideID(LegType sendingSideId) {
        this.sendingSideId = sendingSideId;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IOException, AsnException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, this.sendingSideId.value());
        aos.FinalizeContent(lenPos);
    }

    @Override
    final void decode(AsnInputStream ais) throws AsnException, IOException {
        this.sendingSideId = LegType.valueOf((int) ais.readInteger());
    }

    @Override
    public LegType getLegType() {
        return this.sendingSideId;
    }

}
