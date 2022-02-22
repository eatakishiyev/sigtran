/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * T-BcsmCamelTDPDataList ::= SEQUENCE SIZE (1..maxNumOfCamelTDPData) OF
 * T-BcsmCamelTDPData
 * --- T-BcsmCamelTDPDataList shall not contain more than one instance of
 * --- T-BcsmCamelTDPData containing the same value for
 * t-BcsmTriggerDetectionPoint.
 * --- For CAMEL Phase 2, this means that only one instance of
 * T-BcsmCamelTDPData is allowed
 * --- with t-BcsmTriggerDetectionPoint being equal to DP12.
 * --- For CAMEL Phase 3, more TDP’s are allowed.
 * @author eatakishiyev
 */
public class TBcsmCamelTDPDataList {

    private final List<TBcsmCamelTDPData> tBCSMCamelTDPDatas;

    public TBcsmCamelTDPDataList() {
        this.tBCSMCamelTDPDatas = new ArrayList();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if (tBCSMCamelTDPDatas.size() < 1
                || tBCSMCamelTDPDatas.size() > Constants.maxNumOfCamelTDPData) {
            throw new UnexpectedDataException("TBCSMCamelTDPData count must be in [1..10] range. Current count is " + tBCSMCamelTDPDatas.size());
        }

        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (TBcsmCamelTDPData tBcsmCamelTDPData : tBCSMCamelTDPDatas) {
                tBcsmCamelTDPData.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
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
                    TBcsmCamelTDPData tBcsmCamelTDPData = new TBcsmCamelTDPData();
                    tBcsmCamelTDPData.decode(ais.readSequenceStream());
                    tBCSMCamelTDPDatas.add(tBcsmCamelTDPData);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
            if (tBCSMCamelTDPDatas.size() < 1
                    || tBCSMCamelTDPDatas.size() > Constants.maxNumOfCamelTDPData) {
                throw new UnexpectedDataException("TBCSMCamelTDPData count must be in [1..10] range. Current count is " + tBCSMCamelTDPDatas.size());
            }

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public List<TBcsmCamelTDPData> getTBCSMCamelTDPDatas() {
        return tBCSMCamelTDPDatas;
    }

}
