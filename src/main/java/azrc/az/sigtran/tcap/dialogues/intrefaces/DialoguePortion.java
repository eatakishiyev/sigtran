/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.dialogues.intrefaces;

import azrc.az.sigtran.tcap.Encodable;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface DialoguePortion extends Encodable {

    public static final int DIALOGUE_PORTION_TAG = 0x0B;
    public static final boolean IS_DIALOGUE_PORTION_TAG_PRIMITIVE = false;
    public static final int DIALOGUE_PORTION_TAG_CLASS = Tag.CLASS_APPLICATION;

    public void setDialogAPDU(DialoguePDU dialogue);

    public DialoguePDU getDialogueAPDU();

    public void setOid(boolean Oid);

    public boolean isOid();

    public void setAsn(boolean asn);

    public boolean isAsn();

    public void setOidValue(long[] oid);

    public long[] getOidValue();
}
