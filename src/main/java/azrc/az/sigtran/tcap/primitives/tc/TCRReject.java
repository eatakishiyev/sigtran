/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.primitives.tc;

import azrc.az.sigtran.tcap.TCAPDialogue;
import azrc.az.sigtran.tcap.parameters.ProblemImpl;

/**
 *
 * @author root
 */
public class TCRReject implements Operation {

    private TCAPDialogue dialogue;
    private Short invokeId;
    private ProblemImpl problemCode;
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
    public Short getInvokeId() {
        return invokeId;
    }

    /**
     * @param invokeId the invokeId to set
     */
    public void setInvokeId(Short invokeId) {
        this.invokeId = invokeId;
    }

    /**
     * @return the problemCode
     */
    public ProblemImpl getProblem() {
        return problemCode;
    }

    /**
     * @param problemCode the problemCode to set
     */
    public void setProblem(ProblemImpl problemCode) {
        this.problemCode = problemCode;
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
        return Operations.TCRREJECT;
    }
}
