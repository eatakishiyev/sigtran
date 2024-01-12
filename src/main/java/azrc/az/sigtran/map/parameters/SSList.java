/*
 * To change this template, choose Tools | Templates
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
 * SS-List ::= SEQUENCE SIZE (1..maxNumOfSS) OF
 * SS-Code
 * @author eatakishiyev
 */
public class SSList {

    private final List<SSCode> sSCodes;

    public SSList() {
        sSCodes = new ArrayList();
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag != Tag.STRING_OCTET
                        && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                    throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting Tag[OCTET_STRING] CLASS[UNIVERSAL] found Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }

                SSCode sSCode = new SSCode();
                sSCode.decode(ais);
                sSCodes.add(sSCode);
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if ((sSCodes.size() < 1) || (sSCodes.size() > 30)) {
            throw new UnexpectedDataException("SSCodeList size must be between 1 and 30. Current size " + sSCodes.size());
        }

        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            for (SSCode ssCode : sSCodes) {
                ssCode.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    /**
     * @return the sSCodes
     */
    public List<SSCode> getsSCodes() {
        return sSCodes;
    }
}
