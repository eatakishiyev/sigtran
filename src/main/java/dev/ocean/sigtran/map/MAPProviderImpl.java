/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map;

import dev.ocean.sigtran.tcap.TCAPProvider;

/**
 *
 * @author root
 */
public class MAPProviderImpl implements MAPProvider {

    private transient final MAPStackImpl stack;

    protected MAPProviderImpl(MAPStackImpl stack) {
        this.stack = stack;
    }

    //Delegator
    @Override
    public MAPDialogue getMapDialogue(long dialogueId) {
        return this.stack.getMapDialogue(dialogueId);
    }


    /**
     * @return the tcapProvider
     */
    @Override
    public TCAPProvider getTcapProvider() {
        return stack.getTcapProvider();
    }

    @Override
    public MAPDialogueFactory getDialogueFactory() {
        return stack.mapDialogueFactory;
    }

}
