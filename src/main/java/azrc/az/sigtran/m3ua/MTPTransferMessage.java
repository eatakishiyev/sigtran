/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import azrc.az.sigtran.utils.ByteUtils;


/**
 *
 * @author eatakishiyev
 */
public class MTPTransferMessage {

    protected int mp = 0;
//  SIF - Service Information Field  
    protected int opc;
    protected int dpc;
    protected int sls;
    protected byte[] userData;
//  SIO - Service Information Octet
    protected ServiceIdentificator si;
    protected NetworkIndicator ni;

    public MTPTransferMessage(int opc, int dpc, int sls, ServiceIdentificator si, int mp, NetworkIndicator ni, byte[] userData) {
        this.opc = opc;
        this.dpc = dpc;
        this.sls = sls;
        this.si = si;
        this.mp = mp;
        this.ni = ni;
        this.userData = userData;
    }

    /**
     * @return the opc
     */
    public int getOpc() {
        return opc;
    }

    /**
     * @param opc the opc to set
     */
    public void setOpc(int opc) {
        this.opc = opc;
    }

    /**
     * @return the dpc
     */
    public int getDpc() {
        return dpc;
    }

    /**
     * @param dpc the dpc to set
     */
    public void setDpc(int dpc) {
        this.dpc = dpc;
    }

    /**
     * @return the sls
     */
    public int getSls() {
        return sls;
    }

    /**
     * @param sls the sls to set
     */
    public void setSls(int sls) {
        this.sls = sls;
    }

    /**
     * @return the sio
     */
    public ServiceIdentificator getSi() {
        return si;
    }

    /**
     * @param si
     */
    public void setSi(ServiceIdentificator si) {
        this.si = si;
    }

    /**
     * @return the userData
     */
    public byte[] getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(byte[] userData) {
        this.userData = userData;
    }

    /**
     * @return the mp
     */
    public int getMp() {
        return mp;
    }

    /**
     * @param mp the mp to set
     */
    public void setMp(int mp) {
        this.mp = mp;
    }

    /**
     * @return the ni
     */
    public NetworkIndicator getNi() {
        return ni;
    }

    /**
     * @param ni the ni to set
     */
    public void setNi(NetworkIndicator ni) {
        this.ni = ni;
    }

    @Override
    public String toString() {
        
        return ("MtpTransgerMessage: [OPC = " + opc
                + "; DPC = " + dpc
                + "; MP = " + mp
                + "; NI = " + ni
                + "; SI = " + si
                + "; SLS = " + sls
                + "; UserData = " + ByteUtils.bytes2Hex(userData) + "]");
    }

}
