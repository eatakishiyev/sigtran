/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.primitives.tc;

import dev.ocean.sigtran.tcap.TCAPDialogue;
import dev.ocean.sigtran.tcap.parameters.ParameterImpl;

/**
 *
 * @author root
 */
public class TCInvoke implements Operation {

    private TCAPDialogue dialogue;
    private OperationClass operationClass;
    private Short invokeID;
    private Short linkedId;
    private Integer operationCodes;//OpCode
    private ParameterImpl parameters;
    private long timeout;

    public TCInvoke() {
    }

    public TCInvoke(Short invokeId, Integer operationCode, OperationClass operationClass, long timeout, TCAPDialogue dialogue) {
        this.invokeID = invokeId;
        this.operationCodes = operationCode;
        this.operationClass = operationClass;
        this.timeout = timeout;
        this.dialogue = dialogue;
    }

    public TCInvoke(Short invokeId, Integer operationCode, OperationClass operationClass, ParameterImpl parameters, long timeout,
            TCAPDialogue dialogue) {
        this.invokeID = invokeId;
        this.operationCodes = operationCode;
        this.operationClass = operationClass;
        this.parameters = parameters;
        this.timeout = timeout;
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
     * @return the operationClass
     */
    public OperationClass getOperationClass() {
        return operationClass;
    }

    /**
     * @param operationClass the operationClass to set
     */
    public void setOperationClass(OperationClass operationClass) {
        this.operationClass = operationClass;
    }

    /**
     * @return the invokeID
     */
    public Short getInvokeID() {
        return invokeID;
    }

    /**
     * @param invokeID the invokeID to set
     */
    public void setInvokeID(Short invokeID) {
        this.invokeID = invokeID;
    }

    /**
     * @return the linkedId
     */
    public Short getLinkedId() {
        return linkedId;
    }

    /**
     * @param linkedId the linkedId to set
     */
    public void setLinkedId(Short linkedId) {
        this.linkedId = linkedId;
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
    public void setParameter(ParameterImpl parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the timeout
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * @return the Operation
     */
    public Integer getOperationCode() {
        return operationCodes;
    }

    /**
     * @param operationCodes
     */
    public void setOperation(Integer operationCodes) {
        this.operationCodes = operationCodes;
    }

    @Override
    public Operations getOperation() {
        return Operations.TCINVOKE;
    }

    @Override
    public String toString() {
        return String.format("OperationClass = %s InvokeId = %d LinkedId = %d OperationId = %d TimeOut = %d",
                operationClass, invokeID, linkedId, operationCodes, timeout);
    }
}
