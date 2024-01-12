/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * Ext-SS-Info ::= CHOICE {
 * forwardingInfo [0] Ext-ForwInfo,
 * callBarringInfo [1] Ext-CallBarInfo,
 * cug-Info [2] CUG-Info,
 * ss-Data [3] Ext-SS-Data,
 * emlpp-Info [4] EMLPP-Info }
 *
 * /* @author eatakishiyev
 *
 *
 */
public class ExtSSInfo implements MAPParameter {

    public final static int FORWARDING_INFO_TAG = 0x00;
    public final static int CALL_BARRING_TAG = 0x01;
    public final static int CUG_INFO_TAG = 0x02;
    public final static int SSDATA_TAG = 0x03;
    public final static int EMLPPINFO_TAG = 0x04;

    private ExtForwInfo forwardingInfo;
    private ExtCallBarInfo callBarringInfo;
    private CUGInfo cugInfo;
    private ExtSSData ssData;
    private EMLPPInfo emlppInfo;

//
    public ExtSSInfo() {
    }

    public ExtSSInfo(ExtForwInfo forwardingInfo) {
        this.forwardingInfo = forwardingInfo;
    }

    public ExtSSInfo(ExtCallBarInfo callBarringInfo) {
        this.callBarringInfo = callBarringInfo;
    }

    public ExtSSInfo(CUGInfo cugInfo) {
        this.cugInfo = cugInfo;
    }

    public ExtSSInfo(ExtSSData ssData) {
        this.ssData = ssData;
    }

    public ExtSSInfo(EMLPPInfo emlppInfo) {
        this.emlppInfo = emlppInfo;
    }

    public void encode(AsnOutputStream aos) throws Exception {
        if (forwardingInfo != null) {
            forwardingInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, FORWARDING_INFO_TAG, aos);
        } else if (callBarringInfo != null) {
            callBarringInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, CALL_BARRING_TAG, aos);
        } else if (cugInfo != null) {
            cugInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, CUG_INFO_TAG, aos);
        } else if (ssData != null) {
            ssData.encode(Tag.CLASS_CONTEXT_SPECIFIC, SSDATA_TAG, aos);
        } else if (emlppInfo != null) {
            emlppInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, EMLPPINFO_TAG, aos);
        }
    }

    @Override
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws Exception {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();
        this.encode(aos);
        aos.FinalizeContent(lenPos);
    }

    @Override
    public void decode(AsnInputStream ais) throws Exception {
        int tag = ais.getTag();
        this.decode(tag, ais);
    }

    public void decode(int tag, AsnInputStream ais) throws Exception {
        switch (tag) {
            case FORWARDING_INFO_TAG:
                this.forwardingInfo = new ExtForwInfo();
                forwardingInfo.decode(ais.readSequenceStream());
                break;
            case CALL_BARRING_TAG:
                this.callBarringInfo = new ExtCallBarInfo();
                callBarringInfo.decode(ais.readSequenceStream());
                break;
            case CUG_INFO_TAG:
                this.cugInfo = new CUGInfo();
                cugInfo.decode(ais.readSequenceStream());
                break;
            case SSDATA_TAG:
                this.ssData = new ExtSSData();
                ssData.decode(ais.readSequenceStream());
                break;
            case EMLPPINFO_TAG:
                this.emlppInfo = new EMLPPInfo();
                emlppInfo.decode(ais.readSequenceStream());
                break;
        }
    }

    public ExtCallBarInfo getCallBarringInfo() {
        return callBarringInfo;
    }

    public CUGInfo getCugInfo() {
        return cugInfo;
    }

    public EMLPPInfo getEmlppInfo() {
        return emlppInfo;
    }

    public ExtForwInfo getForwardingInfo() {
        return forwardingInfo;
    }

    public ExtSSData getSsData() {
        return ssData;
    }

}
