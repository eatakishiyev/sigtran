/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.primitives.tc;

import azrc.az.sigtran.tcap.TCAPDialogue;


/**
 *
 * @author root
 */
public class TCTimerReset implements Operation {

    private TCAPDialogue dialogue;
    private Integer invokeId;
    private boolean lastComponent;

    /**
     * @return the dialogue
     */
    @Override
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
     * @return the invokeId
     */
    public Integer getInvokeId() {
        return invokeId;
    }

    /**
     * @param invokeId the invokeId to set
     */
    public void setInvokeId(Integer invokeId) {
        this.invokeId = invokeId;
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

    @Override
    public Operations getOperation() {
        return Operations.TCTIMERRESET;
    }
}
