/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.parameters;

import azrc.az.sigtran.tcap.Encodable;
import org.mobicents.protocols.asn.Tag;

/**
 * @author root
 */
public interface UserInformation extends Encodable {

    public static final int USERINFORMATION_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    public static final boolean USERINFORMATION_TAG_PC = false;
    public static final int USERINFORMATION_TAG = 0x1E;

    long[] getDirectReference();

    void setDirectReference(long[] directReference);

    byte[] getExternalData();

    void setExternalData(byte[] externalData);
}
