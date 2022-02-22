/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class ZoneCode {

    public byte[] zoneCode;

    public ZoneCode() {
    }

    public ZoneCode(byte[] zoneCode) throws UnexpectedDataException {
        if (zoneCode.length != 2) {
            throw new UnexpectedDataException(String.format("Unexpected zone code value received. Max size = 2. Received = %s", zoneCode.length));
        }

        this.zoneCode = zoneCode;
    }

    public final void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeOctetString(tagClass, tag, zoneCode);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public final void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int length = ais.readLength();
            if (length != 2) {
                throw new UnexpectedDataException(String.format("Unexpected zone code value received. Max size = 2. Received = %s", length));
            }
            this.zoneCode = ais.readOctetStringData(length);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public byte[] getZoneCode() {
        return zoneCode;
    }
}
