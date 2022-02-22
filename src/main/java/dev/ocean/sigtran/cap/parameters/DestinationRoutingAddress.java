/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import dev.ocean.isup.parameters.CalledPartyNumber;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class DestinationRoutingAddress implements Serializable {

    private CalledPartyNumber calledPartyNumber;

    public DestinationRoutingAddress() {
    }

    public DestinationRoutingAddress(CalledPartyNumber calledPartyNumber) {
        this.calledPartyNumber = calledPartyNumber;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            baos.write(this.calledPartyNumber.encode());

            aos.writeTag(Tag.CLASS_UNIVERSAL, true, Tag.STRING_OCTET);
            int lenPos_ = aos.StartContentDefiniteLength();
            aos.write(baos.toByteArray());
            aos.FinalizeContent(lenPos_);

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IllegalNumberFormatException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, IllegalNumberFormatException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this.decode_(tmpAis);
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException, IllegalNumberFormatException {
        int tag = ais.readTag();
        if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
            throw new AsnException(String.format("Unexpected tag received. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
        }

        byte[] data = new byte[ais.readLength()];
        ais.read(data);
        this.calledPartyNumber = new CalledPartyNumber();
        this.calledPartyNumber.decode(data);
    }

    /**
     * @return the calledPartyNumber
     */
    public CalledPartyNumber getCalledPartyNumber() {
        return calledPartyNumber;
    }

    /**
     * @param calledPartyNumber the calledPartyNumber to set
     */
    public void setCalledPartyNumber(CalledPartyNumber calledPartyNumber) {
        this.calledPartyNumber = calledPartyNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DestinationRoutingAddress [").append(calledPartyNumber).append("]\n");
        return sb.toString();
    }
}
