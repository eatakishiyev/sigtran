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
 * NAEA-CIC ::= OCTET STRING (SIZE (3))
 * -- The internal structure is defined by the Carrier Identification
 * -- parameter in ANSI T1.113.3. Carrier codes between “000” and “999” may
 * -- be encoded as 3 digits using “000” to “999” or as 4 digits using
 * -- “0000” to “0999”. Carrier codes between “1000” and “9999” are encoded
 * -- using 4 digits.
 * @author eatakishiyev
 */
public class NAEACIC {

    private byte[] naeaCIC;

    public NAEACIC() {
    }

    public NAEACIC(byte[] naeaCIC) {
        this.naeaCIC = naeaCIC;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeOctetString(tagClass, tag, naeaCIC);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try{
        this.naeaCIC = ais.readSequence();
        }catch(AsnException |IOException ex){
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the naeaCIC
     */
    public byte[] getNaeaCIC() {
        return naeaCIC;
    }

    /**
     * @param naeaCIC the naeaCIC to set
     */
    public void setNaeaCIC(byte[] naeaCIC) {
        this.naeaCIC = naeaCIC;
    }

}
