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
 * SS-CSI::= SEQUENCE {
 * ss-CamelData SS-CamelData,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...,
 * notificationToCSE [0] NULL OPTIONAL,
 * csi-Active [1] NULL OPTIONAL
 * -- notificationToCSE and csi-Active shall not be present when SS-CSI is sent
 * to VLR.
 * -- They may only be included in ATSI/ATM ack/NSDC message.
 * }
 * @author eatakishiyev
 */
public class SSCSI {

    private SSCamelData ssCamelData;
    private ExtensionContainer extensionContainer;
    private Boolean notificationToCSE;
    private Boolean csiActive;

    public SSCSI() {
    }

    public SSCSI(SSCamelData ssCamelData) {
        this.ssCamelData = ssCamelData;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            ssCamelData.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);

            if (this.extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            if (notificationToCSE) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 0);
            }

            if (csiActive) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 1);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag == Tag.SEQUENCE
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.ssCamelData = new SSCamelData();
                ssCamelData.decode(ais.readSequenceStream());
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            while (ais.available() > 0) {
                tag = ais.readTag();
                switch (tag) {
                    case Tag.SEQUENCE:
                        if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                            this.extensionContainer = new ExtensionContainer(ais);
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                    case 0:
                        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                            this.notificationToCSE = true;
                            ais.readNull();
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expecting Tag[0] Class[CONTEXT]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                    case 1:
                        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                            this.csiActive = true;
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expecting Tag[1] Class[CONTEXT]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the ssCamelData
     */
    public SSCamelData getSsCamelData() {
        return ssCamelData;
    }

    /**
     * @param ssCamelData the ssCamelData to set
     */
    public void setSsCamelData(SSCamelData ssCamelData) {
        this.ssCamelData = ssCamelData;
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
     * @return the notificationToCSE
     */
    public Boolean getNotificationToCSE() {
        return notificationToCSE;
    }

    /**
     * @param notificationToCSE the notificationToCSE to set
     */
    public void setNotificationToCSE(Boolean notificationToCSE) {
        this.notificationToCSE = notificationToCSE;
    }

    /**
     * @return the csiActive
     */
    public Boolean getCsiActive() {
        return csiActive;
    }

    /**
     * @param csiActive the csiActive to set
     */
    public void setCsiActive(Boolean csiActive) {
        this.csiActive = csiActive;
    }
}
