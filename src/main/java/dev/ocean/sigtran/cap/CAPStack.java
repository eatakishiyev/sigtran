/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap;

import dev.ocean.sigtran.tcap.TCAPProvider;
import dev.ocean.sigtran.tcap.TCUser;

/**
 *
 * @author eatakishiyev
 */
public interface CAPStack extends TCUser {

    /**
     * @return the capProvider
     */
    public CAPProvider getCAPProvider();

    /**
     * @return the tcapProvider
     */
    public TCAPProvider getTcapProvider();

    public void setCAPUser(CAPUser user);
    
    public CAPDialogueFactory getCAPDialogueFactory();

}
