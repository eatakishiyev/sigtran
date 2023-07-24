/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.messages;

import dev.ocean.sigtran.tcap.Encodable;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface BeginMessage extends Encodable, TCAPMessage {

    public void setDialoguePortion(byte[] dialoguePortion);

    public void setComponents(byte[] component);
}
