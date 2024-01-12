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
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;

/**
 * AllowedServices ::= BIT STRING {
 * firstServiceAllowed (0),
 * secondServiceAllowed (1) } (SIZE (2..8))
 * -- firstService is the service indicated in the networkSignalInfo
 * -- secondService is the service indicated in the networkSignalInfo2
 * -- Other bits than listed above shall be discarded
 *
 * @author eatakishiyev
 */
public class AllowedServices implements MAPParameter {

    private boolean firsServiceAllowed;
    private boolean secondServiceAllowed;

    public AllowedServices() {
    }

    public AllowedServices(boolean firsServiceAllowed, boolean secondServiceAllowed) {
        this.firsServiceAllowed = firsServiceAllowed;
        this.secondServiceAllowed = secondServiceAllowed;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            BitSetStrictLength bitSetStrictLength = new BitSetStrictLength(2);
            bitSetStrictLength.set(0, firsServiceAllowed);
            bitSetStrictLength.set(1, secondServiceAllowed);
            aos.writeBitString(tagClass, tag, bitSetStrictLength);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_BIT, aos);
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            BitSetStrictLength bitSetStrictLength = ais.readBitString();
            this.firsServiceAllowed = bitSetStrictLength.get(0);
            this.secondServiceAllowed = bitSetStrictLength.get(1);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public boolean isFirsServiceAllowed() {
        return firsServiceAllowed;
    }

    public boolean isSecondServiceAllowed() {
        return secondServiceAllowed;
    }

}
