/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * DestinationNumberLengthList ::= SEQUENCE SIZE
 * (1..maxNumOfCamelDestinationNumberLengths) OF
 * INTEGER(1..maxNumOfISDN-AddressDigits)
 * @author eatakishiyev
 */
public class DestinationNumberLengthList implements MAPParameter{

    private final List<Integer> destinationNumberLengths;

    public DestinationNumberLengthList() {
        this.destinationNumberLengths = new ArrayList();
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws UnexpectedDataException, IncorrectSyntaxException {
        if (destinationNumberLengths.size() < 1
                || destinationNumberLengths.size() > Constants.maxNumOfISDNAddressDigits) {
            throw new UnexpectedDataException("DestinationNumberLength count must be in range [1..15]. Current count is " + destinationNumberLengths.size());
        }
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (Integer destinationNumberLength : destinationNumberLengths) {
                aos.writeInteger(destinationNumberLength);
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
                if (tag == Tag.INTEGER
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    int destinationNumberLength = (int) ais.readInteger();
                    destinationNumberLengths.add(destinationNumberLength);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[INTEGER] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }

        if (destinationNumberLengths.size() < 1
                || destinationNumberLengths.size() > Constants.maxNumOfISDNAddressDigits) {
            throw new UnexpectedDataException("DestinationNumberLength count must be in range [1..15]. Current count is " + destinationNumberLengths.size());
        }

    }

    public List<Integer> getDestinationNumberLengths() {
        return destinationNumberLengths;
    }
}
