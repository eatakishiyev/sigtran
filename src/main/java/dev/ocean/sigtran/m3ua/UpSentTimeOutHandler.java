/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import dev.ocean.state.machines.TimeOutHandler;


/**
 *
 * @author eatakishiyev
 */
public class UpSentTimeOutHandler extends TimeOutHandler {

    private final Asp asp;

    public UpSentTimeOutHandler(Asp asp) {
        this.asp = asp;
    }

    @Override
    public void onTimeOut() {
        asp.sendAspUp();
    }
}
