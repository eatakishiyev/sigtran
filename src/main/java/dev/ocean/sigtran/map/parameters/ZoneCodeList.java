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
 *
 * @author eatakishiyev
 */
public class ZoneCodeList {

    private List<ZoneCode> zoneCodes;
    public final int MAX_ZONE_CODES_COUNT = 10;

    public ZoneCodeList() {
        this.zoneCodes = new ArrayList();
    }

    public final void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if (zoneCodes.size() < 1
                || zoneCodes.size() > 10) {
            throw new UnexpectedDataException(String.format("ZoneCodes counts must be between [1..10]. Current count is %s", zoneCodes.size()));
        }

        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            for (ZoneCode zoneCode : zoneCodes) {
                zoneCode.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public final void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            AsnInputStream tmpAis = ais.readSequenceStream();
            while (tmpAis.available() > 0) {
                int tag = tmpAis.readTag();
                if (tag != Tag.STRING_OCTET
                        && tmpAis.getTagClass() != Tag.CLASS_UNIVERSAL) {
                    throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                            + " found Tag[%s] TagClass[%s]", tag, tmpAis.getTagClass()));
                }

                ZoneCode zoneCode = new ZoneCode();
                zoneCode.decode(tmpAis);
                this.zoneCodes.add(zoneCode);
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     *
     * @return Return readonly list of zone codes
     */
    public List<ZoneCode> getZoneCodes() {
        return zoneCodes;
    }
}
