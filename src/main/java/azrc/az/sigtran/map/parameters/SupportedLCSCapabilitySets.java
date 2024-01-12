/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import java.util.BitSet;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;

/**
 * SupportedLCS-CapabilitySets ::= BIT STRING {
 * lcsCapabilitySet1 (0),
 * lcsCapabilitySet2 (1),
 * lcsCapabilitySet3 (2),
 * lcsCapabilitySet4 (3) ,
 * lcsCapabilitySet5 (4) } (SIZE (2..16))
 * -- Core network signalling capability set1 indicates LCS Release98 or Release99 version.
 * -- Core network signalling capability set2 indicates LCS Release4.
 * -- Core network signalling capability set3 indicates LCS Release5.
 * -- Core network signalling capability set4 indicates LCS Release6.
 * -- Core network signalling capability set5 indicates LCS Release7 or later version.
 * -- A node shall mark in the BIT STRING all LCS capability sets it supports.
 * -- If no bit is set then the sending node does not support LCS.
 * -- If the parameter is not sent by an VLR then the VLR may support at most capability set1.
 * -- If the parameter is not sent by an SGSN then no support for LCS is assumed.
 * -- An SGSN is not allowed to indicate support of capability set1.
 * -- Other bits than listed above shall be discarded.
 *
 * @author eatakishiyev
 */
public class SupportedLCSCapabilitySets {

    private boolean lcsCapabilitySet1 = false;
    private boolean lcsCapabilitySet2 = false;
    private boolean lcsCapabilitySet3 = false;
    private boolean lcsCapabilitySet4 = false;
    private boolean lcsCapabilitySet5 = false;

    public SupportedLCSCapabilitySets() {
    }

    public SupportedLCSCapabilitySets(boolean lcsCapabilitySet1, boolean lcsCapabilitySet2, boolean lcsCapabilitySet3, boolean lcsCapabilitySet4, boolean lcsCapabilitySet5) {
        this.lcsCapabilitySet1 = lcsCapabilitySet1;
        this.lcsCapabilitySet2 = lcsCapabilitySet2;
        this.lcsCapabilitySet3 = lcsCapabilitySet3;
        this.lcsCapabilitySet4 = lcsCapabilitySet4;
        this.lcsCapabilitySet5 = lcsCapabilitySet5;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        BitSet bitSet = new BitSet(6);
        bitSet.set(0, lcsCapabilitySet1);
        bitSet.set(1, lcsCapabilitySet2);
        bitSet.set(2, lcsCapabilitySet3);
        bitSet.set(3, lcsCapabilitySet4);
        bitSet.set(4, lcsCapabilitySet5);
        aos.writeStringBinary(tagClass, tag, bitSet);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException {
        BitSetStrictLength bitSet = ais.readBitString();
        lcsCapabilitySet1 = bitSet.get(0);
        lcsCapabilitySet2 = bitSet.get(1);
        lcsCapabilitySet3 = bitSet.get(2);
        lcsCapabilitySet4 = bitSet.get(3);
        lcsCapabilitySet5 = bitSet.get(4);
    }

    /**
     * @return the lcsCapabilitySet1
     */
    public Boolean getLcsCapabilitySet1() {
        return lcsCapabilitySet1;
    }

    /**
     * @param lcsCapabilitySet1 the lcsCapabilitySet1 to set
     */
    public void setLcsCapabilitySet1(Boolean lcsCapabilitySet1) {
        this.lcsCapabilitySet1 = lcsCapabilitySet1;
    }

    /**
     * @return the lcsCapabilitySet2
     */
    public Boolean getLcsCapabilitySet2() {
        return lcsCapabilitySet2;
    }

    /**
     * @param lcsCapabilitySet2 the lcsCapabilitySet2 to set
     */
    public void setLcsCapabilitySet2(Boolean lcsCapabilitySet2) {
        this.lcsCapabilitySet2 = lcsCapabilitySet2;
    }

    /**
     * @return the lcsCapabilitySet3
     */
    public Boolean getLcsCapabilitySet3() {
        return lcsCapabilitySet3;
    }

    /**
     * @param lcsCapabilitySet3 the lcsCapabilitySet3 to set
     */
    public void setLcsCapabilitySet3(Boolean lcsCapabilitySet3) {
        this.lcsCapabilitySet3 = lcsCapabilitySet3;
    }

    /**
     * @return the lcsCapabilitySet4
     */
    public Boolean getLcsCapabilitySet4() {
        return lcsCapabilitySet4;
    }

    /**
     * @param lcsCapabilitySet4 the lcsCapabilitySet4 to set
     */
    public void setLcsCapabilitySet4(Boolean lcsCapabilitySet4) {
        this.lcsCapabilitySet4 = lcsCapabilitySet4;
    }

    /**
     * @return the lcsCapabilitySet5
     */
    public Boolean getLcsCapabilitySet5() {
        return lcsCapabilitySet5;
    }

    /**
     * @param lcsCapabilitySet5 the lcsCapabilitySet5 to set
     */
    public void setLcsCapabilitySet5(Boolean lcsCapabilitySet5) {
        this.lcsCapabilitySet5 = lcsCapabilitySet5;
    }
}
