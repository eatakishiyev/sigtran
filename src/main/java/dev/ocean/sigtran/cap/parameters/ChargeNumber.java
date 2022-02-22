/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.isup.parameters.LocationNumber;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class ChargeNumber {

    private LocationNumber locationNumber;

    public ChargeNumber() {
    }

    public ChargeNumber(LocationNumber locationNumber) {
        this.locationNumber = locationNumber;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IllegalNumberFormatException, IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
        this.locationNumber.encode(baos);

        aos.writeTag(tagClass, true, tag);
        int lenPos = aos.StartContentDefiniteLength();
        aos.write(baos.toByteArray());
        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IllegalNumberFormatException, IncorrectSyntaxException {
        try {
            this.locationNumber = new LocationNumber();
            this.locationNumber.decode(new ByteArrayInputStream(ais.readOctetString()));
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public LocationNumber getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(LocationNumber locationNumber) {
        this.locationNumber = locationNumber;
    }

}
