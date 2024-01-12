/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map;

import azrc.az.sigtran.tcap.TCAPProvider;
import azrc.az.sigtran.tcap.TCUser;

/**
 *
 * @author eatakishiyev
 */
public interface MAPStack extends TCUser {

     void addMapUser(MAPListener mapUser);
    
     MAPProvider getMAPProvider();
    
     TCAPProvider getTcapProvider();
    
     MAPDialogueFactory getMAPDialogueFactory();
}
