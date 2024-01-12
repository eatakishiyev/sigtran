/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.dialogues.intrefaces;

import azrc.az.sigtran.tcap.parameters.AbortSource;

/**
 *
 * @author root
 */
public interface DialogueAbortPDU extends DialoguePDU {

    public void setAbortSource(AbortSource abortSource);

    public AbortSource getAbortSource();
}
