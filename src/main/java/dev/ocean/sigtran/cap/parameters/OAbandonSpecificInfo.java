/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class OAbandonSpecificInfo {

    private boolean routeNotPermitted = false;

    public OAbandonSpecificInfo() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (routeNotPermitted) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 50);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        while (tmpAis.available() > 0) {
            int tag = tmpAis.readTag();
            if (tag != 50 || tmpAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Received invalid tag. Expecting Tag[50] TagClass[2], found Tag[%s] TagClass[%s]", tag, tmpAis.getTagClass()));
            }

            this.routeNotPermitted = true;
            tmpAis.readNull();
        }
    }

    /**
     * @return the routeNotPermitted
     */
    public boolean isRouteNotPermitted() {
        return routeNotPermitted;
    }

    /**
     * @param routeNotPermitted the routeNotPermitted to set
     */
    public void setRouteNotPermitted(boolean routeNotPermitted) {
        this.routeNotPermitted = routeNotPermitted;
    }

}
