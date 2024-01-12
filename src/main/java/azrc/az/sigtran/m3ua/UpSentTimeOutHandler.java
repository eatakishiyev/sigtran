/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import azrc.az.state.machines.TimeOutHandler;


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
