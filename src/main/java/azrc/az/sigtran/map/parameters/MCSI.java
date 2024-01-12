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
 * M-CSI::= SEQUENCE {
 * mobilityTriggers MobilityTriggers,
 * serviceKey ServiceKey,
 * gsmSCF-Address [0] ISDN-AddressString,
 * extensionContainer [1] ExtensionContainer OPTIONAL,
 * notificationToCSE [2] NULL OPTIONAL,
 * csi-Active [3] NULL OPTIONAL,
 * ...}
 * -- notificationToCSE and csi-Active shall not be present when M-CSI is sent
 * to VLR.
 * -- They may only be included in ATSI/ATM ack/NSDC message.
 * @author eatakishiyev
 */
public class MCSI {

    private MobilityTriggers mobilityTriggers;
    private Integer serviceKey;
    private ISDNAddressString gsmSCFAddress;
    private ExtensionContainer extensionContainer;
    private Boolean notificationToCSE;
    private Boolean csiActive;

    public MCSI() {
    }

    public MCSI(MobilityTriggers mobilityTriggers, Integer serviceKey, ISDNAddressString gsmSCFAddress) {
        this.mobilityTriggers = mobilityTriggers;
        this.serviceKey = serviceKey;
        this.gsmSCFAddress = gsmSCFAddress;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            mobilityTriggers.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);

            aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.INTEGER, serviceKey);

            gsmSCFAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }

            if (notificationToCSE) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 2);
            }

            if (csiActive) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 3);
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
                this.mobilityTriggers = new MobilityTriggers();
                mobilityTriggers.decode(ais.readSequenceStream());
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[SEQUENCE] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == Tag.INTEGER
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.serviceKey = (int) ais.readInteger();
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[INTEGER] Class[UNIVERSAL]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == 0
                    && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.gsmSCFAddress = new ISDNAddressString(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Expecting Tag[0] Class[CONTEXT]. Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }

            while (ais.available() > 0) {
                tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new IncorrectSyntaxException(String.format("Expecting Class[CONTEXT] tag. Received Class[%s]", ais.getTagClass()));
                }
                switch (tag) {
                    case 1:
                        this.extensionContainer = new ExtensionContainer(ais);
                        break;
                    case 2:
                        this.notificationToCSE = true;
                        ais.readNull();
                        break;
                    case 3:
                        this.csiActive = true;
                        ais.readNull();
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
     * @return the mobilityTriggers
     */
    public MobilityTriggers getMobilityTriggers() {
        return mobilityTriggers;
    }

    /**
     * @param mobilityTriggers the mobilityTriggers to set
     */
    public void setMobilityTriggers(MobilityTriggers mobilityTriggers) {
        this.mobilityTriggers = mobilityTriggers;
    }

    /**
     * @return the serviceKey
     */
    public Integer getServiceKey() {
        return serviceKey;
    }

    /**
     * @param serviceKey the serviceKey to set
     */
    public void setServiceKey(Integer serviceKey) {
        this.serviceKey = serviceKey;
    }

    /**
     * @return the gsmSCFAddress
     */
    public ISDNAddressString getGsmSCFAddress() {
        return gsmSCFAddress;
    }

    /**
     * @param gsmSCFAddress the gsmSCFAddress to set
     */
    public void setGsmSCFAddress(ISDNAddressString gsmSCFAddress) {
        this.gsmSCFAddress = gsmSCFAddress;
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
