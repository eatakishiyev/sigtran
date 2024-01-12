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
 * D-CSI::= SEQUENCE {
 * dp-AnalysedInfoCriteriaList [0] DP-AnalysedInfoCriteriaList OPTIONAL,
 * camelCapabilityHandling [1] CamelCapabilityHandling OPTIONAL,
 * extensionContainer [2] ExtensionContainer OPTIONAL,
 * notificationToCSE [3] NULL OPTIONAL,
 * csi-Active [4] NULL OPTIONAL,
 * ...}
 * -- notificationToCSE and csi-Active shall not be present when D-CSI is sent
 * to VLR/GMSC.
 * -- They may only be included in ATSI/ATM ack/NSDC message.
 * -- DP-AnalysedInfoCriteria and camelCapabilityHandling shall be present in
 * -- the D-CSI sequence.
 * -- If D-CSI is segmented, then the first segment shall contain
 * dp-AnalysedInfoCriteriaList
 * -- and camelCapabilityHandling. Subsequent segments shall not contain
 * -- camelCapabilityHandling, but may contain dp-AnalysedInfoCriteriaList.
 * @author eatakishiyev
 */
public class DCSI  implements MAPParameter{
    
    private DPAnayzedInfoCriteriaList dPAnayzedInfoCriteriaList;
    private CamelCapabilityHandling camelCapabilityHandling;
    private ExtensionContainer extensionContainer;
    private Boolean notificationToCSE;
    private Boolean csiActive;
    
    public DCSI() {
    }
    
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            if (dPAnayzedInfoCriteriaList != null) {
                dPAnayzedInfoCriteriaList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }
            
            if (camelCapabilityHandling != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 1, camelCapabilityHandling.getValue());
            }
            
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            
            if (notificationToCSE) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 3);
            }
            
            if (csiActive) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 4);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }
    
    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new IncorrectSyntaxException(String.format("Expecting Class[CONTEXT] tag. Received Class[%s]", ais.getTagClass()));
                }
                switch (tag) {
                    case 0:
                        this.dPAnayzedInfoCriteriaList = new DPAnayzedInfoCriteriaList();
                        dPAnayzedInfoCriteriaList.decode(ais.readSequenceStream());
                        break;
                    case 1:
                        this.camelCapabilityHandling = CamelCapabilityHandling.getInstance((int) ais.readInteger());
                        break;
                    case 2:
                        this.extensionContainer = new ExtensionContainer(ais);
                        break;
                    case 3:
                        this.notificationToCSE = true;
                        ais.readNull();
                        break;
                    case 4:
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
     * @return the dPAnayzedInfoCriteriaList
     */
    public DPAnayzedInfoCriteriaList getdPAnayzedInfoCriteriaList() {
        return dPAnayzedInfoCriteriaList;
    }

    /**
     * @param dPAnayzedInfoCriteriaList the dPAnayzedInfoCriteriaList to set
     */
    public void setdPAnayzedInfoCriteriaList(DPAnayzedInfoCriteriaList dPAnayzedInfoCriteriaList) {
        this.dPAnayzedInfoCriteriaList = dPAnayzedInfoCriteriaList;
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
