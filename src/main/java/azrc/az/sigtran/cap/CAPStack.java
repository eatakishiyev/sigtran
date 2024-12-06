/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap;

import azrc.az.sigtran.tcap.TCAPProvider;
import azrc.az.sigtran.tcap.TCUser;

import java.util.concurrent.ConcurrentHashMap;

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

    ConcurrentHashMap<Long, CAPDialogue> getDialogues();
}
