/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.errors.params;

import java.io.IOException;

import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class FacilityNotSupParam {

    private ExtensionContainer extensionContainer;
    private Boolean shapeOfLocationEstimateNotSupported;
    private Boolean neededLcsCapabilityNotSupportedInServingNode;

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (this.extensionContainer != null) {
                this.extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            if (this.shapeOfLocationEstimateNotSupported) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0x00);
            }

            if (this.neededLcsCapabilityNotSupportedInServingNode) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0x01);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                switch (tag) {
                    case Tag.SEQUENCE:
                        if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                            this.extensionContainer = new ExtensionContainer(ais);
                        } else {
                            throw new IncorrectSyntaxException(String.format("Unexpected Class[%s] tag received. Expecting Class[UNIVERSAL]", ais.getTagClass()));
                        }
                        break;
                    case 0:
                        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                            this.shapeOfLocationEstimateNotSupported = true;
                            ais.readNull();
                        } else {
                            throw new IncorrectSyntaxException(String.format("Unexpected Class[%s] tag received. Expecting Class[CONTEXT]", ais.getTagClass()));
                        }
                        break;
                    case 1:
                        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                            this.neededLcsCapabilityNotSupportedInServingNode = true;
                            ais.readNull();
                        } else {
                            throw new IncorrectSyntaxException(String.format("Unexpected Class[%s] tag received. Expecting Class[CONTEXT]", ais.getTagClass()));
                        }
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] received", tag));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
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

    /**
     * @return the shapeOfLocationEstimateNotSupported
     */
    public Boolean getShapeOfLocationEstimateNotSupported() {
        return shapeOfLocationEstimateNotSupported;
    }

    /**
     * @param shapeOfLocationEstimateNotSupported the
     * shapeOfLocationEstimateNotSupported to set
     */
    public void setShapeOfLocationEstimateNotSupported(Boolean shapeOfLocationEstimateNotSupported) {
        this.shapeOfLocationEstimateNotSupported = shapeOfLocationEstimateNotSupported;
    }

    /**
     * @return the neededLcsCapabilityNotSupportedInServingNode
     */
    public Boolean getNeededLcsCapabilityNotSupportedInServingNode() {
        return neededLcsCapabilityNotSupportedInServingNode;
    }

    /**
     * @param neededLcsCapabilityNotSupportedInServingNode the
     * neededLcsCapabilityNotSupportedInServingNode to set
     */
    public void setNeededLcsCapabilityNotSupportedInServingNode(Boolean neededLcsCapabilityNotSupportedInServingNode) {
        this.neededLcsCapabilityNotSupportedInServingNode = neededLcsCapabilityNotSupportedInServingNode;
    }

    @Override
    public String toString() {
        return "FacilityNotSupParam{" +
                "extensionContainer=" + extensionContainer +
                ", shapeOfLocationEstimateNotSupported=" + shapeOfLocationEstimateNotSupported +
                ", neededLcsCapabilityNotSupportedInServingNode=" + neededLcsCapabilityNotSupportedInServingNode +
                '}';
    }
}
