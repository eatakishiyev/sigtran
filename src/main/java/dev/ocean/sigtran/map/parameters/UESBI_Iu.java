/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * UESBI-Iu ::= SEQUENCE {
 * uesbi-IuA [0] UESBI-IuA OPTIONAL,
 * uesbi-IuB [1] UESBI-IuB OPTIONAL,
 * ...}
 *
 * @author eatakishiyev
 */
public class UESBI_Iu implements MAPParameter {

    private byte[] uesbi_IuA;
    private byte[] uesbi_IuB;

    public UESBI_Iu() {
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws Exception {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        if (uesbi_IuA != null) {
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 0, uesbi_IuA);
        }

        if (uesbi_IuB != null) {
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 1, uesbi_IuB);
        }

        aos.FinalizeContent(lenPos);
    }

    @Override
    public void decode(AsnInputStream ais) throws Exception {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case 0:
                        this.uesbi_IuA = ais.readOctetString();
                        break;
                    case 1:
                        this.uesbi_IuB = ais.readOctetString();
                        break;
                }
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] found. Expecting Class[CONTEXT]", tag, ais.getTagClass()));
            }
        }
    }

    public byte[] getUesbi_IuA() {
        return uesbi_IuA;
    }

    public void setUesbi_IuA(byte[] uesbi_IuA) {
        this.uesbi_IuA = uesbi_IuA;
    }

    public byte[] getUesbi_IuB() {
        return uesbi_IuB;
    }

    public void setUesbi_IuB(byte[] uesbi_IuB) {
        this.uesbi_IuB = uesbi_IuB;
    }

}
