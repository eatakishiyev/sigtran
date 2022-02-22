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

/**
 * BasicServiceList ::= SEQUENCE SIZE (1..maxNumOfBasicServices) OF
 * Ext-BasicServiceCode
 *
 * @author eatakishiyev
 */
public class BasicServiceList implements MAPParameter {

    private final List<ExtBasicServiceCode> basicServiceCodes;

    public BasicServiceList() {
        this.basicServiceCodes = new ArrayList();
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();

                switch (tag) {
                    case ExtBasicServiceCode.EXT_BEARER_SERVICE_TAG:
                        ExtBearerServiceCode extBearerServiceCode = new ExtBearerServiceCode();
                        extBearerServiceCode.decode(ais);
                        ExtBasicServiceCode extBasicServiceCode = new ExtBasicServiceCode();
                        extBasicServiceCode.setExtBearerService(extBearerServiceCode);
                        basicServiceCodes.add(extBasicServiceCode);
                        break;
                    case ExtBasicServiceCode.EXT_TELE_SERVICE_TAG:
                        ExtTeleServiceCode extTeleServiceCode = new ExtTeleServiceCode();
                        extTeleServiceCode.decode(ais);
                        extBasicServiceCode = new ExtBasicServiceCode();
                        extBasicServiceCode.setExtTeleService(extTeleServiceCode);
                        basicServiceCodes.add(extBasicServiceCode);
                        break;
                }
            }
            if (basicServiceCodes.size() < 1
                    || basicServiceCodes.size() > Constants.maxNumOfBasicServices) {
                throw new UnexpectedDataException(String.format("BasicServiceCodes count must be in [1..70] range. Current count is ", basicServiceCodes.size()));
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if (basicServiceCodes.size() < 1
                || basicServiceCodes.size() > Constants.maxNumOfBasicServices) {
            throw new UnexpectedDataException(String.format("BasicServiceCodes count must be in [1..70] range. Current count is ", basicServiceCodes.size()));
        }

        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (ExtBasicServiceCode extBasicServiceCode : basicServiceCodes) {
                extBasicServiceCode.encode(aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }
//
//    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
//        for (ExtBasicServiceCode extBasicServiceCode : basicServiceCodes) {
//            extBasicServiceCode.encode(aos);
//        }
//    }

    /**
     * @return the basicServiceCodes
     */
    public List<ExtBasicServiceCode> getBasicServiceCodes() {
        return basicServiceCodes;
    }
}
