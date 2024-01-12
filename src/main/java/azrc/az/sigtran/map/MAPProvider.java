/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map;

import azrc.az.sigtran.tcap.TCAPProvider;

/**
 *
 * @author eatakishiyev
 */
public interface MAPProvider {

    public TCAPProvider getTcapProvider();

    public MAPDialogue getMapDialogue(long dialogueId);
    
    public MAPDialogueFactory getDialogueFactory();
}
