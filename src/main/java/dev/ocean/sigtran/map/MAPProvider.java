/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map;

import dev.ocean.sigtran.tcap.TCAPProvider;

/**
 *
 * @author eatakishiyev
 */
public interface MAPProvider {

    public TCAPProvider getTcapProvider();

    public MAPDialogue getMapDialogue(long dialogueId);
    
    public MAPDialogueFactory getDialogueFactory();
}
