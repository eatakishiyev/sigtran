/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap.components;

import azrc.az.sigtran.tcap.parameters.ParameterImpl;
import azrc.az.sigtran.tcap.parameters.ErrorCodeImpl;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public interface ReturnError extends Return {

    public static final int RETURN_ERROR_TAG = 0x03;
    public static final int RE_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    public static final boolean RE_PRIMITIVE = false;

    public void setErrorCode(ErrorCodeImpl errCode);

    public ErrorCodeImpl getErrorCode();

    public void setParameter(ParameterImpl params);
}
