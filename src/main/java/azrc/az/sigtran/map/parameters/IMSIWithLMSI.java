/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class IMSIWithLMSI  implements MAPParameter{

    private IMSI imsi;
    private LMSI lmsi;

    public IMSIWithLMSI() {
    }

    public IMSIWithLMSI(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        this.decode(ais);
    }

    public IMSIWithLMSI(IMSI imsi, LMSI lmsi) {
        this.imsi = imsi;
        this.lmsi = lmsi;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            imsi.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            lmsi.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            aos.FinalizeContent(lenPos);

        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    public final void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    if (imsi == null) {
                        this.imsi = new IMSI(ais);
                    } else {
                        this.lmsi = new LMSI(ais);
                    }
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected tag received, Expecting Tag[4] Class[0], found Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public IMSI getImsi() {
        return imsi;
    }

    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    public LMSI getLmsi() {
        return lmsi;
    }

    public void setLmsi(LMSI lmsi) {
        this.lmsi = lmsi;
    }

}
