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
 * CUG-FeatureList ::= SEQUENCE SIZE (1..maxNumOfExt-BasicServiceGroups) OF
 * CUG-Feature
 * @author eatakishiyev
 */
public class CUGFeatureList  implements MAPParameter{

    private final List<CUGFeature> cUGFeatures;

    public CUGFeatureList() {
        this.cUGFeatures = new ArrayList();
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.SEQUENCE
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    CUGFeature cUGFeature = new CUGFeature();
                    cUGFeature.decode(ais.readSequenceStream());
                    cUGFeatures.add(cUGFeature);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected tag received."
                            + "Expecting Tag[SEQUENCE] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
            if (cUGFeatures.size() < 1
                    || cUGFeatures.size() > Constants.maxNumOfExtBasicServiceGroups) {
                throw new UnexpectedDataException("CUGFeatureList count must be in [1..32] interval. Current count is " + cUGFeatures);
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws UnexpectedDataException, IncorrectSyntaxException {
        if (cUGFeatures.size() < 1
                || cUGFeatures.size() > Constants.maxNumOfExtBasicServiceGroups) {
            throw new UnexpectedDataException("CUGFeatureList count must be in [1..32] interval. Current count is " + cUGFeatures);
        }

        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            for (CUGFeature cugFeature : cUGFeatures) {
                cugFeature.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the cUGFeatures
     */
    public List<CUGFeature> getcUGFeatures() {
        return cUGFeatures;
    }
}
