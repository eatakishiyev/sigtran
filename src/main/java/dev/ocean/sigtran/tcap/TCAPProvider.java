/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap;

import dev.ocean.sigtran.sccp.address.SCCPAddress;

/**
 *
 * @author eatakishiyev
 */
public interface TCAPProvider {

    //Localy initiate dialogue
    public TCAPDialogue createDialogue(QoS qos) throws ResourceLimitationException;

    public void send(SCCPAddress calledParty, SCCPAddress callingParty, QoS qos,
            byte[] userData);

    public void setUser(TCUser user);

    public long getConcurrentDialogues();
}
