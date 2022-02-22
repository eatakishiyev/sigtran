/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import java.util.BitSet;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;

/**
 * SupportedCamelPhases::= BIT STRING {
 * phase1 (0),
 * phase2 (1),
 * phase3 (2),
 * phase4 (3)} (SIZE (1..16))
 * -- A node shall mark in the BIT STRING all CAMEL Phases it supports.
 * -- Other values than listed above shall be discarded.
 *
 * @author eatakishiyev
 */
public class SupportedCamelPhases {

    private boolean phase1;
    private boolean phase2;
    private boolean phase3;
    private boolean phase4;

    public SupportedCamelPhases() {
    }

    public SupportedCamelPhases(boolean phase1, boolean phase2, boolean phase3, boolean phase4) {
        this.phase1 = phase1;
        this.phase2 = phase2;
        this.phase3 = phase3;
        this.phase4 = phase4;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            BitSet bitSet = new BitSet(8);
            bitSet.set(0, phase1);
            bitSet.set(1, phase2);
            bitSet.set(2, phase3);
            bitSet.set(3, phase4);
            aos.writeStringBinary(tagClass, tag, bitSet);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_BIT, aos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        BitSetStrictLength bitSet = ais.readBitString();
        phase1 = bitSet.get(0);
        phase2 = bitSet.get(1);
        phase3 = bitSet.get(2);
        phase4 = bitSet.get(3);
    }

    /**
     * @return the phase1
     */
    public boolean getPhase1() {
        return phase1;
    }

    /**
     * @param phase1 the phase1 to set
     */
    public void setPhase1(boolean phase1) {
        this.phase1 = phase1;
    }

    /**
     * @return the phase2
     */
    public boolean getPhase2() {
        return phase2;
    }

    /**
     * @param phase2 the phase2 to set
     */
    public void setPhase2(boolean phase2) {
        this.phase2 = phase2;
    }

    /**
     * @return the phase3
     */
    public boolean getPhase3() {
        return phase3;
    }

    /**
     * @param phase3 the phase3 to set
     */
    public void setPhase3(boolean phase3) {
        this.phase3 = phase3;
    }

    /**
     * @return the phase4
     */
    public boolean getPhase4() {
        return phase4;
    }

    /**
     * @param phase4 the phase4 to set
     */
    public void setPhase4(boolean phase4) {
        this.phase4 = phase4;
    }
}
