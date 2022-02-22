/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class BearerServiceList implements MAPParameter {

//      BearerServiceList ::= SEQUENCE SIZE (1..maxNumOfBearerServices) OF
//      Ext-BearerServiceCode   
    private final List<ExtBearerServiceCode> extBearerBasicServiceCodes;

    public BearerServiceList() {
        this.extBearerBasicServiceCodes = new ArrayList();
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        if (extBearerBasicServiceCodes != null
                && ((extBearerBasicServiceCodes.size() < 1) && (extBearerBasicServiceCodes.size() > Constants.maxNumOfBearerServices))) {
            throw new IncorrectSyntaxException("BearerServiceList size must be between 1 and 50. Current size is:" + extBearerBasicServiceCodes.size());
        }

        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            for (ExtBearerServiceCode extBearerServiceCode : extBearerBasicServiceCodes) {
                extBearerServiceCode.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag != Tag.STRING_OCTET
                        && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                    throw new IncorrectSyntaxException(String.format("Unexpected tag received. Expecting Tag[SEQUENCE] CLASS[UNIVERSLA] found Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }

                ExtBearerServiceCode extBearerServiceCode = new ExtBearerServiceCode();
                extBearerServiceCode.decode(ais);
                extBearerBasicServiceCodes.add(extBearerServiceCode);
            }

            if (extBearerBasicServiceCodes.size() > Constants.maxNumOfBearerServices) {
                throw new IncorrectSyntaxException("BearerServiceList size must be between 1 and 50. Current size is:" + extBearerBasicServiceCodes.size());
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the extBeaBasicServiceCode
     */
    public List<ExtBearerServiceCode> getExtBeaBasicServiceCode() {
        return extBearerBasicServiceCodes;
    }
}
