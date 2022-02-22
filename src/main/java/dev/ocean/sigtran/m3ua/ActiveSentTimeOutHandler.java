/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import dev.ocean.state.machines.TimeOutHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;


/**
 *
 * @author eatakishiyev
 */
public class ActiveSentTimeOutHandler extends TimeOutHandler {

    private final Logger logger = LogManager.getLogger(ActiveSentTimeOutHandler.class);
    private final Asp asp;
    private final AspStateMachine aspStateMachine;

    public ActiveSentTimeOutHandler(Asp asp, AspStateMachine aspStateMachine) {
        this.asp = asp;
        this.aspStateMachine = aspStateMachine;
    }

    @Override
    public void onTimeOut() {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Timeout for sent ASP_ACTIVE message expired. Trying to resend ASP_ACTIVE. Asp = %s", asp.getName()));
            }

            this.asp.sendAspActive(aspStateMachine.getAs());
            this.aspStateMachine.doTransition("SEND_ACTIVE");
        } catch (Exception ex) {
            logger.error("Error occured while retrying activate the asp", ex);
        }
    }
}
