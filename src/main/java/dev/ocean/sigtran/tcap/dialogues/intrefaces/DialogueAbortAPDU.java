/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.dialogues.intrefaces;

import dev.ocean.sigtran.tcap.parameters.AbortSource;

/**
 *
 * @author root
 */
public interface DialogueAbortAPDU extends DialoguePDU {

    public void setAbortSource(AbortSource abortSource);

    public AbortSource getAbortSource();
}
