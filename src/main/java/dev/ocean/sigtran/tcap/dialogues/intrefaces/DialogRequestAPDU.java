/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.dialogues.intrefaces;

import dev.ocean.sigtran.tcap.parameters.ApplicationContextImpl;
import dev.ocean.sigtran.tcap.parameters.ProtocolVersionImpl;

/**
 *
 * @author root
 */
public interface DialogRequestAPDU extends DialoguePDU {

    public ProtocolVersionImpl getProtocolVersion();

    public void setApplicationContext(ApplicationContextImpl ac);

    public ApplicationContextImpl getApplicationContext();
}
