/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * This parameter indicates, if present, the support of ADD function by the HLR.
 * ADD - Automatic Device Detection
 * @author eatakishiyev
 */
public class ADDInfo implements MAPParameter {

    private IMEI imeiSV;
    private boolean skipSubscriberDataUpdate = false;

    public ADDInfo() {
    }

    public ADDInfo(AsnInputStream ais) throws IOException, AsnException {
        this.decode(ais);
    }

    @Override
    public final void encode(int tagClass, int tag, AsnOutputStream aos) throws IOException, AsnException {
        aos.writeTag(tagClass, false, tag);
        int lenPos = aos.StartContentDefiniteLength();

        imeiSV.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        if (skipSubscriberDataUpdate) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 1);
        }

        aos.FinalizeContent(lenPos);
    }

    @Override
    public final void decode(AsnInputStream ais) throws IOException, AsnException {
        int tag = ais.readTag();
        switch (tag) {
            case 0:
                imeiSV = new IMEI();
                imeiSV.decode(ais);
                break;
            case 1:
                ais.readNull();
                skipSubscriberDataUpdate = true;
                break;
        }
    }

    /**
     * @return the imeiSV
     */
    public IMEI getImeiSV() {
        return imeiSV;
    }

    /**
     * @param imeiSV the imeiSV to set
     */
    public void setImeiSV(IMEI imeiSV) {
        this.imeiSV = imeiSV;
    }

    public void setImeiSV(String imeiSv) {
        this.imeiSV = new IMEI(imeiSv);
    }

    /**
     * @return the skipSubscriberDataUpdate
     */
    public Boolean getSkipSubscriberDataUpdate() {
        return skipSubscriberDataUpdate;
    }

    /**
     * @param skipSubscriberDataUpdate the skipSubscriberDataUpdate to set
     */
    public void setSkipSubscriberDataUpdate(Boolean skipSubscriberDataUpdate) {
        this.skipSubscriberDataUpdate = skipSubscriberDataUpdate;
    }
}
