/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import dev.ocean.isup.parameters.GenericNumber;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class AssistingSSPIPRoutingAddress {

    private GenericNumber digits;

    public AssistingSSPIPRoutingAddress() {
    }

    public AssistingSSPIPRoutingAddress(GenericNumber digits) {
        this.digits = digits;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IllegalNumberFormatException, IOException {
        aos.writeTag(tagClass, true, tag);
        int lenPos = aos.StartContentDefiniteLength();
        byte[] data = digits.encode();
        aos.write(data);
        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, IllegalNumberFormatException, AsnException {
        this.digits = new GenericNumber();
        digits.decode(ais.readOctetString());
    }

    /**
     * @return the digits
     */
    public GenericNumber getDigits() {
        return digits;
    }

    /**
     * @param digits the digits to set
     */
    public void setDigits(GenericNumber digits) {
        this.digits = digits;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AssistingSSPIPRoutingAddress[")
                .append(digits)
                .append("]");
        return sb.toString();
    }

}
