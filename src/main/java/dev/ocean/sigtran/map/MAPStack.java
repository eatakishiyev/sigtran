/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map;

import dev.ocean.sigtran.map.parameters.MAPApplicationContextName;
import dev.ocean.sigtran.tcap.TCAPProvider;
import dev.ocean.sigtran.tcap.TCUser;

/**
 *
 * @author eatakishiyev
 */
public interface MAPStack extends TCUser{

    public void addMapUser(MAPApplicationContextName acName, MAPListener mapUser);
    
    public MAPProvider getMAPProvider();
    
    public TCAPProvider getTcapProvider();
    
    public MAPDialogueFactory getMAPDialogueFactory();
}
