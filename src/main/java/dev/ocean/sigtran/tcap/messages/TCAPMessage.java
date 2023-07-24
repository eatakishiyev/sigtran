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
public interface TCAPMessage extends Encodable {

    /**
     *
     * @return Long value of TCAP Dialog OTID. Null in case of absence.
     */
    public Long getOriginatingTransactionId();

    public void setOriginatingTransactionId(Long id);

    /**
     * 
     * @return Long value of TCAP Dialog DTID. Null in case of absence.
     */
    public Long getDestinationTransactionId();

    public void setDestinationTransactionId(Long trid);

    public byte[] getComponents();

    public byte[] getDialogPortion();

    public MessageType getMessageType();
}
