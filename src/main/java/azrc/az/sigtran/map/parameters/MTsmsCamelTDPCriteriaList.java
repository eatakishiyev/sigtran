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
 * MT-smsCAMELTDP-CriteriaList ::= SEQUENCE SIZE (1.. maxNumOfCamelTDPData) OF
 * MT-smsCAMELTDP-Criteria
 * @author eatakishiyev
 */
public class MTsmsCamelTDPCriteriaList {

    private final List<MTsmsCamelTDPCriteria> mTsmsCamelTDPCriterias;

    public MTsmsCamelTDPCriteriaList() {
        this.mTsmsCamelTDPCriterias = new ArrayList();
    }

    public MTsmsCamelTDPCriteriaList(List<MTsmsCamelTDPCriteria> mTsmsCamelTDPCriterias) {
        this.mTsmsCamelTDPCriterias = mTsmsCamelTDPCriterias;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if (mTsmsCamelTDPCriterias.size() < 1
                || mTsmsCamelTDPCriterias.size() > Constants.maxNumOfCamelTDPData) {
            throw new UnexpectedDataException(String.format("MTsmsCamelTDPCriteria count must be in [1..10] range. Current count is " + mTsmsCamelTDPCriterias.size()));
        }
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (MTsmsCamelTDPCriteria mTsmsCamelTDPCriteria : mTsmsCamelTDPCriterias) {
                mTsmsCamelTDPCriteria.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
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
                    MTsmsCamelTDPCriteria mTsmsCamelTDPCriteria = new MTsmsCamelTDPCriteria();
                    mTsmsCamelTDPCriteria.decode(ais.readSequenceStream());
                    this.mTsmsCamelTDPCriterias.add(mTsmsCamelTDPCriteria);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
        if (mTsmsCamelTDPCriterias.size() < 1
                || mTsmsCamelTDPCriterias.size() > Constants.maxNumOfCamelTDPData) {
            throw new UnexpectedDataException(String.format("MTsmsCamelTDPCriteria count must be in [1..10] range. Current count is " + mTsmsCamelTDPCriterias.size()));
        }

    }

    /**
     * @return the mTsmsCamelTDPCriterias
     */
    public List<MTsmsCamelTDPCriteria> getmTsmsCamelTDPCriterias() {
        return mTsmsCamelTDPCriterias;
    }

}
