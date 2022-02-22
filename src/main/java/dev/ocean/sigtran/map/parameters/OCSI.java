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
 * O-CSI::= SEQUENCE {
 * o-BcsmCamelTDPDataList O-BcsmCamelTDPDataList,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...,
 * camelCapabilityHandling [0] CamelCapabilityHandling OPTIONAL,
 * notificationToCSE [1] NULL OPTIONAL,
 * csiActive [2] NULL OPTIONAL}
 * -- notificationtoCSE and csiActive shall not be present when O-CSI is sent to
 * VLR/GMSC.
 * -- They may only be included in ATSI/ATM ack/NSDC message.
 * -- O-CSI shall not be segmented.
 * @author eatakishiyev
 */
public class OCSI {

    private OBcsmCamelTDPDataList oBcsmCamelTDPDataList;
    private ExtensionContainer extensionContainer;
    private CamelCapabilityHandling camelCapabilityHandling;
    private Boolean notificationToCSE;
    private Boolean csiActive;

    public OCSI() {
    }

    public OCSI(OBcsmCamelTDPDataList oBcsmCamelTDPDataList) {
        this.oBcsmCamelTDPDataList = oBcsmCamelTDPDataList;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            oBcsmCamelTDPDataList.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            if (camelCapabilityHandling != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, camelCapabilityHandling.getValue());
            }

            if (notificationToCSE) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 1);
            }

            if (csiActive) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 2);
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
                this.oBcsmCamelTDPDataList = new OBcsmCamelTDPDataList();
                oBcsmCamelTDPDataList.decode(ais.readSequenceStream());
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
                            this.camelCapabilityHandling = CamelCapabilityHandling.getInstance((int) ais.readInteger());
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expecting Tag[0] Class[CONTEXT]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                    case 1:
                        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                            ais.readNull();
                            this.notificationToCSE = true;
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expecting Tag[1] Class[CONTEXT]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                    case 2:
                        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                            ais.readNull();
                            this.csiActive = true;
                        } else {
                            throw new IncorrectSyntaxException(String.format("Expecting Tag[2] Class[CONTEXT]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
                        }
                        break;
                }
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the oBcsmCamelTDPDataList
     */
    public OBcsmCamelTDPDataList getoBcsmCamelTDPDataList() {
        return oBcsmCamelTDPDataList;
    }

    /**
     * @param oBcsmCamelTDPDataList the oBcsmCamelTDPDataList to set
     */
    public void setoBcsmCamelTDPDataList(OBcsmCamelTDPDataList oBcsmCamelTDPDataList) {
        this.oBcsmCamelTDPDataList = oBcsmCamelTDPDataList;
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
     * @return the camelCapabilityHandling
     */
    public CamelCapabilityHandling getCamelCapabilityHandling() {
        return camelCapabilityHandling;
    }

    /**
     * @param camelCapabilityHandling the camelCapabilityHandling to set
     */
    public void setCamelCapabilityHandling(CamelCapabilityHandling camelCapabilityHandling) {
        this.camelCapabilityHandling = camelCapabilityHandling;
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
