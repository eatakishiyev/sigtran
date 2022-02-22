/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * O-BcsmCamelTDP-Criteria ::= SEQUENCE {
 * o-BcsmTriggerDetectionPoint O-BcsmTriggerDetectionPoint,
 * destinationNumberCriteria [0] DestinationNumberCriteria OPTIONAL,
 * basicServiceCriteria [1] BasicServiceCriteria OPTIONAL,
 * callTypeCriteria [2] CallTypeCriteria OPTIONAL,
 * ...,
 * o-CauseValueCriteria [3] O-CauseValueCriteria OPTIONAL,
 * extensionContainer [4] ExtensionContainer OPTIONAL }
 * @author eatakishiyev
 */
public class OBcsmCamelTDPCriteria {

    private OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint;
    private DestinationNumberCriteria destinationNumberCriteria;
    private BasicServiceCriteria basicServiceCriteria;
    private CallTypeCriteria callTypeCriteria;
    private OCauseValueCriteria oCauseValueCriteria;
    private ExtensionContainer extensionContainer;

    public OBcsmCamelTDPCriteria() {
    }

    public OBcsmCamelTDPCriteria(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint) {
        this.oBcsmTriggerDetectionPoint = oBcsmTriggerDetectionPoint;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, oBcsmTriggerDetectionPoint.getValue());

            if (destinationNumberCriteria != null) {
                destinationNumberCriteria.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }

            if (basicServiceCriteria != null) {
                basicServiceCriteria.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }

            if (callTypeCriteria != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 2, callTypeCriteria.getValue());
            }

            if (oCauseValueCriteria != null) {
                oCauseValueCriteria.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
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
                this.oBcsmTriggerDetectionPoint = OBcsmTriggerDetectionPoint.getInstance((int) ais.readInteger());
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[ENUMERATED] Class[UNIVERSAL]. Received "
                        + "Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            while (ais.available() > 0) {
                tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new IncorrectSyntaxException(String.format("Expecting CONTEXT SPECIFIC[2] Class. Received Class[%s]", ais.getTagClass()));
                }
                switch (tag) {
                    case 0:
                        this.destinationNumberCriteria = new DestinationNumberCriteria();
                        destinationNumberCriteria.decode(ais.readSequenceStream());
                        break;
                    case 1:
                        this.basicServiceCriteria = new BasicServiceCriteria();
                        basicServiceCriteria.decode(ais.readSequenceStream());
                        break;
                    case 2:
                        this.callTypeCriteria = CallTypeCriteria.getInstance((int) ais.readInteger());
                        break;
                    case 3:
                        this.oCauseValueCriteria = new OCauseValueCriteria();
                        oCauseValueCriteria.decode(ais.readSequenceStream());
                        break;
                    case 4:
                        this.extensionContainer = new ExtensionContainer();
                        extensionContainer.decode(ais);
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] received.", tag));
                }

            }

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the oBcsmTriggerDetectionPoint
     */
    public OBcsmTriggerDetectionPoint getoBcsmTriggerDetectionPoint() {
        return oBcsmTriggerDetectionPoint;
    }

    /**
     * @param oBcsmTriggerDetectionPoint the oBcsmTriggerDetectionPoint to set
     */
    public void setoBcsmTriggerDetectionPoint(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint) {
        this.oBcsmTriggerDetectionPoint = oBcsmTriggerDetectionPoint;
    }

    /**
     * @return the destinationNumberCriteria
     */
    public DestinationNumberCriteria getDestinationNumberCriteria() {
        return destinationNumberCriteria;
    }

    /**
     * @param destinationNumberCriteria the destinationNumberCriteria to set
     */
    public void setDestinationNumberCriteria(DestinationNumberCriteria destinationNumberCriteria) {
        this.destinationNumberCriteria = destinationNumberCriteria;
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
     * @return the callTypeCriteria
     */
    public CallTypeCriteria getCallTypeCriteria() {
        return callTypeCriteria;
    }

    /**
     * @param callTypeCriteria the callTypeCriteria to set
     */
    public void setCallTypeCriteria(CallTypeCriteria callTypeCriteria) {
        this.callTypeCriteria = callTypeCriteria;
    }

    /**
     * @return the oCauseValueCriteria
     */
    public OCauseValueCriteria getoCauseValueCriteria() {
        return oCauseValueCriteria;
    }

    /**
     * @param oCauseValueCriteria the oCauseValueCriteria to set
     */
    public void setoCauseValueCriteria(OCauseValueCriteria oCauseValueCriteria) {
        this.oCauseValueCriteria = oCauseValueCriteria;
    }

    /**
     * @return the extensionContainer
     */
    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    /**
     * @param extensionContainer the extensionContainer to set
     */
    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }
}
