/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * AccessRestrictionData ::= BIT STRING {
 * utranNotAllowed (0),
 * geranNotAllowed (1),
 * ganNotAllowed (2),
 * i-hspa-evolutionNotAllowed (3),
 * e-utranNotAllowed (4),
 * ho-toNon3GPP-AccessNotAllowed (5) } (SIZE (2..8))
 * -- exception handling:
 * -- access restriction data related to an access type not supported by a node
 * -- shall be ignored
 * -- bits 6 to 7 shall be ignored if received and not understood
 * @author eatakishiyev
 */
public class AccessRestrictionData implements MAPParameter{

    private boolean utranNotAllowed;
    private boolean geranNotAllowed;
    private boolean ganNotAllowed;
    private boolean iHSPAEvolutionNotAllowed;
    private boolean eUtranNotAllowed;
    private boolean hoToNon3GPPAccessNotAllowed;

    public AccessRestrictionData() {
    }

    public AccessRestrictionData(boolean utranNotAllowed, boolean geranNotAllowed, boolean ganNotAllowed, boolean iHSPAEvolutionNotAllowed, boolean eUtranNotAllowed, boolean hoToNon3GPPAccessNotAllowed) {
        this.utranNotAllowed = utranNotAllowed;
        this.geranNotAllowed = geranNotAllowed;
        this.ganNotAllowed = ganNotAllowed;
        this.iHSPAEvolutionNotAllowed = iHSPAEvolutionNotAllowed;
        this.eUtranNotAllowed = eUtranNotAllowed;
        this.hoToNon3GPPAccessNotAllowed = hoToNon3GPPAccessNotAllowed;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            BitSet bitSet = new BitSet(8);
            bitSet.set(0, utranNotAllowed);
            bitSet.set(1, geranNotAllowed);
            bitSet.set(2, ganNotAllowed);
            bitSet.set(3, iHSPAEvolutionNotAllowed);
            bitSet.set(4, eUtranNotAllowed);
            bitSet.set(5, hoToNon3GPPAccessNotAllowed);
            aos.writeStringBinary(tagClass, tag, bitSet);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_BIT, aos);
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            BitSetStrictLength bitSet = ais.readBitString();
            this.utranNotAllowed = bitSet.get(0);
            this.geranNotAllowed = bitSet.get(1);
            this.ganNotAllowed = bitSet.get(2);
            this.iHSPAEvolutionNotAllowed = bitSet.get(3);
            this.eUtranNotAllowed = bitSet.get(4);
            this.hoToNon3GPPAccessNotAllowed = bitSet.get(5);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the utranNotAllowed
     */
    public boolean isUtranNotAllowed() {
        return utranNotAllowed;
    }

    /**
     * @param utranNotAllowed the utranNotAllowed to set
     */
    public void setUtranNotAllowed(boolean utranNotAllowed) {
        this.utranNotAllowed = utranNotAllowed;
    }

    /**
     * @return the geranNotAllowed
     */
    public boolean isGeranNotAllowed() {
        return geranNotAllowed;
    }

    /**
     * @param geranNotAllowed the geranNotAllowed to set
     */
    public void setGeranNotAllowed(boolean geranNotAllowed) {
        this.geranNotAllowed = geranNotAllowed;
    }

    /**
     * @return the ganNotAllowed
     */
    public boolean isGanNotAllowed() {
        return ganNotAllowed;
    }

    /**
     * @param ganNotAllowed the ganNotAllowed to set
     */
    public void setGanNotAllowed(boolean ganNotAllowed) {
        this.ganNotAllowed = ganNotAllowed;
    }

    /**
     * @return the iHSPAEvolutionNotAllowed
     */
    public boolean isiHSPAEvolutionNotAllowed() {
        return iHSPAEvolutionNotAllowed;
    }

    /**
     * @param iHSPAEvolutionNotAllowed the iHSPAEvolutionNotAllowed to set
     */
    public void setiHSPAEvolutionNotAllowed(boolean iHSPAEvolutionNotAllowed) {
        this.iHSPAEvolutionNotAllowed = iHSPAEvolutionNotAllowed;
    }

    /**
     * @return the eUtranNotAllowed
     */
    public boolean iseUtranNotAllowed() {
        return eUtranNotAllowed;
    }

    /**
     * @param eUtranNotAllowed the eUtranNotAllowed to set
     */
    public void seteUtranNotAllowed(boolean eUtranNotAllowed) {
        this.eUtranNotAllowed = eUtranNotAllowed;
    }

    /**
     * @return the hoToNon3GPPAccessNotAllowed
     */
    public boolean isHoToNon3GPPAccessNotAllowed() {
        return hoToNon3GPPAccessNotAllowed;
    }

    /**
     * @param hoToNon3GPPAccessNotAllowed the hoToNon3GPPAccessNotAllowed to set
     */
    public void setHoToNon3GPPAccessNotAllowed(boolean hoToNon3GPPAccessNotAllowed) {
        this.hoToNon3GPPAccessNotAllowed = hoToNon3GPPAccessNotAllowed;
    }

}
