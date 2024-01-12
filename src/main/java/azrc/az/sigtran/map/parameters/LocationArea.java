/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * LocationArea ::= CHOICE {
 * laiFixedLength [0] LAIFixedLength,
 * lac [1] LAC}
 *
 * @author eatakishiyev
 */
public class LocationArea {

    private LAIFixedLength lAIFixedLength;
    private LAC lac;

    public LocationArea() {
    }

    public LocationArea(LAIFixedLength lAIFixedLength) {
        this.lAIFixedLength = lAIFixedLength;
    }

    public LocationArea(LAC lac) {
        this.lac = lac;
    }
//
//    public void encode(int tagClass, int tag, AsnOutputStream aos) {
//
//    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            if (lAIFixedLength != null) {
                lAIFixedLength.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            } else {
                lac.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int tag = ais.readTag();
            this.decode(ais, tag);
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais, int tag) throws IncorrectSyntaxException {
        try {
            if (tag == 0 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.lAIFixedLength = new LAIFixedLength();
                lAIFixedLength.decode(ais);
            } else if (tag == 1 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.lac = new LAC();
                lac.decode(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, ais.getTagClass()));
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public LAC getLac() {
        return lac;
    }

    public LAIFixedLength getlAIFixedLength() {
        return lAIFixedLength;
    }

}
