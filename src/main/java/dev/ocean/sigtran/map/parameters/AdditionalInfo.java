/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;

/**
 * AdditionalInfo ::= BIT STRING (SIZE (1..136))
 * -- Refers to Additional Info as specified in 3GPP TS 43.068
 * @author eatakishiyev
 */
public class AdditionalInfo implements MAPParameter{

    private BitSetStrictLength bitSet;

    public AdditionalInfo() {
    }

    public AdditionalInfo(BitSetStrictLength bitSet) {
        this.bitSet = bitSet;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            if (bitSet.size() < 1
                    || bitSet.size() > 136) {
                throw new UnexpectedDataException("Additional info length must be in range [1..136]. Current count = " + bitSet.size());
            }
            aos.writeBitString(tagClass, tag, bitSet);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            this.bitSet = ais.readBitString();
            if (bitSet.size() < 1
                    || bitSet.size() > 136) {
                throw new UnexpectedDataException("Additional info length must be in range [1..136]. Current count = " + bitSet.size());
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public BitSetStrictLength getBitSet() {
        return this.bitSet;
    }
}
