/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * MobilityTriggers::= SEQUENCE SIZE (1..maxNumOfMobilityTriggers) OF
 * MM-Code
 * @author eatakishiyev
 */
public class MobilityTriggers {

    private final List<MMCode> mmCodes;

    public MobilityTriggers() {
        this.mmCodes = new ArrayList();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if (mmCodes.size() < 1
                || mmCodes.size() > Constants.maxNumOfMobilityTriggers) {
            throw new UnexpectedDataException("MMCode count must be in [1..10] range. Current count is " + mmCodes.size());
        }

        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (MMCode mmCode : mmCodes) {
                aos.writeOctetString(new byte[]{(byte) mmCode.getValue()});
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.STRING_OCTET
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    MMCode mmCode = MMCode.getInstance((int) ais.readOctetString()[0]);
                    mmCodes.add(mmCode);
                }
            }

            if (mmCodes.size() < 1
                    || mmCodes.size() > Constants.maxNumOfMobilityTriggers) {
                throw new UnexpectedDataException("MMCode count must be in [1..10] range. Current count is " + mmCodes.size());
            }

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public List<MMCode> getMmCodes() {
        return mmCodes;
    }
}
