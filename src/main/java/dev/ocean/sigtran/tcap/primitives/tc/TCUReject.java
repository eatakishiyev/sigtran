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
public class TCUReject implements Operation {

    private TCAPDialogue dialogue;
    private Short invokeId;
    private ProblemImpl problem;
    private boolean lastComponent;

    public TCUReject() {
    }

    public TCUReject(Short invokeId, ProblemImpl problem, TCAPDialogue dialogue) {
        this.invokeId = invokeId;
        this.problem = problem;
        this.dialogue = dialogue;
    }

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
     * @return the problem
     */
    public ProblemImpl getProblem() {
        return problem;
    }

    /**
     * @param problem the problem to set
     */
    public void setProblem(ProblemImpl problem) {
        this.problem = problem;
    }

    @Override
    public Operations getOperation() {
        return Operations.TCUREJECT;
    }
}
