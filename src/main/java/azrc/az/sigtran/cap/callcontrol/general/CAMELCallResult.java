/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.general;

import java.io.IOException;

import azrc.az.sigtran.cap.api.ITimeDurationChargingResult;
import azrc.az.sigtran.cap.callcontrol.v4.TimeDurationChargingResult;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * CAMEL-CallResult {PARAMETERS-BOUND : bound} ::= CHOICE {
 * timeDurationChargingResult [0] SEQUENCE { partyToCharge [0] ReceivingSideID,
 * timeInformation [1] TimeInformation, legActive [2] BOOLEAN DEFAULT TRUE,
 * callLegReleasedAtTcpExpiry [3] NULL OPTIONAL, extensions [4] Extensions
 * {bound}OPTIONAL, aChChargingAddress [5] AChChargingAddress {bound} DEFAULT
 * legID:receivingSideID:leg1, ... } }
 *
 * @author eatakishiyev
 */
public class CAMELCallResult {

    private ITimeDurationChargingResult timeDurationChargingResult;

    public CAMELCallResult() {
    }

    public CAMELCallResult(ITimeDurationChargingResult timeDurationChargingResult) {
        this.timeDurationChargingResult = timeDurationChargingResult;
    }

    public void encode(AsnOutputStream aos) throws AsnException, IOException, ParameterOutOfRangeException {
        this.timeDurationChargingResult.encode(aos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, ParameterOutOfRangeException {
        int tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || tag != 0) {
            throw new AsnException(String.format("Received unexpected data, expecting TagClass[2] Tag[0], found TagClass[%s] Tag[%s]", ais.getTagClass(), tag));
        }
        this.timeDurationChargingResult = new TimeDurationChargingResult();
        this.timeDurationChargingResult.decode(ais.readSequenceStream());
    }

    /**
     * @return the timeDurationChargingResult
     */
    public ITimeDurationChargingResult getTimeDurationChargingResult() {
        return timeDurationChargingResult;
    }
}
