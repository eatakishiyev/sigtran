/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.primitives.tc;

import dev.ocean.sigtran.tcap.TCAPDialogue;


/**
 *
 * @author eatakishiyev
 */
public interface Operation {

    public Operations getOperation();

    public TCAPDialogue getDialogue();

    public enum Operations {

        TCINVOKE,
        TCLCANCEL,
        TCLREJECT,
        TCRREJECT,
        TCTIMERRESET,
        TCUCANCEL,
        TCUERROR,
        TCUREJECT,
        TCRETURNRESULTLAST,
        TCRETURNRESULTNOTLAST, TCUABORT;
    }
}
