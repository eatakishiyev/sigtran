/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.location.information.LocationInformation;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class CallAcceptedSpecificInfo {

    private LocationInformation locationInformation;

    public CallAcceptedSpecificInfo() {
    }

    public CallAcceptedSpecificInfo(LocationInformation locationInformation) {
        this.locationInformation = locationInformation;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IncorrectSyntaxException, IOException, IllegalNumberFormatException, UnexpectedDataException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (this.locationInformation != null) {
            this.locationInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 50, aos);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws AsnException, IOException, IncorrectSyntaxException, UnexpectedDataException, IllegalNumberFormatException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        while (tmpAis.available() > 0) {
            int tag = tmpAis.readTag();
            if (tag != 50 || tmpAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new AsnException(String.format("Received incorrect tag. Expecting Tag[50] TagClass[2], found Tag[%s] TagClass[%s]", tag, tmpAis.getTagClass()));
            }

            this.locationInformation = new LocationInformation();
            this.locationInformation.decode(tmpAis);
        }
    }

    /**
     * @return the locationInformation
     */
    public LocationInformation getLocationInformation() {
        return locationInformation;
    }

    /**
     * @param locationInformation the locationInformation to set
     */
    public void setLocationInformation(LocationInformation locationInformation) {
        this.locationInformation = locationInformation;
    }

}
