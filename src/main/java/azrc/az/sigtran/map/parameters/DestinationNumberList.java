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
 * DestinationNumberList ::= SEQUENCE SIZE (1..maxNumOfCamelDestinationNumbers)
 * OF
 * ISDN-AddressString -- The receiving entity shall not check the format of a
 * number in
 * -- the dialled number list
 *
 * @author eatakishiyev
 */
public class DestinationNumberList implements MAPParameter {

    private final List<ISDNAddressString> destinationNumbers;

    public DestinationNumberList() {
        this.destinationNumbers = new ArrayList();
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        if (destinationNumbers.size() < 1
                || destinationNumbers.size() > Constants.maxNumOfCamelDestinationNumbers) {
            throw new UnexpectedDataException("DestinationNumber count must be in [1..10] range. Current count is " + destinationNumbers.size());
        }

        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            for (ISDNAddressString destinationNumber : destinationNumbers) {
                destinationNumber.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(AsnInputStream ais) throws UnexpectedDataException, IncorrectSyntaxException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == Tag.STRING_OCTET
                        && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    ISDNAddressString destinationNumber = new ISDNAddressString(ais);
                    destinationNumbers.add(destinationNumber);
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[OCTET] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
            if (destinationNumbers.size() < 1
                    || destinationNumbers.size() > Constants.maxNumOfCamelDestinationNumbers) {
                throw new UnexpectedDataException("DestinationNumber count must be in [1..10] range. Current count is " + destinationNumbers.size());
            }

        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public List<ISDNAddressString> getDestinationNumbers() {
        return destinationNumbers;
    }
}
