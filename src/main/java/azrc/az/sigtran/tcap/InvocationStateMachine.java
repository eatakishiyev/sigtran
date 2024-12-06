/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap;

import azrc.az.sigtran.tcap.primitives.tc.OperationClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author eatakishiyev
 */
public class InvocationStateMachine implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(InvocationStateMachine.class);
    protected short invokeId;
    protected long timeOutInSeconds;
    protected OperationClass operationClass;

    private TCAPDialogue dialogue;
    private Future invocationTimer;
    private Future rejectTimer;

    private TCAPStack stack;
    private int operationCode;

    public InvocationStateMachine(OperationClass operationClass) {
        this.operationClass = operationClass;
    }

    public InvocationStateMachine(TCAPStack stack, TCAPDialogue dialogue, short invokeId, long timeOutInSeconds, OperationClass operationClass, int operationCode) {
        this.stack = stack;
        this.invokeId = invokeId;
        this.timeOutInSeconds = timeOutInSeconds;
        this.operationClass = operationClass;
        this.dialogue = dialogue;
        this.operationCode = operationCode;
    }

    @Override
    public String toString() {
        return String.format("InvocationStateMachine[InvokeId = %s; "
                        + "TimeOutInSeconds = %s; OperationClass = %s; OperationCode = %s; "
                        + "Dialogue = %s]", invokeId, timeOutInSeconds, operationClass,
                operationCode, dialogue);
    }

    public short getInvokeId() {
        return invokeId;
    }

    public OperationClass getOperationClass() {
        return operationClass;
    }

    public long getTimeOutInSeconds() {
        return timeOutInSeconds;
    }

    protected void startInvocationTimer() {
        this.invocationTimer = stack.invocationScheduler.schedule(this, timeOutInSeconds, TimeUnit.SECONDS);
    }

    protected void stopInvocationTimer() {
        if (invocationTimer != null) {
            invocationTimer.cancel(true);
            invocationTimer = null;
        }
    }

    protected void stopRejectTimer() {
        if (rejectTimer != null) {
            rejectTimer.cancel(true);
            rejectTimer = null;
        }
    }


    protected void startRejectTimer() throws IllegalStateException {
        if (rejectTimer != null) {
            throw new IllegalStateException();
        } else {
            rejectTimer = stack.rejectScheduler.schedule(this, timeOutInSeconds, TimeUnit.SECONDS);
        }
    }

    void terminate() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Invocation state machine terminated. InvokeId = "
                    + invokeId + ": Dialogue = " + dialogue);
        }

        stopRejectTimer();
        stopInvocationTimer();
    }

    public int getOperationCode() {
        return operationCode;
    }

    @Override
    public void run() {

        if (invocationTimer != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Invocation timer expired. " + this);
            }
            stack.onTCLCancel(dialogue, invokeId, operationCode);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Reject timer expired. " + this);
            }
        }
        dialogue.terminateIsm(invokeId);
    }

}
