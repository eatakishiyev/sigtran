/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * TeleserviceList ::= SEQUENCE SIZE (1..maxNumOfTeleservices) OF
 * Ext-TeleserviceCode
 *
 * @author eatakishiyev
 *
 *
 */
public class TeleServiceList {

    private final List<ExtTeleServiceCode> extTeleServiceCodes;

    public TeleServiceList() {
        this.extTeleServiceCodes = new ArrayList();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            if ((extTeleServiceCodes.size() < 1
                    || extTeleServiceCodes.size() > Constants.maxNumOfTeleservices)) {
                throw new UnexpectedDataException("TeleServiceList size must be in range [1..20]. Current size is " + extTeleServiceCodes.size());
            }

            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            for (ExtTeleServiceCode extTeleServiceCode : extTeleServiceCodes) {
                extTeleServiceCode.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.STRING_OCTET
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    ExtTeleServiceCode extTeleServiceCode = new ExtTeleServiceCode();
                    extTeleServiceCode.decode(ais);
                    extTeleServiceCodes.add(extTeleServiceCode);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting Tag[OCTET] Class[UNIVERSAL]."
                            + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }

            if ((extTeleServiceCodes.size() < 1
                    && extTeleServiceCodes.size() > Constants.maxNumOfTeleservices)) {
                throw new UnexpectedDataException("TeleServiceList size must be in range [1..20]. Current size is " + extTeleServiceCodes.size());
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the extTeleServiceCodes
     */
    public List<ExtTeleServiceCode> getExtTeleServiceCodes() {
        return extTeleServiceCodes;
    }
}
