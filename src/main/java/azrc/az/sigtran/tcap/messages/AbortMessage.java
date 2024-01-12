/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.messages;

import azrc.az.sigtran.tcap.primitives.tc.PAbortCause;

/**
 *
 * @author root
 */
public interface AbortMessage extends TCAPMessage {

//    public static final int ABORT_MESSAGE_TAG = MessageType.ABORT.value();
//    public static final int ABORT_MESSAGE_CLASS = Tag.CLASS_APPLICATION;
//    public static final boolean IS_ABORT_MESSAGE_PRIMITIVE = false;
////    public static final int ABORT_MESSAGE_TAG = 0x07;//ITU Q.774 4.2.1.2
//    public static final int P_ABORT_CAUSE_TAG_CLASS = Tag.CLASS_APPLICATION;
//    public static final boolean P_ABORT_CAUSE_TAG_PC = true;
//    public static final int P_ABORT_CAUSE_TAG = 0x0A;//ITU Q.773 4.2.1.4

    public Long getDestinationTransactionId();

    public void setDestinationTransactionId(Long trid);
    
    public byte[] getUAbortCause();

    public void setUAbortCause(byte[] dp);

    public PAbortCause getPAbortCause();

    public void setPAbortCause(PAbortCause cause);
}
