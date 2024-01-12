/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * DestinationNumberCriteria ::= SEQUENCE {
 * matchType [0] MatchType,
 * destinationNumberList [1] DestinationNumberList OPTIONAL,
 * destinationNumberLengthList [2] DestinationNumberLengthList OPTIONAL,
 * -- one or both of destinationNumberList and destinationNumberLengthList
 * -- shall be present
 * ...}
 * @author eatakishiyev
 */
public class DestinationNumberCriteria implements MAPParameter {

    private MatchType matchType;
    private DestinationNumberList destinationNumberList;
    private DestinationNumberLengthList destinationNumberLengthList;

    public DestinationNumberCriteria() {
    }

    public DestinationNumberCriteria(MatchType matchType) {
        this.matchType = matchType;
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, matchType.getValue());
            if (destinationNumberList != null) {
                destinationNumberList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
            if (destinationNumberLengthList != null) {
                destinationNumberLengthList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new IncorrectSyntaxException(String.format("Expecting CONTEXT SPECIFIC[2] Tag. Received Class[%s]", ais.getTagClass()));
                }

                switch (tag) {
                    case 0:
                        this.matchType = MatchType.getInstance((int) ais.readInteger());
                        break;
                    case 1:
                        this.destinationNumberList = new DestinationNumberList();
                        destinationNumberList.decode(ais.readSequenceStream());
                        break;
                    case 2:
                        this.destinationNumberLengthList = new DestinationNumberLengthList();
                        destinationNumberLengthList.decode(ais.readSequenceStream());
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Received unexpected tag. Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the matchType
     */
    public MatchType getMatchType() {
        return matchType;
    }

    /**
     * @param matchType the matchType to set
     */
    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    /**
     * @return the destinationNumberList
     */
    public DestinationNumberList getDestinationNumberList() {
        return destinationNumberList;
    }

    /**
     * @param destinationNumberList the destinationNumberList to set
     */
    public void setDestinationNumberList(DestinationNumberList destinationNumberList) {
        this.destinationNumberList = destinationNumberList;
    }

    /**
     * @return the destinationNumberLengthList
     */
    public DestinationNumberLengthList getDestinationNumberLengthList() {
        return destinationNumberLengthList;
    }

    /**
     * @param destinationNumberLengthList the destinationNumberLengthList to set
     */
    public void setDestinationNumberLengthList(DestinationNumberLengthList destinationNumberLengthList) {
        this.destinationNumberLengthList = destinationNumberLengthList;
    }
}
