/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.primitives.tc;

import azrc.az.sigtran.tcap.TCAPDialogue;
import azrc.az.sigtran.tcap.parameters.OperationCodeImpl;
import azrc.az.sigtran.tcap.parameters.ParameterImpl;

/**
 *
 * @author eatakishiyev
 */
public class TCResultNotLast implements Operation {

    private TCAPDialogue dialogue;
    private Short invokeId;
    private OperationCodeImpl operationCode;//OpCode
    private ParameterImpl parameters;

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
     */
    public void setOperationCode(OperationCodeImpl operationCode) {
        this.operationCode = operationCode;
    }

    @Override
    public Operations getOperation() {
        return Operations.TCRETURNRESULTNOTLAST;
    }
}
