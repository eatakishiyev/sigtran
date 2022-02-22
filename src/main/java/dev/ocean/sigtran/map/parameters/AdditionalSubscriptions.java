/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;

/**
 * AdditionalSubscriptions ::= BIT STRING {
 * privilegedUplinkRequest (0),
 * emergencyUplinkRequest (1),
 * emergencyReset (2)} (SIZE (3..8))
 * -- Other bits than listed above shall be discarded.
 * @author eatakishiyev
 */
public class AdditionalSubscriptions implements MAPParameter{

    private boolean privilegedUplinkRequest;
    private boolean emergencyUplinkRequest;
    private boolean emergencyReset;

    public AdditionalSubscriptions() {
    }

    public AdditionalSubscriptions(boolean privilegedUplinkRequest, boolean emergencyUplinkRequest, boolean emergencyReset) {
        this.privilegedUplinkRequest = privilegedUplinkRequest;
        this.emergencyUplinkRequest = emergencyUplinkRequest;
        this.emergencyReset = emergencyReset;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            BitSetStrictLength bitSet = new BitSetStrictLength(3);
            bitSet.set(0, privilegedUplinkRequest);
            bitSet.set(1, emergencyUplinkRequest);
            bitSet.set(2, emergencyReset);

            aos.writeBitString(tagClass, tag, bitSet);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            BitSetStrictLength bitSet = ais.readBitString();
            this.privilegedUplinkRequest = bitSet.get(0);
            this.emergencyUplinkRequest = bitSet.get(1);
            this.emergencyReset = bitSet.get(2);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the privilegedUplinkRequest
     */
    public boolean isPrivilegedUplinkRequest() {
        return privilegedUplinkRequest;
    }

    /**
     * @param privilegedUplinkRequest the privilegedUplinkRequest to set
     */
    public void setPrivilegedUplinkRequest(boolean privilegedUplinkRequest) {
        this.privilegedUplinkRequest = privilegedUplinkRequest;
    }

    /**
     * @return the emergencyUplinkRequest
     */
    public boolean isEmergencyUplinkRequest() {
        return emergencyUplinkRequest;
    }

    /**
     * @param emergencyUplinkRequest the emergencyUplinkRequest to set
     */
    public void setEmergencyUplinkRequest(boolean emergencyUplinkRequest) {
        this.emergencyUplinkRequest = emergencyUplinkRequest;
    }

    /**
     * @return the emergencyReset
     */
    public boolean isEmergencyReset() {
        return emergencyReset;
    }

    /**
     * @param emergencyReset the emergencyReset to set
     */
    public void setEmergencyReset(boolean emergencyReset) {
        this.emergencyReset = emergencyReset;
    }

}
