/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
public class OfferedCamel4Functionalities {
    
    private boolean oCsi;
    private boolean dCsi;
    private boolean vtCsi;
    private boolean tCsi;
    private boolean mtSmsCsi;
    private boolean mgCsi;
    private boolean psiEnhancements;
    
    public OfferedCamel4Functionalities() {
    }
    
    public OfferedCamel4Functionalities(boolean oCsi, boolean dCsi, boolean vtCsi, boolean tCsi, boolean mtSmsCsi, boolean mgCsi, boolean psiEnhancements) {
        this.oCsi = oCsi;
        this.dCsi = dCsi;
        this.vtCsi = vtCsi;
        this.tCsi = tCsi;
        this.mtSmsCsi = mtSmsCsi;
        this.mgCsi = mgCsi;
        this.psiEnhancements = psiEnhancements;
    }
    
    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        BitSet bitSet = new BitSet();
        bitSet.set(0, oCsi);
        bitSet.set(1, dCsi);
        bitSet.set(2, vtCsi);
        bitSet.set(3, tCsi);
        bitSet.set(4, mtSmsCsi);
        bitSet.set(5, mgCsi);
        bitSet.set(6, psiEnhancements);
        aos.writeStringBinary(bitSet);
    }
    
    public void decode(AsnInputStream ais) throws AsnException, IOException {
        BitSet bitSet = new BitSet();
        ais.readBitString(bitSet);
        
        this.oCsi = bitSet.get(0);
        this.dCsi = bitSet.get(1);
        this.vtCsi = bitSet.get(2);
        this.tCsi = bitSet.get(3);
        this.mtSmsCsi = bitSet.get(4);
        this.mgCsi = bitSet.get(5);
        this.psiEnhancements = bitSet.get(6);
    }

    /**
     * @return the oCsi
     */
    public boolean isoCsi() {
        return oCsi;
    }

    /**
     * @param oCsi the oCsi to set
     */
    public void setoCsi(boolean oCsi) {
        this.oCsi = oCsi;
    }

    /**
     * @return the dCsi
     */
    public boolean isdCsi() {
        return dCsi;
    }

    /**
     * @param dCsi the dCsi to set
     */
    public void setdCsi(boolean dCsi) {
        this.dCsi = dCsi;
    }

    /**
     * @return the vtCsi
     */
    public boolean isVtCsi() {
        return vtCsi;
    }

    /**
     * @param vtCsi the vtCsi to set
     */
    public void setVtCsi(boolean vtCsi) {
        this.vtCsi = vtCsi;
    }

    /**
     * @return the tCsi
     */
    public boolean istCsi() {
        return tCsi;
    }

    /**
     * @param tCsi the tCsi to set
     */
    public void settCsi(boolean tCsi) {
        this.tCsi = tCsi;
    }

    /**
     * @return the mtSmsCsi
     */
    public boolean isMtSmsCsi() {
        return mtSmsCsi;
    }

    /**
     * @param mtSmsCsi the mtSmsCsi to set
     */
    public void setMtSmsCsi(boolean mtSmsCsi) {
        this.mtSmsCsi = mtSmsCsi;
    }

    /**
     * @return the mgCsi
     */
    public boolean isMgCsi() {
        return mgCsi;
    }

    /**
     * @param mgCsi the mgCsi to set
     */
    public void setMgCsi(boolean mgCsi) {
        this.mgCsi = mgCsi;
    }

    /**
     * @return the psiEnhancements
     */
    public boolean isPsiEnhancements() {
        return psiEnhancements;
    }

    /**
     * @param psiEnhancements the psiEnhancements to set
     */
    public void setPsiEnhancements(boolean psiEnhancements) {
        this.psiEnhancements = psiEnhancements;
    }
    
}
