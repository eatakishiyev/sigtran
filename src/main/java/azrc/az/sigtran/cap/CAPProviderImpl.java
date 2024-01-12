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
public class CAPProviderImpl implements CAPProvider {

    private transient final CAPStackImpl stack;

    public CAPProviderImpl(CAPStackImpl stack) {
        this.stack = stack;
    }

    @Override
    public CAPDialogue getDialogue(long dialogueId) {
        return stack.getCAPDialogue(dialogueId);
    }
    
    public CAPDialogueFactory getCAPDialogueFactory(){
        return stack.getCAPDialogueFactory();
    }
}
