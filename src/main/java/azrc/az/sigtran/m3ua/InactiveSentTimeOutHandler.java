/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import azrc.az.state.machines.TimeOutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author eatakishiyev
 */
public class InactiveSentTimeOutHandler extends TimeOutHandler {

    private final Asp asp;
    private final Logger logger = LoggerFactory.getLogger(InactiveSentTimeOutHandler.class);

    public InactiveSentTimeOutHandler(Asp asp) {
        this.asp = asp;
    }

    @Override
    public void onTimeOut() {
        try {
            asp.sendAspInactive();
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }
}
