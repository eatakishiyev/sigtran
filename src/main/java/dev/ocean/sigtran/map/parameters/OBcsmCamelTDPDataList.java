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
 * O-BcsmCamelTDPDataList::= SEQUENCE SIZE (1..maxNumOfCamelTDPData) OF
 * O-BcsmCamelTDPData
 * -- O-BcsmCamelTDPDataListshall not contain more than one instance of
 * -- O-BcsmCamelTDPDatacontaining the same value for
 * o-BcsmTriggerDetectionPoint.
 * -- For CAMEL Phase 2, this means that only one instance of
 * O-BcsmCamelTDPDatais allowed
 * -- with o-BcsmTriggerDetectionPointbeing equal to DP2.
 * @author eatakishiyev
 */
public class OBcsmCamelTDPDataList {

    private final List<OBcsmCamelTDPData> obcsmCamelTDPDatas;
    public final int maxNumOfCamelTDPData = 10;

    public OBcsmCamelTDPDataList() {
        this.obcsmCamelTDPDatas = new ArrayList();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws UnexpectedDataException, IncorrectSyntaxException {
        if (obcsmCamelTDPDatas.size() < 1
                && obcsmCamelTDPDatas.size() > Constants.maxNumOfCamelTDPData) {
            throw new UnexpectedDataException(String.format("OBCSMCamelTDPData count must be in [1..10] range. Current count is " + obcsmCamelTDPDatas.size()));
        }
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (OBcsmCamelTDPData oBcsmCamelTDPData : obcsmCamelTDPDatas) {
                oBcsmCamelTDPData.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
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
                    OBcsmCamelTDPData oBcsmCamelTDPData = new OBcsmCamelTDPData();
                    oBcsmCamelTDPData.decode(ais.readSequenceStream());
                    this.obcsmCamelTDPDatas.add(oBcsmCamelTDPData);
                }
            }
            if (obcsmCamelTDPDatas.size() < 1
                    && obcsmCamelTDPDatas.size() > Constants.maxNumOfCamelTDPData) {
                throw new UnexpectedDataException(String.format("OBCSMCamelTDPData count must be in [1..10] range. Current count is " + obcsmCamelTDPDatas.size()));
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public List<OBcsmCamelTDPData> getObcsmCamelTDPDatas() {
        return obcsmCamelTDPDatas;
    }

}
