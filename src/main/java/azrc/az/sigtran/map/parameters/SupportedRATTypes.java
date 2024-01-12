/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import java.util.BitSet;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class SupportedRATTypes {

    private Boolean utran;
    private Boolean geran;
    private Boolean gan;
    private Boolean iHSPAEvolution;
    private Boolean eUtran;

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        BitSet bitSet = new BitSet();
        bitSet.set(0, utran);
        bitSet.set(1, geran);
        bitSet.set(2, gan);
        bitSet.set(3, iHSPAEvolution);
        bitSet.set(4, eUtran);
        aos.writeStringBinary(tagClass, tag, bitSet);
    }

    public void decode(AsnInputStream ais) throws IOException, AsnException {
        BitSet bitSet = new BitSet();
        ais.readBitString(bitSet);
        utran = bitSet.get(0);
        geran = bitSet.get(1);
        gan = bitSet.get(2);
        iHSPAEvolution = bitSet.get(3);
        eUtran = bitSet.get(4);
    }

    /**
     * @return the utran
     */
    public Boolean getUtran() {
        return utran;
    }

    /**
     * @param utran the utran to set
     */
    public void setUtran(Boolean utran) {
        this.utran = utran;
    }

    /**
     * @return the geran
     */
    public Boolean getGeran() {
        return geran;
    }

    /**
     * @param geran the geran to set
     */
    public void setGeran(Boolean geran) {
        this.geran = geran;
    }

    /**
     * @return the gan
     */
    public Boolean getGan() {
        return gan;
    }

    /**
     * @param gan the gan to set
     */
    public void setGan(Boolean gan) {
        this.gan = gan;
    }

    /**
     * @return the iHSPAEvolution
     */
    public Boolean getiHSPAEvolution() {
        return iHSPAEvolution;
    }

    /**
     * @param iHSPAEvolution the iHSPAEvolution to set
     */
    public void setiHSPAEvolution(Boolean iHSPAEvolution) {
        this.iHSPAEvolution = iHSPAEvolution;
    }

    /**
     * @return the eUtran
     */
    public Boolean geteUtran() {
        return eUtran;
    }

    /**
     * @param eUtran the eUtran to set
     */
    public void seteUtran(Boolean eUtran) {
        this.eUtran = eUtran;
    }
}
