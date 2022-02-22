/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.general;

/**
 *
 * @author eatakishiyev
 */
public class SequenceControl {

    private int seqControl;
    private String calledParty;
    private int sls;

    public SequenceControl(int seqControl, String calledParty, int sls) {
        this.seqControl = seqControl;
        this.calledParty = calledParty;
        this.sls = sls;
    }

    /**
     * @return the seqControl
     */
    public int getSeqControl() {
        return seqControl;
    }

    /**
     * @param seqControl the seqControl to set
     */
    public void setSeqControl(int seqControl) {
        this.seqControl = seqControl;
    }

    /**
     * @return the calledParty
     */
    public String getCalledParty() {
        return calledParty;
    }

    /**
     * @param calledParty the calledParty to set
     */
    public void setCalledParty(String calledParty) {
        this.calledParty = calledParty;
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
}
