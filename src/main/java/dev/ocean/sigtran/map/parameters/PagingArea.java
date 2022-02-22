/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * PagingArea ::= SEQUENCE SIZE (1..5) OF LocationArea
 *
 * @author eatakishiyev
 */
public class PagingArea {

    private final List<LocationArea> locationAreas;

    public PagingArea() {
        this.locationAreas = new ArrayList();
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (LocationArea locationArea : locationAreas) {
                locationArea.encode(aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        while (ais.available() > 0) {
            LocationArea locationArea = new LocationArea();
            locationArea.decode(ais);
            locationAreas.add(locationArea);
        }
    }

    public List<LocationArea> getLocationAreas() {
        return locationAreas;
    }

}
