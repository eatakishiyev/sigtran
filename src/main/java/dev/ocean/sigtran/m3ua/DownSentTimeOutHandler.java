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
public class DownSentTimeOutHandler extends TimeOutHandler {

    private final Asp asp;
    private final Logger logger = LogManager.getLogger(DownSentTimeOutHandler.class);

    public DownSentTimeOutHandler(Asp asp) {
        this.asp = asp;
    }

    @Override
    public void onTimeOut() {
        try {
            asp.stop();
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }
}
