/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.dialogues.intrefaces;

import azrc.az.sigtran.tcap.parameters.interfaces.ApplicationContext;
import azrc.az.sigtran.tcap.parameters.interfaces.ProtocolVersion;

/**
 *
 * @author root
 */
public interface DialogRequestAPDU extends DialoguePDU {

    public ProtocolVersion getProtocolVersion();

    public void setApplicationContext(ApplicationContext ac);

    public ApplicationContext getApplicationContext();
}
