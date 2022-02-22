/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import java.io.Serializable;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * LSAIdentity ::= OCTET STRING (SIZE (3))
 * -- Octets are coded according to TS 3GPP TS 23.003 [17]
 * @author eatakishiyev
 */
public class LSAIdentity  implements MAPParameter {

    private int lsaIdentity;
    private Type type;

    public LSAIdentity() {
    }

    public LSAIdentity(int lsaIdentity, Type type) {
        this.lsaIdentity = lsaIdentity;
        this.type = type;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IOException, AsnException {
        byte[] tmpBuf = new byte[3];

        tmpBuf[0] = (byte) ((lsaIdentity >> 16) & 0b11111111);
        tmpBuf[1] = (byte) ((lsaIdentity >> 8) & 0b11111111);
        tmpBuf[3] = (byte) ((lsaIdentity & 0b11111110) | (this.type.value() & 0b00000001));
        
        aos.writeOctetString(tagClass, tag, tmpBuf);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            byte[] tmpBuf = ais.readOctetString();
            this.lsaIdentity = tmpBuf[0] & 0xFF;
            this.lsaIdentity = (lsaIdentity << 8) | (tmpBuf[1] & 0xFF);

            int tmp = tmpBuf[3] & 0xFF;
            this.lsaIdentity = (lsaIdentity << 7) | (tmp >> 1);
            this.type = Type.getInstance(tmp & 0b00000001);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the lsaIdentity
     */
    public int getLsaIdentity() {
        return lsaIdentity;
    }

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    public enum Type {

        PLMN_SIGNIFICANT_NUMBER(0),
        UNIVERSAL_LSA(1),
        UNKNOWN(-1);

        private int value;

        private Type(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static Type getInstance(int value) {
            switch (value) {
                case 0:
                    return PLMN_SIGNIFICANT_NUMBER;
                case 1:
                    return UNIVERSAL_LSA;
                default:
                    return UNKNOWN;
            }
        }
    }
}
