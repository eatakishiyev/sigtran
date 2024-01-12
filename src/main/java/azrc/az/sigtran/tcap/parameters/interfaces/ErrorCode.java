/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.parameters.interfaces;

import azrc.az.sigtran.tcap.Encodable;
import azrc.az.sigtran.tcap.messages.ErrorCodeType;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface ErrorCode extends Encodable {

    public final static int LOCAL_ERROR_CODE_TAG_ = 0x02;
    public final static int GLOBAL_ERROR_CODE_TAG_ = 0x06;
    public final static int ERROR_CODE_TAG_CLASS_ = Tag.CLASS_UNIVERSAL;
    public final static boolean ERROR_CODE_TAG_PRIMITIVE_ = true;

    public void setErrorCodeType(ErrorCodeType errType);

    public ErrorCodeType getErrorCodeType();

    public void setErrorCode(Integer data);

    public Integer getErrorCode();
}
