/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * LAIFixedLength ::= OCTET STRING (SIZE (5)) -- Refers to Location Area
 * Identification defined in 3GPP TS 23.003 [17]. -- The internal structure is
 * defined as follows: -- octet 1 bits 4321 Mobile Country Code 1st digit --
 * bits 8765 Mobile Country Code 2nd digit -- octet 2 bits 4321 Mobile Country
 * Code 3rd digit -- bits 8765 Mobile Network Code 3rd digit -- or filler (1111)
 * for 2 digit MNCs -- octet 3 bits 4321 Mobile Network Code 1st digit -- bits
 * 8765 Mobile Network Code 2nd digit -- octets 4 and 5 Location Area Code
 * according to 3GPP TS 24.008 [35]
 *
 * @author eatakishiyev
 */
public class LAIFixedLength implements MAPParameter {

    //CellGlobalIdOrServiceAreaIdFixedLength extends this class which is with cellId. Because of this used NONE_CELL_ID
    // to distingues if encoding/decoding for LAIFixedLength class or CellGlobalIdOrServiceAreaIdFixedLength. 
    // Thanks for understanding :)
    private LAI lai;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LAIFixedLength[")
                .append(lai)
                .append("]");
        return sb.toString();
    }

    public LAIFixedLength() {
    }

    public LAIFixedLength(LAI lai) {
        this.lai = lai;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeOctetString(tagClass, tag, lai.encode());
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        byte[] octetData = ais.readOctetString();
        this.lai = new LAI(octetData);
    }

    public void decode(AsnInputStream ais, int length) throws AsnException, IOException {
        byte[] octetData = ais.readOctetStringData(length);
        this.lai = new LAI(octetData);
    }

    /**
     * @return the lai
     */
    public LAI getLai() {
        return lai;
    }

    /**
     * @param lai the lai to set
     */
    public void setLai(LAI lai) {
        this.lai = lai;
    }

}
