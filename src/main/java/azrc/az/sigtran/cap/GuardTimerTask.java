/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap;

/**
 *
 * @author eatakishiyev
 */
public class GuardTimerTask implements Runnable {

    private final CAPDialogue dialogue;
    private final Short invokeId;

    public GuardTimerTask(CAPDialogue dialogue, Short invokeId) {
        this.dialogue = dialogue;
        this.invokeId = invokeId;
    }

    @Override
    public void run() {
        dialogue.terminatePerformingSSM(invokeId);
    }

}
