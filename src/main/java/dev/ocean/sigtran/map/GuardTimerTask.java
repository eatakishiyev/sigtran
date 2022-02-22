/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map;

/**
 *
 * @author eatakishiyev
 */
public class GuardTimerTask implements Runnable {

    private final MAPDialogue dialogue;
    private final Short invokeId;

    public GuardTimerTask(MAPDialogue dialogue, Short invokeId) {
        this.dialogue = dialogue;
        this.invokeId = invokeId;
    }

    @Override
    public void run() {
        dialogue.terminatePerformingSSM(invokeId);
    }

}
