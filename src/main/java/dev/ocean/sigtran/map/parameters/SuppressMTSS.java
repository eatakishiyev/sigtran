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
import org.mobicents.protocols.asn.Tag;

/**
 * SuppressMTSS ::= BIT STRING {
 * suppressCUG (0),
 * suppressCCBS (1) } (SIZE (2..16))
 * -- Other bits than listed above shall be discarded
 * @author eatakishiyev
 */
public class SuppressMTSS {

    private boolean suppressCUG;
    private boolean suppressCCBS;

    public SuppressMTSS() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            BitSetStrictLength bitSetStrictLength = new BitSetStrictLength(2);
            bitSetStrictLength.set(0, suppressCUG);
            bitSetStrictLength.set(1, suppressCCBS);
            aos.writeBitString(tagClass, tag, bitSetStrictLength);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_BIT, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            BitSetStrictLength bitSetStrictLength = ais.readBitString();
            
            this.suppressCUG = bitSetStrictLength.get(0);
            this.suppressCCBS = bitSetStrictLength.get(1);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the suppressCUG
     */
    public boolean isSuppressCUG() {
        return suppressCUG;
    }

    /**
     * @param suppressCUG the suppressCUG to set
     */
    public void setSuppressCUG(boolean suppressCUG) {
        this.suppressCUG = suppressCUG;
    }

    /**
     * @return the suppressCCBS
     */
    public boolean isSuppressCCBS() {
        return suppressCCBS;
    }

    /**
     * @param suppressCCBS the suppressCCBS to set
     */
    public void setSuppressCCBS(boolean suppressCCBS) {
        this.suppressCCBS = suppressCCBS;
    }

}
