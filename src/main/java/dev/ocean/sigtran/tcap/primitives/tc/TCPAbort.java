/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.primitives.tc;

import dev.ocean.sigtran.tcap.QoS;
import dev.ocean.sigtran.tcap.TCAPDialogue;

/**
 *
 * @author root
 */
public class TCPAbort {

    private TCAPDialogue dialogue;
    private PAbortCause pAbort;
    private boolean lastComponent;

    /**
     * @return the dialogue
     */
    public TCAPDialogue getDialogue() {
        return dialogue;
    }

    /**
     * @param dialogue the dialogue to set
     */
    public void setDialogue(TCAPDialogue dialogue) {
        this.dialogue = dialogue;
    }

    /**
     * @return the pAbort
     */
    public PAbortCause getpAbort() {
        return pAbort;
    }

    /**
     * @param pAbort the pAbort to set
     */
    public void setpAbort(PAbortCause pAbort) {
        this.pAbort = pAbort;
    }

    /**
     * @return the lastComponent
     */
    public boolean isLastComponent() {
        return lastComponent;
    }

    /**
     * @param lastComponent the lastComponent to set
     */
    public void setLastComponent(boolean lastComponent) {
        this.lastComponent = lastComponent;
    }
}
