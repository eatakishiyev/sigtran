/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import dev.ocean.gsm.string.utils.bcd.BCDStringUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * IMSI ::= TBCD-STRING (SIZE (3..8))
 * -- digits of MCC, MNC, MSIN are concatenated in this order.
 *
 * @author eatakishiyev
 */
public class IMSI implements ISMRPAddress, MAPParameter {

    private String value;

    public IMSI() {
    }

    public IMSI(String value) {
        this.value = value;
    }

    public IMSI(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        this.decode(ais);
    }

    @Override
    public final void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            int length = ais.readLength();

            if (length < 3 || length > 8) {
                throw new UnexpectedDataException("Unexpected parameter length");
            }

            byte[] data = ais.readOctetStringData(length);
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            this.value = BCDStringUtils.fromTBCDToString(bais);

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());

        }
    }

    public final void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
    }

    public final void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            BCDStringUtils.toTBCDString(value, baos);

            byte[] data = baos.toByteArray();

            if (data.length < 3 || data.length > 8) {
                throw new IncorrectSyntaxException("Unexpected parameter length");
            }

            aos.writeOctetString(tagClass, tag, data);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the imsi
     */
    public String getValue() {
        return value;
    }

    /**
     * @param imsi the imsi to set
     */
    public void setValue(String imsi) {
        this.value = imsi;
    }

    @Override
    public String toString() {
        return String.format("Imsi=%s", this.value);
    }
}
