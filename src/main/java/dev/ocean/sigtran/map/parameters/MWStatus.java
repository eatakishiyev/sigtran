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
import org.mobicents.protocols.asn.BitSetStrictLength;

/**
 *
 * @author eatakishiyev
 */
public class MWStatus {

    private boolean scAddressNotIncluded;
    private boolean mnrfSet;
    private boolean mcefSet;
    private boolean mnrgSet;

    @Override
    public String toString() {
        return new StringBuilder().
                append("MWStatus:[")
                .append("SCAddressNotIncluded = ").append(scAddressNotIncluded)
                .append(";MNRFSet = ").append(mnrfSet)
                .append(";MCEFSet = ").append(mcefSet)
                .append(";MNRGSet = ").append(mnrgSet)
                .append("]").toString();
    }

    public MWStatus() {
    }

    public MWStatus(boolean scAddressIncluded, boolean mnrfSet, boolean mcefSet, boolean mnrgSet) {
        this.scAddressNotIncluded = scAddressIncluded;
        this.mnrfSet = mnrfSet;
        this.mcefSet = mcefSet;
        this.mnrgSet = mnrgSet;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        BitSetStrictLength bitSet = new BitSetStrictLength(6);
        if (scAddressNotIncluded) {
            bitSet.set(0);
        }
        if (mnrfSet) {
            bitSet.set(1);
        }
        if (mcefSet) {
            bitSet.set(2);
        }
        if (mnrgSet) {
            bitSet.set(3);
        }
        try {
            aos.writeBitString(tagClass, tag, bitSet);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            BitSetStrictLength bitSetStrictLength = ais.readBitString();
            this.scAddressNotIncluded = bitSetStrictLength.get(0);
            this.mnrfSet = bitSetStrictLength.get(1);
            this.mcefSet = bitSetStrictLength.get(2);
            this.mnrgSet = bitSetStrictLength.get(3);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the scAddressIncluded
     */
    public boolean isScAddressIncluded() {
        return scAddressNotIncluded;
    }

    /**
     * @param scAddressIncluded the scAddressIncluded to set
     */
    public void setScAddressIncluded(boolean scAddressIncluded) {
        this.scAddressNotIncluded = scAddressIncluded;
    }

    /**
     * @return the mnrfSet
     */
    public boolean isMnrfSet() {
        return mnrfSet;
    }

    /**
     * @param mnrfSet the mnrfSet to set
     */
    public void setMnrfSet(boolean mnrfSet) {
        this.mnrfSet = mnrfSet;
    }

    /**
     * @return the mcefSet
     */
    public boolean isMcefSet() {
        return mcefSet;
    }

    /**
     * @param mcefSet the mcefSet to set
     */
    public void setMcefSet(boolean mcefSet) {
        this.mcefSet = mcefSet;
    }

    /**
     * @return the mnrgSet
     */
    public boolean isMnrgSet() {
        return mnrgSet;
    }

    /**
     * @param mnrgSet the mnrgSet to set
     */
    public void setMnrgSet(boolean mnrgSet) {
        this.mnrgSet = mnrgSet;
    }
}
