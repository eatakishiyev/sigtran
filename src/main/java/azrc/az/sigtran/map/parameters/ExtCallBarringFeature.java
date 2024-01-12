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
public class ExtCallBarringFeature  implements MAPParameter {

//       Ext-CallBarringFeature  ::= SEQUENCE { 
//              basicService            Ext-BasicServiceCode  OPTIONAL, 
//              ss-Status           [4] Ext-SS-Status,   
//              extensionContainer      ExtensionContainer    OPTIONAL, 
// ...}
    private ExtBasicServiceCode basicService;
    private ExtSSStatus ssStatus;
    private ExtensionContainer extensionContainer;

    public ExtCallBarringFeature() {
    }

    public ExtCallBarringFeature(ExtSSStatus ssStatus) {
        this.ssStatus = ssStatus;
    }

    @Override
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                switch (tag) {
                    case ExtBasicServiceCode.EXT_BEARER_SERVICE_TAG:
                        ExtBearerServiceCode extBearerServiceCode = new ExtBearerServiceCode();
                        extBearerServiceCode.decode(ais);

                        this.basicService = new ExtBasicServiceCode();
                        this.basicService.setExtBearerService(extBearerServiceCode);
                        break;
                    case ExtBasicServiceCode.EXT_TELE_SERVICE_TAG:
                        ExtTeleServiceCode extTeleServiceCode = new ExtTeleServiceCode();
                        extTeleServiceCode.decode(ais);
                        this.basicService = new ExtBasicServiceCode();
                        this.basicService.setExtTeleService(extTeleServiceCode);
                        break;
                    case 4:
                        this.ssStatus = new ExtSSStatus();
                        this.ssStatus.decode(ais);
                        break;
                    case Tag.SEQUENCE:
                        this.extensionContainer = new ExtensionContainer();
                        this.extensionContainer.decode(ais);
                        break;
                }
            }
        } catch (IOException ex) {
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

            this.ssStatus.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException  ex) {
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
     * @return the sSStatus
     */
    public ExtSSStatus getsSStatus() {
        return ssStatus;
    }

    /**
     * @param sSStatus the sSStatus to set
     */
    public void setsSStatus(ExtSSStatus sSStatus) {
        this.ssStatus = sSStatus;
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
