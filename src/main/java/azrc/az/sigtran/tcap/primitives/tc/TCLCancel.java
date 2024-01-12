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
public class TCLCancel implements Operation {

    private TCAPDialogue dialogue;
    private Short invokeId;
    private int opCode;

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
    public Short getInvokeId() {
        return invokeId;
    }

    /**
     * @param invokeId the invokeId to set
     */
    public void setInvokeId(Short invokeId) {
        this.invokeId = invokeId;
    }

    @Override
    public Operations getOperation() {
        return Operations.TCLCANCEL;
    }

    public void setOperationCode(int opCode) {
        this.opCode = opCode;
    }

    public int getOperationCode() {
        return this.opCode;
    }
}
