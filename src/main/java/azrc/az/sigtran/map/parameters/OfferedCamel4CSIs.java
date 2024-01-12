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
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;

/**
 * OfferedCamel4CSIs::= BIT STRING {
 * o-csi (0),
 * d-csi (1),
 * vt-csi (2),
 * t-csi (3),
 * mt-sms-csi (4),
 * mg-csi (5),
 * psi-enhancements (6)
 * } (SIZE (7..16))
 * -- A node supporting Camel phase 4 shall mark in the BIT STRING all Camel4
 * CSIs
 * -- it offers.
 * -- Other values than listed above shall be discarded.
 *
 * @author eatakishiyev
 */
public class OfferedCamel4CSIs {

    private Boolean oCSI;
    private Boolean dCSI;
    private Boolean vtCSI;
    private Boolean tCSI;
    private Boolean mtSMSCSI;
    private Boolean mgCSI;
    private Boolean psiEnhancements;

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            BitSetStrictLength bitSet = new BitSetStrictLength(7);
            bitSet.set(0, oCSI);
            bitSet.set(1, dCSI);
            bitSet.set(2, vtCSI);
            bitSet.set(3, tCSI);
            bitSet.set(4, mtSMSCSI);
            bitSet.set(5, mgCSI);
            bitSet.set(6, psiEnhancements);
            aos.writeBitString(tagClass, tag, bitSet);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_BIT, aos);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException {
        BitSetStrictLength bitSet = ais.readBitString();
        oCSI = bitSet.get(0);
        dCSI = bitSet.get(1);
        vtCSI = bitSet.get(2);
        tCSI = bitSet.get(3);
        mtSMSCSI = bitSet.get(4);
        mgCSI = bitSet.get(5);
        psiEnhancements = bitSet.get(6);
    }

    /**
     * @return the oCSI
     */
    public Boolean getoCSI() {
        return oCSI;
    }

    /**
     * @param oCSI the oCSI to set
     */
    public void setoCSI(Boolean oCSI) {
        this.oCSI = oCSI;
    }

    /**
     * @return the dCSI
     */
    public Boolean getdCSI() {
        return dCSI;
    }

    /**
     * @param dCSI the dCSI to set
     */
    public void setdCSI(Boolean dCSI) {
        this.dCSI = dCSI;
    }

    /**
     * @return the vtCSI
     */
    public Boolean getVtCSI() {
        return vtCSI;
    }

    /**
     * @param vtCSI the vtCSI to set
     */
    public void setVtCSI(Boolean vtCSI) {
        this.vtCSI = vtCSI;
    }

    /**
     * @return the tCSI
     */
    public Boolean gettCSI() {
        return tCSI;
    }

    /**
     * @param tCSI the tCSI to set
     */
    public void settCSI(Boolean tCSI) {
        this.tCSI = tCSI;
    }

    /**
     * @return the mtSMSCSI
     */
    public Boolean getMtSMSCSI() {
        return mtSMSCSI;
    }

    /**
     * @param mtSMSCSI the mtSMSCSI to set
     */
    public void setMtSMSCSI(Boolean mtSMSCSI) {
        this.mtSMSCSI = mtSMSCSI;
    }

    /**
     * @return the mgCSI
     */
    public Boolean getMgCSI() {
        return mgCSI;
    }

    /**
     * @param mgCSI the mgCSI to set
     */
    public void setMgCSI(Boolean mgCSI) {
        this.mgCSI = mgCSI;
    }

    /**
     * @return the psiEnhancements
     */
    public Boolean getPsiEnhancements() {
        return psiEnhancements;
    }

    /**
     * @param psiEnhancements the psiEnhancements to set
     */
    public void setPsiEnhancements(Boolean psiEnhancements) {
        this.psiEnhancements = psiEnhancements;
    }
}
