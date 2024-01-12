/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.parameters.location.information.LocationInformation;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class ChangeOfPositionSpecificInfo {

    private LocationInformation locationInformation;
    private MetDPCriteriaList metDPCriteriaList;

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IncorrectSyntaxException, IOException, IllegalNumberFormatException, UnexpectedDataException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        if (locationInformation != null) {
            locationInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 50, aos);
        }

        if (metDPCriteriaList != null) {
            metDPCriteriaList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 51, aos);
        }

        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            AsnInputStream tmpAis = ais.readSequenceStream();
            this.decode_(tmpAis);
        } catch (AsnException | IOException | IllegalNumberFormatException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException, IllegalNumberFormatException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new AsnException(String.format("Received incorrect tag. Expecting TagClass[2], found TagClass[%s]", ais.getTagClass()));
                }

                switch (tag) {
                    case 50:
                        this.locationInformation = new LocationInformation();
                        this.locationInformation.decode(ais);
                        break;
                    case 51:
                        this.metDPCriteriaList = new MetDPCriteriaList();
                        this.metDPCriteriaList.decode(ais);
                        break;
                    default:
                        throw new AsnException(String.format("Received unexpected tag. Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

}
