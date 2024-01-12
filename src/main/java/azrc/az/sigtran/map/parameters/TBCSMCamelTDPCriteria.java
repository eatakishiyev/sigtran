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
 * T-BCSM-CAMEL-TDP-Criteria ::= SEQUENCE {
 * t-BCSM-TriggerDetectionPoint T-BcsmTriggerDetectionPoint,
 * basicServiceCriteria [0] BasicServiceCriteria OPTIONAL,
 * t-CauseValueCriteria [1] T-CauseValueCriteria OPTIONAL,
 * ... }
 * @author eatakishiyev
 */
public class TBCSMCamelTDPCriteria {

    private TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint;
    private BasicServiceCriteria basicServiceCriteria;
    private TCauseValueCriteria tCauseValueCriteria;

    public TBCSMCamelTDPCriteria() {
    }

    public TBCSMCamelTDPCriteria(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint) {
        this.tBcsmTriggerDetectionPoint = tBcsmTriggerDetectionPoint;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, tBcsmTriggerDetectionPoint.getValue());
            if (basicServiceCriteria != null) {
                basicServiceCriteria.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }

            if (tCauseValueCriteria != null) {
                tCauseValueCriteria.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.ENUMERATED
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.tBcsmTriggerDetectionPoint = TBcsmTriggerDetectionPoint.getInstance((int) ais.readInteger());
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[ENUMERATED] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }
            while (ais.available() > 0) {
                tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new IncorrectSyntaxException(String.format("Expecting Class[CONTEXT] tag. Received Class[%s]", ais.getTagClass()));
                }
                switch (tag) {
                    case 0:
                        this.basicServiceCriteria = new BasicServiceCriteria();
                        basicServiceCriteria.decode(ais.readSequenceStream());
                        break;
                    case 1:
                        this.tCauseValueCriteria = new TCauseValueCriteria();
                        tCauseValueCriteria.decode(ais.readSequenceStream());
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Unexpected tag received. Tag[%s]", tag));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the tBcsmTriggerDetectionPoint
     */
    public TBcsmTriggerDetectionPoint gettBcsmTriggerDetectionPoint() {
        return tBcsmTriggerDetectionPoint;
    }

    /**
     * @param tBcsmTriggerDetectionPoint the tBcsmTriggerDetectionPoint to set
     */
    public void settBcsmTriggerDetectionPoint(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint) {
        this.tBcsmTriggerDetectionPoint = tBcsmTriggerDetectionPoint;
    }

    /**
     * @return the basicServiceCriteria
     */
    public BasicServiceCriteria getBasicServiceCriteria() {
        return basicServiceCriteria;
    }

    /**
     * @param basicServiceCriteria the basicServiceCriteria to set
     */
    public void setBasicServiceCriteria(BasicServiceCriteria basicServiceCriteria) {
        this.basicServiceCriteria = basicServiceCriteria;
    }

    /**
     * @return the tCauseValueCriteria
     */
    public TCauseValueCriteria gettCauseValueCriteria() {
        return tCauseValueCriteria;
    }

    /**
     * @param tCauseValueCriteria the tCauseValueCriteria to set
     */
    public void settCauseValueCriteria(TCauseValueCriteria tCauseValueCriteria) {
        this.tCauseValueCriteria = tCauseValueCriteria;
    }

}
