/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.parameters;

import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.tcap.parameters.interfaces.ProtocolVersion;
import java.io.IOException;
import org.mobicents.protocols.asn.*;

/**
 *
 * @author root
 */
public class ProtocolVersionImpl implements ProtocolVersion {

    private BitSetStrictLength bs;
    private boolean isCorrect = true;

    protected ProtocolVersionImpl() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            BitSetStrictLength bs = new BitSetStrictLength(1);
            bs.set(0);
            aos.writeBitString(Tag.CLASS_CONTEXT_SPECIFIC, 0x00, bs);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
            throw new IncorrectSyntaxException(String.format("Incorrect formed tag. CLASS = %d PRIMITIVE =%b TAG = %d", ais.getTagClass(), ais.isTagPrimitive(), ais.getTag()));
        }
        try {
            this.bs = ais.readBitString();
            isCorrect = (bs.getStrictLength() == 1 && bs.get(0));
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the isCorrect
     */
    public boolean isCorrect() {
        return isCorrect;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProtocolVersion[")
                .append(bs.toString())
                .append("]");

        return sb.toString();
    }

}
