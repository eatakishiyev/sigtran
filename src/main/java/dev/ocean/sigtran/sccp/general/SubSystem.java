/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.general;

import java.io.IOException;
import java.io.Serializable;
import org.apache.logging.log4j.*;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.address.SubSystemNumber;
import dev.ocean.sigtran.sccp.messages.connectionless.SCCPMessage;

/**
 *
 * @author eatakishiyev
 */
public class SubSystem implements Serializable {

    private State state = State.IDLE;
    private SubSystemNumber ssn;
    private final Logger logger = LogManager.getLogger(SubSystem.class);
    private final SCCPStackImpl sccpStack;

    protected SubSystem(SubSystemNumber ssn, SCCPStackImpl sccpStack) throws IOException {
        this.ssn = ssn;
        this.sccpStack = sccpStack;
        this.state = State.RUNNING;
        logger.info(String.format("Subsystem initialized. SSN = %s", ssn));
    }

    public void start() {
        this.state = State.RUNNING;
    }

    public void stop() {
        this.state = State.IDLE;
    }

    //Delegate
    public void onMessage(SCCPMessage message) {
        boolean sequenceControl = false;
        switch (message.getProtocolClass()) {
            case CLASS1:
            case CLASS3:
                sequenceControl = true;
                break;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Firing received data to the local SSN " + ssn + message);
        }
        sccpStack.getSCCPUser(ssn).onMessage(message.getCalledPartyAddress(),
                message.getCallingPartyAddress(), message.getData(),
                sequenceControl, this.sccpStack.getSlsGenerator().generate(),
                message.getMessageHandling());
    }

    public void onNotice(SCCPAddress calledParty, SCCPAddress callingParty, byte[] userData,
            ErrorReason reason, int messagePriority) {
        logger.warn("onNotice received for subsystem " + ssn + ". " + callingParty
                + ". " + calledParty + ". Reason = " + reason);
        sccpStack.getSCCPUser(ssn).onNotice(calledParty, callingParty, userData,
                reason, messagePriority);
    }

    /**
     * @return the state
     */
    public State getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * @return the ssn
     */
    public SubSystemNumber getSsn() {
        return ssn;
    }

    /**
     * @param ssn the ssn to set
     */
    public void setSsn(SubSystemNumber ssn) {
        this.ssn = ssn;
    }

    public enum State {

        IDLE,
        RUNNING;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("SSN = %s\r\n", this.ssn));
        sb.append(String.format("STATE = %s\r\n", this.state));
        return sb.toString();
    }
}
