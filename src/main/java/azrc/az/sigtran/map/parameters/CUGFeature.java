/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class CUGFeature  implements MAPParameter{

//          CUG-Feature ::= SEQUENCE { 
//              basicService                Ext-BasicServiceCode    OPTIONAL, 
//              preferentialCUG-Indicator   CUG-Index               OPTIONAL, 
//              interCUG-Restrictions       InterCUG-Restrictions, 
//              extensionContainer          ExtensionContainer      OPTIONAL, 
//              ...} 
    private ExtBasicServiceCode basicService;
    private Integer preferentialCUGIndicator;
    private InterCUGRestrictions interCUGRestrictions;
    private ExtensionContainer extensionContainer;

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                switch (tag) {
                    case ExtBasicServiceCode.EXT_BEARER_SERVICE_TAG:
                        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                            ExtBearerServiceCode extBearerServiceCode = new ExtBearerServiceCode();
                            extBearerServiceCode.decode(ais);
                            this.basicService = new ExtBasicServiceCode();
                            this.basicService.setExtBearerService(extBearerServiceCode);
                        }
                        if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                            this.preferentialCUGIndicator = ((Long) ais.readInteger()).intValue();
                        }
                        break;
                    case ExtBasicServiceCode.EXT_TELE_SERVICE_TAG:
                        ExtTeleServiceCode extTeleServiceCode = new ExtTeleServiceCode();
                        extTeleServiceCode.decode(ais);
                        this.basicService = new ExtBasicServiceCode();
                        this.basicService.setExtTeleService(extTeleServiceCode);
                        break;
                    case Tag.STRING_OCTET:
                        this.interCUGRestrictions = new InterCUGRestrictions();
                        this.interCUGRestrictions.decode(ais);
                        break;
                    case Tag.SEQUENCE:
                        this.extensionContainer = new ExtensionContainer(ais);
                        break;
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            if (basicService != null) {
                basicService.encode(aos);
            }
            aos.writeInteger(tag);
            if (preferentialCUGIndicator != null) {
                aos.writeInteger(preferentialCUGIndicator);
            }
            interCUGRestrictions.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the basicService
     */
    public ExtBasicServiceCode getBasicService() {
        return basicService;
    }

    /**
     * @param basicService the basicService to set
     */
    public void setBasicService(ExtBasicServiceCode basicService) {
        this.basicService = basicService;
    }

    /**
     * @return the preferentialCUGIndicator
     */
    public Integer getPreferentialCUGIndicator() {
        return preferentialCUGIndicator;
    }

    /**
     * @param preferentialCUGIndicator the preferentialCUGIndicator to set
     */
    public void setPreferentialCUGIndicator(Integer preferentialCUGIndicator) {
        this.preferentialCUGIndicator = preferentialCUGIndicator;
    }

    /**
     * @return the interCUGRestrictions
     */
    public InterCUGRestrictions getInterCUGRestrictions() {
        return interCUGRestrictions;
    }

    /**
     * @param interCUGRestrictions the interCUGRestrictions to set
     */
    public void setInterCUGRestrictions(InterCUGRestrictions interCUGRestrictions) {
        this.interCUGRestrictions = interCUGRestrictions;
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
