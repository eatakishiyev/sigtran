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

/**
 * SEQUENCE SIZE (1..maxNumOfExt-BasicServiceGroups) OF
 * Ext-BasicServiceCode
 * @author eatakishiyev
 */
public class ExtBasicServiceGroupList  implements MAPParameter {

    private final List<ExtBasicServiceCode> extBasicServiceCodes;

    public ExtBasicServiceGroupList() {
        this.extBasicServiceCodes = new ArrayList();
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
                        extBasicServiceCodes.add(extBasicServiceCode);
                        break;
                    case ExtBasicServiceCode.EXT_TELE_SERVICE_TAG:
                        ExtTeleServiceCode extTeleServiceCode = new ExtTeleServiceCode();
                        extTeleServiceCode.decode(ais);
                        extBasicServiceCode = new ExtBasicServiceCode();
                        extBasicServiceCode.setExtTeleService(extTeleServiceCode);
                        extBasicServiceCodes.add(extBasicServiceCode);
                        break;
                }
            }
            if (extBasicServiceCodes.size() < 1
                    || extBasicServiceCodes.size() > Constants.maxNumOfExtBasicServiceGroups) {
                throw new UnexpectedDataException("ExtBasicServiceCode count must be in [1..32] range. Current count is " + extBasicServiceCodes.size());
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            if (extBasicServiceCodes.size() < 1
                    || extBasicServiceCodes.size() > Constants.maxNumOfExtBasicServiceGroups) {
                throw new UnexpectedDataException("ExtBasicServiceCode count must be in [1..32] range. Current count is " + extBasicServiceCodes.size());
            }

            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            for (ExtBasicServiceCode extBasicServiceCode : extBasicServiceCodes) {
                extBasicServiceCode.encode(aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the extBasicServiceCodes
     */
    public List<ExtBasicServiceCode> getExtBasicServiceCodes() {
        return extBasicServiceCodes;
    }
}
