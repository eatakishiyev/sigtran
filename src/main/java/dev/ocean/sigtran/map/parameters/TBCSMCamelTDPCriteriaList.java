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
 * T-BCSM-CAMEL-TDP-CriteriaList::= SEQUENCE SIZE (1..maxNumOfCamelTDPData) OF
 * T-BCSM-CAMEL-TDP-Criteria
 * @author eatakishiyev
 */
public class TBCSMCamelTDPCriteriaList {

    private final List<TBCSMCamelTDPCriteria> tBCSMCamelTDPCriterias;

    public TBCSMCamelTDPCriteriaList() {
        this.tBCSMCamelTDPCriterias = new ArrayList();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if (tBCSMCamelTDPCriterias.size() < 1
                || tBCSMCamelTDPCriterias.size() > Constants.maxNumOfCamelTDPData) {
            throw new UnexpectedDataException("TBCSMCamelTDPCriteria count must be in [1..10] range. Current count is " + tBCSMCamelTDPCriterias.size());
        }

        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            for (TBCSMCamelTDPCriteria tBCSMCamelTDPCriteria : tBCSMCamelTDPCriterias) {
                tBCSMCamelTDPCriteria.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
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
                    TBCSMCamelTDPCriteria tBCSMCamelTDPCriteria = new TBCSMCamelTDPCriteria();
                    tBCSMCamelTDPCriteria.decode(ais.readSequenceStream());
                    tBCSMCamelTDPCriterias.add(tBCSMCamelTDPCriteria);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
            if (tBCSMCamelTDPCriterias.size() < 1
                    || tBCSMCamelTDPCriterias.size() > Constants.maxNumOfCamelTDPData) {
                throw new UnexpectedDataException("TBCSMCamelTDPCriteria count must be in [1..10] range. Current count is " + tBCSMCamelTDPCriterias.size());
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public List<TBCSMCamelTDPCriteria> getTBCSMCamelTDPCriteria() {
        return tBCSMCamelTDPCriterias;
    }

}
