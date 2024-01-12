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
 * TPDU-TypeCriterion::= SEQUENCE SIZE (1..maxNumOfTPDUTypes) OF
 * MT-SMS-TPDU-Type
 * @author eatakishiyev
 */
public class TPDUTypeCriterion {

    private final List<MTSMSTPDUType> mTSMSTPDUTypes;

    public TPDUTypeCriterion() {
        this.mTSMSTPDUTypes = new ArrayList();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if (mTSMSTPDUTypes.size() < 1
                || mTSMSTPDUTypes.size() > Constants.maxNumOfTPDUTypes) {
            throw new UnexpectedDataException("MTSMSTPDUType count must be in [1..5] range. Current count is " + mTSMSTPDUTypes.size());
        }
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            for (MTSMSTPDUType mTSMSTPDUType : mTSMSTPDUTypes) {
                aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, mTSMSTPDUType.getValue());
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.ENUMERATED
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    MTSMSTPDUType mtsmstpdut = MTSMSTPDUType.getInstance((int) ais.readInteger());
                    this.mTSMSTPDUTypes.add(mtsmstpdut);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[ENUMERATED] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
            if (mTSMSTPDUTypes.size() < 1
                    || mTSMSTPDUTypes.size() > Constants.maxNumOfTPDUTypes) {
                throw new UnexpectedDataException("MTSMSTPDUType count must be in [1..5] range. Current count is " + mTSMSTPDUTypes.size());
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the mTSMSTPDUTypes
     */
    public List<MTSMSTPDUType> getmTSMSTPDUTypes() {
        return mTSMSTPDUTypes;
    }

}
