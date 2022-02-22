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
 *
 * @author eatakishiyev
 */
public class RequestedNodes {

    private boolean mme;
    private boolean sgsn;

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            BitSetStrictLength bitSetStrictLength = new BitSetStrictLength(8);
            bitSetStrictLength.set(0, mme);
            bitSetStrictLength.set(1, sgsn);
            aos.writeBitString(tagClass, tag, bitSetStrictLength);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            BitSetStrictLength bitSetStrictLength = ais.readBitString();
            this.mme = bitSetStrictLength.get(0);
            this.sgsn = bitSetStrictLength.get(1);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void setMme(boolean mme) {
        this.mme = mme;
    }

    public void setSgsn(boolean sgsn) {
        this.sgsn = sgsn;
    }

    public boolean isMme() {
        return mme;
    }

    public boolean isSgsn() {
        return sgsn;
    }

}
