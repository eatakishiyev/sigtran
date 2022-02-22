/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.primitives.tc;

import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.parameters.ErrorCodeImpl;
import dev.ocean.sigtran.tcap.parameters.Parameter;
import dev.ocean.sigtran.tcap.parameters.ParameterImpl;

/**
 *
 * @author root
 */
public class TCUError implements Operation {

    private TCAPDialogue dialogue;
    private Short invokeId;
    private ErrorCodeImpl error;
    private Parameter parameter;

    public TCUError() {
    }

    public TCUError(Short invokeId, ErrorCodeImpl error, TCAPDialogue dialogue) {
        this.invokeId = invokeId;
        this.error = error;
        this.dialogue = dialogue;
    }

    public TCUError(Short invokeId, ErrorCodeImpl error, ParameterImpl parameter, TCAPDialogue dialogue) {
        this(invokeId, error, dialogue);
        this.parameter = parameter;
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
     * @return the error
     */
    public ErrorCodeImpl getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(ErrorCodeImpl error) {
        this.error = error;
    }

    /**
     * @return the parameters
     */
    public Parameter getParameter() {
        return this.parameter;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public Operations getOperation() {
        return Operations.TCUERROR;
    }
}
