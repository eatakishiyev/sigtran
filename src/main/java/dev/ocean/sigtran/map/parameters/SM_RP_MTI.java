/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class SM_RP_MTI {

    private int smRPMTI;

    public SM_RP_MTI() {
    }

    public SM_RP_MTI(int smRPMTI) {
        this.smRPMTI = smRPMTI;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        if (smRPMTI < 0 || smRPMTI > 10) {
            throw new IncorrectSyntaxException("Unexpected value");
        }
        try {
            aos.writeInteger(tagClass, tag, smRPMTI);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException();
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            this.smRPMTI = ((Long) ais.readInteger()).intValue();
            if (smRPMTI < 0 || smRPMTI > 10) {
                throw new IncorrectSyntaxException("Unexpected value");
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the smRPMTI
     */
    public int getSmRPMTI() {
        return smRPMTI;
    }

    /**
     * @param smRPMTI the smRPMTI to set
     */
    public void setSmRPMTI(int smRPMTI) {
        this.smRPMTI = smRPMTI;
    }
}
