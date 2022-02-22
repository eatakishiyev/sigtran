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
 * O-BcsmCamelTDPCriteriaList::= SEQUENCE SIZE (1..maxNumOfCamelTDPData) OF
 * O-BcsmCamelTDP-Criteria
 * @author eatakishiyev
 */
public class OBcsmCamelTDPCriteriaList {

    private final List<OBcsmCamelTDPCriteria> oBcsmCamelTDPCriterias;

    public OBcsmCamelTDPCriteriaList() {
        this.oBcsmCamelTDPCriterias = new ArrayList();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if (oBcsmCamelTDPCriterias.size() < 1
                || oBcsmCamelTDPCriterias.size() > Constants.maxNumOfCamelTDPData) {
            throw new UnexpectedDataException("OBcsmCamelTDPCriteria count must be in [1..10] range. Current count is " + oBcsmCamelTDPCriterias.size());
        }
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (OBcsmCamelTDPCriteria oBcsmCamelTDPCriteria : oBcsmCamelTDPCriterias) {
                oBcsmCamelTDPCriteria.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.SEQUENCE
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    OBcsmCamelTDPCriteria oBcsmCamelTDPCriteria = new OBcsmCamelTDPCriteria();
                    oBcsmCamelTDPCriteria.decode(ais.readSequenceStream());
                    this.oBcsmCamelTDPCriterias.add(oBcsmCamelTDPCriteria);
                }
            }
            if (oBcsmCamelTDPCriterias.size() < 1
                    || oBcsmCamelTDPCriterias.size() > Constants.maxNumOfCamelTDPData) {
                throw new UnexpectedDataException("OBcsmCamelTDPCriteria count must be in [1..10] range. Current count is " + oBcsmCamelTDPCriterias.size());
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public List<OBcsmCamelTDPCriteria> getOBcsmCamelTDPCriterias() {
        return oBcsmCamelTDPCriterias;
    }

}
