/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.messages;

import azrc.az.sigtran.tcap.Encodable;

/**
 *
 * @author root
 */
public interface BeginMessage extends Encodable, TCAPMessage {

    public void setDialoguePortion(byte[] dialoguePortion);

    public void setComponents(byte[] component);
}
