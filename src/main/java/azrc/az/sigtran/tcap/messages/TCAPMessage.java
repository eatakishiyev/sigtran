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
