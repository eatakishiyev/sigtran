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
 * CellGlobalIdOrServiceAreaIdFixedLength ::= OCTET STRING (SIZE (7)) -- Refers
 * to Cell Global Identification or Service Are Identification -- defined in
 * 3GPP TS 23.003. -- The internal structure is defined as follows: -- octet 1
 * bits 4321 Mobile Country Code 1st digit -- bits 8765 Mobile Country Code 2nd
 * digit -- octet 2 bits 4321 Mobile Country Code 3rd digit -- bits 8765 Mobile
 * Network Code 3rd digit -- or filler (1111) for 2 digit MNCs -- octet 3 bits
 * 4321 Mobile Network Code 1st digit -- bits 8765 Mobile Network Code 2nd digit
 * -- octets 4 and 5 Location Area Code according to 3GPP TS 24.008 -- octets 6
 * and 7 Cell Identity (CI) value or -- Service Area Code (SAC) value --
 * according to 3GPP TS 23.003
 *
 * @author eatakishiyev
 */
public class CellGlobalIdOrServiceAreaIdFixedLength implements MAPParameter {

    private CGI cgi;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CellGlobalIdOrServiceAreaIdFixedLength[")
                .append(cgi)
                .append("]");
        return sb.toString();
    }

    public CellGlobalIdOrServiceAreaIdFixedLength() {
    }

    public CellGlobalIdOrServiceAreaIdFixedLength(CGI cgi) {
        this.cgi = cgi;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IOException, AsnException {
        aos.writeOctetString(tagClass, tag, cgi.encode());
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        byte[] octetData = ais.readOctetString();
        this.cgi = new CGI(octetData);
    }

    public void decode(AsnInputStream ais, int length) throws AsnException, IOException {
        byte[] octetData = ais.readOctetStringData(length);
        this.cgi = new CGI(octetData);
    }

    public CGI getCgi() {
        return cgi;
    }

    public void setCgi(CGI cgi) {
        this.cgi = cgi;
    }
}
