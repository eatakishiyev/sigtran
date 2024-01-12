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
 *
 * @author eatakishiyev
 */
public class ReceivingSideID extends LegId {

    private LegType receivingSideId;

    public ReceivingSideID() {
    }

    public ReceivingSideID(LegType receivingSideId) {
        this.receivingSideId = receivingSideId;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, receivingSideId.value());
        aos.FinalizeContent(lenPos);
    }

    @Override
    final void decode(AsnInputStream ais) throws AsnException, IOException {
        this.receivingSideId = LegType.valueOf((int) ais.readInteger());
    }

    @Override
    public LegType getLegType() {
        return this.receivingSideId;
    }

}
