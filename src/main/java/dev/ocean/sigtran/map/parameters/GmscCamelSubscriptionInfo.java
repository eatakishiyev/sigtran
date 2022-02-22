/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * GmscCamelSubscriptionInfo::= SEQUENCE {
 * t-CSI [0] T-CSI OPTIONAL,
 * o-CSI [1] O-CSI OPTIONAL,
 * extensionContainer [2] ExtensionContainer OPTIONAL,
 * ...,
 * o-BcsmCamelTDP-CriteriaList [3] O-BcsmCamelTDPCriteriaList OPTIONAL,
 * t-BCSM-CAMEL-TDP-CriteriaList [4] T-BCSM-CAMEL-TDP-CriteriaList OPTIONAL,
 * d-csi [5] D-CSI
 *
 * @author eatakishiyev
 */
public class GmscCamelSubscriptionInfo  implements MAPParameter{

    private TCSI tCSI;
    private OCSI oCSI;
    private ExtensionContainer extensionContainer;
    private OBcsmCamelTDPCriteriaList oBcsmCamelTDPCriteriaList;
    private TBCSMCamelTDPCriteriaList tBCSMCamelTDPCriteriaList;
    private DCSI dCSI;

    public GmscCamelSubscriptionInfo() {
    }

    public GmscCamelSubscriptionInfo(TCSI tCSI, OCSI oCSI) {
        this.tCSI = tCSI;
        this.oCSI = oCSI;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            tCSI.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            oCSI.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            if (oBcsmCamelTDPCriteriaList != null) {
                oBcsmCamelTDPCriteriaList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }
            if (tBCSMCamelTDPCriteriaList != null) {
                tBCSMCamelTDPCriteriaList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }
            if (dCSI != null) {
                dCSI.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();
            if (tag == 0 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.tCSI = new TCSI();
                tCSI.decode(ais.readSequenceStream());
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received."
                        + "Expecting Tag[0] Class[CONTEXT]", tag, ais.getTagClass()));
            }
            if (tag == 1 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.oCSI = new OCSI();
                oCSI.decode(ais.readSequenceStream());
            } else {
                throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received."
                        + "Expecting Tag[1] Class[CONTEXT]", tag, ais.getTagClass()));
            }

            while (ais.available() > 0) {
                tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new IncorrectSyntaxException(String.format("Unexpected tag Class[%s] received."
                            + "Expecting Class[CONTEXT]", ais.getTagClass()));
                }
                switch (tag) {
                    case 2:
                        this.extensionContainer = new ExtensionContainer(ais);
                        break;
                    case 3:
                        this.oBcsmCamelTDPCriteriaList = new OBcsmCamelTDPCriteriaList();
                        oBcsmCamelTDPCriteriaList.decode(ais.readSequenceStream());
                        break;
                    case 4:
                        this.tBCSMCamelTDPCriteriaList = new TBCSMCamelTDPCriteriaList();
                        tBCSMCamelTDPCriteriaList.decode(ais.readSequenceStream());
                        break;
                    case 5:
                        this.dCSI = new DCSI();
                        dCSI.decode(ais.readSequenceStream());
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
     * @return the tCSI
     */
    public TCSI gettCSI() {
        return tCSI;
    }

    /**
     * @param tCSI the tCSI to set
     */
    public void settCSI(TCSI tCSI) {
        this.tCSI = tCSI;
    }

    /**
     * @return the oCSI
     */
    public OCSI getoCSI() {
        return oCSI;
    }

    /**
     * @param oCSI the oCSI to set
     */
    public void setoCSI(OCSI oCSI) {
        this.oCSI = oCSI;
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
     * @return the oBcsmCamelTDPCriteriaList
     */
    public OBcsmCamelTDPCriteriaList getoBcsmCamelTDPCriteriaList() {
        return oBcsmCamelTDPCriteriaList;
    }

    /**
     * @param oBcsmCamelTDPCriteriaList the oBcsmCamelTDPCriteriaList to set
     */
    public void setoBcsmCamelTDPCriteriaList(OBcsmCamelTDPCriteriaList oBcsmCamelTDPCriteriaList) {
        this.oBcsmCamelTDPCriteriaList = oBcsmCamelTDPCriteriaList;
    }

    /**
     * @return the tBCSMCamelTDPCriteriaList
     */
    public TBCSMCamelTDPCriteriaList gettBCSMCamelTDPCriteriaList() {
        return tBCSMCamelTDPCriteriaList;
    }

    /**
     * @param tBCSMCamelTDPCriteriaList the tBCSMCamelTDPCriteriaList to set
     */
    public void settBCSMCamelTDPCriteriaList(TBCSMCamelTDPCriteriaList tBCSMCamelTDPCriteriaList) {
        this.tBCSMCamelTDPCriteriaList = tBCSMCamelTDPCriteriaList;
    }

    /**
     * @return the dCSI
     */
    public DCSI getdCSI() {
        return dCSI;
    }

    /**
     * @param dCSI the dCSI to set
     */
    public void setdCSI(DCSI dCSI) {
        this.dCSI = dCSI;
    }

}
