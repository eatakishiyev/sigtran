/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * DP-AnalysedInfoCriteriaList ::= SEQUENCE SIZE
 * (1..maxNumOfDP-AnalysedInfoCriteria) OF
 * DP-AnalysedInfoCriterium
 * @author eatakishiyev
 */
public class DPAnayzedInfoCriteriaList  implements MAPParameter{

    private final List<DPAnalyzedInfoCriterium> analyzedInfoCriteriums;

    public DPAnayzedInfoCriteriaList() {
        this.analyzedInfoCriteriums = new ArrayList();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if (analyzedInfoCriteriums.size() < 1
                || analyzedInfoCriteriums.size() > Constants.maxNumOfDPAnalysedInfoCriteria) {
            throw new UnexpectedDataException("DPAnalyzedInfoCriterium count must be in [1..10] range. Current count is " + analyzedInfoCriteriums.size());
        }
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (DPAnalyzedInfoCriterium analyzedInfoCriterium : analyzedInfoCriteriums) {
                analyzedInfoCriterium.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
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
                if (tag == Tag.SEQUENCE
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    DPAnalyzedInfoCriterium analyzedInfoCriterium = new DPAnalyzedInfoCriterium();
                    analyzedInfoCriterium.decode(ais.readSequenceStream());
                    this.analyzedInfoCriteriums.add(analyzedInfoCriterium);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
            if (analyzedInfoCriteriums.size() < 1
                    || analyzedInfoCriteriums.size() > Constants.maxNumOfDPAnalysedInfoCriteria) {
                throw new UnexpectedDataException("DPAnalyzedInfoCriterium count must be in [1..10] range. Current count is " + analyzedInfoCriteriums.size());
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the anayzedInfoCriteriums
     */
    public List<DPAnalyzedInfoCriterium> getAnayzedInfoCriteriums() {
        return analyzedInfoCriteriums;
    }

}
