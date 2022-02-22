/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.utils.ByteUtils;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * * The Local Mobile Station Identity (LMSI) is a temporarily identification
 * that can be assigned to a mobile station that visits another network than
 * its home network. This supplementary LMSI may be assigned in the case where
 * the roaming telephone number (MSRN) is allocated on a call-by-call basis.
 * In these cases a LMSI can be used to speed up the search for the subscriber
 * data in the visitors location register (VLR).
 *
 * The LMSI is allocated by the VLR if the location is updated and is sent to
 * the home location register (HLR) together with the subscriber identity (IMSI).
 * However, the HLR does not make use of the LMSI. It includes it together with
 * the IMSI in all messages sent to the VLR concerning that mobile station.
 *
 * @author eatakishiyev
 */
public class LMSI implements ISMRPAddress {

    private byte[] lmsi;

    public LMSI() {

    }

    public LMSI(byte[] lmsi) {
        this.lmsi = lmsi;
    }

    public LMSI(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        this.decode(ais);
    }

    public final void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            if (lmsi.length > 4) {
                throw new UnexpectedDataException("Unexpected parameter length");
            }

            aos.writeOctetString(tagClass, tag, lmsi);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public final void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            int length = ais.readLength();
            if (length > 4) {
                throw new UnexpectedDataException("Unexpected parameter length");
            }
            lmsi = ais.readOctetStringData(length);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public String toString() {
        return String.format("Lmsi=%s", ByteUtils.bytes2Hex(lmsi));
    }
}
