/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.primitives.tc;

import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.parameters.OperationCodeImpl;
import dev.ocean.sigtran.tcap.parameters.ParameterImpl;

/**
 *
 * @author root
 */
public class TCResult implements Operation {

    private TCAPDialogue dialogue;
    private Short invokeId;
    private OperationCodeImpl operationCode;//OpCode
    private ParameterImpl parameters;

    public TCResult() {
    }

    public TCResult(Short invokeId, TCAPDialogue dialogue){
        this.invokeId = invokeId;
        this.dialogue = dialogue;
    }
    
    public TCResult(Short invokeId, OperationCodeImpl operationCode, ParameterImpl parameters, TCAPDialogue dialogue) {
        this.invokeId = invokeId;
        this.operationCode = operationCode;
        this.parameters = parameters;
        this.dialogue = dialogue;
    }

    public TCResult(Short invokeId, ParameterImpl parameters, TCAPDialogue dialogue) {
        this.invokeId = invokeId;
        this.parameters = parameters;
        this.dialogue = dialogue;
        operationCode = null;
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
     * @return the parameters
     */
    public ParameterImpl getParameter() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(ParameterImpl parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the operation
     */
    public OperationCodeImpl getOperationCode() {
        return operationCode;
    }

    /**
     * @param operationCode
     * @param operation the operation to set
     */
    public void setOperationCode(OperationCodeImpl operationCode) {
        this.operationCode = operationCode;
    }

    @Override
    public Operations getOperation() {
        return Operations.TCRETURNRESULTLAST;
    }
}
