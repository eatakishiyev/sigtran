/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;

/**
 * RequestedEquipmentInfo::= BIT STRING {
 * equipmentStatus (0),
 * bmuef (1)} (SIZE (2..8))
 * -- exception handling: reception of unknown bit assignments in the
 * -- RequestedEquipmentInfo data type shall be discarded by the receiver
 *
 * @author eatakishiyev
 */
public class RequestedEquipmentInfo {

    private BitSetStrictLength requestedEquipmentInfo = new BitSetStrictLength(8);

    public RequestedEquipmentInfo() {
    }

    
    
    public RequestedEquipmentInfo(boolean equipmentStatus, boolean bmuef) {
        requestedEquipmentInfo.set(0, equipmentStatus);
        requestedEquipmentInfo.set(1, bmuef);
    }

    public boolean isEquipmentStatus() {
        return requestedEquipmentInfo.get(0);
    }

    public boolean isBmuef() {
        return requestedEquipmentInfo.get(1);
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeBitString(tagClass, tag, requestedEquipmentInfo);
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeBitString(requestedEquipmentInfo);
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        this.requestedEquipmentInfo = ais.readBitString();
    }
}
