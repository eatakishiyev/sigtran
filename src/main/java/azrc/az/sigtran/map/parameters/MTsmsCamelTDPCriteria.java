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
 * MT-smsCAMELTDP-Criteria ::= SEQUENCE {
 * sms-TriggerDetectionPoint SMS-TriggerDetectionPoint,
 * tpdu-TypeCriterion [0] TPDU-TypeCriterion OPTIONAL,
 * ... }
 * @author eatakishiyev
 */
public class MTsmsCamelTDPCriteria {

    private SMSTriggerDetectionPoint smsTriggerDetectionPoint;
    private TPDUTypeCriterion tpduTypeCriterion;

    public MTsmsCamelTDPCriteria() {
    }

    public MTsmsCamelTDPCriteria(SMSTriggerDetectionPoint smsTriggerDetectionPoint) {
        this.smsTriggerDetectionPoint = smsTriggerDetectionPoint;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, smsTriggerDetectionPoint.getValue());

            if (tpduTypeCriterion != null) {
                tpduTypeCriterion.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
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
                this.smsTriggerDetectionPoint = SMSTriggerDetectionPoint.getInstance((int) ais.readInteger());
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[ENUMERATED] Class[UNIVERSAL]. Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            if (ais.available() > 0) {
                tag = ais.readTag();
                if (tag == 0
                        && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.tpduTypeCriterion = new TPDUTypeCriterion();
                    tpduTypeCriterion.decode(ais.readSequenceStream());
                } else {
                    throw new IncorrectSyntaxException(String.format("Expecting Tag[0] Class[CONTEXT]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the smsTriggerDetectionPoint
     */
    public SMSTriggerDetectionPoint getSmsTriggerDetectionPoint() {
        return smsTriggerDetectionPoint;
    }

    /**
     * @param smsTriggerDetectionPoint the smsTriggerDetectionPoint to set
     */
    public void setSmsTriggerDetectionPoint(SMSTriggerDetectionPoint smsTriggerDetectionPoint) {
        this.smsTriggerDetectionPoint = smsTriggerDetectionPoint;
    }

    /**
     * @return the tpduTypeCriterion
     */
    public TPDUTypeCriterion getTpduTypeCriterion() {
        return tpduTypeCriterion;
    }

    /**
     * @param tpduTypeCriterion the tpduTypeCriterion to set
     */
    public void setTpduTypeCriterion(TPDUTypeCriterion tpduTypeCriterion) {
        this.tpduTypeCriterion = tpduTypeCriterion;
    }

}
