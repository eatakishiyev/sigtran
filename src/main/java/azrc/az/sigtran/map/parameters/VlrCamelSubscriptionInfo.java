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
 * VlrCamelSubscriptionInfo::= SEQUENCE {
 * o-CSI [0] O-CSI OPTIONAL,
 * extensionContainer [1] ExtensionContainer OPTIONAL,
 * ...,
 * ss-CSI [2] SS-CSI OPTIONAL,
 * o-BcsmCamelTDP-CriteriaList [4] O-BcsmCamelTDPCriteriaList OPTIONAL,
 * tif-CSI [3] NULL OPTIONAL,
 * m-CSI [5] M-CSI OPTIONAL,
 * mo-sms-CSI [6] SMS-CSI OPTIONAL,
 * vt-CSI [7] T-CSI OPTIONAL,
 * t-BCSM-CAMEL-TDP-CriteriaList [8] T-BCSM-CAMEL-TDP-CriteriaList OPTIONAL,
 * d-CSI [9] D-CSI OPTIONAL,
 * mt-sms-CSI [10] SMS-CSI OPTIONAL,
 * mt-smsCAMELTDP-CriteriaList [11] MT-smsCAMELTDP-CriteriaList OPTIONAL
 * }
 * @author eatakishiyev
 */
public class VlrCamelSubscriptionInfo {

    private OCSI oCSI;
    private ExtensionContainer extensionContainer;
    private SSCSI ssCSI;
    private OBcsmCamelTDPCriteriaList oBcsmCamelTDPCriteriaList;
    private Boolean tifCSI;
    private MCSI mCSI;
    private SMSCSI mosmsCSI;
    private TCSI vtCSI;
    private TBCSMCamelTDPCriteriaList tBCSMCamelTDPCriteriaList;
    private DCSI dCSI;
    private SMSCSI mtsmsCSI;
    private MTsmsCamelTDPCriteriaList mTsmsCamelTDPCriteriaList;

    public VlrCamelSubscriptionInfo() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            if (oCSI != null) {
                oCSI.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
            if (ssCSI != null) {
                ssCSI.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            if (oBcsmCamelTDPCriteriaList != null) {
                oBcsmCamelTDPCriteriaList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }
            if (tifCSI) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 3);
            }
            if (mCSI != null) {
                mCSI.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            }
            if (mosmsCSI != null) {
                mosmsCSI.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }
            if (vtCSI != null) {
                vtCSI.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }
            if (tBCSMCamelTDPCriteriaList != null) {
                tBCSMCamelTDPCriteriaList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 8, aos);
            }
            if (dCSI != null) {
                dCSI.encode(Tag.CLASS_CONTEXT_SPECIFIC, 9, aos);
            }
            if (mtsmsCSI != null) {
                mtsmsCSI.encode(Tag.CLASS_CONTEXT_SPECIFIC, 10, aos);
            }
            if (mTsmsCamelTDPCriteriaList != null) {
                mTsmsCamelTDPCriteriaList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 11, aos);
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
                    throw new IncorrectSyntaxException(String.format("Unexpected Class[%s] received. Expecting Class[CONTEXT] tag"));
                }
                switch (tag) {
                    case 0:
                        this.oCSI = new OCSI();
                        oCSI.decode(ais.readSequenceStream());
                        break;
                    case 1:
                        this.extensionContainer = new ExtensionContainer();
                        extensionContainer.decode(ais);
                        break;
                    case 2:
                        ssCSI = new SSCSI();
                        ssCSI.decode(ais.readSequenceStream());
                        break;
                    case 3:
                        this.tifCSI = true;
                        ais.readNull();
                        break;
                    case 4:
                        this.oBcsmCamelTDPCriteriaList = new OBcsmCamelTDPCriteriaList();
                        oBcsmCamelTDPCriteriaList.decode(ais.readSequenceStream());
                        break;
                    case 5:
                        this.mCSI = new MCSI();
                        mCSI.decode(ais.readSequenceStream());
                        break;
                    case 6:
                        this.mosmsCSI = new SMSCSI();
                        mosmsCSI.decode(ais.readSequenceStream());
                        break;
                    case 7:
                        this.vtCSI = new TCSI();
                        vtCSI.decode(ais.readSequenceStream());
                        break;
                    case 8:
                        this.tBCSMCamelTDPCriteriaList = new TBCSMCamelTDPCriteriaList();
                        tBCSMCamelTDPCriteriaList.decode(ais.readSequenceStream());
                        break;
                    case 9:
                        this.dCSI = new DCSI();
                        dCSI.decode(ais.readSequenceStream());
                        break;
                    case 10:
                        this.mtsmsCSI = new SMSCSI();
                        mtsmsCSI.decode(ais.readSequenceStream());
                        break;
                    case 11:
                        this.mTsmsCamelTDPCriteriaList = new MTsmsCamelTDPCriteriaList();
                        mTsmsCamelTDPCriteriaList.decode(ais.readSequenceStream());
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
     * @return the ssCSI
     */
    public SSCSI getSsCSI() {
        return ssCSI;
    }

    /**
     * @param ssCSI the ssCSI to set
     */
    public void setSsCSI(SSCSI ssCSI) {
        this.ssCSI = ssCSI;
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
     * @return the tifCSI
     */
    public Boolean getTifCSI() {
        return tifCSI;
    }

    /**
     * @param tifCSI the tifCSI to set
     */
    public void setTifCSI(Boolean tifCSI) {
        this.tifCSI = tifCSI;
    }

    /**
     * @return the mCSI
     */
    public MCSI getmCSI() {
        return mCSI;
    }

    /**
     * @param mCSI the mCSI to set
     */
    public void setmCSI(MCSI mCSI) {
        this.mCSI = mCSI;
    }

    /**
     * @return the mosmsCSI
     */
    public SMSCSI getMosmsCSI() {
        return mosmsCSI;
    }

    /**
     * @param mosmsCSI the mosmsCSI to set
     */
    public void setMosmsCSI(SMSCSI mosmsCSI) {
        this.mosmsCSI = mosmsCSI;
    }

    /**
     * @return the vtCSI
     */
    public TCSI getVtCSI() {
        return vtCSI;
    }

    /**
     * @param vtCSI the vtCSI to set
     */
    public void setVtCSI(TCSI vtCSI) {
        this.vtCSI = vtCSI;
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

    /**
     * @return the mtsmsCSI
     */
    public SMSCSI getMtsmsCSI() {
        return mtsmsCSI;
    }

    /**
     * @param mtsmsCSI the mtsmsCSI to set
     */
    public void setMtsmsCSI(SMSCSI mtsmsCSI) {
        this.mtsmsCSI = mtsmsCSI;
    }

    /**
     * @return the mTsmsCamelTDPCriteriaList
     */
    public MTsmsCamelTDPCriteriaList getmTsmsCamelTDPCriteriaList() {
        return mTsmsCamelTDPCriteriaList;
    }

    /**
     * @param mTsmsCamelTDPCriteriaList the mTsmsCamelTDPCriteriaList to set
     */
    public void setmTsmsCamelTDPCriteriaList(MTsmsCamelTDPCriteriaList mTsmsCamelTDPCriteriaList) {
        this.mTsmsCamelTDPCriteriaList = mTsmsCamelTDPCriteriaList;
    }

}
