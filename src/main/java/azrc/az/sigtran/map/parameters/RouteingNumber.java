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

/**
 * RouteingNumber ::= TBCD-STRING (SIZE (1..5))
 * @author eatakishiyev
 */
public class RouteingNumber extends TBCDString {

    private String value;

    public RouteingNumber() {
    }

    public RouteingNumber(String value) {
        this.value = value;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, true, tag);
            int lenPos = aos.StartContentDefiniteLength();
            super.encodeTBCD(aos);
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            super.decodeTBCD(ais);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public String getValue() {
        return this.value;
    }

}
