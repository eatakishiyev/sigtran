/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.primitives.tc;

import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.parameters.ProblemImpl;

/**
 *
 * @author root
 */
public class TCLReject implements Operation {

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

    @Override
    public Operations getOperation() {
        return Operations.TCLREJECT;
    }

    public boolean isInvokeIdPresent() {
        return invokeId != null;
    }
}
