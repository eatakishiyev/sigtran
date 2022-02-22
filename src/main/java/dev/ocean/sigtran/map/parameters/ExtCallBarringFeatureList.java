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
 * Ext-CallBarFeatureList ::= SEQUENCE SIZE (1..maxNumOfExt-BasicServiceGroups)
 * OF
 * Ext-CallBarringFeature
 * @author eatakishiyev
 */
public class ExtCallBarringFeatureList  implements MAPParameter {

    private final List<ExtCallBarringFeature> extCallBarringFeatures;

    public ExtCallBarringFeatureList() {
        this.extCallBarringFeatures = new ArrayList();
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            if (extCallBarringFeatures.size() < 1
                    || extCallBarringFeatures.size() > Constants.maxNumOfExtBasicServiceGroups) {
                throw new UnexpectedDataException("ExtCallBarringFeature count must be in [1..32] interval. Current length is " + extCallBarringFeatures.size());
            }

            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            for (ExtCallBarringFeature extCallBarringFeature : extCallBarringFeatures) {
                extCallBarringFeature.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }

    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.SEQUENCE
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    ExtCallBarringFeature extCallBarringFeature = new ExtCallBarringFeature();
                    extCallBarringFeature.decode(ais.readSequenceStream());
                    extCallBarringFeatures.add(extCallBarringFeature);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag received. Expecting Tag[SEQUENCE] Class[UNIVERSAL]"
                            + ". Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }

            if (extCallBarringFeatures.size() < 1
                    || extCallBarringFeatures.size() > Constants.maxNumOfExtBasicServiceGroups) {
                throw new UnexpectedDataException("ExtCallBarringFeature count must be in [1..32] interval. Current length is " + extCallBarringFeatures.size());
            }

        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the extCallBarringFeatures
     */
    public List<ExtCallBarringFeature> getExtCallBarringFeatures() {
        return extCallBarringFeatures;
    }
}
