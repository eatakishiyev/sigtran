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

     ProtocolVersion getProtocolVersion();

    void setProtocolVersion(ProtocolVersion protocolVersion);

     void setApplicationContext(ApplicationContext ac);

     ApplicationContext getApplicationContext();
}
