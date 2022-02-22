/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.api;

import java.io.IOException;
import dev.ocean.sigtran.cap.callcontrol.v2.TimeDurationCharging;
import dev.ocean.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 *
 * @author eatakishiyev
 */
public class CAMELAChBillingChargingCharacteristics {

    private ITimeDurationCharging timeDurationCharging;

    public CAMELAChBillingChargingCharacteristics() {
    }

    public CAMELAChBillingChargingCharacteristics(ITimeDurationCharging timeDurationCharging) {
        this.timeDurationCharging = timeDurationCharging;
    }

    public void encode(AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException {
        this.timeDurationCharging.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException, ParameterOutOfRangeException {
        int tag = ais.readTag();
        if (tag != 0 || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
            throw new AsnException(String.format("Unexpected tag received.Expecting TagClass[2] Tag[0], found TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
        }

        this.timeDurationCharging = new TimeDurationCharging();
        this.timeDurationCharging.decode(ais);
    }

    public ITimeDurationCharging getTimeDurationCharging() {
        return timeDurationCharging;
    }

}
