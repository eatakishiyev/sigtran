/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.ParameterOutOfRangeException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * CAP-GPRS-ReferenceNumber ::= SEQUENCE {
 * destinationReference [0] Integer4 OPTIONAL,
 * originationReference [1] Integer4 OPTIONAL
 * }
 *
 * @author eatakishiyev
 */
public class CapGPRSReferenceNumber {

    public static final long[] CAP_GPRS_REFERENCE_NUMBER_OID = {0, 4, 0, 0, 1, 1, 5, 2};

    private Integer4 destinationReference;
    private Integer4 originationReference;

    public CapGPRSReferenceNumber() {
    }

    public CapGPRSReferenceNumber(Integer4 destinationReference, Integer4 originationReference) {
        this.destinationReference = destinationReference;
        this.originationReference = originationReference;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, ParameterOutOfRangeException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            if (destinationReference != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 0);
                int lenPos1 = aos.StartContentDefiniteLength();
                destinationReference.encode(aos);
                aos.FinalizeContent(lenPos1);
            }
            if (originationReference != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 1);
                int lenPos1 = aos.StartContentDefiniteLength();
                originationReference.encode(aos);
                aos.FinalizeContent(lenPos1);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, ParameterOutOfRangeException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, ParameterOutOfRangeException {
        try {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new IncorrectSyntaxException(String.format("Expecting CLASS[CONTEXT] tag. Received CLASS[%s] tag", ais.getTagClass()));
            }

            switch (tag) {
                case 0:
                    this.destinationReference = new Integer4();
                    destinationReference.decode(ais);
                    break;
                case 1:
                    this.originationReference = new Integer4();
                    originationReference.decode(ais);
                    break;
                default:
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] received.", tag));
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the destinationReference
     */
    public Integer4 getDestinationReference() {
        return destinationReference;
    }

    /**
     * @param destinationReference the destinationReference to set
     */
    public void setDestinationReference(Integer4 destinationReference) {
        this.destinationReference = destinationReference;
    }

    /**
     * @return the originationReference
     */
    public Integer4 getOriginationReference() {
        return originationReference;
    }

    /**
     * @param originationReference the originationReference to set
     */
    public void setOriginationReference(Integer4 originationReference) {
        this.originationReference = originationReference;
    }

}
