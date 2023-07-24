/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.messages;

import dev.ocean.sigtran.tcap.Encodable;

/**
 *
 * @author root
 */
public interface EndMessage extends Encodable, TCAPMessage {

    public void setDialoguePortion(byte[] dp);

    public void setComponents(byte[] components);
}
