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
import org.mobicents.protocols.asn.Tag;

/**
 * SS-Status ::= OCTET STRING (SIZE (1))
 * -- bits 8765: 0000 (unused)
 * -- bits 4321: Used to convey the "P bit","R bit","A bit" and "Q bit",
 * -- representing supplementary service state information
 * -- as defined in TS 3GPP TS 23.011 [22]
 * -- bit 4: "Q bit"
 * -- bit 3: "P bit"
 * -- bit 2: "R bit"
 * -- bit 1: "A bit"
 *
 * @author eatakishiyev
 */
public class SSStatus {

    private boolean qBit;
    private boolean pBit;
    private boolean rBit;
    private boolean aBit;

    public SSStatus() {
    }

    public SSStatus(boolean qBit, boolean pBit, boolean rBit, boolean aBit) {
        this.qBit = qBit;
        this.pBit = pBit;
        this.rBit = rBit;
        this.aBit = aBit;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            int extSSStatus = 0;
            extSSStatus = extSSStatus << 4;
            extSSStatus = extSSStatus | (qBit ? 1 : 0);
            extSSStatus = extSSStatus << 1;
            extSSStatus = extSSStatus | (pBit ? 1 : 0);
            extSSStatus = extSSStatus << 1;
            extSSStatus = extSSStatus | (rBit ? 1 : 0);
            extSSStatus = extSSStatus << 1;
            extSSStatus = extSSStatus | (aBit ? 1 : 0);

            aos.writeOctetString(tagClass, tag, new byte[]{(byte) extSSStatus});
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            byte firstOctet = ais.readOctetString()[0];
            this.qBit = (((firstOctet >> 3) & 0x01) == 1);
            this.pBit = (((firstOctet >> 2) & 0x01) == 1);
            this.rBit = ((firstOctet >> 1 & 0x01) == 1);
            this.aBit = ((firstOctet & 0x01) == 1);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the operativeState
     */
    public Boolean getOperativeState() {
        return qBit;
    }

    /**
     * @param operativeState the operativeState to set
     */
    public void setOperativeState(Boolean operativeState) {
        this.qBit = operativeState;
    }

    /**
     * @return the provisioningState
     */
    public Boolean getProvisioningState() {
        return pBit;
    }

    /**
     * @param provisioningState the provisioningState to set
     */
    public void setProvisioningState(Boolean provisioningState) {
        this.pBit = provisioningState;
    }

    /**
     * @return the registrationState
     */
    public Boolean getRegistrationState() {
        return rBit;
    }

    /**
     * @param registrationState the registrationState to set
     */
    public void setRegistrationState(Boolean registrationState) {
        this.rBit = registrationState;
    }

    /**
     * @return the activationState
     */
    public Boolean getActivationState() {
        return aBit;
    }

    /**
     * @param activationState the activationState to set
     */
    public void setActivationState(Boolean activationState) {
        this.aBit = activationState;
    }

}
