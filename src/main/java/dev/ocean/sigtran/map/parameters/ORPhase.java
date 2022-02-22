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
 * OR-Phase ::= INTEGER (1..127)
 * @author eatakishiyev
 */
public class ORPhase {

    private int value;

    public ORPhase() {
    }

    public ORPhase(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            if (value < 1 && value > 127) {
                throw new IncorrectSyntaxException(String.format("OR-Phase must be in [1..127] range."
                        + "Current value is %s", value));
            }
            aos.writeInteger(tagClass, tag, value);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.INTEGER, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            this.value = (int) ais.readInteger();
            if (value < 1 && value > 127) {
                throw new IncorrectSyntaxException(String.format("OR-Phase must be in [1..127] range."
                        + "Current value is %s", value));
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }
}
