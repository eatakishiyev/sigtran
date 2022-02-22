/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v2;

import java.io.IOException;
import dev.ocean.sigtran.cap.parameters.ApplicationTimer;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class DpSpecificCriteria {

    private ApplicationTimer applicationTimer;

    public DpSpecificCriteria() {

    }

    public DpSpecificCriteria(ApplicationTimer applicationTimer) {
        this.applicationTimer = applicationTimer;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws ParameterOutOfRangeException, IOException, AsnException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        this.applicationTimer.encode(1, Tag.CLASS_CONTEXT_SPECIFIC, aos);
        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        int tag = tmpAis.readTag();

        switch (tag) {
            case 1:
                this.applicationTimer = new ApplicationTimer();
                this.applicationTimer.decode(tmpAis);
                break;
        }
    }

}
