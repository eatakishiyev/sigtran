/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import java.io.IOException;
import java.util.List;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class MetDPCriteriaList {

    private List<MetDPCriterion> metDPCriterions;

    public MetDPCriteriaList() {
    }

    public MetDPCriteriaList(List<MetDPCriterion> metDPCriterions) {
        this.metDPCriterions = metDPCriterions;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        for (MetDPCriterion metDPCriterion : metDPCriterions) {
            metDPCriterion.encode(aos);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this.decode_(tmpAis);
    }

    private void decode_(AsnInputStream ais) throws IOException, AsnException {
        while (ais.available() > 0) {
            MetDPCriterion metDPCriterion = MetDPCriterion.create();
            metDPCriterion.decode(ais);
        }
    }

    /**
     * @return the metDPCriterions
     */
    public List<MetDPCriterion> getMetDPCriterions() {
        return metDPCriterions;
    }

}
