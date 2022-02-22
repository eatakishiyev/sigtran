/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * Ext-ForwFeatureList ::= SEQUENCE SIZE (1..maxNumOfExt-BasicServiceGroups) OF Ext-ForwFeature
 *
 * @author eatakishiyev
 */
public class ExtForwFeatureList implements MAPParameter {

    private final List<ExtForwFeature> extForwFeatures = new ArrayList<>();

    public ExtForwFeatureList() {
    }

    @Override
    public void decode(AsnInputStream ais) throws Exception {
        AsnInputStream _ais = ais.readSequenceStream();
        while (_ais.available() > 0) {
            int tag = _ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received. Expecting Tag[SEQUENCE] "
                        + "Class[UNIVERSAL]", tag, _ais.getTagClass()));
            }

            ExtForwFeature extForwFeature = new ExtForwFeature();
            extForwFeature.decode(_ais.readSequenceStream());
            extForwFeatures.add(extForwFeature);
        }

        if (extForwFeatures.size() > Constants.maxNumOfExtBasicServiceGroups) {
            throw new IncorrectSyntaxException("Index out of bounds. Expecting SEQUENCE SIZE (1..maxNumOfExt-BasicServiceGroups) OF\n"
                    + "Ext-ForwFeature. Found " + extForwFeatures.size());
        }
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws Exception {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        for (ExtForwFeature extForwFeature : extForwFeatures) {
            extForwFeature.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
        }
        aos.FinalizeContent(lenPos);
    }

    public List<ExtForwFeature> getExtForwFeatures() {
        return extForwFeatures;
    }

    public void addExtForwFeatures(ExtForwFeature extForwFeature) throws IncorrectSyntaxException {
        if (extForwFeatures.size() > Constants.maxNumOfExtBasicServiceGroups) {
            throw new IncorrectSyntaxException("Index out of bounds. Expecting SEQUENCE SIZE (1..maxNumOfExt-BasicServiceGroups) OF\n"
                    + "Ext-ForwFeature. Found " + extForwFeatures.size());
        }
        extForwFeatures.add(extForwFeature);
    }

}
