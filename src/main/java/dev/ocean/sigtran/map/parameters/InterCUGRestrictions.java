/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class InterCUGRestrictions  implements MAPParameter{

//    InterCUG-Restrictions  ::= OCTET STRING (SIZE (1)) 
// 
//  -- bits 876543: 000000 (unused) 
//  -- Exception handling: 
//  -- bits 876543 shall be ignored if received and not understood 
// 
//  -- bits 21 
//  --  00  CUG only facilities 
//  --  01  CUG with outgoing access 
//  --  10  CUG with incoming access 
//  --  11  CUG with both outgoing and incoming access 
    private Restrictions restrictions;

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeOctetString(tagClass, tag, new byte[]{(byte) restrictions.getValue()});
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int firstOctet = ais.readOctetString()[0];
            this.restrictions = Restrictions.getInstance(firstOctet & 0x03);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the restrictions
     */
    public Restrictions getRestrictions() {
        return restrictions;
    }

    /**
     * @param restrictions the restrictions to set
     */
    public void setRestrictions(Restrictions restrictions) {
        this.restrictions = restrictions;
    }

    public enum Restrictions {

        CUGOnlyFacilities(0),
        CUGWithOutgoingAccess(1),
        CUGWithIncomingAccess(2),
        CUGWithBothAccess(3);
        private int value;

        private Restrictions(int value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        public static Restrictions getInstance(int value) {
            switch (value) {
                case 0:
                    return CUGOnlyFacilities;
                case 1:
                    return CUGWithOutgoingAccess;
                case 2:
                    return CUGWithIncomingAccess;
                case 3:
                    return CUGWithBothAccess;
                default:
                    return null;
            }
        }
    }
}
