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

/**
 * GroupId ::= TBCD-STRING (SIZE (3))
 * -- When Group-Id is less than six characters in length, the TBCD filler
 * (1111)
 * -- is used to fill unused half octets.
 * -- Refers to the Group Identification as specified in 3GPP TS 23.003
 * -- and 3GPP TS 43.068/ 43.069
 *
 * @author eatakishiyev
 */
public class GroupId extends TBCDString implements MAPParameter {

    private String value;

    public GroupId() {
    }

    public GroupId(String value) {
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
